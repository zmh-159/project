package org.parallel.client.service;

import com.alibaba.fastjson.JSONObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.parallel.client.config.NetworkConfig;
import org.parallel.client.kernel.model.BaseInfo;
import org.parallel.client.kernel.model.NodeInfo;
import org.parallel.common.utils.RestTemplateClient;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author yuyifade
 * @description 获取节点动态及静态数据
 * @date 2021/6/29 下午9:08
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class DataService {
    private boolean auth = false;
    private final ServerTime serverTime;
    private final DeviceId deviceId;
    private final RestTemplateClient httpClient;
    private final NetworkConfig networkConfig;
    private final NodeInfo nodeInfo;
    private final NetTools nt;
    private ScheduledExecutorService timer = Executors.newScheduledThreadPool(1);
    private final static Object lock = new Object();

    //小间隔
    private int sInterval;
    //大间隔
    private int bInterval;
    private int mode;
    private int sends = 0;

    private JSONObject alarm;//报警策略
    private JSONObject alarmState;//上次报警信息

    //多设备属性过滤及汇总运算
    private void computeInfo(JSONObject res) {
        JSONObject mutiVerifyResults = new JSONObject();//多设备报警验证
        JSONObject cpuState = res.getJSONObject("cpu_state");//获取cpu列表
        JSONObject gpuState = res.getJSONObject("gpu_state");
        JSONObject networkState = res.getJSONObject("network_state");
        JSONObject diskState = res.getJSONObject("disk_state");
        double nodeFrequency, nodeCpuBusy, nodeTemperature;//节点平均cpu负载
        nodeCpuBusy = 0;
        nodeFrequency = 0;
        nodeTemperature = 0;
        for (String keyCpuState : cpuState.keySet()) {
            JSONObject cpu = cpuState.getJSONObject(keyCpuState);//获取单个cpu
            JSONObject cores = cpu.getJSONObject("core_state");
            double user, idle, system, busy, frequency, temperature;//单cpu平均负载
            user = 0;
            idle = 0;
            system = 0;
            frequency = 0;
            temperature = cpu.getDoubleValue("temperature");
            nodeTemperature += temperature;
            for (String keyCore : cores.keySet()) {
                JSONObject core = cores.getJSONObject(keyCore);
                user += core.getDoubleValue("cpu_user");
                idle += core.getDoubleValue("cpu_idle");
                system += core.getDoubleValue("cpu_system");
                frequency += core.getDoubleValue("frequency");
                core.put("cpu_busy", BaseInfo.twoDecimal(100 - core.getDoubleValue("cpu_idle")));
            }
            user = BaseInfo.twoDecimal(user / cores.size());
            idle = BaseInfo.twoDecimal(idle / cores.size());
            system = BaseInfo.twoDecimal(system / cores.size());
            busy = BaseInfo.twoDecimal(100 - idle);
            frequency = BaseInfo.twoDecimal(frequency / cores.size());
            //cpu报警过滤
            for (String apId : alarm.keySet()) {
                JSONObject alarmPolicy = alarm.getJSONObject(apId);
                String option = "cpu_system";
                if (alarmPolicy.containsKey(option)) {

                    compare(system, alarmPolicy.getJSONObject(option), apId, option, mutiVerifyResults);
                }
                option = "cpu_user";
                if (alarmPolicy.containsKey(option)) {
                    compare(user, alarmPolicy.getJSONObject(option), apId, option, mutiVerifyResults);
                }
                option = "cpu_busy";
                if (alarmPolicy.containsKey(option)) {
                    compare(busy, alarmPolicy.getJSONObject(option), apId, option, mutiVerifyResults);
                }
                option = "cpu_temperature";
                if (alarmPolicy.containsKey(option)) {
                    compare(temperature, alarmPolicy.getJSONObject(option), apId, option, mutiVerifyResults);
                }
                option = "cpu_frequency";
                if (alarmPolicy.containsKey(option)) {
                    compare(frequency, alarmPolicy.getJSONObject(option), apId, option, mutiVerifyResults);
                }
            }
            cpu.put("cpu_user", user);
            cpu.put("cpu_idle", idle);
            cpu.put("cpu_system", system);
            cpu.put("cpu_busy", busy);
            cpu.put("frequency", frequency);
            nodeFrequency += frequency;
            nodeCpuBusy += busy;
        }
        res.put("cpu_busy", BaseInfo.twoDecimal(nodeCpuBusy / cpuState.size()));
        res.put("frequency", nodeFrequency / cpuState.size());
        res.put("cpu_temperature", nodeTemperature / cpuState.size());
        cpuState.put("temperature", nodeTemperature / cpuState.size());
        double nodeMemoryUtilization, nodeGpuUtilization, nodeGpuTemperature, nodeGpuPower;//节点平均gpu负载,总负载
        nodeMemoryUtilization = 0;
        nodeGpuUtilization = 0;
        nodeGpuTemperature = 0;
        nodeGpuPower = 0;
        if (gpuState.size() > 0) {
            for (String keyGpuState : gpuState.keySet()) {
                JSONObject gpu = gpuState.getJSONObject(keyGpuState);
                double tmpGpuCurrentTemp = 0;
                double temGpuPowerDraw = 0;
                try {
                    String gct = gpu.getString("gpu_current_temp").split(" ")[0];
                    String gcp = gpu.getString("gpu_power_draw").split(" ")[0];
                    tmpGpuCurrentTemp = Double.parseDouble(gct);
                    temGpuPowerDraw = Double.parseDouble(gcp);
                } catch (Exception e) {

                }
                nodeMemoryUtilization += gpu.getDoubleValue("memory_utilization");
                nodeGpuUtilization += gpu.getDoubleValue("gpu_utilization");
                nodeGpuTemperature += tmpGpuCurrentTemp;
                nodeGpuPower += temGpuPowerDraw;
                //gpu报警
                for (String apId : alarm.keySet()) {
                    JSONObject alarmPolicy = alarm.getJSONObject(apId);
                    String option = "gpu_utilization";
                    if (alarmPolicy.containsKey(option)) {
                        compare(gpu.getDoubleValue("gpu_utilization"), alarmPolicy.getJSONObject(option), apId, option, mutiVerifyResults);
                    }
                    option = "memory_utilization";
                    if (alarmPolicy.containsKey(option)) {
                        compare(gpu.getDoubleValue("memory_utilization"), alarmPolicy.getJSONObject(option), apId, option, mutiVerifyResults);
                    }
                    option = "gpu_temperature";
                    if (alarmPolicy.containsKey(option)) {
                        try {
                            compare(gpu.getDoubleValue("gpu_current_temp"), alarmPolicy.getJSONObject(option), apId, option, mutiVerifyResults);
                        } catch (Exception e) {

                        }
                    }
                    option = "gpu_power_draw";
                    if (alarmPolicy.containsKey(option)) {
                        try {
                            compare(gpu.getDoubleValue("gpu_power_draw"), alarmPolicy.getJSONObject(option), apId, option, mutiVerifyResults);
                        } catch (Exception e) {

                        }
                    }
                }
            }
            nodeMemoryUtilization /= gpuState.size();
            nodeGpuUtilization /= gpuState.size();
            nodeGpuTemperature /= gpuState.size();
            nodeGpuPower /= gpuState.size();
        }
        res.put("memory_utilization", nodeMemoryUtilization);
        res.put("gpu_utilization", nodeGpuUtilization);
        res.put("gpu_current_temp", nodeGpuTemperature);
        res.put("gpu_power_draw", nodeGpuPower);
        res.put("gpu_power_draw_total", nodeGpuPower);

        double nodeDownSpeed, nodeUpSpeed;//节点总网速
        nodeDownSpeed = 0;
        nodeUpSpeed = 0;
        for (String keyNetworkState : networkState.keySet()) {
            JSONObject network = networkState.getJSONObject(keyNetworkState);
            nodeDownSpeed += network.getDoubleValue("down_speed");
            nodeUpSpeed += network.getDoubleValue("up_speed");
            //网卡报警过滤
            for (String apId : alarm.keySet()) {
                JSONObject alarmPolicy = alarm.getJSONObject(apId);
                String option = "network_down_speed";
                if (alarmPolicy.containsKey(option)) {
                    compare(network.getDoubleValue("down_speed"), alarmPolicy.getJSONObject(option), apId, option, mutiVerifyResults);
                }
                option = "network_up_speed";
                if (alarmPolicy.containsKey(option)) {
                    compare(network.getDoubleValue("up_speed"), alarmPolicy.getJSONObject(option), apId, option, mutiVerifyResults);
                }
            }

        }
        res.put("down_speed", BaseInfo.twoDecimal(nodeDownSpeed));
        res.put("up_speed", BaseInfo.twoDecimal(nodeUpSpeed));

        double nodeReadSpeed, nodeWriteSpeed;//磁盘总速度
        nodeReadSpeed = 0;
        nodeWriteSpeed = 0;
        for (String keyDiskState : diskState.keySet()) {
            JSONObject disk = diskState.getJSONObject(keyDiskState);
            nodeReadSpeed += disk.getDoubleValue("read_speed");
            nodeWriteSpeed += disk.getDoubleValue("write_speed");
            //磁盘报警过滤
            for (String apId : alarm.keySet()) {
                JSONObject alarmPolicy = alarm.getJSONObject(apId);
                String option = "disk_read_speed";
                if (alarmPolicy.containsKey(option)) {
                    compare(disk.getDoubleValue("read_speed"), alarmPolicy.getJSONObject(option), apId, option, mutiVerifyResults);
                }
                option = "disk_write_speed";
                if (alarmPolicy.containsKey(option)) {
                    compare(disk.getDoubleValue("write_speed"), alarmPolicy.getJSONObject(option), apId, option, mutiVerifyResults);
                }
            }
        }
        res.put("read_speed", nodeReadSpeed);
        res.put("write_speed", nodeWriteSpeed);
        res.put("alarm", mutiVerifyResults);
    }

    //节点属性过滤
    private void verify(JSONObject info) {
        JSONObject verifyResults = info.getJSONObject("alarm");
        for (String apId : alarm.keySet()) {
            JSONObject alarmPolicy = alarm.getJSONObject(apId);

            for (String option : alarmPolicy.keySet()) {
                JSONObject ap = alarmPolicy.getJSONObject(option);
                double v = -1;
                switch (option) {
                    case "memory_rate":
                        v = info.getDoubleValue("memory_rate");
                        break;
                    case "memory_used":
                        v = info.getDoubleValue("memory_used");
                        break;
                    case "swap_rate":
                        v = info.getDoubleValue("swap_rate");
                        break;
                    case "swap_used":
                        v = info.getDoubleValue("swap_used");
                        break;
                    case "disk_rate":
                        v = info.getDoubleValue("disk_rate");
                        break;
                    case "disk_used":
                        v = info.getDoubleValue("disk_used");
                        break;
                    case "cpu_system_avg":
                        v = info.getDoubleValue("cpu_system");
                        break;
                    case "cpu_user_avg":
                        v = info.getDoubleValue("cpu_user");
                        break;
                    case "cpu_busy_avg":
                        v = info.getDoubleValue("cpu_busy");
                        break;
                    case "cpu_temperature_avg":
                        v = info.getDoubleValue("cpu_temperature");
                        break;
                    case "cpu_frequency_avg":
                        v = info.getDoubleValue("frequency");
                        break;

                    case "gpu_utilization_avg":
                        v = info.getDoubleValue("gpu_utilization");
                        break;
                    case "memory_utilization_avg":
                        v = info.getDoubleValue("memory_utilization");
                        break;
                    case "gpu_temperature_avg":
                        v = info.getDoubleValue("gpu_current_temp");
                        break;
                    case "gpu_power_draw_avg":
                        v = info.getDoubleValue("gpu_power_draw");
                        break;

                    case "disk_read_speed_total":
                        v = info.getDoubleValue("read_speed");
                        break;
                    case "disk_write_speed_total":
                        v = info.getDoubleValue("write_speed");
                        break;

                    case "network_up_speed_total":
                        v = info.getDoubleValue("up_speed");
                        break;
                    case "network_down_speed_total":
                        v = info.getDoubleValue("down_speeds");
                        break;
                    case "gpu_power_draw_total":
                        v = info.getDoubleValue("gpu_power_draw_total");
                        break;
                }
                if (v != -1) {
                    compare(v, ap, apId, option, verifyResults);
                }
            }
        }
        return;
    }

    //报警项比较
    private boolean compare(double value, JSONObject ap, String apId, String option, JSONObject result) {
        boolean res = false;
        int type = ap.getIntValue("type");
        //todo 实现区间内和区间外报警
        double threshold = 0;
        if (type == 0) {
            threshold = ap.getDoubleValue("max_value");
            res = value > threshold ? true : false;
        } else if (type == 1) {
            threshold = ap.getDoubleValue("min_value");
            res = value < threshold ? true : false;
        } else if (type == 2) {
            if (value > ap.getDoubleValue("min_value") && value < ap.getDoubleValue("max_value")) {
                res = true;
            }
        } else if (type == 3) {
            if (value < ap.getDoubleValue("min_value") || value > ap.getDoubleValue("max_value")) {
                res = true;
            }
        }

        if (res) {
            JSONObject vr = new JSONObject();
            vr.put("type", type);
            vr.put("threshold", threshold);
            vr.put("outlier", value);
            byte addType = 1;
            if (alarmState.getJSONObject(apId).getIntValue(option) == 0) {
                alarmState.getJSONObject(apId).put(option, 1);
                addType = 0;
            }
            vr.put("add_type", addType);
            result.put(option, vr);
        } else if (alarmState.getJSONObject(apId).getIntValue(option) == 1) {
            alarmState.getJSONObject(apId).put(option, 0);
        }
        return res;
    }

    public void initAlarm(JSONObject alarm) {
        synchronized (lock) {
            this.alarm = alarm;
            if (alarm.size() == 0) {
                return;
            }
            JSONObject alarmState = new JSONObject();
            for (String key : alarm.keySet()) {
                JSONObject singleAp = alarm.getJSONObject(key);
                JSONObject singleAlarmState = new JSONObject();
                for (String option : singleAp.keySet()) {
                    singleAlarmState.put(option, 0);
                }
                alarmState.put(key, singleAlarmState);
            }
            this.alarmState = alarmState;
        }
    }


    /***
     * @description http连接服务器，获取时间误差，设备id，采集策略，报警策略
     * @author yuyifade
     * @date 2021/12/6 上午9:22
     * @return void
     */
    private void initClient() {
        JSONObject staticInfo = nodeInfo.getStatic();
        long sendTime = System.currentTimeMillis();
        //todo 动态获取端口
        String url = "http://" + networkConfig.getServer() + ":9001/api/client";
        JSONObject res = httpClient.sendPostString(url, "staticInfo", staticInfo.toJSONString());
        //判断连线是否成功
        if (res.size() == 0) {
            return;
        }
        System.out.println("1111111:" + res);
        try {
            //更新时间误差
            long receiveTime = System.currentTimeMillis();
            long errorTime = ((res.getLong("server_receive_time") - sendTime) + (res.getLong("server_send_time") - receiveTime)) / 2;
            serverTime.update(errorTime);
            //更新设备id
            deviceId.setDeviceId(res);
            //初始化采集策略
            JSONObject mp = res.getJSONObject("monitor_policy");
            sInterval = 1000;
            bInterval = mp.getIntValue("big_interval") * 1000;
            mode = res.getByteValue("mode");
            //初始化报警策略
            initAlarm(res.getJSONObject("alarm"));
            auth = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /***
     * @description 重连机制
     * @author yuyifade
     * @date 2021/12/6 上午10:41
     * @return boolean
     */
    private boolean isAuth() {
        //尚未初始化
        if (!auth) {
            int reConnect = 100;
            while (reConnect-- > 0) {//100次重连
                initClient();
                if (auth) {
                    break;
                }
                log.error("初始化client失败！尝试重连\n");
                try {
                    Thread.sleep(5000);
                } catch (Exception e) {
                }
            }
        }
        return auth;
    }


    /***
     * @description 开启采样
     * @author yuyifade
     * @date 2021/12/6 下午1:08
     * @param
     * @return void
     */
    public void updateTrendsInfo() {
        //检查初始化
        if (!isAuth()) {
            return;
        }
        int interval = sInterval;
        long time1 = serverTime.getServerTime() % interval;
        timer.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                //判断采集模式
                if (mode == 1 && interval != bInterval) {
                    if (sends++ == 5) {
                        timer.shutdown();
                        sends = 0;
                        sInterval = bInterval;
                        timer = Executors.newScheduledThreadPool(1);
                        updateTrendsInfo();
                    }
                }
                //采集时间
                long create_time = serverTime.getServerTime();
                JSONObject dynamicInfo = nodeInfo.getDynamic();
                dynamicInfo.put("create_time", create_time);
                //报警过滤
                synchronized (lock) {
                    computeInfo(dynamicInfo);
                    verify(dynamicInfo);
                }
                //是否存入数据库
                int save = 0;
                int alarm_state = 0;
                if (!dynamicInfo.getJSONObject("alarm").isEmpty()) {
                    alarm_state = 1;
                    save = 1;
                }
                if (create_time % bInterval < 1000) {
                    save = 1;
                }

                dynamicInfo.put("save", save);
                dynamicInfo.put("alarm_state", alarm_state);
                JSONObject pack = new JSONObject();
                pack.put("dynamic", dynamicInfo);
                nt.sendUdp(pack.toJSONString().getBytes(StandardCharsets.UTF_8));
                Long nowTime = System.currentTimeMillis();
                System.out.println(nowTime + ": " + dynamicInfo);
            }
        }, interval - time1, interval, TimeUnit.MILLISECONDS);
    }

    /***
     * @description 定义动态信息采样间隔
     * @author yuyifade
     * @date 2021/11/8 上午9:19
     * @param interval 采样间隔，单位秒（s）5s <= interval <=
     * @return void
     */
    public void update(int si, int bi, int mode) {

        if (this.mode == mode && si * 1000 == sInterval && bi * 1000 == bInterval) {
            if (mode == 1 && sends != 0) {
                sends = 0;
            }
            return;
        }

        this.mode = mode;
        this.sInterval = si * 1000;
        this.bInterval = bi * 1000;
        try {
            timer.shutdown();
            while (true) {
                boolean isOver = timer.awaitTermination(1000, TimeUnit.MILLISECONDS);
                if (isOver) {
                    break;
                }
            }
            timer = Executors.newScheduledThreadPool(1);
            this.updateTrendsInfo();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
