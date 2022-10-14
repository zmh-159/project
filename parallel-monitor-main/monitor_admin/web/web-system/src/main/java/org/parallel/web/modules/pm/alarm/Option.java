package org.parallel.web.modules.pm.alarm;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;

/**
 * @author yuyifade
 * @website https://el-admin.vip
 * @date 2021-06-18
 * @description 返回报警选项
 **/
@Service
public class Option {

    public JSONObject getOption() {
        JSONObject option = new JSONObject();

        option.put("singleOption", getNode());
        JSONObject multiOption = new JSONObject();

        multiOption.put("cpu", getCpu());
        multiOption.put("disk", getDisk());
        multiOption.put("network", getNetwork());
        multiOption.put("gpu", getGpu());

        option.put("mutiOption", multiOption);
        option.put("type", getType());
        return option;
    }


    private JSONObject getNode() {
        String key[] = {
                "memory_rate",//
                "memory_used",//

                "swap_rate",//
                "swap_used",//

                "disk_rate",//
                "disk_used",//

                "cpu_system_avg",//
                "cpu_user_avg",//
                "cpu_busy_avg",//
                "cpu_temperature_avg",//
                "cpu_frequency_avg",//

//                "disk_read_speed_avg",
//                "disk_write_speed_avg",
//
//                "network_up_speed_avg",
//                "network_down_speed_avg",

                "gpu_utilization_avg",//
                "memory_utilization_avg",//
                "gpu_temperature_avg",//
                "gpu_power_draw_avg",//

                "disk_read_speed_total",//
                "disk_write_speed_total",//

                "network_up_speed_total",//
                "network_down_speed_total",//

                "gpu_power_draw_total"//

        };

        String description[] = {
                "内存使用率",
                "内存使用",

                "交换区使用率",
                "交换区使用",

                "磁盘使用率",
                "磁盘使用",

                "cpu平均系统使用率",
                "cpu平均用户使用率",
                "cpu平均总使用率",
                "cpu平均温度",
                "cpu平均频率",

//                "磁盘平均读速度",
//                "磁盘平均写速度",
//
//                "网卡平均上传速度",
//                "网卡平均下载速度",

                "显卡平均使用率",
                "显存平均使用率",
                "显卡平均温度",
                "显卡平均功耗",

                "磁盘总读速度",
                "磁盘总写速度",

                "网卡总上传速度",
                "网卡总下载速度",

                "显卡总功耗",
        };
        return getJson(key, description);
    }

    private JSONObject getCpu() {
        String key[] = {
                "cpu_system",
                "cpu_user",
                "cpu_busy",
                "cpu_temperature",
                "cpu_frequency"
        };
        String description[] = {
                "cpu系统使用率",
                "cpu用户使用率",
                "cpu总使用率",
                "cpu温度",
                "cpu频率"
        };
        return getJson(key, description);
    }

//    public void getMemory() {
//        String key[] = {
//                "memory_rate",
//                "memory_used"
//        };
//        String description[] = {
//                "内存使用率",
//                "内存使用"
//        };
//    }

//    public void getSwap() {
//        String key[] = {
//                "swap_rate",
//                "swap_used"
//        };
//        String description[] = {
//                "交换区使用率",
//                "交换区使用"
//        };
//    }

    private JSONObject getDisk() {
        String key[] = {
//                "disk_rate",
//                "disk_used",
                "disk_read_speed",
                "disk_write_speed"
        };
        String description[] = {
//                "磁盘总使用率",
//                "磁盘使用",
                "磁盘读速度",
                "磁盘写速度"
        };
        return getJson(key, description);
    }

    private JSONObject getNetwork() {
        String key[] = {
                "network_up_speed",
                "network_down_speed"
        };
        String description[] = {
                "网卡上传速度",
                "网卡下载速度"
        };
        return getJson(key, description);
    }

    private JSONObject getGpu() {
        String key[] = {
                "gpu_utilization",
                "memory_utilization",
                "gpu_temperature",
                "gpu_power_draw"
        };
        String description[] = {
                "显卡使用率",
                "显存使用率",
                "显卡温度",
                "显卡功耗",
        };
        return getJson(key, description);
    }

    private JSONObject getJson(String k[], String v[]) {
        JSONObject jo = new JSONObject();
        if (k.length == v.length) {
            for (int i = 0; i < k.length; ++i) {
                jo.put(k[i], v[i]);
            }
        }
        return jo;
    }

    private JSONObject getType() {
        String k[] = {
                "0",
                "1",
                "2",
                "3",
        };
        String v[] = {
                "大于",
                "小于",
                "区间内",
                "区间外",
        };
        return getJson(k, v);
    }


    public static void main(String[] args) {
        Option p = new Option();
        System.out.println(p.getOption());
    }
}
