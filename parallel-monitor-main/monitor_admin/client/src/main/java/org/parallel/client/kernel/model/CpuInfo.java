package org.parallel.client.kernel.model;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.parallel.client.kernel.model.obtain.DynamicInfoWithDelay;
import org.parallel.client.kernel.model.obtain.StaticInfo;
import org.parallel.common.utils.CommonUtils;
import org.parallel.common.utils.ShellUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;
import oshi.hardware.Sensors;

import java.io.BufferedReader;
import java.io.File;
import java.util.*;
import java.util.concurrent.Future;
import java.util.regex.*;

@Data
@Component
@Slf4j
public class CpuInfo extends BaseInfo implements StaticInfo, DynamicInfoWithDelay {
    static List<String> cpuinfo = new ArrayList<>();//proc文件里的CPU信息
    static List<String> dmidecode = new ArrayList<>();//linux命令dmidecode里的CPU信息

    double cpu_idle;//CPU总空闲率
    double cpu_sys;//CPU总系统占用率
    double cpu_user;//CPU总用户占用率
    int cpu_num;//cpu数量
    int node_core;//一个节点的总核心数
    List<String> mfrs;//制造商
    boolean isVirtual;

    CpuInfo() {
        //初始化cpu名，cpu数量
        init();
    }

    private void init() {
        initCpuInfo();
        initDmidecode();
        initCpuName();
        initCpuNum();

        List<String> hypervisor = interceptList(1, "hypervisor", cpuinfo);
        isVirtual = (hypervisor.size() != 0);
    }

    @SneakyThrows
    private void initCpuInfo() {
        File file = new File("/proc/cpuinfo");
        if (file.exists()) {
            String shell = "cat /proc/cpuinfo";
            BufferedReader reader = ShellUtils.execShell(shell);
            String str;
            while ((str = reader.readLine()) != null) {
                cpuinfo.add(str);
            }
        } else {
            log.error("/proc/cpuinfo文件打不开或没有此文件");
        }
    }

    @SneakyThrows
    private void initDmidecode() {
        String shell = "dmidecode";
        BufferedReader reader = ShellUtils.execShell(shell);
        String str;
        while ((str = reader.readLine()) != null) {
            dmidecode.add(str);
        }
    }

    @SneakyThrows
    private void initCpuName() {
        mfrs = interceptList(1, "model name", cpuinfo);
    }

    @SneakyThrows
    private void initCpuNum() {
        String get_name = getCpuName(0);

        if (get_name != null && (get_name.toLowerCase().contains("amd") || get_name.toLowerCase().contains("intel") || get_name.toLowerCase().contains("qemu"))) {
            List<String> physical_id = interceptList(1, "physical id", cpuinfo);
            HashSet<Integer> hi = new HashSet<>();
            for (String s : physical_id) {
                hi.add(Integer.parseInt(s.split(": ")[1]));
            }
            cpu_num = hi.size();
        }
        if (get_name != null && (get_name.toLowerCase().contains("phytium") || get_name.toLowerCase().contains("kunpeng"))) {
            String shell0 = "dmidecode | grep 'Processor Information' | wc -l";
            String[] shell_0 = {"sh", "-c", shell0};
            BufferedReader reader0 = ShellUtils.execShell(shell_0);
            cpu_num = Integer.parseInt(reader0.readLine());
        }

    }

    private String getCpuName(int i) {
        String name = "N/A";
        if (i >= 0 && i < mfrs.size()) {
            name = mfrs.get(i).split(": ")[1];
        }
        return name;
    }

    public int getCpuNum() {
        return cpu_num;
    }

