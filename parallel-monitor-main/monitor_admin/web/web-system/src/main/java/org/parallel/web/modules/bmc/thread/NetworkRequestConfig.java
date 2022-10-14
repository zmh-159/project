package org.parallel.web.modules.bmc.thread;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.Map;
@Data
@Component
@Configuration
@ConfigurationProperties(prefix = "network-request")
public class NetworkRequestConfig {
    private String url;
    private Map map;
    private JSONObject body;
    private String method;
    private String part;
}
