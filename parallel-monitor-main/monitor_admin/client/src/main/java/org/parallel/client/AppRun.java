package org.parallel.client;

import org.parallel.common.utils.ShellUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.BufferedReader;
import java.io.IOException;


/**
 * @author yuyifade
 * @description 主启动类
 * @date 2020/9/18 上午12:19
 */
@SpringBootApplication
@EnableScheduling
@EnableAsync(proxyTargetClass = true)
@ComponentScan(basePackages = {"org.parallel.client", "org.parallel.common"})
public class AppRun {
    public static void main(String[] args) {

        try {
            BufferedReader reader = ShellUtils.execShell("whoami");
            String name = reader.readLine();
            System.out.println(name);
            if (name.contains("root")) {
                SpringApplication.run(AppRun.class, args);
            } else {
                System.out.println("未获取root权限，无法运行");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
