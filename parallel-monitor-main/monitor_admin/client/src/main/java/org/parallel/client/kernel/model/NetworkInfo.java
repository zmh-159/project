package org.parallel.client.kernel.model;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.parallel.client.kernel.model.obtain.DynamicInfoWithDelay;
import org.parallel.client.kernel.model.obtain.StaticInfo;
import org.parallel.common.utils.ShellUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @author yuyifade
 * @description
 * @date 2021/3/20 下午3:19
 */
@Data
@Component
@Slf4j
public class NetworkInfo extends BaseInfo implements StaticInfo, DynamicInfoWithDelay {
    private static final List<NetworkAdapter> networkAdapters = new ArrayList<>();
    private static final String[] cmd = {"sh", "-c", "cat /proc/net/dev | grep :"};//获取网卡名
    private static boolean isInit = false;
    double nodeDownSpeed = 0, nodeUpSpeed = 0;

    @SneakyThrows
    NetworkInfo() {
        if (!isInit) {
            BufferedReader bufferedReader = ShellUtils.execShell(cmd);
            String str;
            while ((str = bufferedReader.readLine()) != null) {
                String name = str.trim().split(":")[0];
                networkAdapters.add(new NetworkAdapter(name));
            }
            isInit = true;
        }
    }

    @SneakyThrows
    @Override
    public JSONObject getStatic() {
        String shell_0 = "ls /sys/class/net/";
        BufferedReader reader_0 = ShellUtils.execShell(shell_0);
        String name, mac, ip;//网卡名，mac地址，ip地址
        int i = 0;
        while ((name = reader_0.readLine()) != null) {
            JSONObject netWork = new JSONObject();
            netWork.put("name", name);
            File netAddressFile = new File("/sys/class/net/" + name + "/address");
            if (netAddressFile.exists()) {
                String shell2 = "cat /sys/class/net/" + name + "/address";
                BufferedReader reader_2 = ShellUtils.execShell(shell2);
                String str2 = reader_2.readLine();
                if (str2.length() < 18) {
                    mac = str2;
                } else {
                    mac = str2.substring(0, 17);
                }
                netWork.put("mac", mac);
                String shell3 = "ifconfig " + name + " | grep 'inet' | awk '{ print $2}'";
                String[] shell_3 = {"sh", "-c", shell3};
                BufferedReader reader_3 = ShellUtils.execShell(shell_3);

                String s1 = reader_3.readLine();
                if (s1 != null) {
                    Pattern p = Pattern.compile("\\d+\\.\\d+\\.\\d+\\.\\d*");
                    Matcher m = p.matcher(s1);
                    if (m.find()) {
                        ip = m.group();
                    } else {
                        ip = "无";
                    }
                } else {
                    ip = "无";
                }

                netWork.put("ip", ip);
                staticInfo.put("" + i++, netWork);
            }
        }
        System.out.println(staticInfo);
        return staticInfo;
    }

    private long preTime = 0l;
    private Object mutex = new Object();

    @SneakyThrows
    @Override
    @Async
    public Future<JSONObject> getDynamicWithDelay(int delay) {

        synchronized (mutex) {
            long now = System.currentTimeMillis();
            if (now - preTime >= 10) {
                for (NetworkAdapter networkAdapter : networkAdapters) {
                    networkAdapter.getDynamic();
                }
                Thread.sleep(delay);
                int i = 0;
                for (NetworkAdapter networkAdapter : networkAdapters) {
                    JSONObject tmp = networkAdapter.getDynamic();
                    dynamicInfo.put("" + i++, tmp);
                    nodeDownSpeed += tmp.getDoubleValue("down_speed");
                    nodeUpSpeed += tmp.getDoubleValue("up_speed");
                }
                nodeDownSpeed = nodeDownSpeed / dynamicInfo.size();
                nodeUpSpeed = nodeUpSpeed / dynamicInfo.size();
                preTime = System.currentTimeMillis();
            }
            return new AsyncResult<>(dynamicInfo);
        }
    }
}