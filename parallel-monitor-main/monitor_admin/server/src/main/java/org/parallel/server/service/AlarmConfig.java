package org.parallel.server.service;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.fastjson.JSONObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * @author yuyifade
 * @description 获取报警项相关配置
 * @date 2021/11/15 下午9:24
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class AlarmConfig {
    private final DruidDataSource dataSource;

    public JSONObject getConfig(long nodeId) {
        String sql = "SELECT pas.effect_node,pas.detail,pas.alarm_scheme_id FROM pm_alarm_scheme pas WHERE STATUS =1";
        JSONObject config = new JSONObject();
        try (Connection con = dataSource.getConnection()) {
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String nodeIdsStr = rs.getString(1);
                String nodeIds[] = nodeIdsStr.split(",");
                String nis = nodeId + "";
                for (int i = 0; i < nodeIds.length; ++i) {
                    if (nodeIds[i].equals(nis)) {
                        config.put(rs.getString(3), JSONObject.parseObject(rs.getString(2)));
                    }
                }
            }
            ps.close();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return config;
    }

    /***
     * @description 批量获取报警方案
     * @author yuyifade
     * @date 2021/12/8 下午7:28
     * @param nodeIds
     * @return com.alibaba.fastjson.JSONObject
     */
    public JSONObject getConfigs(String nodeIds[]) {
        String sql = "SELECT pas.effect_node,pas.detail,pas.alarm_scheme_id FROM pm_alarm_scheme pas WHERE STATUS =1";
        //初始化容器
        JSONObject config = new JSONObject();
        for (String nodeId : nodeIds) {
            JSONObject jo = new JSONObject();
            config.put(nodeId, jo);
        }

        //查询符合条件的报警策略
        try (Connection con = dataSource.getConnection()) {
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String nodeIdsStr = rs.getString(1);
                String alarmSchemeId = rs.getString(3);
                JSONObject alarmScheme = JSONObject.parseObject(rs.getString(2));

                for (String nstr : nodeIdsStr.split(",")) {
                    for (String nodeId : nodeIds) {
                        if (nstr.equals(nodeId)) {
                            config.getJSONObject(nstr).put(alarmSchemeId, alarmScheme);
                        }
                    }
                }
            }
            ps.close();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return config;
    }


}
