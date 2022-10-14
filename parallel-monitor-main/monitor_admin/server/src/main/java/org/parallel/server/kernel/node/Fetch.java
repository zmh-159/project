package org.parallel.server.kernel.node;

import com.alibaba.fastjson.JSONObject;
import lombok.RequiredArgsConstructor;
import org.parallel.common.utils.RestTemplateClient;
import org.parallel.server.bean.ClientConfig;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.Future;

@Component
@RequiredArgsConstructor
public class Fetch {
    private final RestTemplateClient rtc;
    private final ClientConfig config;

    @Async
    public Future<JSONObject> connectNode(JSONObject params, long nodeId) {
        JSONObject res = rtc.sendPostString(config.getInfo(nodeId), "agreement", params.toJSONString());
        return new AsyncResult<>(res);
    }

    @Async
    public Future<JSONObject> nodeShutdown(JSONObject params, long nodeId) {
        JSONObject res = rtc.send(config.shutdown(nodeId));
        return new AsyncResult<>(res);
    }

}
