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
public class PowerInfo {
    @Autowired
    Kp kp;

    @Autowired
    private Rtc rtc;

    public JSONObject getPower(Long nodeId) {
        JSONObject res = new JSONObject();

        String url = kp.getPowerUrl(kp.getIp(nodeId));
        ResponseEntity<String> response = rtc.getResponseEntity(url, kp.getMap(nodeId), null);

        JSONObject info = JSON.parseObject(response.getBody());
        JSONArray powerSupplies = info.getJSONArray("PowerSupplies");
        powerSupplies.forEach(key -> {
            JSONObject power = new JSONObject();
            JSONObject powerJson = JSON.parseObject(key.toString());
            JSONObject huawei = powerJson.getJSONObject("Oem").getJSONObject("Huawei");

            power.put("name", powerJson.get("Name"));
            power.put("slotNumber", huawei.get("SlotNumber"));
            power.put("partNumber", powerJson.get("PartNumber"));
            power.put("manufacturer", powerJson.get("Manufacturer"));
            power.put("model", powerJson.get("Model"));
            power.put("sn", powerJson.get("SerialNumber"));
            power.put("firmwareVersion", powerJson.get("FirmwareVersion"));
            power.put("ratedPower", powerJson.get("PowerCapacityWatts"));
            power.put("type", powerJson.get("PowerSupplyType"));
            power.put("inputVoltage", powerJson.get("LineInputVoltage"));
            power.put("outputVoltage", huawei.get("OutputVoltage"));

            res.put(res.size() + "", power);
        });

        return res;
    }
}
