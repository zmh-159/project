package org.parallel.client.kernel.model;


import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.parallel.client.kernel.model.obtain.DynamicInfo;
import org.parallel.client.kernel.model.obtain.StaticInfo;
import org.parallel.client.service.DeviceId;
import org.parallel.common.utils.ShellUtils;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

@Data
@Component
@Slf4j
@RequiredArgsConstructor
public class NodeInfo extends BaseInfo implements StaticInfo, DynamicInfo {
    private final DeviceId deviceId;

    private String uuid;
    private String nodeId;
    //cpu数量
    private int cpu_nums;
    //gpu数量
    private int gpu_nums;
    //算力
    String performance = "1000Flops";
    //内存最大扩展容量
    private String max_extend_memory;
    //内存
    private String memory;
    //内存插槽数
    private int memory_slot;
    //操作系统
    private String os_name;
    //系统版本
    private String os_version;
    //os位数
    private int os_bit;
    //获取hostname
    private String host_name;
    //可用磁盘
    private String disk_total;
    //开机时间
    private String start_time;

    //进程数
    int process_nums;
    //CPU空闲率
    double cpu_idle;
    //CPU系统占用率
    double cpu_sys;
    //CPU用户占用率
    double cpu_user;
    //总内存
    double memory_total;
    //内存使用
    double memory_used;
    //内存使用率
    double memory_rate;
    //交换区
    double swap_total;
    //交换区使用
    double swap_used;
    //交换区使用率
    double swap_rate;
    //磁盘使用
    double disk_used;
    //磁盘使用率
    double disk_rate;
    //GPU使用率
    double gpu_utilization = 0;
    //内存使用率
    double memory_utilization = 0;

    private final CpuInfo cpuInfo;
    MemoryInfo memoryInfo = new MemoryInfo();
    private final GpuInfo gpuInfo;
    private final DiskInfo diskInfo;
    private final NetworkInfo networkInfo;

    private final String memo = "cat /proc/meminfo";
    private final String os = "cat /etc/os-release";
    private final String osbit = "getconf LONG_BIT";
    private final String hs = "cat /proc/sys/kernel/hostname";
    private final String[] st = {"sh", "-c", "date -d \"$(awk -F. '{print $1}' /proc/uptime) second ago\" +\"%Y-%m-%d %H:%M:%S\""};

    @SneakyThrows
    public List<String> getMemorySwap() {
        BufferedReader reader_mem = ShellUtils.execShell(memo);
        String str_mem;
        List<String> mem_list = new ArrayList<>();
        while ((str_mem = reader_mem.readLine()) != null) {
            mem_list.add(str_mem);
        }
        return mem_list;
    }

    @SneakyThrows
    @Override
    public JSONObject getStatic() {
        this.cpu_nums = cpuInfo.getCpuNum();
        this.gpu_nums = gpuInfo.getGpuNum();
        this.max_extend_memory = memoryInfo.maxExtendMemory();
        this.memory_slot = memoryInfo.getMemoryNum();

        String mem_total = interceptString("MemTotal:", getMemorySwap());
        memory_total = twoDecimal(Double.parseDouble(mem_total.trim().split("\\s+")[1]) / M);
        this.memory = memory_total + "GB";

        BufferedReader reader_os = ShellUtils.execShell(os);
        String str_os;
        List<String> os_list = new ArrayList<>();
        while ((str_os = reader_os.readLine()) != null) {
            os_list.add(str_os);
        }
        List<String> name = interceptList(1, "NAME=", os_list);
        List<String> version = interceptList(1, "VERSION=", os_list);
        this.os_name = name.get(0).replace("NAME=", "").replaceAll("^\"|\"$", "").trim();
        this.os_version = version.get(0).replace("VERSION=", "").replaceAll("^\"|\"$", "").trim();

        BufferedReader reader_osbit = ShellUtils.execShell(osbit);
        this.os_bit = Integer.parseInt(reader_osbit.readLine());

        BufferedReader reader_hs = ShellUtils.execShell(hs);
        this.host_name = reader_hs.readLine();

        BufferedReader reader_st = ShellUtils.execShell(st);
        this.start_time = reader_st.readLine();

        this.disk_total = diskInfo.getDisk_total() + "GB";

        JSONObject physic_network = new JSONObject();
        JSONObject physic_network1 = new JSONObject();
        physic_network.put("name", "WAYDKW1344");
        physic_network1.put("" + 0, physic_network);

        staticInfo.put("cpu_nums", cpu_nums);
        staticInfo.put("gpu_nums", gpu_nums);
        staticInfo.put("performance", performance);
        staticInfo.put("max_extend_memory", max_extend_memory);
        staticInfo.put("memory", memory);
        staticInfo.put("memory_slot", memory_slot);
        staticInfo.put("os_name", os_name);
        staticInfo.put("os_version", os_version);
        staticInfo.put("host_name", host_name);
        staticInfo.put("os_bit", os_bit);
        staticInfo.put("disk_total", disk_total);
        staticInfo.put("start_time", start_time);
        staticInfo.put("cpu", cpuInfo.getStatic());
        staticInfo.put("gpu", gpuInfo.getStatic());
        staticInfo.put("memory_moudle", memoryInfo.getStatic());
        staticInfo.put("network", networkInfo.getStatic());
        staticInfo.put("disk", diskInfo.getStatic());
        staticInfo.put("physic_network", physic_network1);
        staticInfo.put("uuid", deviceId.getUuid());
        initDeviceId();
        return staticInfo;
    }

