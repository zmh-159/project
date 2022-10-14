package org.parallel.server.cmd.service;

import com.alibaba.fastjson.JSONObject;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.parallel.server.cmd.model.GetNetworkInfo;
import org.parallel.server.cmd.model.GetNodeId;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class NetService {
    private final GetNodeId getNodeId;
    private final GetNetworkInfo getNetworkInfo;

    @SneakyThrows
    public String netStat(String para) {
        JSONObject st;
        JSONObject dy;
        if (para.equals("")) {
            st = getNetworkInfo.staticNetworkInfo(getNodeId.getOnlineId());
            dy = getNetworkInfo.dynamicNetworkInfo(getNodeId.getOnlineId());
        } else {
            List<Long> list = new ArrayList<>();
            for (int i = 0; i < para.split(",").length; i++) {
                list.add(Long.parseLong(para.split(",")[i]));
            }
            st = getNetworkInfo.staticNetworkInfo(getNodeId.getOnlineId(list));
            dy = getNetworkInfo.dynamicNetworkInfo(getNodeId.getOnlineId(list));
        }

        StringBuilder res = new StringBuilder();
        res.append("\u001B[0;0H\u001B[2K");
        for (String s : st.keySet()) {
            int i = 0;
            JSONObject netSt = st.getJSONObject(s);
            JSONObject netDy = dy.getJSONObject(s);
            res.append("\u001B[").append(91).append("m").append("节点ID:").append(s).append("\u001B[m").append("\r\n");
            for (String t : netSt.keySet()) {
                res.append("\u001B[").append(91).append("m").append("Network Adapter").append(i).append("\u001B[m").append("\r\n");
                JSONObject ns = netSt.getJSONObject(t);
                JSONObject nd = netDy.getJSONObject(t);
                String name = ns.getString("name");
                String ip = ns.getString("ip");
                String down_speed = nd.getString("down_speed");
                String up_speed = nd.getString("up_speed");
                res.append("name: ").append(name).append("      ip: ").append(ip).append("\r\n");
                res.append("down speed: ").append(down_speed).append("      up speed: ").append(up_speed).append("\r\n");
                i++;
            }
        }
        return res.toString();
    }
}
