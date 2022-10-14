package org.parallel.server.cmd.service;

import com.alibaba.fastjson.JSONObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.parallel.common.utils.cmd.StringFormat;
import org.parallel.server.cmd.model.GetMemoryInfo;
import org.parallel.server.cmd.model.GetNodeId;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class MemService {
    private final GetNodeId getNodeId;
    private final GetMemoryInfo getMemoryInfo;

    public String memoryStatic(String para) {
        JSONObject object;
        if (para.equals("")) {
            object = getMemoryInfo.staticMemoryInfo(getNodeId.getOnlineId());
        } else {
            List<Long> list = new ArrayList<>();
            for (int i = 0; i < para.split(",").length; i++) {
                list.add(Long.parseLong(para.split(",")[i]));
            }
            object = getMemoryInfo.staticMemoryInfo(getNodeId.getOnlineId(list));
        }

        StringBuilder res = new StringBuilder();
        for (String s : object.keySet()) {
            int i = 0;
            JSONObject jo = object.getJSONObject(s);
            res.append("\u001B[").append(91).append("m").append("节点ID:").append(s).append("\u001B[m").append("\r\n");
            for (String t : jo.keySet()) {
                res.append("\u001B[").append(91).append("m").append("Memory Device").append(i).append("\u001B[m").append("\r\n");
                JSONObject mem = jo.getJSONObject(t);
                if (mem.size() == 0) {
                    res.append("虚拟机无内存条").append("\r\n");
                } else {
                    for (String r : mem.keySet()) {
                        res.append(StringFormat.sufInsertSpaceFormat(r + ": ", 20)).append(mem.getString(r)).append("\r\n");
                    }
                }
                i++;
            }
        }
        return res.toString();
    }
}
