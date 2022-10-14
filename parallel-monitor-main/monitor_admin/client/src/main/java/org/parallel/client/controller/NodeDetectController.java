package org.parallel.client.controller;


import com.alibaba.fastjson.JSONObject;
import lombok.RequiredArgsConstructor;
import org.parallel.client.service.NodeDetect;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/nodeDetect")
@RequiredArgsConstructor
public class NodeDetectController {
    private final NodeDetect nd;

    @GetMapping(value = "/memoryDetect")
    public ResponseEntity<Object> memoryDetect() {
        return new ResponseEntity<>(nd.memoryTest(), HttpStatus.OK);
    }

    @GetMapping(value = "/diskDetect")
    public ResponseEntity<Object> diskDetect(HttpServletRequest request) {
        String filepath = request.getParameter("filepath");
        return new ResponseEntity<>(nd.diskTest(nd.filePath(filepath)), HttpStatus.OK);
    }
}
