package org.parallel.server.bean;

import io.netty.util.internal.StringUtil;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.parallel.server.service.OnlineNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author yuyifade
 * @description
 * @date 2021/8/20 下午5:08
 */
@Configuration
@Data
@RequiredArgsConstructor
public class ClientConfig {
    @Value("${pm.client.port}")
    private int port;
    @Value("${pm.web.port}")
    private int webPort;
    @Value("${pm.web.ip}")
    private String webIp = "127.0.0.1";
    private final OnlineNode nic;

    private String getIp(long nodeId) {
        return nic.getIp(nodeId);
    }

    public String shutdown(long nodeId) {
        String ip = getIp(nodeId);
        if (StringUtil.isNullOrEmpty(ip)) {
            return "";
        }
        return "http://" + ip + ":" + port + "/api/control/shutdown";
    }

    public String cpuTestStart(long nodeId) {
        String ip = getIp(nodeId);
        if (StringUtil.isNullOrEmpty(ip)) {
            return "";
        }
        return "http://" + ip + ":" + port + "/api/cpuTest/start";
    }

    public String cpuTestResult(long nodeId) {
        String ip = getIp(nodeId);
        if (StringUtil.isNullOrEmpty(ip)) {
            return "";
        }
        return "http://" + ip + ":" + port + "/api/cpuTest/result";
    }

    public String appInstall(long nodeId) {
        String ip = getIp(nodeId);
        if (StringUtil.isNullOrEmpty(ip)) {
            return "";
        }
        return "http://" + ip + ":" + port + "/api/app/install";
    }

    public String appResult(long nodeId) {
        String ip = getIp(nodeId);
        if (StringUtil.isNullOrEmpty(ip)) {
            return "";
        }
        return "http://" + ip + ":" + port + "/api/app/result";
    }

    public String updateAlarmPolicy(long nodeId) {
        String ip = getIp(nodeId);
        if (StringUtil.isNullOrEmpty(ip)) {
            return "";
        }
        return "http://" + ip + ":" + port + "/api/control/alarm";
    }

    public String getInfo(long nodeId) {
        String ip = getIp(nodeId);
        if (StringUtil.isNullOrEmpty(ip)) {
            return "";
        }
        return "http://" + ip + ":" + port + "/api/control";
    }

    public String upNodeDynamicInfo(){
        return "http://"+webIp+":"+webPort+"/api/nodeInfo/dynamicInfo";
    }

    public String upNodeStaticInfo(){
        return "http://"+webIp+":"+webPort+"/api/nodeInfo/staticInfo";
    }


}
