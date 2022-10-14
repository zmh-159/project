package org.parallel.web.modules.bmc.kp;

import com.alibaba.fastjson.JSONObject;
import org.parallel.web.modules.bmc.access.NodesStatus;
import org.springframework.beans.factory.annotation.Autowired;

public class NodeInfo {
    @Autowired
    CpuInfo cpuInfo;
    @Autowired
    MemoryInfo memoryInfo;
    @Autowired
    NodesStatus nodesStatus;
    public JSONObject getNode(Long nodeId){
        JSONObject mes = new JSONObject();
        JSONObject cpus = cpuInfo.getCpu(nodeId);
        String cpuType =new String();
        for (String cpu:cpus.keySet()) {
            cpuType=cpuType+cpus.getJSONObject(cpu).getString("name")+"\n";
        }
        mes.put("主机名",nodeId);
        mes.put("CPU型号",cpuType);
        mes.put("内存","");
        mes.put("磁盘","");
        mes.put("状态",nodesStatus.status.get(nodeId));
        return mes;
    }
}
