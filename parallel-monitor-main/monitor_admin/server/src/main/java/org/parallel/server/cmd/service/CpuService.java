package org.parallel.server.cmd.service;

import com.alibaba.fastjson.JSONObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.parallel.common.utils.cmd.Sort;
import org.parallel.common.utils.cmd.StringFormat;
import org.parallel.server.cmd.model.GetCpuInfo;
import org.parallel.server.cmd.model.GetNodeId;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
@RequiredArgsConstructor
public class CpuService {
    private final GetNodeId getNodeId;
    private final GetCpuInfo getCpuInfo;

    public String cpuStatic(String para) {
        StringBuilder res = new StringBuilder();
        JSONObject object;
        if (para.equals("")) {
            object = getCpuInfo.staticCpuInfo(getNodeId.getOnlineId());
        } else {
            List<Long> list = new ArrayList<>();
            for (int i = 0; i < para.split(",").length; i++) {
                list.add(Long.parseLong(para.split(",")[i]));
            }
            object = getCpuInfo.staticCpuInfo(getNodeId.getOnlineId(list));
        }
        for (String s : object.keySet()) {
            JSONObject jo = object.getJSONObject(s);
            res.append("\u001B[").append(91).append("m").append("节点ID:").append(s).append("\u001B[m").append("\r\n");
            int i = 1;
            for (String t : jo.keySet()) {
                res.append("\u001B[").append(91).append("m").append("CPU").append(i).append("\u001B[m").append("\r\n");
                JSONObject cpu = jo.getJSONObject(t);
                for (String r : cpu.keySet()) {
                    res.append(StringFormat.sufInsertSpaceFormat(r + ": ", 20)).append(cpu.getString(r)).append("\r\n");
                }
                i++;
            }
        }
        return res.toString();
    }

    public String cpuDynamic(String para) {
        JSONObject object;
        if (para.equals("")) {
            object = getCpuInfo.dynamicCpuInfo(getNodeId.getOnlineId());
        } else {
            List<Long> list = new ArrayList<>();
            for (int i = 0; i < para.split(",").length; i++) {
                list.add(Long.parseLong(para.split(",")[i]));
            }
            object = getCpuInfo.dynamicCpuInfo(getNodeId.getOnlineId(list));
        }

        StringBuilder res = new StringBuilder();
        res.append("\u001B[2J\u001B[H");
        for (String s : object.keySet()) {
            int i = 0;
            JSONObject jo = object.getJSONObject(s);
            res.append("\u001B[").append(91).append("m").append("节点ID:").append(s).append("\u001B[m").append("\r\n");
            for (String t : jo.keySet()) {
                Map<String, Double> nodes = new HashMap<>();
                res.append("\u001B[").append(91).append("m").append("CPU").append(i).append("\u001B[m").append("\r\n");
                JSONObject cpus = jo.getJSONObject(t);
                JSONObject cores = cpus.getJSONObject("core_state");
                for (String r : cores.keySet()) {
                    JSONObject core = cores.getJSONObject(r);
                    double user = core.getDouble("cpu_user");
                    double sys = core.getDouble("cpu_system");
                    double cs = Double.parseDouble(r);
                    String cpuUser = StringFormat.progressBar(StringFormat.roundByScale(user, 1), 92);
                    String cpuSystem = StringFormat.progressBar(StringFormat.roundByScale(sys, 1), 91);
                    String cpuinfo = StringFormat.sufInsertSpaceFormat(r, 4) +
                            StringFormat.progressBarFormat(cpuUser + cpuSystem, 55) +
                            StringFormat.sufInsertSpaceFormat(StringFormat.roundByScale(user + sys, 1) + "%", 7) +
                            StringFormat.roundByScale(core.getDouble("frequency") / 1000000, 1) + " GHz";
                    nodes.put(cpuinfo, cs);
                }
                nodes = Sort.sortMapByValues(nodes);
                for (String str : nodes.keySet()) {
                    res.append(str).append("\r\n");
                }
                i++;
            }
        }
        return res.toString();
    }
}
