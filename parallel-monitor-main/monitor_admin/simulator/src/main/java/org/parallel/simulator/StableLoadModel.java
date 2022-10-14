package org.parallel.simulator;

import com.alibaba.fastjson.JSONObject;

import java.util.UUID;

public class StableLoadModel extends General {
    public JSONObject getDynamic(JSONObject ids) {
        JSONObject res = new JSONObject();
        res.put("node_id", ids.getString("node_id"));

        /** 交换区 **/
        double swapTotal = 2;
        double swapUsed = getStable(swapTotal);
        res.put("swap_used", swapUsed);
        res.put("swap_total", swapTotal);
        res.put("swap_rate", getDoubleWith2(swapUsed / swapTotal * 100));

        /** 内存 **/
        double memoryTotal = 64;
        double memoryUsed = getStable(memoryTotal);
        res.put("memory_total", memoryTotal);
        res.put("memory_used", memoryUsed);
        res.put("memory_rate", getDoubleWith2(memoryUsed / memoryTotal * 100));

        /** 磁盘 **/
        double diskTotal = 1024;
        double diskUsed = getStable(diskTotal);
        res.put("disk_used", diskUsed);
        res.put("disk_total", diskTotal);
        res.put("disk_rate", getDoubleWith2(diskUsed / diskTotal * 100));
        /** cpu **/
        double cpuUser = 0;
        double cpuSystem = 0;
        double cpuIdle = 0;
//        res.put("cpu_user", cpuUser);
//        res.put("cpu_system", cpuSystem);
//        res.put("cpu_idle", 100 - cpuUser - cpuSystem);

        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        res.put("process_nums", getMaxRandom(300));
        res.put("uuid", uuid);
        res.put("gpu_utilization", getStable(100));
        res.put("memory_utilization", getStable(100));
        JSONObject cpus = new JSONObject();
        int cpu_nums = 2;
        for (int i = 0; i < cpu_nums; ++i) {
            JSONObject cpu = new JSONObject();
            cpu.put("cpu_id", ids.getJSONObject("cpu").getLong(i + ""));
            double temperature = getStable(100);
            cpu.put("temperature", temperature);
            JSONObject cores = new JSONObject();
            for (int j = 0; j < 12; ++j) {
                JSONObject core = new JSONObject();
                double coreUser = getStable(100);
                double coreSystem = getRandom(100 - coreUser);
                double coreIdle = 100 - coreUser - coreSystem;
                double coreFrequency = getStable(3600000);
                core.put("cpu_user", coreUser);
                core.put("cpu_idle", coreIdle);
                core.put("cpu_system", coreSystem);
                core.put("frequency", coreFrequency);
                cores.put(j + "", core);
                cpuUser += coreUser;
                cpuSystem += coreSystem;
            }
            cpuSystem /= 12;
            cpuUser /= 12;
            cpuIdle = 100 - cpuUser - cpuSystem;
            cpu.put("core_state", cores);
            cpus.put(i + "", cpu);
        }
        double cu = getStable(100);
        double cs = getRandom(100 - cu);

        res.put("cpu_user", cu);
        res.put("cpu_system", cs);
        res.put("cpu_idle", 100 - cu - cs);

        JSONObject gpus = new JSONObject();
        int gpu_nums = 1;
        for (int i = 0; i < gpu_nums; ++i) {
            JSONObject gpu = new JSONObject();
            gpu.put("gpu_id", ids.getJSONObject("gpu").getLong(i + ""));
            gpu.put("tx_throughput", "634000 KB/s");
            gpu.put("rx_throughput", "0 KB/s");
            gpu.put("fan_speed", "N/A");
            gpu.put("performance_state", "P0");
            gpu.put("memory_used", "365 MiB");
            gpu.put("memory_utilization", getStable(100));
            gpu.put("gpu_utilization", getStable(100));
            gpu.put("encoder_utilization", 0.0);
            gpu.put("decoder_utilization", 0.0);
            gpu.put("gpu_current_temp", "54 C");
            gpu.put("memory_current_temp", "N/A");
            gpu.put("gpu_power_draw", "N/A");
            gpu.put("current_graphics_frequency", "1189 MHz");
            gpu.put("current_sm_frequency", "1189 MHz");
            gpu.put("current_memory_frequency", "900 MHz");
            gpu.put("current_video_frequency", "1165 MHz");
            gpus.put(i + "", gpu);
        }

        JSONObject disks = new JSONObject();
        int disk_nums = 2;
        for (int i = 0; i < disk_nums; ++i) {
            JSONObject disk = new JSONObject();
            disk.put("disk_id", ids.getJSONObject("disk").getLong(i + ""));
            disk.put("read_speed", getStable(1024));
            disk.put("write_speed", getStable(2048));
            disks.put(i + "", disk);
        }
        JSONObject networks = new JSONObject();
        int network_nums = 1;
        for (int i = 0; i < network_nums; ++i) {
            JSONObject network = new JSONObject();
            network.put("network_id", ids.getJSONObject("network").getLong(i + ""));
            network.put("down_speed", getStable(1024));
            network.put("up_speed", getStable(1024));
            network.put("down_package", 861);
            network.put("up_package", 8161);
            networks.put(i + "", network);
        }

        res.put("cpu_state", cpus);
        res.put("disk_state", disks);
        res.put("gpu_state", gpus);
        res.put("network_state", networks);
        return res;

    }

    public double getStable(double max) {
        return getDoubleWith2(Math.round(max * 0.5 + Math.random() * (max * 0.6 - max * 0.5)));
    }

    public double getRandom(double max) {
        return getDoubleWith2(Math.round(Math.random() * (max * 0.1)));
    }
}
