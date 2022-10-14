package org.parallel.web.modules.bmc;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.ssl.TrustStrategy;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.util.Map;

@Slf4j
@Service
public class Rtc {
    private RestTemplate rt = new RestTemplate(generateHttpRequestFactory());

    //get方法 获取body
    public String get(String url, Map<String, String> headerMap, String body) {
        ResponseEntity<String> response = getResponseEntity(url, headerMap, body);
        return (response == null) ? null : response.getBody();
    }

    //get方法, 获取完整http包
    public ResponseEntity<String> getResponseEntity(String url, Map<String, String> headerMap, String body) {
        try {
            ResponseEntity<String> response = rt.exchange(url, HttpMethod.GET, getRequestEntity(body, headerMap), String.class);
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("获取----------->" + url + " 失败!");
            return null;
        }
    }

    //post方法 获取body
    public String post(String url, Map<String, String> headerMap, String body) {
        ResponseEntity<String> response = postResponseEntity(url, headerMap, body);
        return (response == null) ? null : response.getBody();
    }

    //post方法, 获取完整http包
    public ResponseEntity<String> postResponseEntity(String url, Map<String, String> headerMap, String body) {
        try {
            ResponseEntity<String> response = rt.exchange(url, HttpMethod.POST, getRequestEntity(body, headerMap), String.class);
            response.getBody();
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("获取----------->" + url + " 失败!");
            return null;
        }
    }

    /** 获取HttpEntity **/
    private HttpEntity<String> getRequestEntity(String body, Map<String, String> headerMap) {
        return new HttpEntity<>(body, getHeaders(headerMap));
    }

    /** 获取header **/
    private HttpHeaders getHeaders(Map<String, String> headerMap) {
        HttpHeaders header = new HttpHeaders();
        headerMap.forEach(header::set);
        return header;
    }

    /** 忽略ssl **/
    public HttpComponentsClientHttpRequestFactory generateHttpRequestFactory() {
        try {
            TrustStrategy acceptingTrustStrategy = (x509Certificates, authType) -> true;
            SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(null, acceptingTrustStrategy).build();
            SSLConnectionSocketFactory connectionSocketFactory = new SSLConnectionSocketFactory(sslContext, new NoopHostnameVerifier());
            HttpClientBuilder httpClientBuilder = HttpClients.custom();
            httpClientBuilder.setSSLSocketFactory(connectionSocketFactory);
            CloseableHttpClient httpClient = httpClientBuilder.build();
            HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
            factory.setHttpClient(httpClient);
            return factory;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
