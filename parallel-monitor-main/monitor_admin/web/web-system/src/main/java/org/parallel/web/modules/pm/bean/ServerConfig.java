package org.parallel.web.modules.pm.bean;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author yuyifade
 * @description 获取数据处理端信息
 * @date 2021/8/20 上午11:40
 */
@Configuration
@Data
public class ServerConfig {
    @Value("${pm.server.ip}")
    private String ip;
    @Value("${pm.server.port}")
    private int port;

    private String baseUrl() {
        return ip + ":" + port;
    }

    private String wsBaseUrl() {
        return "ws://" + baseUrl();
    }

    private String httpBaseUrl() {
        return "http://" + baseUrl();
    }

    /***
     * @description 返回数据处理端webssh url
     * @author yuyifade
     * @date 2021/8/20 上午11:53
     * @return java.lang.String
     */
    public String websshUrl() {
        return wsBaseUrl() + "/webssh";
    }

    public String cpuTestStart() {
        return httpBaseUrl() + "/api/cpuTest/start";
    }

    public String cpuTestResult() {
        return httpBaseUrl() + "/api/cpuTest/result";
    }

    public String realInfo() {
        return httpBaseUrl() + "/api/realInfo";
    }

    public String shutdown() {
        return httpBaseUrl() + "/api/cluserControl/shutdown";
    }

    public String appInstall() {
        return httpBaseUrl() + "/api/app/install";
    }

    public String appResult() {
        return httpBaseUrl() + "/api/app/result";
    }

    public String interval() {
        return httpBaseUrl() + "/api/cluserControl/interval";
    }

    public String alarm() {
        return httpBaseUrl() + "/api/alarm";
    }

    public String onlineNode() {
        return httpBaseUrl() + "/api/onlineNode";
    }

}
