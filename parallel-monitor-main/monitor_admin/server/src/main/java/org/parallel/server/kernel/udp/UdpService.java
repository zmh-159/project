package org.parallel.server.kernel.udp;


import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollChannelOption;
import io.netty.channel.epoll.EpollDatagramChannel;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UdpService {
    @Value("${pm.udp.udpPort}")
    private int udpPort;
    @Value("${pm.udp.maxUdpDataSize}")
    private int maxSize;
    private final UdpDecoder udpDecoder;

    @Async
    public void startListen() {
        boolean epoll = Epoll.isAvailable();
        EventLoopGroup group = epoll ? new EpollEventLoopGroup() : new NioEventLoopGroup();
        Class dc = epoll ? EpollDatagramChannel.class : NioDatagramChannel.class;
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(dc)
                .option(ChannelOption.SO_BROADCAST, true)
                .option(ChannelOption.SO_RCVBUF, 1024 * 2048 * 10)
                .option(ChannelOption.RCVBUF_ALLOCATOR, new FixedRecvByteBufAllocator(maxSize))
                .handler(new ChannelInitializer<EpollDatagramChannel>() {
                    @Override
                    protected void initChannel(EpollDatagramChannel channel) throws Exception {
                        channel.pipeline().addLast(udpDecoder);
                    }
                });
        if (epoll) {
            bootstrap.option(EpollChannelOption.SO_REUSEPORT, true);
        }
        try {
            if (epoll) {
                int threadNums = Runtime.getRuntime().availableProcessors();
                threadNums = threadNums > 4 ? 4 : threadNums;
                List<ChannelFuture> futures = new ArrayList<>(threadNums);
                for (int i = 0; i < threadNums; ++i) {
                    ChannelFuture future = bootstrap.bind(udpPort).await();
                    futures.add(future);
                    if (future.isSuccess()) {
                        log.info("=======开启UDP监听线程端口{}：采集节点动态信息======", udpPort);
                    }
                }
                for (ChannelFuture future : futures) {
                    future.channel().closeFuture().await();
                }
            } else {
                ChannelFuture future = bootstrap.bind(udpPort).sync();
                future.channel().closeFuture().sync();
            }
        } catch (InterruptedException e) {
            log.error(e.getMessage());
        } finally {
            group.shutdownGracefully();
        }
    }
}