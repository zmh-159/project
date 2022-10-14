package org.parallel.server.cmd.service;

import com.alibaba.fastjson.JSONObject;
import io.lettuce.core.ScriptOutputType;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.parallel.common.utils.cmd.StringFormat;
import org.parallel.server.cmd.model.GetNodeId;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class NodeStatus {
    private final GetNodeId getNodeId;

    @SneakyThrows
    public String nodeStatus() {
        StringBuilder res = new StringBuilder();
        List<Long> onlineId = getNodeId.getOnlineId();
        List<String> allId = getNodeId.allNodeId();

        List<String> strOnlineId = new ArrayList<>();
        for (Long l : onlineId) {
            strOnlineId.add(l.toString());
        }
        allId.removeAll(strOnlineId);

        JSONObject object = new JSONObject();
        res.append("\u001B[0;0H\u001B[2K");
        res.append("节点状态:\r\n");
        for (long l : onlineId) {
            object.put(l + "", StringFormat.stringGreen("在线●"));
        }
        for (String value : allId) {
            object.put(value + "", StringFormat.stringRed("离线●"));
        }
        for (String s : object.keySet()) {
            res.append(s).append("号节点: ").append(object.getString(s)).append("\r\n");
        }

        return res.toString();
    }

}