    private long preTime = 0l;
    private Object mutex = new Object();

    @SneakyThrows
    @Override
    public JSONObject getDynamic() {
        synchronized (mutex) {
            long now = System.currentTimeMillis();
            if (now - preTime >= 10) {
                // 异步获取磁盘，网卡，CPU动态信息
                Future<JSONObject> f1 = networkInfo.getDynamicWithDelay(10);
                Future<JSONObject> f2 = diskInfo.getDynamicWithDelay(10);
                Future<JSONObject> f3 = cpuInfo.getDynamicWithDelay(10);
                dynamicInfo.put("gpu_state", gpuInfo.getDynamic().get());
                dynamicInfo.put("network_state", f1.get());
                dynamicInfo.put("disk_state", f2.get());
                dynamicInfo.put("cpu_state", f3.get());

                String[] os = {"sh", "-c", "ls /proc |grep '[0-9]' |wc -l"};
                BufferedReader reader_os = ShellUtils.execShell(os);
                process_nums = Integer.parseInt(reader_os.readLine());
                cpu_user = cpuInfo.getCpu_user();
                cpu_sys = cpuInfo.getCpu_sys();
                cpu_idle = cpuInfo.getCpu_idle();

                String mem_available = interceptString("MemAvailable:", getMemorySwap());
                double memory_available = twoDecimal(Double.parseDouble(mem_available.trim().split("\\s+")[1]) / M);
                memory_used = twoDecimal(memory_total - memory_available);
                memory_rate = twoDecimal(100 * memory_used / memory_total);

                String swp_total = interceptString("SwapTotal:", getMemorySwap());
                swap_total = twoDecimal(Double.parseDouble(swp_total.trim().split("\\s+")[1]) / M);
                String swp_available = interceptString("SwapFree:", getMemorySwap());
                double swap_available = twoDecimal(Double.parseDouble(swp_available.trim().split("\\s+")[1]) / M);
                swap_used = twoDecimal(swap_total - swap_available);
                if (swap_total == 0) {
                    swap_rate = 0;
                } else {
                    swap_rate = twoDecimal(100.0 * swap_used / swap_total);
                }

                double disk = diskInfo.getDisk_total();
                disk_used = diskInfo.getDisk_used();
                disk_rate = diskInfo.getDisk_rate();

                //TODO 总使用率未求平均
                if (gpuInfo.getGpuNum() != 0) {
                    memory_utilization = gpuInfo.getNodeMemoryUtilization();
                    gpu_utilization = gpuInfo.getNodeGpuUtilization();
                    dynamicInfo.put("gpu_utilization", gpu_utilization);
                    dynamicInfo.put("memory_utilization", memory_utilization);
                } else {
                    dynamicInfo.put("gpu_utilization", 0);
                    dynamicInfo.put("memory_utilization", 0);
                }
                dynamicInfo.put("process_nums", process_nums);
                dynamicInfo.put("cpu_idle", cpu_idle);
                dynamicInfo.put("cpu_system", cpu_sys);
                dynamicInfo.put("cpu_user", cpu_user);
                dynamicInfo.put("memory_used", memory_used);
                dynamicInfo.put("memory_rate", memory_rate);
                dynamicInfo.put("swap_total", swap_total);
                dynamicInfo.put("swap_used", swap_used);
                dynamicInfo.put("swap_rate", swap_rate);
                dynamicInfo.put("disk_used", disk_used);
                dynamicInfo.put("disk_rate", disk_rate);
                //总内存，总磁盘
                dynamicInfo.put("memory_total", memory_total);
                dynamicInfo.put("disk_total", disk);
                initDynamicId();
                preTime = System.currentTimeMillis();
            }
            return dynamicInfo;
        }
    }

