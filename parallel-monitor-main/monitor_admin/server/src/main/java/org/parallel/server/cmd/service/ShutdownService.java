package org.parallel.server.cmd.service;

import com.alibaba.fastjson.JSONObject;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.parallel.server.cmd.model.GetNodeId;
import org.parallel.server.cmd.model.GetShutdown;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class ShutdownService {
    private final GetNodeId getNodeId;
    private final GetShutdown getShutdown;

    @SneakyThrows
    public String shutdown(String para) {
        JSONObject object;
        StringBuilder res = new StringBuilder();
        if (para.equals("")) {
            object = getShutdown.shutdown(getNodeId.getOnlineId());
        } else {
            List<Long> list = new ArrayList<>();
            for (int i = 0; i < para.split(",").length; i++) {
                list.add(Long.parseLong(para.split(",")[i]));
            }
            object = getShutdown.shutdown(getNodeId.getOnlineId(list));
        }
        for (String s : object.keySet()) {
            res.append("节点").append(s);
            JSONObject js = object.getJSONObject(s);
            for (String t : js.keySet()) {
                res.append(js.getString(t));
            }

        }

        return res.toString();
    }
}
