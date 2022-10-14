package org.parallel.client.kernel.model;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import lombok.SneakyThrows;
import org.parallel.client.kernel.model.obtain.StaticInfo;
import org.springframework.stereotype.Component;
import org.parallel.common.utils.ShellUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.text.DecimalFormat;

@Data
@Component
public class ProcessInfo extends BaseInfo {

    public JSONObject getprocessinfo() throws IOException, InterruptedException {
        int processNum = 0;
        DecimalFormat fnum = new DecimalFormat("##0.##");
        String[][] process = new String[1000][100];
        String shell = "ls /proc/";
        BufferedReader reader = ShellUtils.execShell(shell);
        String str;
        String tmpstr;
        //获取总内存
        String memtotal;
        String shell3 = "cat /proc/meminfo |head -1";
        String[] shell_3 = {"sh", "-c", shell3};
        BufferedReader reader3 = ShellUtils.execShell(shell_3);
        if ((memtotal = reader3.readLine()) != null) {
            memtotal = memtotal.split("\\s+")[1];
        } else {
            System.out.println("get memtatol failed");
        }
        JSONObject pro = new JSONObject();
        while ((str = reader.readLine()) != null && ((str.charAt(0) == '0') ||
                (str.charAt(0) == '1') || (str.charAt(0) == '2') || (str.charAt(0) == '3') ||
                (str.charAt(0) == '4') || (str.charAt(0) == '5') || (str.charAt(0) == '6'
                || (str.charAt(0) == '7') || (str.charAt(0) == '8') || (str.charAt(0) == '9')))) {
            String tmpshell = "cat /proc/" + str + "/stat";
//            String shell1 ="id -nu $"+i;
//            BufferedReader reader1 = ShellUtils.execShell(shell1);
            BufferedReader tmpreader = ShellUtils.execShell(tmpshell);
            while ((tmpstr = tmpreader.readLine()) != null) {
                int shellResultNum = 0;
                JSONObject res = new JSONObject();
                //获取/proc/pid/stat信息
                for (int j = 0; j < tmpstr.split("\\s+").length; j++) {
                    process[processNum][j] = tmpstr.split("\\s+")[j];
                    if (j == 0)
                        res.put("pid", process[processNum][j]);
                    else if (j == 2)
                        res.put("s", process[processNum][j]);
                    else if (j == 17)
                        res.put("pri", process[processNum][j]);
                    else if (j == 18)
                        res.put("ni", process[processNum][j]);
                }
                //获取进程cpu使用率
                double utime=0,stime=0,cutime=0,cstime=0;
                double user=0,nice=0,system=0,idle=0,iowait=0,irq=0,softirq=0,stealstolen=0,guest=0;
                double totalCPUTime=0;
                double processCPUTime=0;
                String shell4  = "cat /proc/stat |head -1";
                String[] shell_4 = {"sh", "-c", shell4};
                String str4_1="cat /proc/" + str + "/stat";
                String str4_3;
                BufferedReader reader4_3 = ShellUtils.execShell(str4_1);
                while ((str4_3 = reader4_3.readLine()) != null) {
                    for (int j = 0; j < str4_3.split("\\s+").length; j++) {
                        if (j == 13)
                            utime = Double.valueOf(str4_3.split("\\s+")[j].toString()).doubleValue();
//                        utime1 = Float.parseFloat(process[processNum][j]);
                        else if (j == 14)
                            stime = Double.valueOf(str4_3.split("\\s+")[j].toString()).doubleValue();
                        else if (j == 15)
                            cutime = Double.valueOf(str4_3.split("\\s+")[j].toString()).doubleValue();
                        else if (j == 16)
                            cstime = Double.valueOf(str4_3.split("\\s+")[j].toString()).doubleValue();
                    }
                }
                BufferedReader reader4 = ShellUtils.execShell(shell_4);
                String str4 = reader4.readLine();
                for (int j = 0; j < str4.split("\\s+").length; j++)
                {
                    if (j==1)
//                        user1 = Float.parseFloat(str4.split("\\s+")[j]);
                        user = Double.valueOf(str4.split("\\s+")[j].toString()).doubleValue();
                    if (j==2)
                        nice = Double.valueOf(str4.split("\\s+")[j].toString()).doubleValue();
                    if (j==3)
                        system = Double.valueOf(str4.split("\\s+")[j].toString()).doubleValue();
                    if (j==4)
                        idle = Double.valueOf(str4.split("\\s+")[j].toString()).doubleValue();
                    if (j==5)
                        iowait = Double.valueOf(str4.split("\\s+")[j].toString()).doubleValue();
                    if (j==6)
                        irq = Double.valueOf(str4.split("\\s+")[j].toString()).doubleValue();
                    if (j==7)
                        softirq = Double.valueOf(str4.split("\\s+")[j].toString()).doubleValue();
                    if (j==8)
                        stealstolen = Double.valueOf(str4.split("\\s+")[j].toString()).doubleValue();
                    if (j==9)
                        guest = Double.valueOf(str4.split("\\s+")[j].toString()).doubleValue();
                }

                processCPUTime = utime+stime+cutime+cstime;
                totalCPUTime = user+nice+system+idle+iowait+irq+softirq+stealstolen+guest;
                res.put("processCPUTime",processCPUTime);
                res.put("totalCPUTime",totalCPUTime);
                pro.put("pid" + res.get("pid"), res);
            }
            processNum++;
        }
        processNum = 0;
        Thread.sleep(100);
        reader = ShellUtils.execShell(shell);
        while ((str = reader.readLine()) != null && ((str.charAt(0) == '0') ||
                (str.charAt(0) == '1') || (str.charAt(0) == '2') || (str.charAt(0) == '3') ||
                (str.charAt(0) == '4') || (str.charAt(0) == '5') || (str.charAt(0) == '6'
                || (str.charAt(0) == '7') || (str.charAt(0) == '8') || (str.charAt(0) == '9')))) {
            String tmpshell = "cat /proc/" + str + "/stat";
//            String shell1 ="id -nu $"+i;
//            BufferedReader reader1 = ShellUtils.execShell(shell1);
            BufferedReader tmpreader = ShellUtils.execShell(tmpshell);
            while ((tmpstr = tmpreader.readLine()) != null) {
                int shellResultNum = 0;
                JSONObject res = new JSONObject();
                //获取/proc/pid/stat信息
                for (int j = 0; j < tmpstr.split("\\s+").length; j++) {
                    process[processNum][j] = tmpstr.split("\\s+")[j];
                    if (j == 0)
                        res.put("pid", process[processNum][j]);
                    else if (j == 2)
                        res.put("s", process[processNum][j]);
                    else if (j == 17)
                        res.put("pri", process[processNum][j]);
                    else if (j == 18)
                        res.put("ni", process[processNum][j]);
                }
                //  获取进程的可执行文件名
                String shell1 ="cat /proc/"+process[processNum][0]+"/comm";
                BufferedReader reader1 = ShellUtils.execShell(shell1);
                String str1;
                if((str1 = reader1.readLine())!=null)
                {
                    res.put("comm",str1);
                }
                else System.out.println("not recieve comm");
                //获取进程内存使用情况
                String shell2 = "cat /proc/" + str + "/status |head -50";
                String[] shell_2 = {"sh", "-c", shell2};
                String str2;
                BufferedReader reader2 = ShellUtils.execShell(shell_2);

                for (int i = 0; (str2 = reader2.readLine()) != null; i++) {
                    if (str2.split("\\s+")[0].equals("VmSize:")){
                        res.put("virt", str2.split("\\s+")[1]);
                    }
                    if (str2.split("\\s+")[0].equals("VmRSS:")) {
                        res.put("rss", str2.split("\\s+")[1]);
                        float memUseRate = ((Float.valueOf(str2.split("\\s+")[1]).floatValue()) / Float.valueOf(memtotal).floatValue())*100;
                        String memUseRate_1 = fnum.format(memUseRate);
                        res.put("%mem", memUseRate_1);
                        break;
                    }
                    if(res.get("virt")==null){
                        res.put("virt",0);
                        if (res.get("%mem")==null){
                            res.put("%mem", 0);
                        }
                    }
                    if(res.get("rss")==null){
                        res.put("rss",0);
                        if (res.get("%mem")==null){
                            res.put("%mem", 0);
                        }
                    }
                }
                double utime=0,stime=0,cutime=0,cstime=0;
                double user=0,nice=0,system=0,idle=0,iowait=0,irq=0,softirq=0,stealstolen=0,guest=0;
                double totalCPUTime=0,totalCPUTime1=0,totalCPUTime2=0;
                double processCPUTime=0,processCPUTime1=0,processCPUTime2=0;
                double processCPUUse=0;
                String shell4  = "cat /proc/stat |head -1";
                String[] shell_4 = {"sh", "-c", shell4};
                String str4_1="cat /proc/" + str + "/stat";
                String str4_3;
                BufferedReader reader4_3 = ShellUtils.execShell(str4_1);
                while ((str4_3 = reader4_3.readLine()) != null) {
                    for (int j = 0; j < str4_3.split("\\s+").length; j++) {
                        if (j == 13)
                            utime = Double.valueOf(str4_3.split("\\s+")[j].toString()).doubleValue();
//                        utime1 = Float.parseFloat(process[processNum][j]);
                        else if (j == 14)
                            stime = Double.valueOf(str4_3.split("\\s+")[j].toString()).doubleValue();
                        else if (j == 15)
                            cutime = Double.valueOf(str4_3.split("\\s+")[j].toString()).doubleValue();
                        else if (j == 16)
                            cstime = Double.valueOf(str4_3.split("\\s+")[j].toString()).doubleValue();
                    }
                }
                BufferedReader reader4 = ShellUtils.execShell(shell_4);
                String str4 = reader4.readLine();
                for (int j = 0; j < str4.split("\\s+").length; j++)
                {
                    if (j==1)
//                        user1 = Float.parseFloat(str4.split("\\s+")[j]);
                        user = Double.valueOf(str4.split("\\s+")[j].toString()).doubleValue();
                    if (j==2)
                        nice = Double.valueOf(str4.split("\\s+")[j].toString()).doubleValue();
                    if (j==3)
                        system = Double.valueOf(str4.split("\\s+")[j].toString()).doubleValue();
                    if (j==4)
                        idle = Double.valueOf(str4.split("\\s+")[j].toString()).doubleValue();
                    if (j==5)
                        iowait = Double.valueOf(str4.split("\\s+")[j].toString()).doubleValue();
                    if (j==6)
                        irq = Double.valueOf(str4.split("\\s+")[j].toString()).doubleValue();
                    if (j==7)
                        softirq = Double.valueOf(str4.split("\\s+")[j].toString()).doubleValue();
                    if (j==8)
                        stealstolen = Double.valueOf(str4.split("\\s+")[j].toString()).doubleValue();
                    if (j==9)
                        guest = Double.valueOf(str4.split("\\s+")[j].toString()).doubleValue();
                }
                processCPUTime1 = utime+stime+cutime+cstime;
//                System.out.println("processCPUTime1 is : "+processCPUTime1);
//                System.out.println("processCPUTime2 is : "+processCPUTime2);
                totalCPUTime1 = user+nice+system+idle+iowait+irq+softirq+stealstolen+guest;
//                System.out.println("totalCPUTime1 is : "+totalCPUTime1);
//                System.out.println("totalCPUTime2 is : "+totalCPUTime2);
                JSONObject singlePro = new JSONObject();
                singlePro = pro.getJSONObject("pid"+str);

//                考虑为空的情况
                if (singlePro==null){
                    res.put("%cpu",0);
                    break;
                }
                processCPUTime2 = singlePro.getDouble("processCPUTime");
                totalCPUTime2 = singlePro.getDouble("totalCPUTime");
                processCPUTime = processCPUTime1-processCPUTime2;
                totalCPUTime = totalCPUTime1-totalCPUTime2;
                processCPUUse = processCPUTime/totalCPUTime;
                String processCPUUse_1 = fnum.format(processCPUUse*100);
                res.put("%cpu",processCPUUse_1);
                //获取启动进程的命令
//                String shell4 ="cat /proc/"+process[processNum][0]+"/cmdline";
//                BufferedReader reader4 = ShellUtils.execShell(shell1);
//                String str4;
//                if((str4 = reader4.readLine())!=null)
//                {
//                    res.put("comm",str4);
//                }
//                else System.out.println("not recieve cmdline");
                pro.put("pid" + res.get("pid"), res);
            }
            processNum++;
        }
        System.out.println("pro is : " + pro);
        return pro;
    }
}