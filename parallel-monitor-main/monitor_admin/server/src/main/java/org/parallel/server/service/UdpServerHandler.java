package org.parallel.server.service;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 服务端业务处理
 *
 * @author LionLi
 */
public class UdpServerHandler extends SimpleChannelInboundHandler<DatagramPacket> {
    static AtomicInteger ai = new AtomicInteger(0);
    String s;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket packet) {
//        synchronized (s) {
//            i++;
//        }

        System.out.println(Thread.currentThread().getName() + "- nums:" + ai.getAndAdd(1) + "服务端接收到消息：" + packet.content().toString(StandardCharsets.UTF_8));
    }
}