package org.parallel.server.shell;

import cn.hutool.json.JSONObject;
import lombok.RequiredArgsConstructor;
import org.parallel.server.service.OnlineNode;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class Terminal {
    private final OnlineNode nic;

    public String getCpuInfo(double cpuLimit) {
        StringBuilder res = new StringBuilder("节点ID  总使用率 用户使用率 系统使用率 空闲率\r\n");
        String infos = nic.getNodeInfo();
        JSONObject jo = new JSONObject(infos);
        Map<String, Double> nodes = new HashMap<>();
        for (String s : jo.keySet()) {
            JSONObject node = jo.getJSONObject(s);
            double cs = node.getDouble("cpu_busy");
            if (cs < cpuLimit) {
                continue;
            }
            String cpuinfo = s + "      " +
                    getDoubleWith2(node.getDouble("cpu_busy")) + "     " +
                    getDoubleWith2(node.getDouble("cpu_user")) + "       " +
                    getDoubleWith2(node.getDouble("cpu_system")) + "        " +
                    getDoubleWith2(node.getDouble("cpu_idle"));
            nodes.put(cpuinfo, cs);
        }
        nodes = sortMapByValues(nodes);
        for (String s : nodes.keySet()) {
            res.append(s).append("\r\n");
        }

        return res.toString();
    }

    /*集群*/
    public String getNodesInfo() {
        StringBuilder res = new StringBuilder("集群节点数  集群CPU数  集群核心数 集群内存大小 集群磁盘大小 下载速度    上传速度\r\n");
        String infos = nic.getNodeInfo();
        JSONObject jo = new JSONObject(infos);
        Map<String, Double> nodes = new HashMap<>();
        int allNodeNumber = 0;
        int allCpuNumber = 0;
        int allCoreNumber = 0;
        double allMemorySize = 0;
        double allDiskSize = 0;
        double allDownSpeed = 0;
        double allUpSpeed = 0;
        for (String s : jo.keySet()) {
            allNodeNumber++;
            JSONObject node = jo.getJSONObject(s);
            allMemorySize += node.getDouble("memory_total");
            allDiskSize += node.getDouble("disk_used");
            JSONObject cpus = node.getJSONObject("cpu_state");
            allCpuNumber += (cpus.size() - 1);
            allDownSpeed += node.getDouble("down_speed");
            allUpSpeed += node.getDouble("up_speed");
            int i = 0;
            for (String c : cpus.keySet()) {
                JSONObject cpu = cpus.getJSONObject(c);
                if (!c.equals("temperature")) {
                    JSONObject cores = cpu.getJSONObject("core_state");
                    allCoreNumber += cores.size();
                }
                i++;
            }
            String nodeInfo = "" + allNodeNumber + "         " + allCpuNumber + "         " + allCoreNumber + "        "
                    + allMemorySize + "GiB   " + allDiskSize + "GiB   " + allDownSpeed + "KiB/s " + allUpSpeed + "KiB/s";
            res.append(nodeInfo).append("\r\n");
        }
        System.out.println("res:" + res + "\n");
        return res.toString();
    }

    /*网卡*/
    public String getNetworkInfo() {
        StringBuilder res = new StringBuilder("节点ID  下载速度 上传速度\r\n");
        String infos = nic.getNodeInfo();
        JSONObject jo = new JSONObject(infos);
        Map<String, Double> nodes = new HashMap<>();
        for (String s : jo.keySet()) {
            JSONObject node = jo.getJSONObject(s);
            double cs = node.getDouble("down_speed");
            String networkInfo = s + "  " +
                    node.getStr("down_speed") + "KiB/s  " +
                    node.getStr("up_speed") + "KiB/s";

            nodes.put(networkInfo, cs);
        }
        nodes = sortMapByValues(nodes);
        for (String s : nodes.keySet()) {
            res.append(s).append("\r\n");
        }

        return res.toString();
    }

    /*磁盘*/
    public String getDiskInfo() {
        double diskLeft;
        StringBuilder res = new StringBuilder("节点ID 磁盘总量 使用空间 剩余空间\r\n");
        String infos = nic.getNodeInfo();
        JSONObject jo = new JSONObject(infos);
        System.out.println("jo:" + jo + "\n");
        Map<String, Double> nodes = new HashMap<>();
        for (String s : jo.keySet()) {
            JSONObject node = jo.getJSONObject(s);
            System.out.println("node:" + node + "\n");
            diskLeft = node.getDouble("disk_total") - node.getDouble("disk_used");
            double cs = node.getDouble("disk_used");
            String diskInfo = s + "  " +
                    node.getStr("disk_total") + "GiB  " +
                    node.getStr("disk_used") + "GiB " + diskLeft + "GiB";
            nodes.put(diskInfo, cs);
        }
        nodes = sortMapByValues(nodes);
        for (String s : nodes.keySet()) {
            res.append(s).append("\r\n");
        }
        System.out.println("res:" + "\n" + res + "\n");
        return res.toString();
    }

    public double getDoubleWith2(double d) {
        return (double) Math.round(d * 100) / 100;
    }

    public static <K extends Comparable, V extends Comparable> Map<K, V> sortMapByValues(Map<K, V> aMap) {
        HashMap<K, V> finalOut = new LinkedHashMap<>();
        aMap.entrySet()
                .stream()
                .sorted((p1, p2) -> p2.getValue().compareTo(p1.getValue()))
                .collect(Collectors.toList()).forEach(ele -> finalOut.put(ele.getKey(), ele.getValue()));
        return finalOut;
    }
}
