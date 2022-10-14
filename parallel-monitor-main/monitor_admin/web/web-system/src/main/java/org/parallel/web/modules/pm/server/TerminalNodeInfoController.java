package org.parallel.web.modules.pm.server;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.parallel.common.utils.RestTemplateClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/cmd")
@RequiredArgsConstructor
public class TerminalNodeInfoController {
    private static final String url = "http://127.0.0.1:9001/api/cmdRun";
    private final RestTemplateClient rtc;

    @ApiOperation("查询命令")
    @GetMapping()
    @PreAuthorize("@el.check('cluser:query')")
    public ResponseEntity<Object> index(String cmd) {
        String urlWithPara = url + "/sendCmd?cmd=" + cmd;
        JSONObject res = rtc.send(urlWithPara);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }
}
