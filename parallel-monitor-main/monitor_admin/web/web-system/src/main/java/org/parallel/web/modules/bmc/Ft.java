package org.parallel.web.modules.bmc;

import com.alibaba.fastjson.JSONObject;
import org.parallel.web.exception.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class Ft {
    @Autowired
    private Rtc rtc;
    @Autowired
    @Lazy
    private Auth auth;
    private String tokenKey = "X-Auth-Token";

    private String getBaseUrl(String ip) {
        return "https://" + ip + "/redfish/v1/";
    }

    /** 获取token url **/
    public String getAuthUrl(String ip) {
        return getBaseUrl(ip) + "SessionService/Sessions";
    }

    /** 获取token **/
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

    /** 获取电源控制url **/
    public String getPowerUrl(String ip) {
        return getBaseUrl(ip) + "Systems/system/Actions/ComputerSystem.Reset";
    }

    /** 电源控制 **/
    public boolean power(Long nodeId, int type) {
        BmcInfo bmcInfo = auth.getBmcInfo(nodeId);
        String url = getPowerUrl(bmcInfo.getIp());
        //添加报头
        Map map = new HashMap<String, String>();
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
                operate = "GracefulRestart";
                break;
        }
        if (null == operate) {
            throw new BadRequestException(StringLib.BAD_OPERATION);
        }
        body.put("ResetType", operate);

        ResponseEntity<String> response = rtc.postResponseEntity(url, map, body.toJSONString());
        return response.getStatusCode() == HttpStatus.OK;
    }
    
}
