package org.parallel.server.pm;

/**
 * @author yuyifade
 * @description 网络监听
 * @date 2021/6/25 下午2:54
 */

import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.parallel.server.kernel.udp.UdpDecoder;
import org.parallel.server.kernel.udp.UdpService;
import org.parallel.server.kernel.udp.observe.DynamicObserve;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@Component
@Slf4j
@Data
@WebListener
public class Collector implements ServletContextListener {
    @Autowired
    private Listener nl;

    @Autowired
    private UdpService udpService;

    @Autowired
    private UdpDecoder udpDecoder;

    @Autowired
    private DynamicObserve dynamicObserve;

    @SneakyThrows
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
//        nl.nodeStaticInfo();
        udpDecoder.addPropertyChangeListener("dynamic",dynamicObserve);
        udpService.startListen();
//        nl.nodeDynamicInfo();
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        log.info("=======关闭TCP监听线程端口{}======",nl.getTcpPort());
        log.info("=======关闭UDP监听线程端口{}======",nl.getUdpPort());
    }
}