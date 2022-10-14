package org.parallel.client.service;

import lombok.extern.slf4j.Slf4j;
import org.parallel.common.utils.ShellUtils;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Hashtable;

/**
 * @author yuyifade
 * @description 执行脚本类
 * @date 2021/9/14 下午2:10
 */
@Slf4j
@Service
public class ShellTool {
    /** 是否有脚本运行资源锁资源锁 **/
    private static final Integer lock = 0;
    /** 标志：是否有脚本运行 **/
    private static Boolean isInstalling = false;
    /** 存储脚本运行状态 **/
    private static final Hashtable<Long, String> result = new Hashtable<>();


    public long execShell(String shell) {
        /** 维护运行锁，确保只有一个脚运行 **/
        synchronized (lock) {
            if (isInstalling) {
                return -1L;
            }
            isInstalling = true;
        }
        long tmpId;
        /** 维护脚本运行结果 **/
        synchronized (result) {
            SecureRandom secureRandom = new SecureRandom();
            while (true) {
                tmpId = Math.abs(secureRandom.nextLong());
                if (!result.containsKey(tmpId)) {
                    result.put(tmpId, "installing");
                    break;
                }
            }
        }
        final long id = tmpId;
        /** 运行脚本 **/
        Thread t = new Thread(() -> {
            try (FileOutputStream fo = new FileOutputStream("./fo.sh")) {
                /** 写入脚本文件 **/
                fo.write(shell.getBytes(StandardCharsets.UTF_8));

                String shells[] = {"sh", "./fo.sh"};
                BufferedReader res = ShellUtils.execShell(shells);
                String pre = null;
                while (true) {
                    String tmp = res.readLine();
                    if (tmp != null) {
                        pre = tmp;
                    } else {
                        break;
                    }
                }
                if (pre != null && pre.equals("success")) {
                    result.put(id, "success");
                } else {
                    result.put(id, "failed");
                }
                /** 删除脚本 **/
                File f = new File("./fo.sh");
                f.delete();
            } catch (IOException e) {
                log.error(e.getMessage());
            }

            synchronized (lock) {
                isInstalling = false;
            }
        });
        t.start();
        return id;
    }

    public String getRes(long id) {
        String res = result.get(id);
        return res == null ? "not found" : res;
    }

    public static void main(String[] args) throws IOException, InterruptedException {
    }
}
