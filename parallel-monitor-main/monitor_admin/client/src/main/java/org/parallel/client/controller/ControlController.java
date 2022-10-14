package org.parallel.client.controller;

import com.alibaba.fastjson.JSONObject;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.parallel.client.service.DataService;
import org.parallel.common.utils.ShellUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author yuyifade
 * @description 远程控制
 * @date 2021/8/18 下午5:30
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/control")
@Slf4j
public class ControlController {
    private final DataService ds;
    @GetMapping("/reboot")
    public ResponseEntity<Object> reboot() {
        JSONObject res = new JSONObject();
        res.put("node_status","正在重启");
        Thread t1 = new Thread() {
            @SneakyThrows
            @Override
            public void run() {
                String[] shell = {"sh", "-c", "reboot"};
                sleep(1000);
                ShellUtils.execShell(shell);
            }
        };
        t1.start();
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping("/shutdown")
    public ResponseEntity<Object> shutdown() {
        JSONObject res = new JSONObject();
        res.put("node_status","已关机");
        Thread t1 = new Thread() {
            @SneakyThrows
            @Override
            public void run() {
                String[] shell = {"sh", "-c", "shutdown -h now"};
                sleep(1000);
                ShellUtils.execShell(shell);
            }
        };
        t1.start();
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping("/interval")
    public ResponseEntity<Object> resetInterval(@RequestParam(name = "small_interval") int small_interval, @RequestParam(name = "big_interval") int big_interval, @RequestParam(name = "mode") int mode) {
        ds.update(small_interval, big_interval, mode);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/alarm")
    public ResponseEntity<Object> alarm(HttpServletRequest request) {
        JSONObject ap = JSONObject.parseObject(request.getParameter("alarm_policy"));
        ds.initAlarm(ap);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
