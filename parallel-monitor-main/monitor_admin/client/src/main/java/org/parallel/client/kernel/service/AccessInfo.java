package org.parallel.client.kernel.service;

import com.alibaba.fastjson.JSONObject;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.parallel.client.kernel.model.*;
import org.springframework.stereotype.Component;

import java.util.concurrent.Future;

/**
 * @author yuyifade
 * @description 数据统一获取层
 * @date 2022/1/5 下午5:30
 */
@Component
@RequiredArgsConstructor
public class AccessInfo {
    private final NodeInfo nodeInfo;
    private final CpuInfo cpuInfo;
    private final DiskInfo diskInfo;
    private final MemoryInfo memoryInfo;
    private final NetworkInfo networkInfo;
    private final GpuInfo gpuInfo;
    private final ProcessInfo processInfo;
    private int delay = 40;

    @SneakyThrows
    public JSONObject getInfo(String type) {
        JSONObject res = null;
        switch (type) {
            case "staticNodeInfo":
                res = nodeInfo.getStatic();
                break;
            case "dynamicNodeInfo":
                res = nodeInfo.getDynamic();
                break;
            case "staticCpuInfo":
                res = cpuInfo.getStatic();
                break;
            case "staticDiskInfo":
                res = diskInfo.getStatic();
                break;
            case "staticMemoryInfo":
                res = memoryInfo.getStatic();
                break;
            case "staticNetworkInfo":
                res = networkInfo.getStatic();
                break;
            case "staticGpuInfo":
                res = gpuInfo.getStatic();
                break;
            case "staticProcessInfo":
                res = processInfo.getprocessinfo();
                break;
            case "dynamicCpuInfo":
                res = cpuInfo.getDynamicWithDelay(delay).get();
                break;
            case "dynamicDiskInfo":
                res = diskInfo.getDynamicWithDelay(delay).get();
                break;
            case "dynamicNetworkInfo":
                res = networkInfo.getDynamicWithDelay(delay).get();
                break;
            case "dynamicGpuInfo":
                res = gpuInfo.getDynamic().get();
                break;
        }
        return res;
    }

    public Future<JSONObject> getInfoWithDelay(String type) {
        Future<JSONObject> res = null;
        switch (type) {
            case "dynamicCpuInfo":
                res = cpuInfo.getDynamicWithDelay(delay);
                break;
            case "dynamicDiskInfo":
                res = diskInfo.getDynamicWithDelay(delay);
                break;
            case "dynamicNetworkInfo":
                res = networkInfo.getDynamicWithDelay(delay);
                break;
            case "dynamicGpuInfo":
                res = gpuInfo.getDynamic();
                break;
        }
        return res;
    }
}
