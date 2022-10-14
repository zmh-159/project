package org.parallel.server.kernel.node;

import com.alibaba.fastjson.JSONObject;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Future;

@Component
@RequiredArgsConstructor
public class Node {
    private final Fetch fetch;

    /***
     * @description 根据节点信息种类获取数据
     * @author yuyifade
     * @date 2022/1/6 上午10:13
     * @param cmd 信息种类：CPU，磁盘，网卡等。。。
     * @param nodeIds 节点id列表
     * @return java.util.concurrent.Future<com.alibaba.fastjson.JSONObject>
     */
    @Async
    @SneakyThrows
    public Future<JSONObject> getNodeInfo(String cmd, List<Long> nodeIds) {
        JSONObject params = new JSONObject();
        params.put("name", cmd);
        params.put("type", 0);
        params.put("returnType", 0);
        HashMap<Long, Future<JSONObject>> cons = new HashMap<>();
        for (long nodeId : nodeIds) {
            cons.put(nodeId, fetch.connectNode(params, nodeId));
        }
        JSONObject res = new JSONObject();
        for (long nodeId : cons.keySet()) {
            res.put(nodeId + "", cons.get(nodeId).get());
        }
        return new AsyncResult<>(res);
    }

    @Async
    @SneakyThrows
    public Future<JSONObject> NodeControl(String cmd, List<Long> nodeIds) {
        JSONObject params = new JSONObject();
        params.put("name", cmd);
        params.put("type", 1);
        params.put("returnType", 0);
        HashMap<Long, Future<JSONObject>> cons = new HashMap<>();
        for (long nodeId : nodeIds) {
            cons.put(nodeId, fetch.nodeShutdown(params, nodeId));
        }
        JSONObject res = new JSONObject();
        for (long nodeId : cons.keySet()) {
            res.put(nodeId + "", cons.get(nodeId).get());
        }
        return new AsyncResult<>(res);
    }
}
