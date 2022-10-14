package org.parallel.client.service;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.parallel.client.config.DeviceConfig;
import org.parallel.client.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @author yuyifade
 * @description 维护设备uuid和设备id
 * @date 2021/9/3 上午11:14
 */
@Slf4j
@Service
public class DeviceId {
    @Autowired
    private DeviceConfig deviceConfig;
    @Autowired
    private RedisUtils redisUtils;

    /***
     * @description 获取设备uuid
     * @author yuyifade
     * @date 2021/9/3 上午11:36
     * @return java.lang.String
     */
    public String getUuid() {
        String key = deviceConfig.getUuid();
        String uuid = redisUtils.get(key);
        if (uuid == null) {
            uuid = UUID.randomUUID().toString().replaceAll("-", "");
            redisUtils.set(key, uuid);
            log.info("生成uuid:" + uuid);
        }
        return uuid;
    }

    /***
     * @description 获取设备数据库id
     * @author yuyifade
     * @date 2021/9/3 上午11:37
     * @return com.alibaba.fastjson.JSONObject
     */
    public JSONObject getDeviceId() {
        String key = deviceConfig.getDeviceId();
        String deviceIdStr = redisUtils.get(key);
        JSONObject res = JSONObject.parseObject(deviceIdStr);
        if (res != null) {
            return res;
        } else {
            return new JSONObject();
        }
    }

    /***
     * @description 更新设备数据库id
     * @author yuyifade
     * @date 2021/9/3 上午11:37
     * @return com.alibaba.fastjson.JSONObject
     */
    public void setDeviceId(JSONObject o) {
        redisUtils.set(deviceConfig.getDeviceId(), o.toJSONString());
    }


}
