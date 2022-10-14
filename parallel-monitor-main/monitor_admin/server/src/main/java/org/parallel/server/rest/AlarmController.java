package org.parallel.server.rest;

import com.alibaba.fastjson.JSONObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.parallel.common.utils.RestTemplateClient;
import org.parallel.jpa.service.mapstruct.PmAlarmSchemeService;
import org.parallel.server.bean.ClientConfig;
import org.parallel.server.service.AlarmConfig;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/alarm")
@Slf4j
public class AlarmController {
    private final RestTemplateClient rtc;
    private final ClientConfig cc;
    private final PmAlarmSchemeService pass;
    private final AlarmConfig ac;

    @GetMapping
    public ResponseEntity<Object> index(HttpServletRequest request) {
        try {
            int alarmSchemeId = Integer.parseInt(request.getParameter("alarm_scheme_id"));
            //查询方案更新影响的节点
            String nodeIds = pass.findById(alarmSchemeId).getEffectNode();
            String nodeIdArray[] = nodeIds.split(",");
            //查询更新后节点报警方案
            JSONObject alarms = ac.getConfigs(nodeIdArray);
            for (String nodeId : alarms.keySet()) {
                long nid = Long.parseLong(nodeId);
                MultiValueMap<String, String> params = new LinkedMultiValueMap();
                params.add("alarm_policy", alarms.getJSONObject(nodeId).toJSONString());
                //向client发送新策略
                String url = cc.updateAlarmPolicy(nid);
                if (!url.equals("")) {
                    rtc.sendPost(cc.updateAlarmPolicy(nid), params);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("更新报警策略失败！");
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
