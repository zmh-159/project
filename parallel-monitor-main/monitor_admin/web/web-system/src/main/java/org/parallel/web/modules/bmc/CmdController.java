package org.parallel.web.modules.bmc;


import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.parallel.web.modules.bmc.jpa.repository.PbNodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cmdRun")
@RequiredArgsConstructor
public class CmdController {
    @Autowired
    Kp kp;

    @Autowired
    Ft ft;

    private final PbNodeRepository pbNodeRepository;

    @SneakyThrows
    @GetMapping(value = "/sendCmd")
    public ResponseEntity<Object> sendCmd(HttpServletRequest request) {
        StringBuilder res = new StringBuilder();
        String cmd = request.getParameter("cmd");
        String[] arr = cmd.split(" ");
        List<Map<String, String>> nodeIds = pbNodeRepository.getNodeId();
        int type = 0;
        switch (arr[0]) {
            case "On":
                break;
            case "ForceOff":
                type = 1;
                break;
            case "GracefulShutdown":
                type = 2;
                break;
            case "GracefulRestart":
                type = 3;
                break;
        }
        int finalType = type;
        if (arr.length > 1) {
            for (int i = 0; i < arr.length; i++) {
                String nodeId = arr[i + 1];
                nodeIds.forEach(s -> {
                    if (s.get("node_id").equals(nodeId)) {
                        if (s.get("node_type").equals("kp")) {
                            if (kp.power(Long.parseLong(nodeId), finalType)) {
                                res.append("节点").append(s.get("node_id")).append("关机成功！").append("\r\n");
                            } else {
                                res.append("节点").append(s.get("node_id")).append("关机失败！").append("\r\n");
                            }
                        } else {
                            if (ft.power(Long.parseLong(nodeId), finalType)) {
                                res.append("节点").append(s.get("node_id")).append("关机成功！").append("\r\n");
                            } else {
                                res.append("节点").append(s.get("node_id")).append("关机失败！").append("\r\n");
                            }
                        }
                    }
                });
            }
        } else {
            nodeIds.forEach(s -> {
                Long nodeId = Long.parseLong(s.get("node_id"));
                if (s.get("node_type").equals("kp")) {
                    if (kp.power(nodeId, finalType)) {
                        res.append("节点").append(s.get("node_id")).append("关机成功！").append("\r\n");
                    } else {
                        res.append("节点").append(s.get("node_id")).append("关机失败！").append("\r\n");
                    }
                } else {
                    if (ft.power(nodeId, finalType)) {
                        res.append("节点").append(s.get("node_id")).append("关机成功！").append("\r\n");
                    } else {
                        res.append("节点").append(s.get("node_id")).append("关机失败！").append("\r\n");
                    }
                }
            });
        }

        return new ResponseEntity<>(res.toString(), HttpStatus.OK);
    }
}
