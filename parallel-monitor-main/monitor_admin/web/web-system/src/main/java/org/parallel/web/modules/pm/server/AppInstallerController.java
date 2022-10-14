package org.parallel.web.modules.pm.server;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.parallel.common.utils.RestTemplateClient;
import org.parallel.web.modules.pm.bean.ServerConfig;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
@Api(tags = "监控：脚本管理")
@RequestMapping("/api/app")
@Slf4j
public class AppInstallerController {
    private final ServerConfig sc;
    private final RestTemplateClient rtc;

    @ApiOperation("安装脚本")
    @PostMapping(value = "/install")
    @PreAuthorize("@el.check('app:install')")
    public ResponseEntity<Object> index(HttpServletRequest request) {
        try {
            long nodeId = Long.parseLong(request.getParameter("node_id"));
            String shell = request.getParameter("shell");

            MultiValueMap<String, String> mss = new LinkedMultiValueMap<>();
            mss.add("node_id", nodeId + "");
            mss.add("shell", shell);
            return new ResponseEntity<>(rtc.sendPost(sc.appInstall(), mss), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @ApiOperation("查询脚本运行状态")
    @GetMapping(value = "/result")
    @PreAuthorize("@el.check('app:result')")
    public ResponseEntity<Object> result(HttpServletRequest request) {
        try {
            long shellId = Long.parseLong(request.getParameter("shell_id"));
            long nodeId = Long.parseLong(request.getParameter("node_id"));

            String url = sc.appResult() + "?shell_id=" + shellId + "&node_id=" + nodeId;
            return new ResponseEntity<>(rtc.send(url), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
