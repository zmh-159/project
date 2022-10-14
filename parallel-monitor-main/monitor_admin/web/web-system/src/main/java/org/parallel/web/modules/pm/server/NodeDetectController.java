package org.parallel.web.modules.pm.server;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.parallel.jpa.service.PmNodeService;
import org.parallel.common.utils.RestTemplateClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequiredArgsConstructor
@Api(tags = "监控：节点测试")
@RequestMapping("/api/detectNode")
@Slf4j
public class NodeDetectController {
    private final RestTemplateClient rtc;
    private final PmNodeService pns;
    private static final String url = "http://127.0.0.1:9001/api/detect";
    private static final String realInfoUrl = "http://127.0.0.1:9001/api/realInfo";

    @ApiOperation("内存测试")
    @GetMapping(value = "/detectMemory")
    public ResponseEntity<Object> memoryDetect(Long nodeId) {
        String urlWithPara = url + "/memory?nodeId=" + nodeId;
        JSONObject res = rtc.send(urlWithPara);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @ApiOperation("磁盘测试")
    @GetMapping(value = "/detectDisk")
    public ResponseEntity<Object> diskDetect(Long nodeId, String filepath) {
        String urlWithPara = url + "/disk?nodeId=" + nodeId + "&" + "filepath=" + filepath;
        JSONObject res = rtc.send(urlWithPara);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @ApiOperation("获取存活节点磁盘信息")
    @GetMapping(value = "/diskInfo")
    //@PreAuthorize("@el.check('cpuTest:list')")
    public ResponseEntity<Object> diskInfo() {
        JSONObject aliveNode = rtc.send(realInfoUrl);
        StringBuilder ids = new StringBuilder();
        if (aliveNode.size() > 0) {
            Set<String> aliveNodeIds = aliveNode.getJSONObject("node_data").keySet();
            ids.append('(');
            for (String id : aliveNodeIds) {
                ids.append(id);
                ids.append(',');
            }
            ids.setCharAt(ids.length() - 1, ')');
        }
        return new ResponseEntity<>(pns.getOnlineDisk(ids.toString()), HttpStatus.OK);
    }
}
