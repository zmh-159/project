package org.parallel.server.rest;


import com.alibaba.fastjson.JSONObject;
import lombok.RequiredArgsConstructor;
import org.parallel.common.utils.RestTemplateClient;
import org.parallel.server.service.OnlineNode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/detect")
@RequiredArgsConstructor
public class NodeDetectController {
    private final OnlineNode nic;
    private final RestTemplateClient rtc;
    private static final String url = ":8002/api/nodeDetect";

    @GetMapping(value = "/memory")
    public ResponseEntity<Object> memoryDetect(HttpServletRequest request) throws Exception {
        Long nodeId = Long.parseLong(request.getParameter("nodeId"));
        String ip = nic.getIp(nodeId);
        if (ip == null) {
            throw new Exception("节点未能接入系统");
        }
        String urlWithPara = "http://" + ip + url + "/memoryDetect";
        JSONObject res = rtc.send(urlWithPara);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping(value = "/disk")
    public ResponseEntity<Object> diskDetect(HttpServletRequest request) throws Exception {
        Long nodeId = Long.parseLong(request.getParameter("nodeId"));
        String filepath = request.getParameter("filepath");
        String ip = nic.getIp(nodeId);
        if (ip == null) {
            throw new Exception("节点未能接入系统");
        }
        String urlWithPara = "http://" + ip + url + "/diskDetect?filepath=" + filepath;
        JSONObject res = rtc.send(urlWithPara);

        return new ResponseEntity<>(res, HttpStatus.OK);
    }
}
