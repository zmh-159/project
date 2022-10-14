package org.parallel.web.modules.pm.server;
import com.alibaba.fastjson.JSONObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.parallel.common.utils.CommonUtils;

import org.parallel.web.utils.RedisUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import java.sql.Timestamp;
import java.util.*;

/**
 * @author yuyifade
 * @description 存储节点最新信息及ip
 * @date 2021/7/24 下午1:15
 */
@RequiredArgsConstructor
@Component
@Slf4j
public class OnlineNode {
    //todo
    private static final String key = "NodeInfoCache";
    private final RedisUtils redisUtils;
    /**
     * 已接入节点列表
     **/
    private static final HashSet<Long> ids = new HashSet<>();
    /**
     * 节点动态信息
     **/
    private static final Hashtable<Long, JSONObject> infos = new Hashtable<>();
    /**
     * 节点ip信息
     **/
    private static final Hashtable<Long, String> ips = new Hashtable<>();


    public String getNodeInfo() {
        JSONObject nodes = new JSONObject();
        long time = System.currentTimeMillis();


        String alarmMessage = "";
        //熵
        int entropy = 0, maxEntropy = 1000;
        //核心
        int cpuTotal = 0;
        double diskUsed = 0, diskTotal = 0, gpuUse = 0, cpuBusy = 0, memoryUsed = 0, memoryTotal = 0;
        byte flag[] = new byte[maxEntropy];
        for (Long i : infos.keySet()) {
            JSONObject tmp = infos.get(i);
            Timestamp ct = tmp.getTimestamp("create_time");
            long now = (time - ct.getTime()) / 1000;
            //传输50s内接收的数据
            if (now < 50) {
                cpuTotal += tmp.getJSONObject("cpu_state").size() - 1;
                diskUsed += tmp.getDoubleValue("disk_used");
                diskTotal += tmp.getDoubleValue("disk_total");
                //todo 计算总数然后平均
                gpuUse = (gpuUse + tmp.getDoubleValue("gpu_utilization")) / 2;
                cpuBusy = (cpuBusy + tmp.getDoubleValue("cpu_busy")) / 2;

                memoryUsed += tmp.getDoubleValue("memory_used");
                memoryTotal += tmp.getDoubleValue("memory_total");
                //求解熵，统计坐标位置
                int p = place(tmp);
                if (flag[p] != 1) {
                    entropy++;
                    flag[p] = 1;
                }
                nodes.put("" + i, tmp);
                //提供一条报警消息
                JSONObject option = getOption();
                if (alarmMessage.length() == 0) {
                    JSONObject alarms = tmp.getJSONObject("alarm");
                    if (alarms.size() > 0) {
                        for (String key : alarms.keySet()) {
                            JSONObject alarm = alarms.getJSONObject(key);
                            if (alarm.getIntValue("add_type") == 0) {
                                String chineseOption = option.getString(key);
                                alarmMessage = tmp.getLongValue("node_id") + "节点" + chineseOption + "异常:" + alarm.getString("outlier");
                            }
                        }
                    }
                }
            }

        }
        JSONObject res = new JSONObject();
        res.put("node_data", nodes);
        res.put("cpu_nums", cpuTotal);
        res.put("cpu_busy", CommonUtils.getDoubleWith2(cpuBusy));
        res.put("gpu_utilization", CommonUtils.getDoubleWith2(gpuUse));
        res.put("disk_use", CommonUtils.getDoubleWith2(diskUsed));
        res.put("disk_total", diskTotal);
        res.put("memory_used", CommonUtils.getDoubleWith2(memoryUsed));
        res.put("memory_total", memoryTotal);

        res.put("alarm_message", alarmMessage);
        res.put("entropy", Math.log(entropy) / Math.log(maxEntropy));
        return res.toJSONString();
    }

    public String getOnlineId() {
        StringBuilder buffer = new StringBuilder();
        buffer.append('(');
        long now = System.currentTimeMillis();
        for (long i : infos.keySet()) {
            long createTime = infos.get(i).getLongValue("create_time");
            //todo 根据采样间隔判断是否离线
            if ((now - createTime) / 1000 < 50) {
                buffer.append(i);
                buffer.append(',');
            }
        }
        if (buffer.length() == 1) {
            buffer.append(')');
        } else {
            buffer.replace(buffer.length() - 1, buffer.length(), ")");
        }
        return buffer.toString();
    }

