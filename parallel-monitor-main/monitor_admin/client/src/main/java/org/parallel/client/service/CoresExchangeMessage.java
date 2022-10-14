package org.parallel.client.service;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import net.openhft.affinity.AffinityLock;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import oshi.SystemInfo;
import oshi.hardware.HardwareAbstractionLayer;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author yuyifade
 * @description 测试核心通讯能力
 * @date 2021/8/1 下午5:14
 * status 0 正在运行
 * status 1 运行结束
 * status 2 参数有误
 */
@Service
@Slf4j
public class CoresExchangeMessage {
    private final static int maxRepeat = 1000000;
    private static Boolean isRuning = false;
    private final static JSONObject res = new JSONObject();

    @Async
    public void startTest(int start, int end, int core1, int core2, int steps, int repeat) throws IOException, InterruptedException {
        synchronized (isRuning) {
            if (isRuning) {
                return;
            } else {
                isRuning = true;
                synchronized (res) {
                    res.clear();
                    res.put("message", "测试程序正在运行！");
                    res.put("status", 0);

                }
            }
        }
        SystemInfo si = new SystemInfo();
        HardwareAbstractionLayer hal = si.getHardware();
        int nums = hal.getProcessor().getLogicalProcessorCount();
        /**判断参数是否合理**/
        if (core1 == core2 || core1 == 0 || core2 == 0 || core1 > nums - 1 || core2 > nums - 1 || end <= start || steps > end - start) {
            synchronized (res) {
                res.clear();
                res.put("message", "参数有误！");
                res.put("status", 2);
                synchronized (isRuning) {
                    isRuning = false;
                }
                return;
            }
        }
        if (repeat > maxRepeat) {
            repeat = maxRepeat;
        }
        JSONObject testRes = testMessageAbility(start, end, core1, core2, steps, repeat);
        synchronized (res) {
            res.clear();
            JSONObject params = new JSONObject();
            params.put("start", start);
            params.put("end", end);
            params.put("core1", core1);
            params.put("core2", core2);
            params.put("steps", steps);
            params.put("repeat", repeat);
            /**测试参数**/
            res.put("params", params);
            /**测试结果**/
            res.put("result", testRes);
            res.put("status", 1);
            synchronized (isRuning) {
                isRuning = false;
                return;
            }
        }
    }

    public JSONObject getRes() {
        synchronized (res) {
            return res;
        }
    }


    private JSONObject testMessageAbility(int start, int end, int core1, int core2, int steps, int repeat) throws IOException, InterruptedException {
        final List<String> packgetSize = new ArrayList<>();
        final List<String> res = new ArrayList<>();
        for (int i = start; i <= end; i += steps) {
            List<byte[]> lb = new ArrayList<>();
            for (int j = 0; j < 100; ++j) {
                lb.add(getRandomByte(i));
            }

            PipedOutputStream pipout = new PipedOutputStream();
            PipedInputStream pipin = new PipedInputStream(pipout, i);

            List<Integer> li = new ArrayList<>();
            List<Integer> flag = new ArrayList<>();

            int finalI = i;
            Thread t1 = new Thread() {
                @Override
                public void run() {
                    try (AffinityLock al = AffinityLock.acquireLock(core1)) {
                        long start = System.currentTimeMillis();
                        int j = 0;
                        while (true) {
                            synchronized (li) {
                                if (0 == li.size()) {
                                    pipout.write(lb.get(j % 100));
                                    li.add(1);
                                    if (j++ == repeat - 1) {
                                        Long times = System.currentTimeMillis() - start;
                                        System.out.println(finalI + ":" + times);
                                        packgetSize.add(finalI + "");
                                        res.add("" + times);
                                        break;
                                    }
                                }
                            }
                        }
                        pipout.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };

            Thread t2 = new Thread() {
                @Override
                public void run() {
                    try (AffinityLock al = AffinityLock.acquireLock(core2)) {
                        byte[] buf = new byte[finalI];
                        int len = 0;
                        int j = 0;
                        while (true) {
                            synchronized (li) {
                                if (li.size() == 1) {
                                    len = pipin.read(buf);
                                    li.remove(0);
                                    if (j++ == repeat - 1) {
                                        flag.add(1);
                                        break;
                                    }
                                }
                            }
                        }
                        pipin.close();
                    } catch (Exception e) {
                    }
                }
            };
            t1.start();
            t2.start();
            try (AffinityLock al = AffinityLock.acquireLock(3)) {
                while (true) {
                    Thread.sleep(10);
                    if (flag.size() == 1) {
                        Thread.sleep(100);
                        break;
                    }
                }
            }
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("x", packgetSize);
        jsonObject.put("y", res);
        return jsonObject;
    }

    private static byte[] getRandomByte(int length) {
        Random random = new Random();
        byte[] res = new byte[length];
        random.nextBytes(res);
        return res;
    }
}
