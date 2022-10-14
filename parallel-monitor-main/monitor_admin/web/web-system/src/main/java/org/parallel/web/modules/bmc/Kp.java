package org.parallel.web.modules.bmc;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.parallel.web.exception.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class Kp {
    @Autowired
    private Rtc rtc;
    @Autowired
    @Lazy
    private Auth auth;
    private String tokenKey = "X-Auth-Token";

    private String getBaseUrl(String ip) {
        return "https://" + ip + "/redfish/v1/";
    }

    /**
     * 获取token url
     **/
    public String getAuthUrl(String ip) {
        return getBaseUrl(ip) + "SessionService/Sessions";
    }

    /**
     * 获取token
     **/
    public String getToken(String username, String password, String ip) {
        String url = getAuthUrl(ip);
        JSONObject body = new JSONObject();
        body.put("UserName", username);
        body.put("Password", password);

        Map map = new HashMap<String, String>();
        map.put("Content-Type", "application/json");
        map.put("Accept", "*/*");

        ResponseEntity<String> response = rtc.postResponseEntity(url, map, body.toJSONString());
        if (null == response) {
            throw new BadRequestException(StringLib.LOGIN_ERROR);
        }
        try {
            return response.getHeaders().get(tokenKey).get(0);
        } catch (Exception e) {
            throw new BadRequestException(StringLib.LOGIN_ERROR);
        }
    }

    /**
     * 获取电源控制url
     **/
    public String getPowerControlUrl(String ip) {
        return getBaseUrl(ip) + "Systems/1/Actions/ComputerSystem.Reset";
    }

    /**
     * 电源控制
     **/
    public boolean power(Long nodeId, int type) {
        BmcInfo bmcInfo = auth.getBmcInfo(nodeId);
        String url = getPowerControlUrl(bmcInfo.getIp());
        //添加报头
        Map<String, String> map = new HashMap<>();
        map.put("Content-Type", "application/json");
        map.put("Accept", "*/*");
        map.put(tokenKey, bmcInfo.getToken());
        //添加指令
        JSONObject body = new JSONObject();
        String operate = null;
        switch (type) {
            case 0:
                operate = "On";
                break;
            case 1:
                operate = "ForceOff";
                break;
            case 2:
                operate = "GracefulShutdown";
                break;
            case 3:
                operate = "ForceRestart";
                break;
            case 4:
                operate = "Nmi";
                break;
            case 5:
                operate = "ForcePowerCycle";
                break;
        }
        if (null == operate) {
            throw new BadRequestException(StringLib.BAD_OPERATION);
        }
        body.put("ResetType", operate);

        ResponseEntity<String> response = rtc.postResponseEntity(url, map, body.toJSONString());
        return response.getStatusCode() == HttpStatus.OK;
    }

    public Map<String, String> getMap(Long nodeId) {
        BmcInfo bmcInfo = auth.getBmcInfo(nodeId);
        Map<String, String> map = new HashMap<>();
        map.put("Content-Type", "application/json");
        map.put("Accept", "*/*");
        map.put(tokenKey, bmcInfo.getToken());
        return map;
    }

    public String getIp(Long nodeId) {
        BmcInfo bmcInfo = auth.getBmcInfo(nodeId);
        return bmcInfo.getIp();
    }

    /**
     * 获取系统集合资源url
     **/
    public String getSystemUrl(String ip) {
        return getBaseUrl(ip) + "Systems/1/";
    }

    /**
     * 获取机箱主板集合资源url
     **/
    public String getMainBoardUrl(String ip) {
        return getBaseUrl(ip) + "/Chassis/1/Boards/chassisMainBoard";
    }

    /**
     * 获取管理集合资源url
     **/
    public String getManagerUrl(String ip) {
        return getBaseUrl(ip) + "Managers/1/";
    }

    /**
     * 获取BIOS集合资源url
     **/
    public String getBiosUrl(String ip) {
        return getBaseUrl(ip) + "Systems/1/Bios/";
    }

    /**
     * 获取CPU集合资源url
     **/
    public String getProcessorsUrl(String ip) {
        return getBaseUrl(ip) + "Systems/1/Processors/";
    }

    /**
     * 获取内存集合资源url
     **/
    public String getMemoryUrl(String ip) {
        return getBaseUrl(ip) + "Systems/1/Memory/";
    }

    /**
     * 获取存储集合资源url
     **/
    public String getStorageUrl(String ip) {
        return getBaseUrl(ip) + "Systems/1/Storages/";
    }

    /**
     * 获取网卡集合资源url
     **/
    public String getNetworkUrl(String ip) {
        return getBaseUrl(ip) + "Chassis/1/NetworkAdapters/";
    }

    /**
     * 获取电源集合资源url
     **/
    public String getPowerUrl(String ip) {
        return getBaseUrl(ip) + "Chassis/1/Power/";
    }

    /**
     * 获取风扇集合资源url
     **/
    public String getFanUrl(String ip) {
        return getBaseUrl(ip) + "Chassis/1/Thermal";
    }
}