    public void initDeviceId() {
        JSONObject ids = deviceId.getDeviceId();

        if (ids.size() > 0) { //为设备添加id避免重复添加
            try {
                JSONObject cpu = ids.getJSONObject("cpu");
                JSONObject gpu = ids.getJSONObject("gpu");
                JSONObject physicNetwork = ids.getJSONObject("physic_network");
                JSONObject network = ids.getJSONObject("network");
                JSONObject memoryMoudle = ids.getJSONObject("memory_moudle");
                JSONObject disk = ids.getJSONObject("disk");
                for (String key : cpu.keySet()) {
                    staticInfo.getJSONObject("cpu").getJSONObject(key).put("cpu_id", cpu.getLong(key));
                }

                for (String key : gpu.keySet()) {
                    staticInfo.getJSONObject("gpu").getJSONObject(key).put("gpu_id", gpu.getLong(key));
                }

                for (String key : physicNetwork.keySet()) {
                    staticInfo.getJSONObject("physic_network").getJSONObject(key).put("physic_network_id", physicNetwork.getLong(key));
                }

                for (String key : memoryMoudle.keySet()) {
                    staticInfo.getJSONObject("memory_moudle").getJSONObject(key).put("memory_id", memoryMoudle.getLong(key));
                }

                for (String key : network.keySet()) {
                    staticInfo.getJSONObject("network").getJSONObject(key).put("network_id", network.getLong(key));
                }

                for (String key : disk.keySet()) {
                    staticInfo.getJSONObject("disk").getJSONObject(key).put("disk_id", disk.getLong(key));
                }
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
    }

    public void initDynamicId() {
        JSONObject ids = deviceId.getDeviceId();
        JSONObject cpu = ids.getJSONObject("cpu");
        JSONObject gpu = ids.getJSONObject("gpu");
        JSONObject disk = ids.getJSONObject("disk");
        JSONObject network = ids.getJSONObject("network");
        dynamicInfo.put("uuid", uuid);
        dynamicInfo.put("node_id", ids.getLongValue("node_id"));
        if (cpu != null && cpu.size() > 0) {
            for (String key : cpu.keySet()) {
                JSONObject cpuState = dynamicInfo.getJSONObject("cpu_state").getJSONObject(key);
                if (cpuState != null) {
                    cpuState.put("cpu_id", cpu.getLong(key));
                }

            }
        }
        if (gpu != null && gpu.size() > 0) {
            for (String key : gpu.keySet()) {
                JSONObject gpuState = dynamicInfo.getJSONObject("gpu_state").getJSONObject(key);
                if (gpuState != null) {
                    gpuState.put("gpu_id", gpu.getLong(key));
                }

            }
        }
        if (disk != null && disk.size() > 0) {
            for (String key : disk.keySet()) {
                JSONObject diskState = dynamicInfo.getJSONObject("disk_state").getJSONObject(key);
                if (diskState != null) {
                    diskState.put("disk_id", disk.getLong(key));
                }
            }
        }
        if (network != null && network.size() > 0) {
            for (String key : network.keySet()) {
                JSONObject networkState = dynamicInfo.getJSONObject("network_state").getJSONObject(key);
                if (networkState != null) {
                    networkState.put("network_id", network.getLong(key));
                }
            }
        }
    }


    public static void main(String[] args) {
//        NodeInfo nodeInfo = new NodeInfo();
//        System.out.println(nodeInfo.getStatic());
//        System.out.println(nodeInfo.getDynamic());
    }
}
