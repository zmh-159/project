package org.parallel.server.rest;

import com.alibaba.fastjson.JSONObject;
import lombok.RequiredArgsConstructor;
import org.parallel.server.service.OnlineNode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author yuyifade
 * @description
 * @date 2021/7/24 下午12:34
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class NodeInfoController {
    private final OnlineNode nic;

    @GetMapping(value = "/realInfo")
    public ResponseEntity<Object> realInfo() {
        return new ResponseEntity<>(nic.getNodeInfo(), HttpStatus.OK);
    }

    @GetMapping(value = "/onlineNode")
    public ResponseEntity<Object> onlineNode() {
        JSONObject res = new JSONObject();
        res.put("res", nic.getOnlineId());
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping(value = "/deleteNode")
    public ResponseEntity<Object> delete(HttpServletRequest request) {
        JSONObject res = new JSONObject();
        String nodeIdStr = request.getParameter("node_id");

        try {
            long nodeId = Long.parseLong(nodeIdStr);
            HttpStatus status;
            if (nic.deleteId(nodeId)) {
                res.put("message", "success");
                status = HttpStatus.OK;
            } else {
                res.put("message", "不能删除在线节点!");
                status = HttpStatus.BAD_REQUEST;
            }
            return new ResponseEntity<>(res, status);
        } catch (Exception e) {
            res.put("message", "参数错误！");
            return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
        }
    }
}
