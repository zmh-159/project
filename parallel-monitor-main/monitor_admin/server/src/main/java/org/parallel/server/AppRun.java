package org.parallel.server;

/**
 * @description 暂时定为主类
 * @author yuyifade
 * @date 2020 9/19
 */

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;


/**
 * @author yuyifade
 * @description 主启动类
 * @date 2020/9/18 上午12:19
 */
@SpringBootApplication
@EnableScheduling
@EnableAsync
@ComponentScan(basePackages = {"org.parallel.jpa", "org.parallel.server", "org.parallel.common"})
@EnableJpaRepositories(basePackages = {"org.parallel.jpa", "org.parallel.server"})
@EntityScan(basePackages = {"org.parallel.jpa", "org.parallel.server"})
public class AppRun {
    public static void main(String[] args) {
        SpringApplication.run(AppRun.class, args);
    }
}