    @Async
    public void updateNodeInfo(JSONObject info, String ip) {
        Long nodeId = info.getLong("node_id");
        if (nodeId != null) {
            ips.put(nodeId, ip);
            infos.put(nodeId, info);
        }
    }

    public String getIp(Long nodeId) {
        return ips.get(nodeId);
    }

    public Hashtable<Long, String> getIps() {
        return ips;
    }

    public HashSet<Long> getIds() {
        log.info(Thread.currentThread().getName());
        return ids;
    }

    public void updateIds(long id) {
        ids.add(id);
    }

    public boolean deleteId(long id) {
        JSONObject nodeInfo = infos.get(id);
        if (nodeInfo != null) {//判断节点是否离线
            Timestamp ct = nodeInfo.getTimestamp("create_time");
            long now = (System.currentTimeMillis() - ct.getTime()) / 1000;
            //50s内未接收数据视为离线
            if (now < 50) {
                return false;
            }
            infos.remove(id);
        }
        ids.remove(id);
        return true;
    }

    public boolean hasId(long id) {
        return ids.contains(id);
    }

//    //todo
//    public String getNodeInfoByRedis() {
//        return redisUtils.get(key);
//    }

    //todo
    public void updateNodeInfoByRedis() {

    }

    /***
     * @description 求解熵
     * @author yuyifade
     * @date 2021/12/19 下午4:26
     * @param node
     * @return int
     */
    private int place(JSONObject node) {
        int x = node.getIntValue("cpu_busy");
        int y = node.getIntValue("memory_rate");
        int z = node.getIntValue("disk_rate");
        int d = 10;
        int n = 10;//100 / d;
        if (x >= 100) {
            x = 99;
        }
        if (y >= 100) {
            y = 99;
        }
        if (z >= 100) {
            z = 99;
        }
        return x / n * d * d + (y / n * d) + (z / n);
    }

    private JSONObject option;

    public JSONObject getOption() {
        if (option == null) {
            option = new JSONObject();
            option.put("memory_rate", "内存使用率");
            option.put("memory_used", "内存使用");
            option.put("swap_rate", "交换区使用率");
            option.put("swap_used", "交换区使用");
            option.put("disk_rate", "磁盘使用率");
            option.put("disk_used", "磁盘使用");

            option.put("cpu_system_avg", "cpu平均系统使用率");
            option.put("cpu_user_avg", "cpu平均用户使用率");
            option.put("cpu_busy_avg", "cpu平均用户使用率");
            option.put("cpu_temperature_avg", "cpu平均温度");
            option.put("cpu_frequency_avg", "cpu平均频率");
            option.put("gpu_utilization_avg", "显卡平均使用率");
            option.put("memory_utilization_avg", "显存平均使用率");
            option.put("gpu_temperature_avg", "显卡平均温度");
            option.put("gpu_power_draw_avg", "显卡平均功耗");
            option.put("disk_read_speed_total", "磁盘总读速度");
            option.put("disk_write_speed_total", "磁盘总写速度");
            option.put("network_up_speed_total", "网卡总上传速度");
            option.put("network_down_speed_total", "网卡总下载速度");
            option.put("gpu_power_draw_total", "显卡总功耗");

            option.put("cpu_system", "cpu系统使用率");
            option.put("cpu_user", "cpu用户使用率");
            option.put("cpu_busy", "cpu总使用率");
            option.put("cpu_temperature", "cpu温度");
            option.put("cpu_frequency", "cpu频率");

            option.put("disk_read_speed", "磁盘读速度");
            option.put("disk_write_speed", "磁盘写速度");

            option.put("network_up_speed", "网卡上传速度");
            option.put("network_down_speed", "网卡下载速度");

            option.put("gpu_utilization", "显卡使用率");
            option.put("memory_utilization", "显存使用率");
            option.put("gpu_temperature", "显卡温度");
            option.put("gpu_power_draw", "显卡功耗");
        }
        return option;
    }


    public static void main(String[] args) {
        while (true) {
            long a = Math.round(100 * 0.9 + Math.random() * (100 - 100 * 0.9));
            if (a > 99) {
                System.out.println(a);
            }
        }
    }
}
