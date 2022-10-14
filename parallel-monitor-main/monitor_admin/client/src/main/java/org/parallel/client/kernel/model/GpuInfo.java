package org.parallel.client.kernel.model;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.parallel.common.utils.ShellUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

@Data
@Component
@Slf4j
public class GpuInfo extends BaseInfo {
    private static List<String> gpuinfo;//nvidia-smi命令里的CPU信息
    public double nodeMemoryUtilization = 0, nodeGpuUtilization = 0;

    private static void init() {
        gpuinfo = getGpuInfo();
    }

    @SneakyThrows
    public static Boolean hasGpu() {
        BufferedReader reader = ShellUtils.execShell("lspci");
        String str;
        StringBuilder lscpi = new StringBuilder();
        while ((str = reader.readLine()) != null) {
            lscpi.append(str.toLowerCase());
        }
        return lscpi.toString().contains("nvidia");
    }

    @SneakyThrows
    public static Boolean hasGpuDriver() {
        String str;
        StringBuilder lscpi = new StringBuilder();
        try {
            BufferedReader reader = ShellUtils.execShell("nvidia-smi");
            while ((str = reader.readLine()) != null) {
                lscpi.append(str.toLowerCase());
            }
        } catch (Exception e) {
        }
        return lscpi.toString().contains("gpu");
    }

    @SneakyThrows
    public static List<String> getGpuInfo() {
        List<String> gpuinfo = new ArrayList<>();
        if (!hasGpu() || !hasGpuDriver()) {
            log.warn("没有NVIDIA显卡或未安装NVIDIA驱动");
        } else {
            String shell = "nvidia-smi -q";
            BufferedReader reader = ShellUtils.execShell(shell);
            String str;
            while ((str = reader.readLine()) != null) {
                gpuinfo.add(str);
            }
        }
        return gpuinfo;
    }

    public static int getGpuNum() {
        if (!hasGpu() || !hasGpuDriver()) {
            return 0;
        } else {
            init();
            List<String> gpu_num = interceptList(1, "Attached GPUs", gpuinfo);
            return Integer.parseInt(StringUtils.strip(gpu_num.toString(), "[]").trim().split(": ")[1]);
        }
    }

    @SneakyThrows
    public JSONObject getStatic() {
        if (!hasGpu() || !hasGpuDriver()) {
            return new JSONObject();
        } else {
            int gpu_nums = getGpuNum();
            List<String> gpu_name = interceptList(1, "Product Name", gpuinfo);
            List<String> uuid = interceptList(1, "UUID", gpuinfo);
            List<String> driver_version = interceptList(1, "Driver Version", gpuinfo);
            List<String> accounting_mode_buffer_size = interceptList(1, "Accounting Mode Buffer Size", gpuinfo);
            List<String> vbios_version = interceptList(1, "VBIOS Version", gpuinfo);

            List<String> fb_memory_usage = interceptList(4, "FB Memory Usage", gpuinfo);
            List<String> gpu_memory = interceptList(1, "Total", fb_memory_usage);

            List<String> temperature = interceptList(8, "Temperature", gpuinfo);
            List<String> gpu_shutdown_temp = interceptList(1, "GPU Shutdown Temp", temperature);
            List<String> gpu_slowdown_temp = interceptList(1, "GPU Slowdown Temp", temperature);
            List<String> gpu_max_operating_temp = interceptList(1, "GPU Max Operating Temp", temperature);
            List<String> memory_max_operating_temp = interceptList(1, "Memory Max Operating Temp", temperature);

            List<String> power_readings = interceptList(8, "Power Readings", gpuinfo);
            List<String> max_power_limit = interceptList(1, "Max Power Limit", power_readings);
            List<String> min_power_limit = interceptList(1, "Min Power Limit", power_readings);

            List<String> max_clocks = interceptList(8, "Max Clocks", gpuinfo);
            List<String> max_graphics_frequency = interceptList(1, "Graphics", max_clocks);
            List<String> max_sm_frequency = interceptList(1, "SM", max_clocks);
            List<String> max_memory_frequency = interceptList(1, "Memory", max_clocks);
            List<String> max_video_frequency = interceptList(1, "Video", max_clocks);

            String manufacturer = "NVIDIA";
            for (int i = 0; i < gpu_nums; i++) {
                JSONObject gpu = new JSONObject();

                gpu.put("name", gpu_name.get(i).trim().split(": ")[1]);
                gpu.put("manufacturer", manufacturer);
                gpu.put("uuid", uuid.get(i).trim().split(": ")[1]);
                gpu.put("driver_version", StringUtils.strip(driver_version.toString(), "[]").trim().split(": ")[1]);
                gpu.put("accounting_mode_buffer_size", accounting_mode_buffer_size.get(i).trim().split(": ")[1]);
                gpu.put("vbios_version", vbios_version.get(i).trim().split(": ")[1]);
                gpu.put("memory", gpu_memory.get(i).trim().split(": ")[1]);
                gpu.put("gpu_shutdown_temp", gpu_shutdown_temp.get(i).trim().split(": ")[1]);
                gpu.put("gpu_slowdown_temp", gpu_slowdown_temp.get(i).trim().split(": ")[1]);
                gpu.put("gpu_max_operating_temp", gpu_max_operating_temp.get(i).trim().split(": ")[1]);
                gpu.put("memory_max_operating_temp", memory_max_operating_temp.get(i).trim().split(": ")[1]);
                gpu.put("max_power_limit", max_power_limit.get(i).trim().split(": ")[1]);
                gpu.put("min_power_limit", min_power_limit.get(i).trim().split(": ")[1]);
                gpu.put("max_graphics_frequency", max_graphics_frequency.get(i).trim().split(": ")[1]);
                gpu.put("max_sm_frequency", max_sm_frequency.get(i).trim().split(": ")[1]);
                gpu.put("max_memory_frequency", max_memory_frequency.get(i).trim().split(": ")[1]);
                gpu.put("max_video_frequency", max_video_frequency.get(i).trim().split(": ")[1]);
                staticInfo.put("" + i, gpu);
            }

            //System.out.println("显卡静态信息：" + staticInfo);
        }
        return staticInfo;
    }

