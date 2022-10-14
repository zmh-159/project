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
public class FanInfo {
    @Autowired
    Kp kp;

    @Autowired
    private Rtc rtc;

    public JSONObject getFan(Long nodeId) {
        JSONObject res = new JSONObject();

        String url = kp.getFanUrl(kp.getIp(nodeId));
        ResponseEntity<String> response = rtc.getResponseEntity(url, kp.getMap(nodeId), null);

        JSONObject info = JSON.parseObject(response.getBody());
        JSONArray fansNum = info.getJSONArray("Fans");

        fansNum.forEach(key -> {
            JSONObject fan = new JSONObject();
            JSONObject fanJson = JSON.parseObject(key.toString());
            JSONObject huawei = fanJson.getJSONObject("Oem").getJSONObject("Huawei");

            fan.put("name", fanJson.get("Name"));
            fan.put("model", fanJson.get("Model"));
            fan.put("partNumber", fanJson.get("PartNumber"));
            fan.put("speedRatio", huawei.get("SpeedRatio"));
            fan.put("slotNumber", huawei.get("SlotNumber"));

            res.put(res.size() + "", fan);
        });

        return res;
    }
    /**
     * 查询进风口历史温度
     **/
    public JSONObject getInletHistoryTemperature(Long nodeId) {
        JSONObject res = new JSONObject();

        String url = kp.getFanUrl(kp.getIp(nodeId));
        ResponseEntity<String> response = rtc.getResponseEntity(url, kp.getMap(nodeId), null);

        JSONObject info = JSON.parseObject(response.getBody());
        JSONArray fansNum = info.getJSONArray("InletHistoryTemperature");

        fansNum.forEach(key -> {
            JSONObject fan = new JSONObject();
            JSONObject fanJson = JSON.parseObject(key.toString());
            JSONObject huawei = fanJson.getJSONObject("Data");

            fan.put("name", fanJson.get("Name"));
            fan.put("id", fanJson.get("Id"));
            fan.put("description", fanJson.get("Description"));
            fan.put("time", huawei.get("Time"));
            fan.put("inletTempCelsius", huawei.get("InletTempCelsius"));

            res.put(res.size() + "", fan);
        });

        return res;
    }
}
