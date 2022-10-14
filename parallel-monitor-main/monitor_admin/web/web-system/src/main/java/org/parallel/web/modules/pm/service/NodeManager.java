package org.parallel.web.modules.pm.service;
import com.alibaba.fastjson.JSONObject;
/**
 * @website https://el-admin.vip
 * @description 定义节点管理类的功能
 * @author yuyifade
 * @date 2022-02-20
 **/
public interface NodeManager {
    JSONObject getIps();

    String getIp(String uuid);




}
