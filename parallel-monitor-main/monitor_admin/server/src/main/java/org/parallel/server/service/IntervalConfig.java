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
 * @description 获取采样间隔相关配置
 * @date 2021/11/15 下午9:24
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class IntervalConfig {
    private final DruidDataSource dataSource;

    public JSONObject getConfig() {
        int bigInterval = 600;
        int smallInterval = 5;
        int mode = 0;
        try (Connection con = dataSource.getConnection()) {
            String queryBigInterval = "SELECT collocation FROM pm_config WHERE keyword=\"big_interval\"";
            String querySmallInterval = "SELECT collocation FROM pm_config WHERE keyword=\"small_interval\"";
            String queryMode = "SELECT collocation FROM pm_config WHERE keyword=\"mode\"";
            PreparedStatement ps = con.prepareStatement(queryBigInterval);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                bigInterval = rs.getInt(1);
            }
            ps = con.prepareStatement(querySmallInterval);
            rs = ps.executeQuery();
            if (rs.next()) {
                smallInterval = rs.getInt(1);
            }

            ps = con.prepareStatement(queryMode);
            rs = ps.executeQuery();
            if (rs.next()) {
                mode = rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        JSONObject jo = new JSONObject();
        jo.put("big_interval", bigInterval);
        jo.put("small_interval", smallInterval);
        jo.put("mode", mode);
        return jo;
    }

}
