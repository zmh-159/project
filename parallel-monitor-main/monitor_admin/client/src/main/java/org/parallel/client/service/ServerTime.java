package org.parallel.client.service;

import org.parallel.common.utils.ShellUtils;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;

/**
 * @author yuyifade
 * @description 维护服务器时间
 * @date 2021/9/3 下午12:33
 */
@Service
public class ServerTime {
    //与服务器的误差
    private long timeError = 0;

    /**
     * @param
     * @description 更新误差
     * @author yuyifade
     * @date 2021/9/2 下午10:50
     * @returnType long
     */

    public void update(long error) {
        timeError = error;
//        long totalMilliSeconds = getServerTime();
//        long totalSeconds = totalMilliSeconds / 1000;
//        long currentSecond = totalSeconds % 60;
//        long totalMinutes = totalSeconds / 60;
//        long currentMinute = totalMinutes % 60;
//        long totalHour = totalMinutes / 60;
//        long currentHour = totalHour % 24;
//        String updateCmd = "date -s"+currentHour+":"+currentMinute+":"+currentSecond;
//        BufferedReader reader1 = ShellUtils.execShell(updateCmd);
//        BufferedReader reader2 = ShellUtils.execShell("hwclock –w");//修改硬件时间
    }

    /**
     * @param
     * @description 获取server时间
     * @author yuyifade
     * @date 2021/9/2 下午10:50
     * @returnType long
     */
    public long getServerTime() {
        return System.currentTimeMillis() + timeError;
    }

    /**
     * @param
     * @description 获取误差
     * @author yuyifade
     * @date 2021/9/2 下午10:50
     * @returnType long
     */
    public long getError() {
        return timeError;
    }
}
