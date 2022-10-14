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
public class NetworkInfo {
    @Autowired
    Kp kp;

    @Autowired
    private Rtc rtc;

    public JSONArray getNetWorkNum(Long nodeId) {
        String url = kp.getNetworkUrl(kp.getIp(nodeId));

        ResponseEntity<String> response = rtc.getResponseEntity(url, kp.getMap(nodeId), null);
        JSONObject resJson = JSON.parseObject(response.getBody());
        return resJson.getJSONArray("Members");
    }

    public JSONObject getNetWork(Long nodeId) {
        JSONObject res = new JSONObject();
        JSONArray netArray = getNetWorkNum(nodeId);
        netArray.forEach(key -> {
            JSONObject object = JSON.parseObject(key.toString());
            String netUrl = object.getString("@odata.id").split("NetworkAdapters/")[1];

            String url = kp.getNetworkUrl(kp.getIp(nodeId)) + netUrl;
            ResponseEntity<String> response = rtc.getResponseEntity(url, kp.getMap(nodeId), null);

            JSONObject info = JSON.parseObject(response.getBody());
            JSONObject net = new JSONObject();
            JSONObject huawei = info.getJSONObject("Oem").getJSONObject("Huawei");

            net.put("allInfo", info);
            net.put("name", huawei.get("Name"));
            net.put("chipManufacturer", info.get("Manufacturer"));
            net.put("manufacturer", huawei.get("CardManufacturer"));
            net.put("model", huawei.get("CardModel"));
            net.put("chipModel", info.get("Model"));
            net.put("resource", huawei.get("AssociatedResource"));
            net.put("PCBVersion", huawei.get("PCBVersion"));
            net.put("rootBDF", huawei.get("RootBDF"));
            net.put("boardIdHex", huawei.get("BoardIdHex"));

            /**
             * 获取网卡的端口集合
             **/
            String portCountStr = info.getJSONObject("NetworkPorts").getString("@odata.id");
            String portCountUrl = "https://" + kp.getIp(nodeId) + portCountStr;
            ResponseEntity<String> portCountResponse = rtc.getResponseEntity(portCountUrl, kp.getMap(nodeId), null);

            JSONObject portCountJson = JSON.parseObject(portCountResponse.getBody());
            JSONArray netWorkPort = portCountJson.getJSONArray("Members");

            JSONArray portInfo = new JSONArray();
            netWorkPort.forEach(str -> {
                JSONObject portJson = JSON.parseObject(str.toString());
                String portStr = portJson.getString("@odata.id");

                String portUrl = "https://" + kp.getIp(nodeId) + portStr;
                ResponseEntity<String> portResponse = rtc.getResponseEntity(portUrl, kp.getMap(nodeId), null);

                JSONObject port = new JSONObject();
                JSONObject portJsonInfo = JSON.parseObject(portResponse.getBody());
                JSONObject portHuawei = portJsonInfo.getJSONObject("Oem").getJSONObject("Huawei");

                port.put("name", portJsonInfo.get("Name"));
                port.put("status", portJsonInfo.get("LinkStatus"));
                port.put("portType", portHuawei.get("PortType"));
                port.put("interfaceType", portHuawei.get("InterfaceType"));
                port.put("busInfo", portHuawei.get("BDF"));
                port.put("mac", portJsonInfo.get("AssociatedNetworkAddresses"));

                portInfo.add(port);
            });
            net.put("portInfo", portInfo);
            res.put(res.size() + "", net);
        });
        System.out.println(res);
        return res;
    }
}
