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
public class MainBoard {
    @Autowired
    Kp kp;

    @Autowired
    private Rtc rtc;
    /**
     * 查询进主板信息
     **/
    public JSONObject getMainBoard(Long nodeId) {
        JSONObject res = new JSONObject();

        String url = kp.getMainBoardUrl(kp.getIp(nodeId));
        ResponseEntity<String> response = rtc.getResponseEntity(url, kp.getMap(nodeId), null);

        JSONObject info = JSON.parseObject(response.getBody());

        JSONObject board = new JSONObject();
        JSONObject status = info.getJSONObject("Status");
        board.put("allInfo", info);
        board.put("name", info.get("Name"));
        board.put("serialNum", info.get("SerialNumber"));
        board.put("health", status.get("Health"));
        board.put("state", status.get("State"));

        res.put(res.size() + "", board);

        return res;
    }
    public String a(){
        return "123";
    }
}
