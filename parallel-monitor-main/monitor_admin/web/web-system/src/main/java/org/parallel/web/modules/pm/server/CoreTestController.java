package org.parallel.web.modules.pm.server;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.parallel.jpa.service.PmNodeService;
import org.parallel.common.utils.RestTemplateClient;
import org.parallel.web.modules.pm.server.ceriteria.CoreTestQueryCriteria;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

/**
 * @author yuyifade
 * @description 与server相关的接口
 * @date 2021/8/3 下午3:35
 */
@RestController
@RequiredArgsConstructor
@Api(tags = "监控：核间通讯相关接口")
@RequestMapping("/api/server")
@Slf4j
public class CoreTestController {
    //todo 通过config设置url
    private static final String url = "http://127.0.0.1:9001/api/cpuTest";
    private static final String realInfoUrl = "http://127.0.0.1:9001/api/realInfo";
    private final RestTemplateClient rtc;
    private final PmNodeService pns;

    @ApiOperation("核间通讯")
    @GetMapping(value = "/cpuTest")
    @PreAuthorize("@el.check('cpuTest:list')")
    public ResponseEntity<Object> detail(CoreTestQueryCriteria criteria) {
        log.info(criteria.toString());
        String urlWithPara = url + "/start" + "?";
        urlWithPara += criteria.getUrl();
        rtc.send(urlWithPara);
//        JSONObject res = JSONObject.parseObject(restTemplate.getForEntity(urlWithPara, String.class).getBody());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation("获取结果")
    @GetMapping(value = "/cpuTestRes")
    @PreAuthorize("@el.check('cpuTest:list')")
    public ResponseEntity<Object> result(Long nodeId) {
        String urlWithPara = url + "/result" + "?" + "nodeId=" + nodeId;
        JSONObject res = rtc.send(urlWithPara);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @ApiOperation("获取存活节点cpu信息")
    @GetMapping(value = "/cpuInfo")
    @PreAuthorize("@el.check('cpuTest:list')")
    public ResponseEntity<Object> cpuInfo() {
        /** 获取存活的节点 **/
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
        return new ResponseEntity<>(pns.getOnlineCpus(ids.toString()), HttpStatus.OK);
    }
}
