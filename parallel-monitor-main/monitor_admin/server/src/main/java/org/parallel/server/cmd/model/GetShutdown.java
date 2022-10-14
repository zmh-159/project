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
public class GetShutdown {
    private final Node node;

    @SneakyThrows
    public JSONObject shutdown(List<Long> nodeIds) {
        Future<JSONObject> fj = node.NodeControl("shutdown", nodeIds);
        return fj.get();
    }
}
