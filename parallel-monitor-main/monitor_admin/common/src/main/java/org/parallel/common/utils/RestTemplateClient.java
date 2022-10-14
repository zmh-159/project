package org.parallel.common.utils;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.ConnectException;
import java.util.Base64;

/**
 * @author yuyifade
 * @description http客户端 ，连接server
 * @date 2021/8/23 下午9:27
 */
@Component
@Slf4j
public class RestTemplateClient {
    private RestTemplate rt = new RestTemplate();
    @Value("${pm.http.user}")
    private String user;
    @Value("${pm.http.passwd}")
    private String passwd;

    private HttpHeaders headers;

    private HttpHeaders getHeaders() {
        if (headers == null) {
            headers = new HttpHeaders();
            headers.set("authorization", "Basic " + Base64.getEncoder().encodeToString((user + ":" + passwd).getBytes()));
        }
        return headers;
    }

    /** get **/
    public JSONObject send(String url) {
        HttpEntity<String> requestEntity = new HttpEntity<>(null, getHeaders());
        try {
            ResponseEntity<String> response = rt.exchange(url, HttpMethod.GET, requestEntity, String.class);
            return JSONObject.parseObject(response.getBody());
        } catch (Exception e) {
            JSONObject res = new JSONObject();
            if (e.getCause() instanceof ConnectException) {
                log.info(url + " 连接异常！");
            }
            return res;
        }
    }

    /** post **/
    public JSONObject sendPost(String url, MultiValueMap<String, String> params) {
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params, getHeaders());
        try {
            ResponseEntity<String> response = rt.exchange(url, HttpMethod.POST, requestEntity, String.class);
            return JSONObject.parseObject(response.getBody());
        } catch (Exception e) {
            JSONObject res = new JSONObject();
            if (e.getCause() instanceof ConnectException) {
                log.info(url + " 连接异常！");
            }
            return res;
        }
    }

    public JSONObject sendPostString(String url, String paramName, String param) {
        MultiValueMap<String, String> paramMap = new LinkedMultiValueMap<>();
        paramMap.add(paramName, param);
        return sendPost(url, paramMap);
    }

    public String getAuthorization() {
        return "Basic " + Base64.getEncoder().encodeToString((user + ":" + passwd).getBytes());
    }
}

