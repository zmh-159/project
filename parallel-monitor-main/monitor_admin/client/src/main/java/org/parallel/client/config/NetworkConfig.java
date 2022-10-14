package org.parallel.client.config;

import lombok.Getter;
import lombok.Setter;
import org.parallel.client.utils.ClientUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author yuyifade
 * @description 获取服务器及本地网络配置
 * @date 2020/9/22 上午9:47
 */
@Configuration
@Setter
@Getter
@ConfigurationProperties(prefix = "network")
public class NetworkConfig {
    /**
     * 服务器ip地址
     **/
    private String server;
    /**
     * 服务器静态信息端口
     **/
    private int staticInfoPort;
    /**
     * 服务器动态信息端口
     **/
    private int dynamicInfoPort;
    /**
     * 本地UDP发送端口
     **/
    private int localPort;
    /**
     * UDP包大小
     **/
    private int udpPackSize;
    /**
     * TCP包大小
     **/
    private int tcpPackSize;
    //todo 读取一次文件
    static String serverByconf = ClientUtils.getConfig("client.conf", "server");
    static String sipByconfig = ClientUtils.getConfig("client.conf", "staticInfoPort");
    static String dipByconfig = ClientUtils.getConfig("client.conf", "dynamicInfoPort");
    static String lpByconfig = ClientUtils.getConfig("client.conf", "localPort");
    static String upsByconfig = ClientUtils.getConfig("client.conf", "udpPackSize");
    static String tpsByconfig = ClientUtils.getConfig("client.conf", "tcpPackSize");

    public String getServerByConf() {
        return serverByconf == null ? server : serverByconf;
    }

    public int getStaticInfoPort() {
        return (sipByconfig == null) ? staticInfoPort : Integer.valueOf(sipByconfig);
    }

    public int getDynamicInfoPort() {
        return (dipByconfig == null) ? dynamicInfoPort : Integer.valueOf(dipByconfig);
    }

    public int getLocalPort() {
        return (lpByconfig == null) ? localPort : Integer.valueOf(lpByconfig);
    }

    public int getUdpPackSize() {
        return (upsByconfig == null) ? udpPackSize : Integer.valueOf(upsByconfig);
    }

    public int getTcpPackSize() {
        return (tpsByconfig == null) ? tcpPackSize : Integer.valueOf(tpsByconfig);
    }

}
