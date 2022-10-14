package org.parallel.server.kernel.rest;

import com.alibaba.fastjson.JSONObject;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MultiValuedMap;
import org.parallel.common.utils.RestTemplateClient;
import org.parallel.server.bean.ClientConfig;
import org.parallel.server.kernel.node.Node;
import org.parallel.server.service.AlarmConfig;
import org.parallel.server.service.IntervalConfig;
import org.parallel.server.service.NodeSave;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

@Slf4j
@RestController
@RequestMapping("api/client")
@RequiredArgsConstructor
public class HttpService {
    private final NodeSave node;
    private final IntervalConfig interValConfig;
    private final AlarmConfig alarmConfig;
    private final Node nodeinfo;
    private final RestTemplateClient rtc;
    private final ClientConfig clientConfig;

    @PostMapping()
    public ResponseEntity<Object> init(HttpServletRequest request) {
        long receiveTime = System.currentTimeMillis();
        String recv = request.getParameter("staticInfo");
        System.out.println(recv);
        JSONObject res = null;
        try {
            JSONObject staticInfo = JSONObject.parseObject(recv);
            String ip = request.getRemoteHost();
            String url = clientConfig.upNodeStaticInfo();
            MultiValueMap<String, String> maps = new LinkedMultiValueMap<>();
            maps.add("ip",ip);
            maps.add("nodeInfo",recv);
            rtc.sendPost(url, maps);

            res = node.saveStaticInfo(staticInfo, ip);
            res.put("server_receive_time", receiveTime);
            res.put("server_send_time", System.currentTimeMillis());
            //传输采样模式，采样间隔相关参数
            JSONObject interval = interValConfig.getConfig();
            res.put("monitor_policy", interval);
            //报警参数
            JSONObject test = alarmConfig.getConfig(res.getLongValue("node_id"));
            res.put("alarm", test);
        } catch (Exception e) {
            res = new JSONObject();
            e.printStackTrace();
            log.warn("节点静态信息格式错误！");
        }
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping(value = "/test")
    @SneakyThrows
    public ResponseEntity<Object> test(HttpServletRequest request) {

        String cmd = request.getParameter("cmd");
        List<Long> nodeids = new ArrayList<>();
        nodeids.add(2166L);
        Future<JSONObject> fj = nodeinfo.getNodeInfo(cmd, nodeids);

        return new ResponseEntity<>(fj.get(), HttpStatus.OK);
    }
}
