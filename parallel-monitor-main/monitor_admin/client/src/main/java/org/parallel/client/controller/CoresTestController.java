package org.parallel.client.controller;

import lombok.extern.slf4j.Slf4j;
import org.parallel.client.service.CoresExchangeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author yuyifade
 * @description 核心通讯测试
 * @date 2021/8/2 下午4:05
 */
@RestController
@RequestMapping("/api/cpuTest")
@Slf4j
public class CoresTestController {
    @Autowired
    private CoresExchangeMessage cem;

    @GetMapping("/start")
    public ResponseEntity<Object> index(HttpServletRequest request) {
        int start = Integer.parseInt(request.getParameter("start"));
        int end = Integer.parseInt(request.getParameter("end"));
        int core1 = Integer.parseInt(request.getParameter("core1"));
        int core2 = Integer.parseInt(request.getParameter("core2"));
        int steps = Integer.parseInt(request.getParameter("steps"));
        int repeat = Integer.parseInt(request.getParameter("repeat"));
        try {
            cem.startTest(start, end, core1, core2, steps, repeat);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/result")
    public ResponseEntity<Object> result() {
        return new ResponseEntity<>(cem.getRes(), HttpStatus.OK);
    }
}
