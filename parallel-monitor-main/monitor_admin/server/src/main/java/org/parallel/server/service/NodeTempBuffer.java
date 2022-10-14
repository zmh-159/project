package org.parallel.server.service;

import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.Vector;

/**
 * @author yuyifade
 * @description 存储节点一段时间的信息
 * @date 2021/7/24 下午1:15
 */
public class NodeTempBuffer {
    //缓冲区
    private Vector<JSONObject> buffer;
    //缓冲区大小
    private int maxSize;

    NodeTempBuffer(int size) {
        buffer = new Vector<>();
        this.maxSize = size;
    }

    /***
      * @description 添加记录，若buffer容量达到最大值则将最老值剔除
      * @author yuyifade
      * @date 2021/10/27 上午11:54
      * @param jo
      * @return void
      */
    public void add(JSONObject jo) {
        if (buffer.size() > maxSize) {
            buffer.remove(0);
        }
        buffer.add(jo);
    }

    public JSONObject getByIndex(ArrayList<Integer> sns) {
        JSONObject res = new JSONObject();
        int j = 0;
        for (int i : sns) {
            res.put(j + "", buffer.get(i));
            j++;
        }
        return res;
    }
    /***
      * @description 根据时间上下界获取数据
      * @author yuyifade
      * @date 2021/10/27 上午11:55
      * @param startTime
      * @param endTime
      * @return com.alibaba.fastjson.JSONObject
      */
    public JSONObject getByTime(long startTime, long endTime) {
        JSONObject res = new JSONObject();
        //最近时间
        long time2 = buffer.lastElement().getLong("create_time");
        //最早时间
        long time1 = buffer.firstElement().getLong("create_time");
        if (startTime > endTime || endTime < time1 || startTime > time2) {
            return res;
        }
        //二分查找
        int startIndex = 0;
        if (startTime > time1) {
            int size = buffer.size();
            int l = 0;
            int r = size - 1;

            while (l < r - 1) {
                int p = (l + r) / 2;
                long tmpTime = buffer.get(p).getLongValue("create_time");
                if (tmpTime < startTime) {
                    l = p;
                } else if (tmpTime > startTime) {
                    r = p;
                } else {
                    l = p;
                    r = p;
                }
            }
            startIndex = r;
        }
        //取值
        int i = 0;
        while (startIndex < buffer.size()) {
            if (buffer.get(startIndex).getLongValue("create_time") < endTime) {
                res.put(i + "", buffer.get(startIndex));
                startIndex++;
                i++;
            } else {
                break;
            }
        }
        return res;
    }
    //获取所有记录
    public JSONObject getAll() {
        JSONObject res = new JSONObject();
        int index = 0;
        for (JSONObject i : buffer) {
            res.put(index + "", i);
            index++;
        }
        return res;
    }


    public static void main(String[] args) {
//        NodeTemBuffer ntb = new NodeTemBuffer(100);
//        for (int i = 0; i < 100; ++i) {
//            JSONObject jo = new JSONObject();
//            jo.put("create_time", i);
//            ntb.add(jo);
//        }
//        System.out.println(ntb.getAll());
//        System.out.println(ntb.getByTime(99,1010));
////        System.out.println(ntb.getAll());
    }
}
