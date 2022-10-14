package org.parallel.client.service;

import com.alibaba.fastjson.JSONObject;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.parallel.common.utils.CommonUtils;
import org.parallel.common.utils.ShellUtils;
import org.springframework.stereotype.Component;
import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;
import static org.parallel.client.kernel.model.BaseInfo.interceptList;

@Component
@RequiredArgsConstructor
@Slf4j
public class NodeDetect {
    private final static Object object = new Object();
    private static int isRuning = 0;

    @SneakyThrows
    public JSONObject memoryTest() {
        double avg = 0.0;
        JSONObject res = new JSONObject();
        List<String> file = new ArrayList<>();
        List<String> stream = new ArrayList<>();
        synchronized (object) {
            if (isRuning == 0) {
                isRuning = 1;
            } else {
                res.put("message", "磁盘测试程序正在运行！");
                res.put("status", 1);
                return res;
            }
            BufferedReader reader = ShellUtils.execShell("ls ./");
            String str;

            while ((str = reader.readLine()) != null) {
                file.add(str);
            }
            if (file.contains("stream.c")) {
                String[] shell = {"sh", "-c", "gcc -mtune=native -march=native -O3 -fopenmp -DSTREAM_ARRAY_SIZE=100000000 -DNTIMES=30 stream.c -o stream"};
                ShellUtils.execShell(shell);
                String shell1 = "./stream";
                BufferedReader reader_stream = ShellUtils.execShell(shell1);
                String str1;
                while ((str1 = reader_stream.readLine()) != null) {
                    stream.add(str1);
                }
                List<String> li = interceptList(4, "Copy:", stream);
                double j;
                for (String s : li) {
                    j = Double.parseDouble(s.trim().split("\\s+")[1]);
                    avg += j;
                }
                avg = CommonUtils.getDoubleWith2(avg / li.size());
                res.put("res", avg);
                //ShellUtils.execShell("rm ./stream");
            } else {
                res.put("failed", "未找到测试程序");
            }

            isRuning = 0;
            return res;
        }

    }

    @SneakyThrows
    public JSONObject diskTest(String filepath) {
        JSONObject res = new JSONObject();
        List<String> folder = new ArrayList<>();

        synchronized (object) {
            if (isRuning == 0) {
                isRuning = 2;
            } else {
                res.put("message", "内存测试程序正在运行！");
                res.put("status", 2);
                return res;
            }

            String shell = "ls " + filepath;
            BufferedReader reader = ShellUtils.execShell(shell);
            String s;
            while ((s = reader.readLine()) != null) {
                folder.add(s);
            }
            if (!folder.contains("1Gb.file")) {
                //写数据
                String shell_write = "echo 3 > /proc/sys/vm/drop_caches;env LANGUAGE=uc_EN dd if=/dev/zero bs=1024 count=10000000 of=" + filepath + "/1Gb.file";
                String[] shell1 = {"sh", "-c", shell_write};
                BufferedReader reader1 = ShellUtils.execShellError(shell1);
                String str, write = "";
                while ((str = reader1.readLine()) != null) {
                    write = str;
                }
                res.put("write", write.trim().split(", ")[3]);
                //读数据
                String shell_read = "echo 3 > /proc/sys/vm/drop_caches;env LANGUAGE=uc_EN dd if=" + filepath + "/1Gb.file bs=1024 of=/dev/null";
                String[] shell2 = {"sh", "-c", shell_read};
                BufferedReader reader2 = ShellUtils.execShellError(shell2);
                String str1, read = "";
                while ((str1 = reader2.readLine()) != null) {
                    //read = str1.trim().split(", ")[1];
                    read = str1;
                }
                res.put("read", read.trim().split(", ")[3]);

                ShellUtils.execShell("rm " + filepath + "/1Gb.file");
            } else {
                res.put("failed", "文件已存在");
            }

            isRuning = 0;
            return res;
        }
    }

    //取出第一个List中最大值的位置对应的第二个List的值
    public String maxValue(List<Long> a, List<String> b) {
        double max = 0;
        int i = 0;
        for (int j = 0; j < a.size(); j++) {
            if (a.get(j) > max) {
                max = a.get(j);
                i = j;
            }
        }

        return b.get(i);
    }

    @SneakyThrows
    public String filePath(String disk) {
        List<String> li = new ArrayList<>();
        List<Long> space = new ArrayList<>();
        List<String> path = new ArrayList<>();

        String shell = "lsblk -b |grep " + disk;
        String[] shell_disk = {"sh", "-c", shell};
        BufferedReader reader = ShellUtils.execShell(shell_disk);
        String str;
        while ((str = reader.readLine()) != null) {
            li.add(str + " null");
        }
        for (int i = 1; i < li.size(); i++) {
            if (li.get(i).split("\\s+")[6] != null) {
                space.add(Long.parseLong(li.get(i).split("\\s+")[3]));
                path.add(li.get(i).split("\\s+")[6]);
            }
        }

        return maxValue(space, path);
    }
}
