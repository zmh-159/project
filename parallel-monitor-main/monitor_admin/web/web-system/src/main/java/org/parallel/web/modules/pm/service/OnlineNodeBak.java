package org.parallel.web.modules.pm.service;

import com.alibaba.fastjson.JSONObject;
import lombok.RequiredArgsConstructor;
import org.parallel.common.utils.RestTemplateClient;
import org.parallel.web.modules.pm.bean.ServerConfig;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class OnlineNodeBak {
    private final RestTemplateClient rtc;
    private final ServerConfig sc;

    /***
     * @description 返回在线节点id
     * @author yuyifade
     * @date 2021/12/10 下午1:24
     * @return String example （1,2,3,4,5,28）
     */
    public String getOnlineNodeId() {
        JSONObject onlineNodeId = rtc.send(sc.onlineNode());
        return onlineNodeId.getString("res");
    }
}
