package org.parallel.server.cmd.service;

import com.alibaba.fastjson.JSONObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.parallel.common.utils.cmd.StringFormat;
import org.parallel.server.cmd.model.GetNodeId;
import org.parallel.server.cmd.model.GetNodeInfo;
import org.parallel.server.cmd.model.GetProcessInfo;
import org.springframework.stereotype.Component;

import java.text.DecimalFormat;

@Component
@Slf4j
@RequiredArgsConstructor

public class ProcessService {
    private final GetProcessInfo getProcessInfo;
    private final GetNodeId getNodeId;
    private final GetNodeInfo getNodeInfo;
    StringFormat sf;
    public String processStatic(){
        StringBuilder res = new StringBuilder();
        res.append("\u001B[0;0H\u001B[2K");
        JSONObject object = getProcessInfo.staticProcessInfo(getNodeId.getOnlineId());
        for (String s : object.keySet()) {
            JSONObject jo = object.getJSONObject(s);
            res.append("\u001B[").append(91).append("m").append("节点:").append(s).append("\u001B[m").append("\r\n");
            res.append(sf.sufInsertSpaceFormat("pid",8)).append(sf.sufInsertSpaceFormat("rss",10))
                    .append(sf.sufInsertSpaceFormat("virt",10)).append(sf.sufInsertSpaceFormat("%mem",6))
                    .append(sf.sufInsertSpaceFormat("%CPU",6))
                    .append(sf.sufInsertSpaceFormat("s",2)).append(sf.sufInsertSpaceFormat("pri",5))
                    .append(sf.sufInsertSpaceFormat("ni",4)+"comm"+"\r\n");
            int i = 0;
            String[] arr = new String[1000];
            for(String k : jo.keySet()){
                arr[i] = jo.getJSONObject(k).toString();
                i++;
            }
            String[] tmp =new String[1];
            for (int i1=0; i1<jo.size()-1; ++i1)  //比较n-1轮
            {
                for (int j=0; j<jo.size()-1-i1; ++j)  //每轮比较n-1-i次,
                {
//                    if (arr[j]. < arr[j+1])
                    if(JSONObject.parseObject(arr[j]).getFloat("%mem")<JSONObject.parseObject(arr[j+1]).getFloat("%mem"))
                    {
                        tmp[0] = arr[j];
                        arr[j] = arr[j+1];
                        arr[j+1] = tmp[0];
                    }
                }
            }
            for (int i2=0;i2<10;i2++){
                res.append(sf.sufInsertSpaceFormat(JSONObject.parseObject(arr[i2]).get("pid").toString(),8))
                        .append(sf.sufInsertSpaceFormat(JSONObject.parseObject(arr[i2]).get("rss").toString(),10))
                        .append(sf.sufInsertSpaceFormat(JSONObject.parseObject(arr[i2]).get("virt").toString(),10))
                        .append(sf.sufInsertSpaceFormat(JSONObject.parseObject(arr[i2]).get("%mem").toString(),6))
                        .append(sf.sufInsertSpaceFormat(JSONObject.parseObject(arr[i2]).get("%cpu").toString(),6))
                        .append(sf.sufInsertSpaceFormat(JSONObject.parseObject(arr[i2]).get("s").toString(),2))
                        .append(sf.sufInsertSpaceFormat(JSONObject.parseObject(arr[i2]).get("pri").toString(),5))
                        .append(sf.sufInsertSpaceFormat(JSONObject.parseObject(arr[i2]).get("ni").toString(),4))
                        .append(JSONObject.parseObject(arr[i2]).get("comm").toString()+"\r\n");
            }
        }
        return res.toString();
    }
    public String processStatic(String para){
        DecimalFormat fnum = new DecimalFormat("##0.##");StringBuilder res = new StringBuilder();
        JSONObject dy = getNodeInfo.dynamicNodeInfo(getNodeId.getOnlineId());
        int nodes = dy.size();
        res.append("\u001B[0;0H\u001B[2K");
        res.append("当前节点数: ").append(nodes).append("\r\n");
        JSONObject object = getProcessInfo.staticProcessInfo(getNodeId.getOnlineId());
//        int[] ipara = new int[para.length()];
        String[] ipara = new String[para.length()];
        for (int i = 0; i < para.length(); i++) {
            ipara[i] = para.charAt(i)+"";
        }
            if(para.equals("-comm")) {
                System.out.println("1");
                JSONObject comm = new JSONObject();
                res.append(sf.sufInsertSpaceFormat("%mem", 6)+sf.sufInsertSpaceFormat("%CPU", 6) + "comm" + "\r\n");
                for (String s : object.keySet()) {
                    JSONObject jo = object.getJSONObject(s);
                    for (String k : jo.keySet()) {
                        JSONObject comm1 = new JSONObject();
                        JSONObject jo1 = jo.getJSONObject(k);
                        int i = 0;
                        if (comm != null) {
                            for (String t : comm.keySet()) {
                                JSONObject jo2 = comm.getJSONObject(t);
                                if (jo2.get("comm").equals(jo1.get("comm"))) {
                                    float tmp1 = jo2.getFloat("%mem") + jo1.getFloat("%mem");
                                    jo2.put("%mem", fnum.format(tmp1 / nodes));
                                    float tmp2 = jo2.getFloat("%cpu") + jo1.getFloat("%cpu");
                                    jo2.put("%cpu", fnum.format(tmp2 / nodes));
                                    i = 1;
                                }
                            }
                            if (i == 0) {
                                comm1.put("%mem", fnum.format(jo1.getFloat("%mem") / nodes));
                                comm1.put("%cpu", fnum.format(jo1.getFloat("%cpu") / nodes));
                                comm1.put("comm", jo1.get("comm"));
                                comm.put(comm1.get("comm").toString(), comm1);
                            }
                        } else {
                            comm1.put("%mem", fnum.format(jo1.getFloat("%mem") / nodes));
                            comm1.put("%cpu", fnum.format(jo1.getFloat("%cpu") / nodes));
                            comm1.put("comm", jo1.get("comm"));
                            comm.put(comm1.get("comm").toString(), comm1);
                        }
                    }
                }
                int i = 0;
                String[] arr = new String[1000];
                for (String s : comm.keySet()) {
                    arr[i] = comm.getJSONObject(s).toString();
                    i++;
                }
                String[] tmp = new String[1];
                for (int i1 = 0; i1 < comm.size() - 1; ++i1)  //比较n-1轮
                {
                    for (int j = 0; j < comm.size() - 1 - i1; ++j)  //每轮比较n-1-i次,
                    {
//                    if (arr[j]. < arr[j+1])
                        if (JSONObject.parseObject(arr[j]).getFloat("%mem") < JSONObject.parseObject(arr[j + 1]).getFloat("%mem")) {
                            tmp[0] = arr[j];
                            arr[j] = arr[j + 1];
                            arr[j + 1] = tmp[0];
                        }
                    }
                }
                for (int i2 = 0; i2 < 10; i2++) {
                    res.append(sf.sufInsertSpaceFormat(JSONObject.parseObject(arr[i2]).get("%mem").toString(), 6)
                            +sf.sufInsertSpaceFormat(JSONObject.parseObject(arr[i2]).get("%cpu").toString(), 6)
                            + JSONObject.parseObject(arr[i2]).get("comm").toString() + "\r\n");
                }
                return res.toString();
            }

            else if(para.equals("-commc")) {
            JSONObject comm = new JSONObject();
            res.append(sf.sufInsertSpaceFormat("%mem", 6)+sf.sufInsertSpaceFormat("%CPU", 6) + "comm" + "\r\n");
            for (String s : object.keySet()) {
                JSONObject jo = object.getJSONObject(s);
                for (String k : jo.keySet()) {
                    JSONObject comm1 = new JSONObject();
                    JSONObject jo1 = jo.getJSONObject(k);
                    int i = 0;
                    if (comm != null) {
                        for (String t : comm.keySet()) {
                            JSONObject jo2 = comm.getJSONObject(t);
                            if (jo2.get("comm").equals(jo1.get("comm"))) {
                                float tmp1 = jo2.getFloat("%mem") + jo1.getFloat("%mem");
                                jo2.put("%mem", fnum.format(tmp1 / nodes));
                                float tmp2 = jo2.getFloat("%cpu") + jo1.getFloat("%cpu");
                                jo2.put("%cpu", fnum.format(tmp2 / nodes));
                                i = 1;
                            }
                        }
                        if (i == 0) {
                            comm1.put("%mem", fnum.format(jo1.getFloat("%mem") / nodes));
                            comm1.put("%cpu", fnum.format(jo1.getFloat("%cpu") / nodes));
                            comm1.put("comm", jo1.get("comm"));
                            comm.put(comm1.get("comm").toString(), comm1);
                        }
                    } else {
                        comm1.put("%mem", fnum.format(jo1.getFloat("%mem") / nodes));
                        comm1.put("%cpu", fnum.format(jo1.getFloat("%cpu") / nodes));
                        comm1.put("comm", jo1.get("comm"));
                        comm.put(comm1.get("comm").toString(), comm1);
                    }
                }
            }
            int i = 0;
            String[] arr = new String[1000];
            for (String s : comm.keySet()) {
                arr[i] = comm.getJSONObject(s).toString();
                i++;
            }
            String[] tmp = new String[1];
            for (int i1 = 0; i1 < comm.size() - 1; ++i1)  //比较n-1轮
            {
                for (int j = 0; j < comm.size() - 1 - i1; ++j)  //每轮比较n-1-i次,
                {
//                    if (arr[j]. < arr[j+1])
                    if (JSONObject.parseObject(arr[j]).getFloat("%cpu") < JSONObject.parseObject(arr[j + 1]).getFloat("%cpu")) {
                        tmp[0] = arr[j];
                        arr[j] = arr[j + 1];
                        arr[j + 1] = tmp[0];
                    }
                }
            }
            for (int i2 = 0; i2 < 10; i2++) {
                res.append(sf.sufInsertSpaceFormat(JSONObject.parseObject(arr[i2]).get("%mem").toString(), 6)
                        +sf.sufInsertSpaceFormat(JSONObject.parseObject(arr[i2]).get("%cpu").toString(), 6)
                        + JSONObject.parseObject(arr[i2]).get("comm").toString() + "\r\n");
            }
            return res.toString();
        }

            else if(((ipara[1].equals("0"))||(ipara[1].equals("1"))
                    ||(ipara[1].equals("2"))||(ipara[1].equals("3")) || (ipara[1].equals("4"))||
                    (ipara[1].equals("5"))||(ipara[1].equals("6"))||
                    (ipara[1].equals("7")) ||(ipara[1].equals("8"))||(ipara[1].equals("9")))&&!(ipara[para.length()-1].equals("c"))){
                for (String s : object.keySet()) {
                    String s1="-"+s;
                    if (s1.equals(para)) {
                        JSONObject jo = object.getJSONObject(s);
                        res.append("\u001B[").append(91).append("m").append("节点:").append(s).append("\u001B[m").append("\r\n");
                        res.append(sf.sufInsertSpaceFormat("pid", 8)).append(sf.sufInsertSpaceFormat("rss", 10))
                                .append(sf.sufInsertSpaceFormat("virt", 10))
                                .append(sf.sufInsertSpaceFormat("%mem", 6)).append(sf.sufInsertSpaceFormat("%CPU", 6))
                                .append(sf.sufInsertSpaceFormat("s", 2)).append(sf.sufInsertSpaceFormat("pri", 5))
                                .append(sf.sufInsertSpaceFormat("ni", 4) + "comm" + "\r\n");
                        int i1 = 0;
                        String[] arr1 = new String[1000];
                        for (String k : jo.keySet()) {
                            arr1[i1] = jo.getJSONObject(k).toString();
                            i1++;
                        }
                        String[] tmp1 = new String[1];
                        for (int i1_1 = 0; i1_1 < jo.size() - 1; ++i1_1)  //比较n-1轮
                        {
                            for (int j1 = 0; j1 < jo.size() - 1 - i1_1; ++j1)  //每轮比较n-1-i次,
                            {
//                    if (arr[j]. < arr[j+1])
                                if (JSONObject.parseObject(arr1[j1]).getFloat("%mem") < JSONObject.parseObject(arr1[j1 + 1]).getFloat("%mem")) {
                                    tmp1[0] = arr1[j1];
                                    arr1[j1] = arr1[j1 + 1];
                                    arr1[j1 + 1] = tmp1[0];
                                }
                            }
                        }
                        for (int i = 0; i < 10; i++) {
                        }
                        for (int i1_2 = 0; i1_2 < 10; i1_2++) {
                            res.append(sf.sufInsertSpaceFormat(JSONObject.parseObject(arr1[i1_2]).get("pid").toString(), 8))
                                    .append(sf.sufInsertSpaceFormat(JSONObject.parseObject(arr1[i1_2]).get("rss").toString(), 10))
                                    .append(sf.sufInsertSpaceFormat(JSONObject.parseObject(arr1[i1_2]).get("virt").toString(), 10))
                                    .append(sf.sufInsertSpaceFormat(JSONObject.parseObject(arr1[i1_2]).get("%mem").toString(), 6))
                                    .append(sf.sufInsertSpaceFormat(JSONObject.parseObject(arr1[i1_2]).get("%cpu").toString(), 6))
                                    .append(sf.sufInsertSpaceFormat(JSONObject.parseObject(arr1[i1_2]).get("s").toString(), 2))
                                    .append(sf.sufInsertSpaceFormat(JSONObject.parseObject(arr1[i1_2]).get("pri").toString(), 5))
                                    .append(sf.sufInsertSpaceFormat(JSONObject.parseObject(arr1[i1_2]).get("ni").toString(), 4))
                                    .append(JSONObject.parseObject(arr1[i1_2]).get("comm").toString() + "\r\n");
                        }
                        return res.toString();
                    }
                }
            }

            else if(((ipara[1].equals("0"))||(ipara[1].equals("1"))
            ||(ipara[1].equals("2"))||(ipara[1].equals("3")) || (ipara[1].equals("4"))||
                    (ipara[1].equals("5"))||(ipara[1].equals("6"))||
                    (ipara[1].equals("7")) ||(ipara[1].equals("8"))||(ipara[1].equals("9")))&&(ipara[para.length()-1].equals("c"))){
                for (String s : object.keySet()) {
                    String s1="-"+s;
                    if (s1.equals(para.substring(0,para.length()-1))) {
                        JSONObject jo = object.getJSONObject(s);
                        res.append("\u001B[").append(91).append("m").append("节点:").append(s).append("\u001B[m").append("\r\n");
                        res.append(sf.sufInsertSpaceFormat("pid", 8)).append(sf.sufInsertSpaceFormat("rss", 10))
                                .append(sf.sufInsertSpaceFormat("virt", 10))
                                .append(sf.sufInsertSpaceFormat("%mem", 6)).append(sf.sufInsertSpaceFormat("%CPU", 6))
                                .append(sf.sufInsertSpaceFormat("s", 2)).append(sf.sufInsertSpaceFormat("pri", 5))
                                .append(sf.sufInsertSpaceFormat("ni", 4) + "comm" + "\r\n");
                        int i1 = 0;
                        String[] arr1 = new String[1000];
                        for (String k : jo.keySet()) {
                            arr1[i1] = jo.getJSONObject(k).toString();
                            i1++;
                        }
                        String[] tmp1 = new String[1];
//                        for (int i1_1 = 0; i1_1 < jo.size() - 1; ++i1_1)  //比较n-1轮
//                        {
//                            for (int j1 = 0; j1 < jo.size() - 1 - i1_1; ++j1)  //每轮比较n-1-i次,

                        for (int i1_1 = 0; i1_1 < jo.size() - 1; ++i1_1)  //比较n-1轮
                        {
                            for (int j1 = 0; j1 < jo.size() - 1 - i1_1; ++j1)  //每轮比较n-1-i次,
                            {
                                if (JSONObject.parseObject(arr1[j1]).getFloat("%cpu") < JSONObject.parseObject(arr1[j1 + 1]).getFloat("%cpu")) {
                                    tmp1[0] = arr1[j1];
                                    arr1[j1] = arr1[j1 + 1];
                                    arr1[j1 + 1] = tmp1[0];
                                }
                            }
                        }
                        for (int i1_2 = 0; i1_2 < 10; i1_2++) {
                            res.append(sf.sufInsertSpaceFormat(JSONObject.parseObject(arr1[i1_2]).get("pid").toString(), 8))
                                    .append(sf.sufInsertSpaceFormat(JSONObject.parseObject(arr1[i1_2]).get("rss").toString(), 10))
                                    .append(sf.sufInsertSpaceFormat(JSONObject.parseObject(arr1[i1_2]).get("virt").toString(), 10))
                                    .append(sf.sufInsertSpaceFormat(JSONObject.parseObject(arr1[i1_2]).get("%mem").toString(), 6))
                                    .append(sf.sufInsertSpaceFormat(JSONObject.parseObject(arr1[i1_2]).get("%cpu").toString(), 6))
                                    .append(sf.sufInsertSpaceFormat(JSONObject.parseObject(arr1[i1_2]).get("s").toString(), 2))
                                    .append(sf.sufInsertSpaceFormat(JSONObject.parseObject(arr1[i1_2]).get("pri").toString(), 5))
                                    .append(sf.sufInsertSpaceFormat(JSONObject.parseObject(arr1[i1_2]).get("ni").toString(), 4))
                                    .append(JSONObject.parseObject(arr1[i1_2]).get("comm").toString() + "\r\n");
                        }
                        return res.toString();
                    }
                    }
                }

//            else
            return res.toString();
        }
    }
