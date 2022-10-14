package org.parallel.server.cmd.model;

import com.alibaba.fastjson.JSONObject;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.parallel.server.kernel.node.Node;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.Future;

@Component
@Slf4j
@RequiredArgsConstructor
public class GetCpuInfo {
    private final Node node;

    @SneakyThrows
    public JSONObject staticCpuInfo(List<Long> nodeIds) {
        Future<JSONObject> fj = node.getNodeInfo("staticCpuInfo", nodeIds);
        return fj.get();
    }

    @SneakyThrows
    public JSONObject dynamicCpuInfo(List<Long> nodeIds) {
        Future<JSONObject> fj = node.getNodeInfo("dynamicCpuInfo", nodeIds);
        return fj.get();
    }
}
