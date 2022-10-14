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
public class StorageInfo {
    @Autowired
    Kp kp;

    @Autowired
    private Rtc rtc;

    public JSONArray getStorageNum(Long nodeId) {
        String url = kp.getStorageUrl(kp.getIp(nodeId));
        ResponseEntity<String> response = rtc.getResponseEntity(url, kp.getMap(nodeId), null);
        JSONObject resJson = JSON.parseObject(response.getBody());

        return resJson.getJSONArray("Members");
    }

    public JSONObject getStorage(Long nodeId) {
        JSONObject res = new JSONObject();
        JSONArray storageArray = getStorageNum(nodeId);

        storageArray.forEach(key -> {
            JSONObject object = JSON.parseObject(key.toString());
            String storageUrl = object.getString("@odata.id").split("Storages/")[1];

            /**
             * 获取阵列卡资源集合
             **/
            String url = kp.getStorageUrl(kp.getIp(nodeId)) + storageUrl;
            ResponseEntity<String> response = rtc.getResponseEntity(url, kp.getMap(nodeId), null);

            JSONObject storage = new JSONObject();
            JSONObject info = JSON.parseObject(response.getBody());
            JSONObject raidInfo = info.getJSONArray("StorageControllers").getJSONObject(0);


            JSONObject status = raidInfo.getJSONObject("Status");
            JSONObject huawei = raidInfo.getJSONObject("Oem").getJSONObject("Huawei");

            storage.put("allInfo", raidInfo);
            storage.put("name", raidInfo.get("Name"));
            storage.put("model", raidInfo.get("Model"));
            storage.put("firmwareVersion", raidInfo.get("FirmwareVersion"));
            storage.put("health", status.get("Health"));
            storage.put("supportedModes", huawei.get("SupportedModes"));
            storage.put("supportedDeviceProtocols", raidInfo.get("SupportedDeviceProtocols"));
            storage.put("speedGbps", raidInfo.get("SpeedGbps"));
            storage.put("minStripeSizeBytes", huawei.get("MinStripeSizeBytes"));
            storage.put("maxStripeSizeBytes", huawei.get("MaxStripeSizeBytes"));
            storage.put("configurationVersion", huawei.get("ConfigurationVersion"));
            storage.put("type", huawei.get("Type"));
            storage.put("supportedRAIDTypes", raidInfo.get("SupportedRAIDTypes"));
            storage.put("memorySizeMiB", huawei.get("MemorySizeMiB"));
            storage.put("SASAddress", huawei.get("SASAddress"));

            /**
             * 获取磁盘资源集合
             **/
            JSONArray diskArray = info.getJSONArray("Drives");
            JSONArray diskInfo = new JSONArray();
            diskArray.forEach(value -> {
                String diskUrl = JSON.parseObject(value.toString()).getString("@odata.id");
                diskUrl = "https://" + kp.getIp(nodeId) + diskUrl;
                ResponseEntity<String> diskResponse = rtc.getResponseEntity(diskUrl, kp.getMap(nodeId), null);

                JSONObject diskJson = JSON.parseObject(diskResponse.getBody());
                JSONObject disk = new JSONObject();
                JSONObject diskStatus = diskJson.getJSONObject("Status");
                JSONObject diskHuawei = diskJson.getJSONObject("Oem").getJSONObject("Huawei");

                disk.put("name", diskJson.get("Name"));
                disk.put("protocol", diskJson.get("Protocol"));
                disk.put("manufacturer", diskJson.get("Manufacturer"));
                disk.put("sn", diskJson.get("SerialNumber"));
                disk.put("mediaType", diskJson.get("MediaType"));
                disk.put("firmwareStatus", diskHuawei.get("FirmwareStatus"));
                disk.put("SASAddress", diskHuawei.get("SASAddress"));
                disk.put("capableSpeedGbs", diskJson.get("CapableSpeedGbs"));
                disk.put("negotiatedSpeedGbs", diskJson.get("NegotiatedSpeedGbs"));
                disk.put("powerState", diskHuawei.get("PowerState"));
                disk.put("predictedMediaLifeLeftPercent", diskJson.get("PredictedMediaLifeLeftPercent"));
                disk.put("health", diskStatus.get("Health"));
                disk.put("model", diskStatus.get("Model"));
                disk.put("revision", diskStatus.get("Revision"));

                diskInfo.add(disk);
            });
            storage.put("diskInfo", diskInfo);
            res.put(res.size() + "", storage);
        });
        System.out.println(res);
        return res;
    }
}
