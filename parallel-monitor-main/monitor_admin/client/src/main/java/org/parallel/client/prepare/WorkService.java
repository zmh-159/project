package org.parallel.client.prepare;/**
 * @description 系统各类事务启动地址
 * @author yuyifade
 * @date 2020/9/21 下午6:48
 */

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.parallel.client.service.DataService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@Setter
@RequiredArgsConstructor
public class WorkService implements ApplicationRunner {
    private final DataService dataService;
    @Override
    public void run(ApplicationArguments args) {
        dataService.updateTrendsInfo();
    }
}
