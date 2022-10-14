package org.parallel.server.rest;

import com.alibaba.fastjson.JSONObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.parallel.common.utils.RestTemplateClient;
import org.parallel.server.service.OnlineNode;
import org.parallel.server.service.IntervalConfig;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Hashtable;

/**
 * @author yuyifade
 * @description 集群控制
 * @date 2021/8/18 下午5:03
 */
@RestController
@RequestMapping("/api/cluserControl")
@RequiredArgsConstructor
@Slf4j
public class CluserControlController {
    private final RestTemplateClient rtc;
    private final OnlineNode nic;
    private final IntervalConfig ic;
    /**
     * todo 通过配置获取url
     **/
    private static final String url = ":8002/api/control";

    @GetMapping("/shutdown")
    public ResponseEntity<Object> index(HttpServletRequest request) {
        Hashtable<Long, String> ips = nic.getIps();
        for (Long nodeId : ips.keySet()) {
            String ip = ips.get(nodeId);
            String urlWithPara = "http://" + ip + url + "/shutdown";
            rtc.send(urlWithPara);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/interval")
    public ResponseEntity<Object> interval() {
        JSONObject jo = ic.getConfig();
        String params = "?big_interval=" + jo.getString("big_interval");
        params += "&small_interval=" + jo.getString("small_interval");
        params += "&mode=" + jo.getString("mode");

        Hashtable<Long, String> ips = nic.getIps();
        for (Long nodeId : ips.keySet()) {
            String ip = ips.get(nodeId);
            String urlWithPara = "http://" + ip + url + "/interval" + params;
            log.info(urlWithPara);
            rtc.send(urlWithPara);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
