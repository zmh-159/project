package org.parallel.server.rest;

import com.alibaba.fastjson.JSONObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.parallel.common.utils.RestTemplateClient;
import org.parallel.server.bean.ClientConfig;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/app")
@Slf4j
public class AppInstallerController {
    private final RestTemplateClient rtc;
    private final ClientConfig cc;

    @PostMapping("/install")
    public ResponseEntity<Object> install(HttpServletRequest request) {
        try {
            String shell = request.getParameter("shell");
            long nodeId = Long.parseLong(request.getParameter("node_id"));

            String url = cc.appInstall(nodeId);
            MultiValueMap<String, String> msl = new LinkedMultiValueMap<>();
            msl.add("shell", shell);

            JSONObject jo = rtc.sendPost(url, msl);
            return new ResponseEntity<>(jo, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/result")
    public ResponseEntity<Object> result(HttpServletRequest request) {
        try {
            long shellId = Long.parseLong(request.getParameter("shell_id"));
            long nodeId = Long.parseLong(request.getParameter("node_id"));

            String url = cc.appResult(nodeId) + "?shell_id=" + shellId;

            return new ResponseEntity<>(rtc.send(url), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