    @SneakyThrows
    @Override
    public JSONObject getStatic() {
        String cpu_name = getCpuName(0);
        int cores = 0, threads = 0;//核心数，线程数
        String arch, l1_i_cache, l1_d_cache, l2_cache, l3_cache, main_frequency, boost_frequency;//架构，一级指令缓存，一级数据缓存，二级缓存，三级缓存，主频，睿频

        BufferedReader reader4 = ShellUtils.execShell("arch");
        arch = reader4.readLine();

        List<String> physical_id = interceptList(1, "physical id", cpuinfo);
        List<String> processor = interceptList(1, "processor", cpuinfo);
        List<Integer> processor_id = new ArrayList<>();
        for (String s : processor) {
            processor_id.add(Integer.parseInt(s.split(": ")[1]));
        }
        List<String> frequency1 = interceptList(1, "Current Speed", dmidecode);
        List<String> frequency2 = interceptList(1, "Max Speed", dmidecode);
        frequency1.add("主频: 未获取到");
        frequency2.add("睿频: 未获取到");

        if (isVirtual) {
            List<String> cpu_cores = interceptList(1, "cpu cores", cpuinfo);

            HashMap<Integer, List<Integer>> map = new HashMap<>();
            for (int i = 0; i < processor_id.size(); i++) {
                int pid = Integer.parseInt(physical_id.get(i).split(": ")[1]);
                if (map.containsKey(pid)) {
                    List<Integer> tmp = map.get(pid);
                    tmp.add(i);
                    map.replace(pid, tmp);
                } else {
                    List<Integer> li = new ArrayList<>();
                    li.add(i);
                    map.put(pid, li);
                }
            }
            for (int i = 0; i < map.size(); ++i) {
                JSONObject cpus = new JSONObject();
                cpu_name = getCpuName(map.get(i).get(0));
                cpus.put("cpu_name", cpu_name);
                cores = Integer.parseInt(cpu_cores.get(map.get(i).get(0)).split(": ")[1]);
                cpus.put("cores", cores);
                node_core += cores;
                threads = map.get(i).size();
                cpus.put("arch", arch);
                cpus.put("threads", threads);

                Pattern pattern = Pattern.compile("[^0-9]");
                File l1iCacheFile = new File("/sys/devices/system/cpu/cpu" + map.get(i).get(0) + "/cache/index0/size");
                if (l1iCacheFile.exists()) {
                    String shell0 = "cat /sys/devices/system/cpu/cpu" + map.get(i).get(0) + "/cache/index0/size";
                    BufferedReader reader0 = ShellUtils.execShell(shell0);
                    String str0 = reader0.readLine();
                    Matcher matcher0 = pattern.matcher(str0);//一级指令缓存大小
                    l1_i_cache = (Integer.parseInt(matcher0.replaceAll(""))) + "KiB";
                    cpus.put("l1_i_cache", l1_i_cache);
                } else {
                    cpus.put("l1_i_cache", "N/A");
                }
                File l1dCacheFile = new File("/sys/devices/system/cpu/cpu" + map.get(i).get(0) + "/cache/index1/size");
                if (l1dCacheFile.exists()) {
                    String shell1 = "cat /sys/devices/system/cpu/cpu" + map.get(i).get(0) + "/cache/index1/size";
                    BufferedReader reader1 = ShellUtils.execShell(shell1);
                    String str1 = reader1.readLine();
                    Matcher matcher1 = pattern.matcher(str1);//一级数据缓存大小
                    l1_d_cache = (Integer.parseInt(matcher1.replaceAll(""))) + "KiB";
                    cpus.put("l1_d_cache", l1_d_cache);
                } else {
                    cpus.put("l1_d_cache", "N/A");
                }
                File l2CacheFile = new File("/sys/devices/system/cpu/cpu" + map.get(i).get(0) + "/cache/index2/size");
                if (l2CacheFile.exists()) {
                    String shell2 = "cat /sys/devices/system/cpu/cpu" + map.get(i).get(0) + "/cache/index2/size";
                    BufferedReader reader2 = ShellUtils.execShell(shell2);
                    String str2 = reader2.readLine();
                    Matcher matcher2 = pattern.matcher(str2);//二级缓存大小
                    l2_cache = (Integer.parseInt(matcher2.replaceAll(""))) + "KiB";
                    cpus.put("l2_cache", l2_cache);
                } else {
                    cpus.put("l2_cache", "N/A");
                }
                File l3CacheFile = new File("/sys/devices/system/cpu/cpu" + map.get(i).get(0) + "/cache/index3/size");
                if (l3CacheFile.exists()) {
                    String shell3 = "cat /sys/devices/system/cpu/cpu" + map.get(i).get(0) + "/cache/index3/size";
                    BufferedReader reader3 = ShellUtils.execShell(shell3);
                    String str3 = reader3.readLine();
                    Matcher matcher3 = pattern.matcher(str3);//二级缓存大小
                    l3_cache = (Integer.parseInt(matcher3.replaceAll(""))) + "KiB";
                    cpus.put("l3_cache", l3_cache);
                } else {
                    cpus.put("l3_cache", "N/A");
                }

                main_frequency = frequency1.get(i).split(": ")[1];
                boost_frequency = frequency2.get(i).split(": ")[1];
                cpus.put("boost_frequency", boost_frequency);
                cpus.put("main_frequency", main_frequency);
                staticInfo.put("" + i, cpus);
            }
        } else {
            if (cpu_name.toLowerCase().contains("amd") || cpu_name.toLowerCase().contains("intel")) {
                List<String> cpu_cores = interceptList(1, "cpu cores", cpuinfo);

                HashMap<Integer, List<Integer>> map = new HashMap<>();
                for (int i = 0; i < processor_id.size(); i++) {
                    int pid = Integer.parseInt(physical_id.get(i).split(": ")[1]);
                    if (map.containsKey(pid)) {
                        List<Integer> tmp = map.get(pid);
                        tmp.add(i);
                        map.replace(pid, tmp);
                    } else {
                        List<Integer> li = new ArrayList<>();
                        li.add(i);
                        map.put(pid, li);
                    }
                }
                for (int i = 0; i < map.size(); ++i) {
                    JSONObject cpus = new JSONObject();
                    cpu_name = getCpuName(map.get(i).get(0));
                    cpus.put("cpu_name", cpu_name);
                    cores = Integer.parseInt(cpu_cores.get(map.get(i).get(0)).split(": ")[1]);
                    cpus.put("cores", cores);
                    node_core += cores;
                    threads = map.get(i).size();
                    cpus.put("arch", arch);
                    cpus.put("threads", threads);
                    Pattern pattern = Pattern.compile("[^0-9]");
                    File l1iCacheFile = new File("/sys/devices/system/cpu/cpu" + map.get(i).get(0) + "/cache/index0/size");
                    if (l1iCacheFile.exists()) {
                        String shell0 = "cat /sys/devices/system/cpu/cpu" + map.get(i).get(0) + "/cache/index0/size";
                        BufferedReader reader0 = ShellUtils.execShell(shell0);
                        String str0 = reader0.readLine();
                        Matcher matcher0 = pattern.matcher(str0);//一级指令缓存大小
                        if (cpu_name.toLowerCase().contains("amd")) {
                            l1_i_cache = (Integer.parseInt(matcher0.replaceAll("")) * cores) + "KiB";
                        } else {
                            l1_i_cache = (Integer.parseInt(matcher0.replaceAll(""))) + "KiB";
                        }
                        cpus.put("l1_i_cache", l1_i_cache);
                    } else {
                        cpus.put("l1_i_cache", "N/A");
                    }
                    File l1dCacheFile = new File("/sys/devices/system/cpu/cpu" + map.get(i).get(0) + "/cache/index1/size");
                    if (l1dCacheFile.exists()) {
                        String shell1 = "cat /sys/devices/system/cpu/cpu" + map.get(i).get(0) + "/cache/index1/size";
                        BufferedReader reader1 = ShellUtils.execShell(shell1);
                        String str1 = reader1.readLine();
                        Matcher matcher1 = pattern.matcher(str1);//一级数据缓存大小
                        if (cpu_name.toLowerCase().contains("amd")) {
                            l1_d_cache = (Integer.parseInt(matcher1.replaceAll("")) * cores) + "KiB";
                        } else {
                            l1_d_cache = (Integer.parseInt(matcher1.replaceAll(""))) + "KiB";
                        }
                        cpus.put("l1_d_cache", l1_d_cache);
                    } else {
                        cpus.put("l1_d_cache", "N/A");
                    }
                    File l2CacheFile = new File("/sys/devices/system/cpu/cpu" + map.get(i).get(0) + "/cache/index2/size");
                    if (l2CacheFile.exists()) {
                        String shell2 = "cat /sys/devices/system/cpu/cpu" + map.get(i).get(0) + "/cache/index2/size";
                        BufferedReader reader2 = ShellUtils.execShell(shell2);
                        String str2 = reader2.readLine();
                        Matcher matcher2 = pattern.matcher(str2);//二级缓存大小
                        if (cpu_name.toLowerCase().contains("amd")) {
                            l2_cache = (Integer.parseInt(matcher2.replaceAll("")) * cores) + "KiB";
                        } else {
                            l2_cache = (Integer.parseInt(matcher2.replaceAll(""))) + "KiB";
                        }
                        cpus.put("l2_cache", l2_cache);
                    } else {
                        cpus.put("l2_cache", "N/A");
                    }
                    File l3CacheFile = new File("/sys/devices/system/cpu/cpu" + map.get(i).get(0) + "/cache/index3/size");
                    if (l3CacheFile.exists()) {
                        String shell3 = "cat /sys/devices/system/cpu/cpu" + map.get(i).get(0) + "/cache/index3/size";
                        String shell3_list = "cat /sys/devices/system/cpu/cpu" + map.get(i).get(0) + "/cache/index3/shared_cpu_list";
                        BufferedReader reader3 = ShellUtils.execShell(shell3);
                        BufferedReader reader3_list = ShellUtils.execShell(shell3_list);
                        String str3 = reader3.readLine();
                        String str3_list = reader3_list.readLine();
                        Matcher matcher3 = pattern.matcher(str3);//三级缓存大小
                        Matcher matcher3_list = pattern.matcher(str3_list);//三级缓存共享的核心数
                        if (cores == map.get(i).size()) {
                            l3_cache = (Integer.parseInt(matcher3.replaceAll("")) * cores / (Integer.parseInt(matcher3_list.replaceAll(" ").split(" ")[1]) - Integer.parseInt(matcher3_list.replaceAll(" ").split(" ")[0]) + 1)) + "KiB";
                        } else {
                            int m = Integer.parseInt(matcher3_list.replaceAll(" ").split(" ")[1]) - Integer.parseInt(matcher3_list.replaceAll(" ").split(" ")[0]) + 1;
                            if (cpu_name.toLowerCase().contains("amd")) {
                                int n = Integer.parseInt(matcher3_list.replaceAll(" ").split(" ")[3]) - Integer.parseInt(matcher3_list.replaceAll(" ").split(" ")[2]) + 1;
                                l3_cache = (Integer.parseInt(matcher3.replaceAll("")) * map.get(i).size() / (m + n)) + "KiB";
                            } else {
                                l3_cache = (Integer.parseInt(matcher3.replaceAll(""))) + "KiB";
                            }
                        }
                        cpus.put("l3_cache", l3_cache);
                    } else {
                        cpus.put("l3_cache", "N/A");
                    }

                    main_frequency = frequency1.get(i).split(": ")[1];
                    if (cpu_name.toLowerCase().contains("amd")) {
                        boost_frequency = frequency2.get(i).split(": ")[1];
                        cpus.put("boost_frequency", boost_frequency);
                    } else {
                        File freqFile = new File("/sys/devices/system/cpu/cpu" + map.get(i).get(0) + "/cache/index2/size");
                        if (freqFile.exists()) {
                            String shell6 = "cat /sys/devices/system/cpu/cpu" + map.get(i).get(0) + "/cpufreq/scaling_max_freq";
                            String[] shell_6 = {"sh", "-c", shell6};
                            BufferedReader reader_6 = ShellUtils.execShell(shell_6);
                            String str6 = reader_6.readLine();
                            if (str6 != null) {
                                boost_frequency = Integer.parseInt(str6) / 1000 + " MHz";
                                cpus.put("boost_frequency", boost_frequency);
                            }
                        }
                    }
                    cpus.put("main_frequency", main_frequency);
                    staticInfo.put("" + i, cpus);
                }
            }
            if (cpu_name.toLowerCase().contains("phytium") || cpu_name.toLowerCase().contains("kunpeng")) {
                List<String> cpu_cores = interceptList(1, "Core Count", dmidecode);
                List<String> version = interceptList(1, "Version", dmidecode);

                HashMap<Integer, List<Integer>> map = new HashMap<>();
                int pid = processor_id.size() / cpu_num;
                int j = 0;
                for (int i = 0; i < processor_id.size(); i += pid) {
                    if (i + pid > processor_id.size()) {
                        pid = processor_id.size() - i;
                    }
                    List<Integer> li = processor_id.subList(i, i + pid);
                    map.put(j++, li);
                }

                for (int i = 0; i < cpu_num; ++i) {
                    cores = Integer.parseInt(cpu_cores.get(i).split(": ")[1]);
                    JSONObject cpus = new JSONObject();
                    if (cpu_name.toLowerCase().contains("phytium")) {
                        cpu_name = version.get(i);
                    } else {
                        cpu_name = getCpuName(map.get(i).get(0));
                    }
                    cpus.put("cpu_name", cpu_name);
                    cpus.put("cores", cores);
                    node_core += cores;
                    threads = map.get(i).size();
                    cpus.put("arch", arch);
                    cpus.put("threads", threads);

                    Pattern pattern = Pattern.compile("[^0-9]");
                    File l1iCacheFile = new File("/sys/devices/system/cpu/cpu" + map.get(i).get(0) + "/cache/index0/size");
                    if (l1iCacheFile.exists()) {
                        String shell0 = "cat /sys/devices/system/cpu/cpu" + map.get(i).get(0) + "/cache/index0/size";
                        BufferedReader reader0 = ShellUtils.execShell(shell0);
                        String str0 = reader0.readLine();
                        Matcher matcher0 = pattern.matcher(str0);//一级指令缓存大小
                        l1_i_cache = (Integer.parseInt(matcher0.replaceAll("")) * cores) + "KiB";
                        cpus.put("l1_i_cache", l1_i_cache);
                    } else {
                        cpus.put("l1_i_cache", "N/A");
                    }
                    File l1dCacheFile = new File("/sys/devices/system/cpu/cpu" + map.get(i).get(0) + "/cache/index1/size");
                    if (l1dCacheFile.exists()) {
                        String shell1 = "cat /sys/devices/system/cpu/cpu" + map.get(i).get(0) + "/cache/index1/size";
                        BufferedReader reader1 = ShellUtils.execShell(shell1);
                        String str1 = reader1.readLine();
                        Matcher matcher1 = pattern.matcher(str1);//一级数据缓存大小
                        l1_d_cache = (Integer.parseInt(matcher1.replaceAll("")) * cores) + "KiB";
                        cpus.put("l1_d_cache", l1_d_cache);
                    } else {
                        cpus.put("l1_d_cache", "N/A");
                    }
                    File l2CacheFile = new File("/sys/devices/system/cpu/cpu" + map.get(i).get(0) + "/cache/index1/size");
                    if (l2CacheFile.exists()) {
                        String shell2 = "cat /sys/devices/system/cpu/cpu" + map.get(i).get(0) + "/cache/index2/size";
                        String shell2_list = "cat /sys/devices/system/cpu/cpu" + map.get(i).get(0) + "/cache/index2/shared_cpu_list";
                        BufferedReader reader2 = ShellUtils.execShell(shell2);
                        BufferedReader reader2_list = ShellUtils.execShell(shell2_list);
                        String str2 = reader2.readLine();
                        String str2_list = reader2_list.readLine();
                        Matcher matcher2 = pattern.matcher(str2);//二级缓存大小
                        Matcher matcher2_list = pattern.matcher(str2_list);//二级缓存共享的核心数
                        if (cpu_name.toLowerCase().contains("phytium")) {
                            l2_cache = (Integer.parseInt(matcher2.replaceAll("")) * cores / (Integer.parseInt(matcher2_list.replaceAll(" ").split(" ")[1]) - Integer.parseInt(matcher2_list.replaceAll(" ").split(" ")[0]) + 1)) + "KiB";
                        } else {
                            l2_cache = (Integer.parseInt(matcher2.replaceAll("")) * cores) + "KiB";
                        }
                        cpus.put("l2_cache", l2_cache);
                    } else {
                        cpus.put("l2_cache", "N/A");
                    }
                    File l3CacheFile = new File("/sys/devices/system/cpu/cpu" + map.get(i).get(0) + "/cache/index1/size");
                    if (l3CacheFile.exists()) {
                        String shell3 = "cat /sys/devices/system/cpu/cpu" + map.get(i).get(0) + "/cache/index3/size";
                        String shell3_list = "cat /sys/devices/system/cpu/cpu" + map.get(i).get(0) + "/cache/index3/shared_cpu_list";
                        BufferedReader reader3 = ShellUtils.execShell(shell3);
                        BufferedReader reader3_list = ShellUtils.execShell(shell3_list);
                        String str3 = reader3.readLine();
                        String str3_list = reader3_list.readLine();
                        Matcher matcher3 = pattern.matcher(str3);//三级缓存大小
                        Matcher matcher3_list = pattern.matcher(str3_list);//三级缓存共享的核心数
                        l3_cache = (Integer.parseInt(matcher3.replaceAll("")) * cores / (Integer.parseInt(matcher3_list.replaceAll(" ").split(" ")[1]) - Integer.parseInt(matcher3_list.replaceAll(" ").split(" ")[0]) + 1)) + "KiB";
                        cpus.put("l3_cache", l3_cache);
                    } else {
                        cpus.put("l3_cache", "N/A");
                    }

                    main_frequency = frequency1.get(i).split(":")[1];
                    boost_frequency = frequency2.get(i).split(":")[1];
                    cpus.put("main_frequency", main_frequency);
                    cpus.put("boost_frequency", boost_frequency);
                    staticInfo.put("" + i, cpus);
                }
            }
        }

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
                String cpu_name = getCpuName(0);
                List<String> pre_list = new ArrayList<>();
                List<String> list = new ArrayList<>();

                File cpuStatFile = new File("/proc/stat");
                if (cpuStatFile.exists()) {
                    String[] core_stat = {"sh", "-c", "cat /proc/stat |grep cpu"};
                    BufferedReader pre_reader = ShellUtils.execShell(core_stat);
                    String pre_stat, stat;
                    while ((pre_stat = pre_reader.readLine()) != null) {
                        pre_list.add(pre_stat);
                    }
                    CommonUtils.sleep(delay);
                    BufferedReader reader = ShellUtils.execShell(core_stat);
                    while ((stat = reader.readLine()) != null) {
                        list.add(stat);
                    }
                    long[] predata = new long[7];
                    long[] data = new long[7];
                    String pre_total_str = pre_list.get(0);
                    for (int i = 0; i < 7; i++) {
                        predata[i] = Long.parseLong(pre_total_str.split("\\s+")[i + 1]);
                    }
                    String total_str = list.get(0);
                    for (int i = 0; i < 7; i++) {
                        data[i] = Long.parseLong(total_str.split("\\s+")[i + 1]);
                    }
                    for (int i = 0; i < data.length; i++) {
                        data[i] -= predata[i];
                    }
                    long user = data[0];
                    long sys = data[2];
                    long idle = data[3];
                    long total = 0;
                    for (long i : data) {
                        total += i;
                    }
                    cpu_idle = twoDecimal(100.0 * idle / total);
                    cpu_sys = twoDecimal(100.0 * sys / total);
                    cpu_user = twoDecimal(100.0 * user / total);

                    List<String> thread_count = interceptList(1, "Thread Count", dmidecode);
                    int thread_co = 0;
                    if (thread_count.size() == 0) {
                        String shell = "lscpu |grep 'CPU(s):'";
                        String[] shell_cpu = {"sh", "-c", shell};
                        BufferedReader cpu_reader = ShellUtils.execShell(shell_cpu);
                        thread_co = Integer.parseInt(cpu_reader.readLine().split(":")[1].trim());
                    }
                    int p = 0;
                    for (int i = 0; i < cpu_num; i++) {
                        JSONObject cpu = new JSONObject();
                        Sensors tem = hal.getSensors();
                        String temperature = tem.toString().split(", ")[0];
                        cpu.put("temperature", getNumeric(temperature));
                        JSONObject cores = new JSONObject();
                        int cc;
                        if (thread_count.size() == 0) {
                            cc = thread_co / cpu_num;
                        } else {
                            cc = Integer.parseInt(thread_count.get(i).split(": ")[1]);
                        }
                        for (int j = 0; j < cc; j++) {
                            JSONObject core = new JSONObject();

                            String pre_str = pre_list.get(p + 1);
                            String str = list.get(p + 1);
                            for (int k = 0; k < 7; k++) {
                                predata[k] = Long.parseLong(pre_str.split("\\s+")[k + 1]);
                                data[k] = Long.parseLong(str.split("\\s+")[k + 1]);
                            }
                            for (int m = 0; m < data.length; m++) {
                                data[m] -= predata[m];
                            }
                            user = data[0];
                            sys = data[2];
                            idle = data[3];
                            total = 0;
                            for (long m : data) {
                                total += m;
                            }
                            double core_idle = twoDecimal(100.0 * idle / total);
                            double core_system = twoDecimal(100.0 * sys / total);
                            double core_user = twoDecimal(100.0 * user / total);
                            long frequency = 0;
                            if (!isVirtual && !cpu_name.toLowerCase().contains("kunpeng")) {
                                File freqFile = new File("/sys/devices/system/cpu/cpu" + p + "/cpufreq/scaling_cur_freq");
                                if (freqFile.exists()) {
                                    String shell_freq = "cat /sys/devices/system/cpu/cpu" + p + "/cpufreq/scaling_cur_freq";
                                    BufferedReader reader_freq = ShellUtils.execShell(shell_freq);
                                    frequency = Long.parseLong(reader_freq.readLine());
                                }
                            }
                            core.put("frequency", frequency);
                            core.put("cpu_idle", core_idle);
                            core.put("cpu_system", core_system);
                            core.put("cpu_user", core_user);
                            cores.put("" + j, core);
                            p++;
                        }
                        cpu.put("core_state", cores);
                        dynamicInfo.put("" + i, cpu);
                    }
                }
                preTime = System.currentTimeMillis();
            }

            JSONObject res = (JSONObject) dynamicInfo.clone();
            return new AsyncResult<>(res);
        }
    }
}