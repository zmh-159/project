package org.parallel.server.cmd.service;

import com.alibaba.fastjson.JSONObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.parallel.common.utils.cmd.StringFormat;
import org.parallel.server.cmd.model.GetNodeId;
import org.parallel.server.cmd.model.GetNodeInfo;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class NodeService {
    private final GetNodeId getNodeId;
    private final GetNodeInfo getNodeInfo;

    public StringBuilder getClusterInfo(String para) {
        List<String> nodeNum = getNodeId.allNodeId();
        JSONObject dy = getNodeInfo.dynamicNodeInfo(getNodeId.getOnlineId());
        JSONObject st = getNodeInfo.staticNodeInfo(getNodeId.getOnlineId());
        StringBuilder strCpu = new StringBuilder();
        StringBuilder strMem = new StringBuilder();
        StringBuilder strDisk = new StringBuilder();
        StringBuilder res = new StringBuilder();
        StringBuilder cpuStr = new StringBuilder();
        StringBuilder memStr = new StringBuilder();
        StringBuilder diskStr = new StringBuilder();
        res.append("\u001B[0;0H\u001B[2K");
        int cores = 0;
        double cpu_user = 0.0;
        double cpu_sys = 0.0;
        double memory_total = 0.0;
        double memory_used = 0.0;
        double disk_total = 0.0;
        double disk_used = 0.0;
        int i = 0;
        int nodes = dy.size();
        for (String s : st.keySet()) {
            JSONObject js = JSONObject.parseObject(st.getString(s));
            JSONObject cpu = JSONObject.parseObject(js.getString("cpu"));
            for (String r : cpu.keySet()) {
                JSONObject core = JSONObject.parseObject(cpu.getString(r));
                cores += core.getInteger("cores");
            }

        }

        for (String s : dy.keySet()) {
            JSONObject js = JSONObject.parseObject(dy.getString(s));
            cpu_user += js.getDouble("cpu_user");
            cpu_sys += js.getDouble("cpu_system");
            memory_total += js.getDouble("memory_total");
            memory_used += js.getDouble("memory_used");
            disk_total += js.getDouble("disk_total");
            disk_used += js.getDouble("disk_used");

            StringBuilder cpu_str = new StringBuilder();
            StringBuilder mem_str = new StringBuilder();
            StringBuilder disk_str = new StringBuilder();
            String cpuUser = StringFormat.progressBar(StringFormat.roundByScale(js.getDouble("cpu_user"), 1), 92);
            String cpuSystem = StringFormat.progressBar(StringFormat.roundByScale(js.getDouble("cpu_system"), 1), 91);
            String cpu = StringFormat.progressBarFormat(cpuUser + cpuSystem, 55);
            String mem = StringFormat.progressBarFormat(StringFormat.progressBar(StringFormat.roundByScale(js.getDouble("memory_used") / js.getDouble("memory_total") * 100, 1), 92), 55);
            String disk = StringFormat.progressBarFormat(StringFormat.progressBar(StringFormat.roundByScale(js.getDouble("disk_used") / js.getDouble("disk_total") * 100, 1), 92), 55);
            cpu_str.append("[").append(cpu).append(StringFormat.preInsertSpaceFormat(StringFormat.roundByScale(js.getDouble("cpu_user") + js.getDouble("cpu_system"), 1) + "", 5)).append("%]").append("\r\n");
            mem_str.append("[").append(mem).append(StringFormat.preInsertSpaceFormat(StringFormat.roundByScale(js.getDouble("memory_used") / js.getDouble("memory_total") * 100, 1) + "", 5)).append("%]").append(StringFormat.preInsertSpaceFormat(StringFormat.roundByScale(memory_used, 2) + "G/" + StringFormat.roundByScale(memory_total, 2) + "G", 20)).append("\r\n");
            disk_str.append("[").append(disk).append(StringFormat.preInsertSpaceFormat(StringFormat.roundByScale(js.getDouble("disk_used") / js.getDouble("disk_total") * 100, 1) + "", 5)).append("%]").append(StringFormat.preInsertSpaceFormat(StringFormat.roundByScale(disk_used, 2) + "G/" + StringFormat.roundByScale(disk_total, 2) + "G", 20)).append("\r\n");

            if (i == dy.size() - 1) {
                strCpu.append(StringFormat.sufInsertSpaceFormat("└─节点" + (i + 1), 10)).append(cpu_str);
                strMem.append(StringFormat.sufInsertSpaceFormat("└─节点" + (i + 1), 10)).append(mem_str);
                strDisk.append(StringFormat.sufInsertSpaceFormat("└─节点" + (i + 1), 10)).append(disk_str);
            } else {
                strCpu.append(StringFormat.sufInsertSpaceFormat("├─节点" + (i + 1), 10)).append(cpu_str);
                strMem.append(StringFormat.sufInsertSpaceFormat("├─节点" + (i + 1), 10)).append(mem_str);
                strDisk.append(StringFormat.sufInsertSpaceFormat("├─节点" + (i + 1), 10)).append(disk_str);
            }
            i++;
        }
        cpu_user = StringFormat.roundByScale(cpu_user / nodes, 1);
        cpu_sys = StringFormat.roundByScale(cpu_sys / nodes, 1);
        double memory_rate = StringFormat.roundByScale(memory_used / memory_total * 100, 1);
        double disk_rate = StringFormat.roundByScale(disk_used / disk_total * 100, 1);

        String cpu = StringFormat.progressBarFormat(StringFormat.progressBar(cpu_user, 92) + StringFormat.progressBar(cpu_sys, 91), 55);
        String mem = StringFormat.progressBarFormat(StringFormat.progressBar(memory_rate, 92), 55);
        String disk = StringFormat.progressBarFormat(StringFormat.progressBar(disk_rate, 92), 55);
        cpuStr.append(StringFormat.sufInsertSpaceFormat("CPU", 12)).append("[").append(cpu).append(StringFormat.preInsertSpaceFormat(StringFormat.roundByScale(cpu_user + cpu_sys, 1) + "", 5)).append("%]").append("\r\n");
        memStr.append(StringFormat.sufInsertSpaceFormat("MEM", 12)).append("[").append(mem).append(StringFormat.preInsertSpaceFormat(memory_rate + "", 5)).append("%] ").append(StringFormat.preInsertSpaceFormat(StringFormat.roundByScale(memory_used, 2) + "G/" + StringFormat.roundByScale(memory_total, 2) + "G", 20)).append("\r\n");
        diskStr.append(StringFormat.sufInsertSpaceFormat("DISK", 12)).append("[").append(disk).append(StringFormat.preInsertSpaceFormat(disk_rate + "", 5)).append("%] ").append(StringFormat.preInsertSpaceFormat(StringFormat.roundByScale(disk_used, 2) + "G/" + StringFormat.roundByScale(disk_total, 2) + "G", 20)).append("\r\n");

        res.append("总节点数: ").append(nodeNum.size()).append("     当前节点数: ").append(nodes).append("     总核心数： ").append(cores).append("\r\n");
        switch (para) {
            case "-lc":
                return res.append(cpuStr).append(strCpu).append(memStr).append(diskStr);
            case "-lm":
                return res.append(cpuStr).append(memStr).append(strMem).append(diskStr);
            case "-ld":
                return res.append(cpuStr).append(memStr).append(diskStr).append(strDisk);
            case "-lcmd":
                return res.append(cpuStr).append(strCpu).append(memStr).append(strMem).append(diskStr).append(strDisk);
            default:
                return res.append(cpuStr).append(memStr).append(diskStr);
        }

    }

    public StringBuilder getClusterInfo() {
        return getClusterInfo("");
    }
}
