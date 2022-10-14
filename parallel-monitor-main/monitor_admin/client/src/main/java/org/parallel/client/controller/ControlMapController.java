package org.parallel.client.controller;

import com.alibaba.fastjson.JSONObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.parallel.client.controller.enity.ControlAgreement;
import org.parallel.client.kernel.service.FilterInfo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author yuyifade
 * @description 控制命令中心
 * @date 2022/1/5 下午5:30
 */
@RestController
@RequestMapping("/api/")
@RequiredArgsConstructor
@Slf4j
public class ControlMapController {
    private final FilterInfo fi;

    @PostMapping(value = "/control")
    public ResponseEntity<Object> index(HttpServletRequest request) {
        String agreement = request.getParameter("agreement");
        ControlAgreement ca = JSONObject.parseObject(agreement, ControlAgreement.class);
        JSONObject res = fi.getInfo(ca.getName(), ca.getParams());
        return new ResponseEntity<>(res, HttpStatus.OK);
    }
}
