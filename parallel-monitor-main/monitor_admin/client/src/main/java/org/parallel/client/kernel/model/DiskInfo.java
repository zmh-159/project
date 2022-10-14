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

@Data
@Component
@Slf4j
public class DiskInfo extends BaseInfo implements StaticInfo, DynamicInfoWithDelay {
    //可用磁盘
    private double disk_total = 0;
    //磁盘使用
    private double disk_used = 0;
    //磁盘使用率
    private double disk_rate = 0;
    private String df = "df";

    DiskInfo() {
        getStatic();
    }


    @SneakyThrows
    @Override
    public JSONObject getStatic() {
        String shell = "lsblk |grep disk";
        String[] shell_disk = {"sh", "-c", shell};
        BufferedReader reader = ShellUtils.execShell(shell_disk);
        String str, name, capacity;
        int p = 0;
        while ((str = reader.readLine()) != null) {
            JSONObject disk = new JSONObject();
            name = str.trim().split("\\s+")[0];//磁盘名
            capacity = str.trim().split("\\s+")[3];//容量
            disk.put("name", name);
            disk.put("capacity", capacity);
            staticInfo.put("" + p++, disk);
        }

        BufferedReader reader_df = ShellUtils.execShell(df);
        String str_df;
        List<String> diskinfo = new ArrayList<>();
        while ((str_df = reader_df.readLine()) != null) {
            diskinfo.add(str_df);
        }
        long total = 0;
        for (int i = 1; i < diskinfo.size(); i++) {
            total += Long.parseLong(diskinfo.get(i).split("\\s+")[1]);
        }
        disk_total = twoDecimal(total / M);

        return staticInfo;
    }


    private long preTime = 0l;
    private Object mutex = new Object();

    @SneakyThrows
    @Override
    @Async
    //todo
    public Future<JSONObject> getDynamicWithDelay(int delay) {
        synchronized (mutex) {
            long now = System.currentTimeMillis();
            if (now - preTime >= 10) {
                String shell = "lsblk |grep disk";
                String[] shell_disk = {"sh", "-c", shell};
                BufferedReader reader = ShellUtils.execShell(shell_disk);
                String str;
                int p = 0;
                while ((str = reader.readLine()) != null) {
                    JSONObject disk = new JSONObject();
                    String disk_name = str.trim().split("\\s+")[0];

                    File diskFile = new File("/proc/diskstats");
                    if (diskFile.exists()) {
                        String shell1 = "cat /proc/diskstats | grep " + disk_name;
                        String[] shell_1 = {"sh", "-c", shell1};
                        BufferedReader reader_old = ShellUtils.execShell(shell_1);
                        String str_old = reader_old.readLine();
                        double old_read = Double.parseDouble(str_old.trim().split("\\s+")[5]);
                        double old_write = Double.parseDouble(str_old.trim().split("\\s+")[9]);
                        Thread.sleep(delay);
                        BufferedReader reader_new = ShellUtils.execShell(shell_1);
                        String str_new = reader_new.readLine();
                        double new_read = Double.parseDouble(str_new.trim().split("\\s+")[5]);
                        double new_write = Double.parseDouble(str_new.trim().split("\\s+")[9]);
                        double read_speed = (new_read - old_read) / (delay * 1.0 / 1000.0) / 2; //读速度:delay / 1000.0为一秒钟，即一秒钟读扇区（521字节）的次数，除以2表示单位为KiB
                        double write_speed = (new_write - old_write) / (delay * 1.0 / 1000.0) / 2; //写速度:delay / 1000.0为一秒钟，即一秒钟写扇区（521字节）的次数，除以2表示单位为KiB
                        disk.put("read_speed", read_speed);
                        disk.put("write_speed", write_speed);
                        dynamicInfo.put("" + p++, disk);
                    } else {
                        log.error("/proc/diskstats文件打不开或没有此文件");
                    }
                }
                BufferedReader reader_df = ShellUtils.execShell(df);
                String str_df;
                List<String> diskinfo = new ArrayList<>();
                while ((str_df = reader_df.readLine()) != null) {
                    diskinfo.add(str_df);
                }
                long total = 0, used = 0;
                for (int i = 1; i < diskinfo.size(); i++) {
                    total += Long.parseLong(diskinfo.get(i).split("\\s+")[1]);
                    used += Long.parseLong(diskinfo.get(i).split("\\s+")[2]);
                }
                disk_total = twoDecimal(total / M);
                disk_used = twoDecimal(used / M);
                disk_rate = twoDecimal(disk_used / disk_total);
                preTime = System.currentTimeMillis();
            }
        }
        return new AsyncResult<>(dynamicInfo);
    }
}
