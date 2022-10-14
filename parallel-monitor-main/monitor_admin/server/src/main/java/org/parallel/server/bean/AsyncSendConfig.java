package org.parallel.server.bean;

import com.alibaba.fastjson.JSONObject;
import lombok.RequiredArgsConstructor;
import org.parallel.common.utils.RestTemplateClient;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.Future;

@Service
@RequiredArgsConstructor
public class AsyncSendConfig {
    private final RestTemplateClient rtc;

    @Async
    public Future<JSONObject> send(String urlWithPara) {
        JSONObject res = rtc.send(urlWithPara);
        return new AsyncResult<>(res);
    }
}
