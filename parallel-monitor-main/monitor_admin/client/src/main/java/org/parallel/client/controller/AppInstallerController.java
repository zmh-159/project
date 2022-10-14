package org.parallel.client.controller;

import com.alibaba.fastjson.JSONObject;
import io.netty.util.internal.StringUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.parallel.client.service.ShellTool;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;


@RequiredArgsConstructor
@RestController
@RequestMapping("api/app")
@Slf4j
public class AppInstallerController {
    private final ShellTool st;

    @PostMapping("/install")
    public ResponseEntity<Object> install(HttpServletRequest request) {
        String shell = request.getParameter("shell");
        if (StringUtil.isNullOrEmpty(shell)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        log.info("收到安装脚本：" + shell);
        long shellId = st.execShell(shell);

        JSONObject res = new JSONObject();
        if (-1 == shellId) {
            log.info("已有安装脚本在运行！");
        } else {
            log.info("脚本正在运行！脚本Id：" + shellId);
        }
        res.put("shellId", shellId+"");
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping("/result")
    public ResponseEntity<Object> result(HttpServletRequest request) {
        JSONObject jo = new JSONObject();
        try {
            long shellId = Long.parseLong(request.getParameter("shell_id"));
            String state = st.getRes(shellId);
            jo.put("state", state);
            return new ResponseEntity<>(jo, HttpStatus.OK);
        } catch (Exception e) {
            log.error("参数错误");
            jo.put("message", "参数错误");
            return new ResponseEntity<>(jo, HttpStatus.BAD_REQUEST);
        }
    }

}
