package org.parallel.server.cmd.service;

import com.alibaba.fastjson.JSONObject;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.parallel.server.cmd.model.GetDiskInfo;
import org.parallel.server.cmd.model.GetNodeId;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class DiskService {
    private final GetNodeId getNodeId;
    private final GetDiskInfo getDiskInfo;

    @SneakyThrows
    public String diskStat(String para) {
        JSONObject st;
        JSONObject dy;
        if (para.equals("")) {
            st = getDiskInfo.staticDiskInfo(getNodeId.getOnlineId());
            dy = getDiskInfo.dynamicDiskInfo(getNodeId.getOnlineId());
        } else {
            List<Long> list = new ArrayList<>();
            for (int i = 0; i < para.split(",").length; i++) {
                list.add(Long.parseLong(para.split(",")[i]));
            }
            st = getDiskInfo.staticDiskInfo(getNodeId.getOnlineId(list));
            dy = getDiskInfo.dynamicDiskInfo(getNodeId.getOnlineId(list));
        }

        StringBuilder res = new StringBuilder();
        res.append("\u001B[0;0H\u001B[2K");
        for (String s : st.keySet()) {
            int i = 0;
            JSONObject diskSt = st.getJSONObject(s);
            JSONObject diskDy = dy.getJSONObject(s);
            res.append("\u001B[").append(91).append("m").append("节点ID:").append(s).append("\u001B[m").append("\r\n");
            for (String t : diskSt.keySet()) {
                res.append("\u001B[").append(91).append("m").append("Disk").append(i).append("\u001B[m").append("\r\n");
                JSONObject ds = diskSt.getJSONObject(t);
                JSONObject dd = diskDy.getJSONObject(t);
                String name = ds.getString("name");
                String capacity = ds.getString("capacity");
                String read_speed = dd.getString("read_speed");
                String write_speed = dd.getString("write_speed");
                res.append("name: ").append(name).append("      capacity: ").append(capacity).append("\r\n");
                res.append("read_speed: ").append(read_speed).append("      write_speed: ").append(write_speed).append("\r\n");
                i++;
            }
        }
        return res.toString();
    }
}
