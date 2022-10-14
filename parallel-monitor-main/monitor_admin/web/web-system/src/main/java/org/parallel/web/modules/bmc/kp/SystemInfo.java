package org.parallel.web.modules.bmc.kp;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.parallel.web.modules.bmc.Kp;
import org.parallel.web.modules.bmc.Rtc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class SystemInfo {
    @Autowired
    Kp kp;

    @Autowired
    private Rtc rtc;

    public JSONObject getSystem(Long nodeId) {
        JSONObject res = new JSONObject();

        /**
         * 获取系统集合资源
         **/
        String systemUrl = kp.getSystemUrl(kp.getIp(nodeId));
        ResponseEntity<String> systemResponse = rtc.getResponseEntity(systemUrl, kp.getMap(nodeId), null);

        JSONObject system = new JSONObject();
        JSONObject systemInfo = JSON.parseObject(systemResponse.getBody());
        JSONObject systemHuawei = systemInfo.getJSONObject("Oem").getJSONObject("Huawei");

        system.put("name", systemHuawei.get("ProductName"));
        system.put("alias", systemHuawei.get("ProductAlias"));
        system.put("manufacturer", systemInfo.get("Manufacturer"));
        system.put("sn", systemInfo.get("SerialNumber"));
        system.put("partNumber", systemInfo.get("PartNumber"));

        /**
         * 获取主板集合资源
         **/
        String mainBoardUrl = kp.getMainBoardUrl(kp.getIp(nodeId));
        ResponseEntity<String> mainBoardResponse = rtc.getResponseEntity(mainBoardUrl, kp.getMap(nodeId), null);

        /**
         * 获取管理集合资源
         **/
        String managerUrl = kp.getManagerUrl(kp.getIp(nodeId));
        ResponseEntity<String> managerResponse = rtc.getResponseEntity(managerUrl, kp.getMap(nodeId), null);
        JSONObject managerInfo = JSON.parseObject(managerResponse.getBody());

        JSONObject mainBoard = new JSONObject();
        JSONObject mainBoardInfo = JSON.parseObject(mainBoardResponse.getBody());
        mainBoard.put("boardName", mainBoardInfo.get("BoardName"));
        mainBoard.put("boardSN", mainBoardInfo.get("SerialNumber"));
        mainBoard.put("boardId", mainBoardInfo.get("BoardId"));
        mainBoard.put("partNumber", mainBoardInfo.get("PartNumber"));
        mainBoard.put("biosVersion", systemInfo.get("BiosVersion"));
        mainBoard.put("boardManufacturer", mainBoardInfo.get("Manufacturer"));
        mainBoard.put("pcbVersion", mainBoardInfo.get("PCBVersion"));
        mainBoard.put("CPLDVersion", mainBoardInfo.get("CPLDVersion"));
        mainBoard.put("firmwareVersion", managerInfo.get("FirmwareVersion"));

        res.put("system", system);
        res.put("mainBoard", mainBoard);
        return res;
    }
}
