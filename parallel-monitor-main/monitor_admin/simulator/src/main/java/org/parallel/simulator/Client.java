package org.parallel.simulator;

import com.alibaba.fastjson.JSONObject;
import netscape.javascript.JSObject;

import java.util.Random;
import java.util.UUID;

public class Client {
    //内存条数量
    private int memory_nums = 8;
    private int network_nums = 2;
    private int disk_nums = 2;
    private int pn_nums = 1;
    private int cpu_nums = 2;
    private int gpu_nums = 2;

    private JSONObject ids;


    private String uuid;
    private String memory;
    private String max_extend_memory;
    private int memory_slot;
    private String start_time;
    private String performance;
    private String disk_total;
    private String os_name;
    private String os_version;
    private int os_bit;
    private String host_name;

    public void setIds(JSONObject jo) {
        this.ids = jo;
    }

    public JSONObject getDynamic(long createTime, int model) {
        JSONObject res = new JSONObject();
        switch (model) {
            case 1:
                StableLoadModel stableLoadModel = new StableLoadModel();
                res = stableLoadModel.getDynamic(ids);
                break;
            case 2:
                UniformChangeModel uniformChangeModel = new UniformChangeModel();
                res = uniformChangeModel.getDynamic(ids);
                break;
            case 3:
                HighLoadModel highLoadModel = new HighLoadModel();
                res = highLoadModel.getDynamic(ids);
                break;

        }

        res.put("create_time", createTime);
        return res;

    }


    public JSONObject getStatic() {
        JSONObject res = new JSONObject();

        uuid = UUID.randomUUID().toString().replaceAll("-", "");
        memory = "64 GB";
        max_extend_memory = "256 GB";
        memory_slot = 8;
        start_time = "2021-09-23 14:49:02.0";
        performance = "1000Flops";
        disk_total = "1000.0GB";
        os_name = "ubuntu";
        os_version = "16.04";
        os_bit = 64;
        host_name = generateString(5);
        JSONObject cpus = new JSONObject();
        for (int i = 0; i < cpu_nums; ++i) {
            JSONObject cpu = new JSONObject();
            cpu.put("cpu_name", "AMD Ryzen 5 3600 6-Core Processor");
            cpu.put("cores", 6);
            cpu.put("l3_cache", "32768KiB");
            cpu.put("threads", 12);
            cpu.put("l2_cache", "3072KiB");
            cpu.put("arch", "x86_64");
            cpu.put("boost_frequency", " 4200 MHz");
            cpu.put("main_frequency", " 3600 MHz");
            cpu.put("l1_i_cache", "192KiB");
            cpu.put("l1_d_cache", "192KiB");
            cpus.put(i + "", cpu);

        }
        JSONObject gpus = new JSONObject();
        for (int i = 0; i < gpu_nums; ++i) {
            JSONObject gpu = new JSONObject();
            gpu.put("name", "GeForce 940MX");
            gpu.put("manufacturer", "NVIDIA");
            gpu.put("uuid", "GPU-af6127d7-665c-2de1-59de-c7ed18b079ed");
            gpu.put("driver_version", "384.130");
            gpu.put("accounting_mode_buffer_size", "1920");
            gpu.put("vbios_version", "82.08.59.00.28");
            gpu.put("memory", 2002 + " MiB");
            gpu.put("gpu_shutdown_temp", "99 C");
            gpu.put("gpu_slowdown_temp", "94 C");
            gpu.put("gpu_max_operating_temp", "90 C");
            gpu.put("memory_max_operating_temp", "N/A");
            gpu.put("max_power_limit", "N/A");
            gpu.put("min_power_limit", "N/A");
            gpu.put("max_graphics_frequency", "1241 MHz");
            gpu.put("max_sm_frequency", "1241 MHz");
            gpu.put("max_memory_frequency", "900 MHz");
            gpu.put("max_video_frequency", "1216 MHz");
            gpus.put(i + "", gpu);
        }

        JSONObject memorys = new JSONObject();
        for (int i = 0; i < memory_nums; ++i) {
            JSONObject memory = new JSONObject();
            memory.put("speed", "DDR4");
            memory.put("memory_type", "2667 MT/s");
            memory.put("manufacturer", "Ramaxel Technology");
            memory.put("capacity", "8192 MB");
            memorys.put(i + "", memory);
        }

        JSONObject networks = new JSONObject();
        for (int i = 0; i < network_nums; ++i) {
            JSONObject network = new JSONObject();
            network.put("ip", "192.168.31.1" + i);
            network.put("name", generateString(5).toLowerCase());
            network.put("mac", "2c:f0:5d:c1:f9:8f");
            networks.put(i + "", network);
        }

        JSONObject disks = new JSONObject();
        for (int i = 0; i < disk_nums; ++i) {
            JSONObject disk = new JSONObject();
            disk.put("name", "sda" + (i + 1));
            disk.put("capacity", "512G");
            disks.put(i + "", disk);
        }

        JSONObject physicNetworks = new JSONObject();
        for (int i = 0; i < pn_nums; ++i) {
            JSONObject physicNetwork = new JSONObject();
            physicNetwork.put("name", "WAYDKW1344");
            physicNetworks.put(i + "", physicNetwork);
        }


        res.put("uuid", uuid);
        res.put("memory", memory);
        res.put("max_extend_memory", max_extend_memory);
        res.put("memory_slot", memory_slot);
        res.put("start_time", start_time);
        res.put("performance", performance);
        res.put("disk_total", disk_total);
        res.put("os_name", os_name);
        res.put("os_version", os_version);
        res.put("os_bit", os_bit);
        res.put("host_name", host_name);

        res.put("gpu_nums", gpu_nums);
        res.put("cpu_nums", cpu_nums);


        res.put("cpu", cpus);
        res.put("gpu", gpus);
        res.put("memory_moudle", memorys);
        res.put("disk", disks);
        res.put("network", networks);
        res.put("physic_network", physicNetworks);
        return res;
    }

    public static String generateString(int length) {
        StringBuffer sb = new StringBuffer();
        String ALLCHAR = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            sb.append(ALLCHAR.charAt(random.nextInt(ALLCHAR.length())));
        }
        return sb.toString();
    }

}
