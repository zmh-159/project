package org.parallel.client.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @description 获取uuid的key
 * @author yuyifade
 * @date 2020/9/21 下午10:36
 */
@Component
@Setter
@Getter
@ConfigurationProperties(prefix = "device")
public class DeviceConfig {
    private String uuid;
    private String deviceId;
}
