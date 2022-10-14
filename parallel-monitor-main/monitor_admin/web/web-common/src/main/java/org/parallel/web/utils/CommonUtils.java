package org.parallel.web.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * @author yuyifade
 * @description
 * @date 2020/12/25 上午11:26
 */
public class CommonUtils {
    /**
     * @param
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
     * @description 字节数组转对象
     * @author yuyifade
     * @date 2020/12/21 下午5:10
     * @param data
     * @return java.lang.Object
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
}
