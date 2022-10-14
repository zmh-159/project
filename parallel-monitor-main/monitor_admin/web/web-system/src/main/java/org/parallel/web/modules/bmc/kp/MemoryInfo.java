package org.parallel.web.modules.bmc.kp;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.parallel.web.modules.bmc.Kp;
import org.parallel.web.modules.bmc.Rtc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@Service
public class MemoryInfo {
    @Autowired
    Kp kp;

    @Autowired
    private Rtc rtc;

    public JSONArray getMemoryNum(Long nodeId) {
        String url = kp.getMemoryUrl(kp.getIp(nodeId));

        ResponseEntity<String> response = rtc.getResponseEntity(url, kp.getMap(nodeId), null);
        JSONObject resJson = JSON.parseObject(response.getBody());
        return resJson.getJSONArray("Members");
    }

    public JSONObject getMemory(Long nodeId) {
        JSONObject res = new JSONObject();
        JSONArray memArray = getMemoryNum(nodeId);

        memArray.forEach(key -> {
            JSONObject object = JSON.parseObject(key.toString());
            String memUrl = object.getString("@odata.id").split("Memory/")[1];

            String url = kp.getMemoryUrl(kp.getIp(nodeId)) + memUrl;
            ResponseEntity<String> response = rtc.getResponseEntity(url, kp.getMap(nodeId), null);

            JSONObject mem = new JSONObject();
            JSONObject info = JSON.parseObject(response.getBody());
            JSONObject huawei = info.getJSONObject("Oem").getJSONObject("Huawei");

            mem.put("allInfo", info);
            mem.put("name",info.get("Name"));
            mem.put("manufacturer", info.get("Manufacturer"));
            mem.put("type", info.get("MemoryDeviceType"));
            mem.put("speed", info.get("OperatingSpeedMhz"));
            mem.put("maxSpeed", info.get("AllowedSpeedsMHz"));
            mem.put("capacity", info.get("CapacityMiB"));
            mem.put("position",huawei.get("Position"));
            mem.put("partNumber",info.get("PartNumber"));
            mem.put("sn",info.get("SerialNumber"));
            mem.put("minVoltage",huawei.get("MinVoltageMillivolt"));
            mem.put("width",info.get("DataWidthBits"));
            mem.put("rankCount",info.get("RankCount"));

            res.put(res.size() + "", mem);
        });
        System.out.println(res);
        return res;
    }
}
