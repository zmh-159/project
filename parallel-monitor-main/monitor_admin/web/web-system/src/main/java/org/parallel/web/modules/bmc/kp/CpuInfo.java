package org.parallel.web.modules.bmc.kp;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.parallel.web.modules.bmc.Kp;
import org.parallel.web.modules.bmc.Rtc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CpuInfo {
    @Autowired
    Kp kp;

    @Autowired
    private Rtc rtc;


    public JSONArray getProcessorNum(Long nodeId) {
        String url = kp.getProcessorsUrl(kp.getIp(nodeId));

        ResponseEntity<String> response = rtc.getResponseEntity(url, kp.getMap(nodeId), null);
        JSONObject resJson = JSON.parseObject(response.getBody());
        return resJson.getJSONArray("Members");
    }

    public JSONObject getCpu(Long nodeId) {
        JSONObject res = new JSONObject();
        JSONArray cpuArray = getProcessorNum(nodeId);
        List<String> cpuUrl = new ArrayList<>();
        cpuArray.forEach(key -> {
            JSONObject object = JSON.parseObject(key.toString());
            cpuUrl.add(object.getString("@odata.id"));
        });

        cpuUrl.forEach(str -> {
            JSONObject cpu = new JSONObject();
            str = str.split("Processors/")[1];
            if (!str.contains("Gpu") && !str.contains("Npu")) {
                String url = kp.getProcessorsUrl(kp.getIp(nodeId)) + str;
                ResponseEntity<String> response = rtc.getResponseEntity(url, kp.getMap(nodeId), null);

                JSONObject info = JSON.parseObject(response.getBody());
                JSONObject huawei = info.getJSONObject("Oem").getJSONObject("Huawei");
                String status = info.getJSONObject("Status").getString("State");

                cpu.put("allInfo", info);
                cpu.put("status", status);
                cpu.put("name", info.get("Name"));
                cpu.put("model", info.get("Model"));
                cpu.put("manufacturer", info.get("Manufacturer"));
                cpu.put("sn", huawei.get("SerialNumber"));
                cpu.put("processorId", info.getJSONObject("ProcessorId").get("IdentificationRegisters"));
                cpu.put("other", huawei.get("OtherParameters"));
                cpu.put("cores", info.get("TotalCores"));
                cpu.put("threads", info.get("TotalThreads"));
                cpu.put("arch", info.get("ProcessorArchitecture"));
                cpu.put("mainFrequency", info.get("MaxSpeedMHz"));
                cpu.put("L1Cache", huawei.get("L1CacheKiB"));
                cpu.put("L2Cache", huawei.get("L2CacheKiB"));
                cpu.put("L3Cache", huawei.get("L3CacheKiB"));

                res.put(res.size() + "", cpu);
            }
        });
        return res;
    }

}
