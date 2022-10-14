package org.parallel.server.rest;

import com.alibaba.fastjson.JSONObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.parallel.common.utils.RestTemplateClient;
import org.parallel.server.service.OnlineNode;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author yuyifade
 * @description 转发web-system的请求到clinet
 * @date 2021/8/3 上午10:08
 */
@RestController
@RequestMapping("/api/cpuTest")
@RequiredArgsConstructor
//@SpringBootApplication(exclude = {
//        org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class
//})
@Slf4j
public class RelayToClientController {

    /**
     * todo 通过配置获取url
     **/
    private static final String url = ":8002/api/cpuTest";
    private final RestTemplateClient rtc;
    private final OnlineNode nic;

    /***
     * @description 接受核间通讯命令并转发到client
     * @author yuyifade
     * @date 2021/8/3 上午10:11
     * @param
     * @return org.springframework.http.ResponseEntity<java.lang.Object>
     */
    @GetMapping("/start")
    public ResponseEntity<Object> index(HttpServletRequest request) throws Exception {
        String start = request.getParameter("start");//起始包大小
        String end = request.getParameter("end");//结束包大小
        String core1 = request.getParameter("core1");//核心1
        String core2 = request.getParameter("core2");//核心2
        String steps = request.getParameter("steps");//步进
        String repeat = request.getParameter("repeat");//每次重复次数
        String ip = nic.getIp(Long.parseLong(request.getParameter("nodeId")));
        if (ip == null) {
            throw new Exception("节点未能接入系统");
        }
        /**开始测试url**/
        String urlWithPara = "http://" + ip + url + "/start";
        urlWithPara += "?" + "start=" + start;
        urlWithPara += "&" + "end=" + end;
        urlWithPara += "&" + "core1=" + core1;
        urlWithPara += "&" + "core2=" + core2;
        urlWithPara += "&" + "steps=" + steps;
        urlWithPara += "&" + "repeat=" + repeat;
       rtc.send(urlWithPara);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/result")
    public ResponseEntity<Object> result(Long nodeId) throws Exception {
        String ip = nic.getIp(nodeId);
        if (ip == null) {
            throw new Exception("节点未能接入系统");
        }
        /**获取结果url**/
        String urlWithPara = "http://" + ip + url + "/result";
        JSONObject res = rtc.send(urlWithPara);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

}
