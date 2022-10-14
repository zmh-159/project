package org.parallel.client.kernel.model;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import lombok.SneakyThrows;
import org.parallel.client.kernel.model.obtain.StaticInfo;
import org.parallel.common.utils.ShellUtils;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;

@Data
@Component
public class MemoryInfo extends BaseInfo implements StaticInfo {
    static List<String> memoryinfo;

    static void init() {
        memoryinfo = getMemoryInfo();
    }

    @SneakyThrows
    public static List<String> getMemoryInfo() {
        List<String> memory = new ArrayList<>();
        String[] shell_memory = {"sh", "-c", "dmidecode | grep -C 16 'Speed:' | grep -A 16 'Memory Device'"};
        BufferedReader reader = ShellUtils.execShell(shell_memory);
        String str;
        while ((str = reader.readLine()) != null) {
            memory.add(str);
        }
        return memory;
    }

    @SneakyThrows
    public static String maxExtendMemory() {
        String[] shell_1 = {"sh", "-c", "dmidecode | grep 'Maximum Capacity'"};
        BufferedReader reader_1 = ShellUtils.execShell(shell_1);
        String max_extend_memory;//内存最大扩展容量
        max_extend_memory = reader_1.readLine().trim().split(": ")[1];
        if (max_extend_memory == null) {
            max_extend_memory = "未获取到";
        }
        return max_extend_memory;
    }

    public static int getMemoryNum() {
        init();
        List<String> manufacturer = interceptList(1, "Manufacturer:", memoryinfo);

        return manufacturer.size();
    }

    @SneakyThrows
    @Override
    public JSONObject getStatic() {
        List<String> manufacturer = interceptList(1, "Manufacturer:", memoryinfo);
        List<String> memory_type = interceptList(1, "Type:", memoryinfo);
        List<String> speed = interceptList(1, "Speed:", memoryinfo);
        List<String> capacity = interceptList(1, "Size:", memoryinfo);
        int memory_nums = getMemoryNum();//获取内存插槽数

        int k = 0;
        for (int i = 0; i < memory_nums; i++) {
            JSONObject menory = new JSONObject();
            if (!capacity.get(i).toLowerCase().contains("unknown") && !capacity.get(i).toLowerCase().contains("no module installed") && !capacity.get(i).toLowerCase().contains("not specified")) {
                menory.put("manufacturer", manufacturer.get(i).split(": ")[1]);
                menory.put("memory_type", memory_type.get(i).split(": ")[1]);
                menory.put("speed", speed.get(i).split(": ")[1]);
                menory.put("capacity", capacity.get(i).split(": ")[1]);
                staticInfo.put("" + k, menory);
                k++;
            }

        }
        return staticInfo;
    }
}