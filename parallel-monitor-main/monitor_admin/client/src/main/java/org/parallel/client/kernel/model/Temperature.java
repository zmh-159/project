package org.parallel.client.kernel.model;

import com.alibaba.fastjson.JSONObject;
import lombok.SneakyThrows;
import org.parallel.common.utils.ShellUtils;

import java.io.BufferedReader;


public class Temperature extends BaseInfo {
    @SneakyThrows
    public JSONObject getDynamic() {
        System.out.println(System.currentTimeMillis());
        JSONObject res = new JSONObject();
        String[] sh = {"sh", "-c", "ls /sys/class/hwmon/ |wc -l"};
        BufferedReader bf = ShellUtils.execShell(sh);
        String str;
        int num = 0;
        while ((str = bf.readLine()) != null) {
            num = Integer.parseInt(str);
        }
        for (int i = 0; i < num; i++) {
            String sh1 = "cat /sys/class/hwmon/hwmon" + i + "/name";
            BufferedReader bf1 = ShellUtils.execShell(sh1);
            String str1;
            while ((str1 = bf1.readLine()) != null) {
                String[] sh2 = {"sh", "-c", "ls /sys/class/hwmon/hwmon" + i + "/ |grep 'input' |grep 'temp'|wc -l"};
                BufferedReader bf2 = ShellUtils.execShell(sh2);
                String str2;
                while ((str2 = bf2.readLine()) != null) {
                    int num1 = Integer.parseInt(str2);
                    if (num1 != 0) {
                        double tmp = 0;
                        for (int j = 1; j <= num1; j++) {
                            String sh3 = "cat /sys/class/hwmon/hwmon" + i + "/temp" + j + "_input";
                            String str3;
                            BufferedReader bf3 = ShellUtils.execShell(sh3);
                            while ((str3 = bf3.readLine()) != null) {
                                System.out.println(str1 + ":" + str3);
                                tmp += Integer.parseInt(str3);
                            }
                        }
                        res.put(str1, twoDecimal(tmp / num1 / 1000));
                    }
                }
            }
        }
        System.out.println(System.currentTimeMillis());
        return res;
    }

    @SneakyThrows
    public static void main(String[] args) {
        Temperature temperature = new Temperature();
        System.out.println(temperature.getDynamic());
    }
}