    private long preTime = 0l;
    private Object mutex = new Object();

    @SneakyThrows
    @Async
    public Future<JSONObject> getDynamic() {
        synchronized (mutex) {
            long now = System.currentTimeMillis();
            if (now - preTime >= 100) {
                if (!hasGpu() || !hasGpuDriver()) {
                    return new AsyncResult<>(dynamicInfo);
                } else {
                    int gpu_nums = getGpuNum();
                    List<String> tx_throughput = interceptList(1, "Tx Throughput", gpuinfo);
                    List<String> rx_throughput = interceptList(1, "Rx Throughput", gpuinfo);
                    List<String> fan_speed = interceptList(1, "Fan Speed", gpuinfo);
                    List<String> performance_state = interceptList(1, "Performance State", gpuinfo);
                    List<String> fb_memory_usage = interceptList(4, "FB Memory Usage", gpuinfo);
                    List<String> memory_used = interceptList(1, "Used", fb_memory_usage);
                    List<String> utilization = interceptList(5, "Utilization", gpuinfo);
                    List<String> memory_utilization = interceptList(1, "Gpu", utilization);
                    List<String> gpu_utilization = interceptList(1, "Memory", utilization);
                    List<String> encoder_utilization = interceptList(1, "Encoder", utilization);
                    List<String> decoder_utilization = interceptList(1, "Decoder", utilization);
                    List<String> temperature = interceptList(8, "Temperature", gpuinfo);
                    List<String> gpu_current_temp = interceptList(1, "GPU Current Temp", temperature);
                    List<String> memory_current_temp = interceptList(1, "Memory Current Temp", temperature);
                    List<String> power_readings = interceptList(13, "Power Readings", gpuinfo);
                    List<String> gpu_power_draw = interceptList(1, "Power Draw", power_readings);
                    List<String> current_graphics_frequency = interceptList(1, "Graphics", power_readings);
                    List<String> current_sm_frequency = interceptList(1, "SM", power_readings);
                    List<String> current_memory_frequency = interceptList(1, "Memory", power_readings);
                    List<String> current_video_frequency = interceptList(1, "Video", power_readings);

                    for (int i = 0; i < gpu_nums; i++) {
                        JSONObject gpu = new JSONObject();

                        gpu.put("tx_throughput", tx_throughput.get(i).trim().split(": ")[1]);
                        gpu.put("rx_throughput", rx_throughput.get(i).trim().split(": ")[1]);
                        gpu.put("fan_speed", fan_speed.get(i).trim().split(": ")[1]);
                        gpu.put("performance_state", performance_state.get(i).trim().split(": ")[1]);
                        gpu.put("memory_used", memory_used.get(i).trim().split(": ")[1]);
                        gpu.put("memory_utilization", strToDouble(memory_utilization.get(i).trim().split(": ")[1]));
                        gpu.put("gpu_utilization", strToDouble(gpu_utilization.get(i).trim().split(": ")[1]));
                        gpu.put("encoder_utilization", strToDouble(encoder_utilization.get(i).trim().split(": ")[1]));
                        gpu.put("decoder_utilization", strToDouble(decoder_utilization.get(i).trim().split(": ")[1]));
                        gpu.put("gpu_current_temp", gpu_current_temp.get(i).trim().split(": ")[1]);
                        gpu.put("memory_current_temp", memory_current_temp.get(i).trim().split(": ")[1]);
                        gpu.put("gpu_power_draw", gpu_power_draw.get(i).trim().split(": ")[1]);
                        gpu.put("current_graphics_frequency", current_graphics_frequency.get(i).trim().split(": ")[1]);
                        gpu.put("current_sm_frequency", current_sm_frequency.get(i).trim().split(": ")[1]);
                        gpu.put("current_memory_frequency", current_memory_frequency.get(i).trim().split(": ")[1]);
                        gpu.put("current_video_frequency", current_video_frequency.get(i).trim().split(": ")[1]);
                        dynamicInfo.put("" + i, gpu);

                        nodeMemoryUtilization += dynamicInfo.getDoubleValue("memory_utilization");
                        nodeGpuUtilization += dynamicInfo.getDoubleValue("gpu_utilization");
                    }
                    nodeMemoryUtilization = nodeMemoryUtilization / dynamicInfo.size();
                    nodeGpuUtilization = nodeGpuUtilization / dynamicInfo.size();
                    preTime = System.currentTimeMillis();
                }
            }
        }
        return new AsyncResult<>(dynamicInfo);
    }
}
