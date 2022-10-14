package org.parallel.client.kernel.model;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import lombok.SneakyThrows;
import org.parallel.client.kernel.model.obtain.DynamicInfo;
import org.parallel.common.utils.ShellUtils;

import java.io.BufferedReader;
import java.io.File;

/**
 * @author yuyifade
 * @description 网卡 网速单位K
 * @date 2021/5/23 上午9:40
 */
@Data
public class NetworkAdapter extends BaseInfo implements DynamicInfo {
    private String name;
    private double downSpeed = 0;
    private double upSpeed = 0;

    private double preDownSize = 0;
    private double preUpSize = 0;
    private long preTime = System.currentTimeMillis();

    private static final String pre = "cat /proc/net/dev | grep ";//脚本可能会失效
    private static final String downloadSuffix = " | sed 's/:/ /g' | awk '{print $2}'";
    private static final String uploadSuffix = " | sed 's/:/ /g' | awk '{print $10}'";

    NetworkAdapter(String name) {
        this.name = name;
        getDynamic();
    }

    @SneakyThrows
    @Override
    public JSONObject getDynamic() {
        File netFile = new File("/proc/net/dev");
        if (netFile.exists()) {
            String shellDown = pre + name + downloadSuffix;
            String shellUp = pre + name + uploadSuffix;
            String[] packetUp = {"/bin/sh", "-c", shellUp};
            String[] packetDown = {"/bin/sh", "-c", shellDown};
            BufferedReader reader_rx1 = ShellUtils.execShell(packetDown);
            BufferedReader reader_tx1 = ShellUtils.execShell(packetUp);
            double downSize = Double.parseDouble(reader_rx1.readLine());
            double upSize = Double.parseDouble(reader_tx1.readLine());
            long now = System.currentTimeMillis();
            double len = (now - preTime) / 1000.0;
            if (len != 0) {
                downSpeed = twoDecimal((downSize - preDownSize) / len / K);
                upSpeed = twoDecimal((upSize - preUpSize) / len / K);
                preDownSize = downSize;
                preUpSize = upSize;
                preTime = now;
                dynamicInfo.put("down_speed", downSpeed);
                dynamicInfo.put("up_speed", upSpeed);
                dynamicInfo.put("down_package", (long) downSize);
                dynamicInfo.put("up_package", (long) upSize);
            }
        }
        return dynamicInfo;
    }
}