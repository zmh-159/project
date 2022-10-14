package org.parallel.client.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.*;


/**
 * @author yuyifade
 * @description
 * @date 2020/9/21 下午10:34
 */
@Slf4j
public class ClientUtils {
    /**
     * @return byte[]
     * @description 将对象转化为字节数组
     * @author yuyifade
     * @date 2020/12/19 下午7:35
     */
    public static byte[] getByteArrayOutputStream(Object object) {
        ByteArrayOutputStream buffers = new ByteArrayOutputStream();
        try {
            ObjectOutputStream out = new ObjectOutputStream(buffers);
            out.writeObject(object);
            out.close();
        } catch (Exception e) {
            System.err.print(e.getMessage());
            return null;
        }
        return buffers.toByteArray();
    }

    /**
     * @return java.lang.Object
     * @description 字节数组转对象
     * @author yuyifade
     * @date 2020/12/21 下午5:10
     */
    public static Object getObject(byte[] data) {

        Object object = null;
        try {
            ByteArrayInputStream buffers = new ByteArrayInputStream(data);
            ObjectInputStream in = new ObjectInputStream(buffers);
            object = in.readObject();
            in.close();
        } catch (Exception e) {
            System.err.print(e.getMessage());
        }
        return object;
    }


    /**
     * @param path 配置文件路径
     * @param key  配置项key
     * @return void
     * @description 从文件读取配置
     * @author hyx yuyifade
     * @date 2020/12/25 下午12:52
     */
    public static String getConfig(String path, String key) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            String line = br.readLine();
            while (line != null) {
                String tmp = line.trim();
                line = br.readLine();
                if (tmp.charAt(0) == '#') {//跳过注释行
                    continue;
                }

                String[] strArr = tmp.split("=");
                if (strArr[0].equals(key)) {
                    return strArr[1];
                }
            }
        } catch (IOException e) {
            log.info("未发现配置文件,使用默认网络配置!");
        }
        return null;
    }

    public static int getNetSpeed() {
        return 0;
    }

    public static void main(String[] args) {
        getNetSpeed();
    }
}


