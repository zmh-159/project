package org.parallel.simulator;

import com.alibaba.fastjson.JSONObject;


import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/***
 * @description 模拟发送节点数据
 * @author yuyifade
 * @date 2021/9/23 下午3:38
 */
public class Net extends General {
    //大间隔
    private int bInterval;
    /**
     * 服务器ip
     **/
    private static String ip = "127.0.0.1";
    /**
     * 服务器端口
     **/
    private static int port = 9001;
    private static int udpPort = 9001;
    private static int localPort = 45000;

    private int nums = 10;

    private List<Client> lc;


    Net(String ip, int port, int udpPort, int nums) {
        this.ip = ip;
        this.port = port;
        this.udpPort = udpPort;
        this.nums = nums;
    }

    public JSONObject realTimeInfo(JSONObject res) {
        JSONObject cpuState = res.getJSONObject("cpu_state");//获取cpu列表
        JSONObject gpuState = res.getJSONObject("gpu_state");
        JSONObject networkState = res.getJSONObject("network_state");
        JSONObject diskState = res.getJSONObject("disk_state");
        double nodeFrequency, nodeTemperature;//节点平均cpu负载
        nodeFrequency = 0;
        nodeTemperature = 0;
        for (String keyCpuState : cpuState.keySet()) {
            JSONObject cpu = cpuState.getJSONObject(keyCpuState);//获取单个cpu
            JSONObject cores = cpu.getJSONObject("core_state");
            double user, idle, system, frequency;//单cpu平均负载
            user = 0;
            idle = 0;
            system = 0;
            frequency = 0;
            nodeTemperature += cpu.getDoubleValue("temperature");
            for (String keyCore : cores.keySet()) {
                JSONObject core = cores.getJSONObject(keyCore);
                user += core.getDoubleValue("cpu_user");
                idle += core.getDoubleValue("cpu_idle");
                system += core.getDoubleValue("cpu_system");
                frequency += core.getDoubleValue("frequency");
                core.put("cpu_busy", getDoubleWith2(100 - core.getDoubleValue("cpu_idle")));
            }
            user = user / cores.size();
            idle = idle / cores.size();
            system = system / cores.size();
            frequency = frequency / cores.size();
            cpu.put("cpu_user", user);
            cpu.put("cpu_idle", idle);
            cpu.put("cpu_system", system);
            cpu.put("cpu_busy", 100 - idle);
            cpu.put("frequency", frequency);
            nodeFrequency += frequency;
        }
        res.put("cpu_busy", getDoubleWith2(100 - res.getDoubleValue("cpu_idle")));
        res.put("frequency", nodeFrequency / cpuState.size());
        res.put("cpu_temperature", nodeTemperature / cpuState.size());
        cpuState.put("temperature", nodeTemperature / cpuState.size());
        double nodeMemoryUtilization, nodeGpuUtilization;//节点平均gpu负载
        nodeMemoryUtilization = 0;
        nodeGpuUtilization = 0;
        for (String keyGpuState : gpuState.keySet()) {
            JSONObject gpu = gpuState.getJSONObject(keyGpuState);
            nodeMemoryUtilization += gpu.getDoubleValue("memory_utilization");
            nodeGpuUtilization += gpu.getDoubleValue("gpu_utilization");
        }
        if (gpuState.size() == 0) {
            res.put("memory_utilization", 0);
            res.put("gpu_utilization", 0);
        } else {
            res.put("memory_utilization", nodeMemoryUtilization / gpuState.size());
            res.put("gpu_utilization", nodeGpuUtilization / gpuState.size());
        }

        double nodeDownSpeed, nodeUpSpeed;//节点总网速
        nodeDownSpeed = 0;
        nodeUpSpeed = 0;
        for (String keyNetworkState : networkState.keySet()) {
            JSONObject network = networkState.getJSONObject(keyNetworkState);
            nodeDownSpeed += network.getDoubleValue("down_speed");
            nodeUpSpeed += network.getDoubleValue("up_speed");
        }
        res.put("down_speed", nodeDownSpeed);
        res.put("up_speed", nodeUpSpeed);

        double nodeReadSpeed, nodeWriteSpeed;//节点总网速
        nodeReadSpeed = 0;
        nodeWriteSpeed = 0;
        for (String keyDiskState : diskState.keySet()) {
            JSONObject disk = diskState.getJSONObject(keyDiskState);
            nodeReadSpeed += disk.getDoubleValue("read_speed");
            nodeWriteSpeed += disk.getDoubleValue("write_speed");
        }
        res.put("read_speed", nodeReadSpeed);
        res.put("write_speed", nodeWriteSpeed);
        res.put("alarm_state", 0);
        return res;
    }

    public void start() {
        lc = new ArrayList<>();
        for (int i = 0; i < nums; ++i) {
            Client c = new Client();
            JSONObject ids = sendTcp(c.getStatic());
            System.out.println(ids);
            if (ids.size() > 0) {
                bInterval = ids.getByteValue("big_interval") * 1000;
            }

            c.setIds(ids);
            lc.add(c);
            try {
                Thread.sleep(10);
            } catch (Exception ignored) {

            }
        }

        while (true) {
            long time = System.currentTimeMillis();
            UniformChangeModel.countNum();
            for (int i = 0; i < lc.size(); i++) {
                JSONObject dynamicInfo = realTimeInfo(lc.get(i).getDynamic(time, 1 + i % 3));
                int save = 0;
                long create_time = System.currentTimeMillis();
                dynamicInfo.put("create_time", create_time);
                if (create_time % bInterval < 1000) {//是否存储
                    save = 1;
                }
                dynamicInfo.put("save", save);
                JSONObject pack = new JSONObject();
                pack.put("dynamic", dynamicInfo);
                sendUdp(pack.toJSONString().getBytes(StandardCharsets.UTF_8));
                try {
                    Thread.sleep(1);
                } catch (Exception ignored) {

                }
            }
            try {
                Thread.sleep(3800);
            } catch (Exception ignored) {

            }
        }
    }

    public JSONObject sendTcp(JSONObject message) {
        try (
                Socket socket = new Socket(ip, port);
                OutputStream outs = socket.getOutputStream();
                InputStream ins = socket.getInputStream();
        ) {
            //发送
            outs.write(message.toJSONString().getBytes(StandardCharsets.UTF_8));
            socket.shutdownOutput();
            //接收
            byte[] recBuffer = new byte[10240];
            int len = ins.read(recBuffer);
            long localTime = System.currentTimeMillis();
            String recive = new String(recBuffer, 0, len);
            if (-1 == ins.read(recBuffer)) {
                System.out.println("已连接到服务器：" + recive);
                JSONObject reciveJO = JSONObject.parseObject(recive);
                reciveJO.put("local_time", localTime);
                return reciveJO;
            } else {
                throw new Exception("报文过长");
            }
        } catch (Exception e) {
            System.out.println("连接服务器异常！");
            return new JSONObject();
        }
    }

    public Boolean sendUdp(byte[] dataByte) {
        try {
            DatagramSocket datagramSocket = new DatagramSocket(localPort);
            DatagramPacket datagramPacket = new DatagramPacket(dataByte, dataByte.length, InetAddress.getByName(ip), udpPort);
            datagramSocket.send(datagramPacket);
            datagramSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

}
