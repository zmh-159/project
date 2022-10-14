package org.parallel.server.rest;

import com.alibaba.fastjson.JSONObject;
import lombok.RequiredArgsConstructor;
import org.parallel.server.shell.Router;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author yuyifade
 * @description 集群控制
 * @date 2021/8/18 下午5:03
 */
@RestController
@RequestMapping("/api/cluser")
@RequiredArgsConstructor
public class CluserController {
    private final Router router;

    @GetMapping()
    public ResponseEntity<Object> shell(HttpServletRequest request) {
        String cmd = request.getParameter("cmd");
        String res = router.route(cmd);
        JSONObject jo = new JSONObject();
        jo.put("res", res);
        return new ResponseEntity<>(jo, HttpStatus.OK);
    }
}
