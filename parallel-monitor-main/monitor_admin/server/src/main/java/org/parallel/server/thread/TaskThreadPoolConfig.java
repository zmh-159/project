package org.parallel.server.thread;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @description 获取线程配置
 * @author yuyifade
 * @date 2020/9/20 上午12:19
 */
@Data
@Component
@Configuration
@ConfigurationProperties(prefix = "thread")
public class TaskThreadPoolConfig {
    private int corePoolSize;
    private int maxPoolSize;
    private int keepAliveSeconds;
    private int queueCapacity;
}
