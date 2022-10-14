package org.parallel.web.modules.bmc.thread;

import com.alibaba.fastjson.JSONObject;
import org.parallel.web.modules.bmc.Rtc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;


public class Http {
    @Autowired
    private static Rtc rtc;
    public static JSONObject httpPull(ArrayList<NetworkRequestConfig> networkRequestConfigs) throws ExecutionException, InterruptedException {
        JSONObject res = new JSONObject();
        ExecutorService testPool = ThreadPool.httpPool();
        List<Future<String>> result = new ArrayList<>();
        for (int i = 0; i < networkRequestConfigs.size(); i++)
        {
            Callable callable = new Callable() {
                JSONObject js;
                @Override
                public JSONObject call() throws Exception {
                    for (int i=0;i< networkRequestConfigs.size();i++){
                        switch (networkRequestConfigs.get(i).getMethod()){
                            case "post":
                                if (networkRequestConfigs.get(i).getPart().equals("http")){
                                    ResponseEntity<String> response = rtc.postResponseEntity(networkRequestConfigs.get(i).getUrl()
                                            , networkRequestConfigs.get(i).getMap()
                                            , networkRequestConfigs.get(i).getBody().toJSONString());
                                    js.put("",response.toString());
                                }
                                else if(networkRequestConfigs.get(i).getPart().equals("body")){
                                    String response = rtc.post(networkRequestConfigs.get(i).getUrl()
                                            , networkRequestConfigs.get(i).getMap()
                                            , networkRequestConfigs.get(i).getBody().toJSONString());
                                    js.put("",response);
                            }
                                break;
                            case "get":
                                if (networkRequestConfigs.get(i).getPart().equals("http")){
                                    ResponseEntity<String> response = rtc.getResponseEntity(networkRequestConfigs.get(i).getUrl()
                                            , networkRequestConfigs.get(i).getMap()
                                            , networkRequestConfigs.get(i).getBody().toJSONString());
                                    js.put("",response.toString());
                                }
                                else if(networkRequestConfigs.get(i).getPart().equals("body")){
                                    String response = rtc.get(networkRequestConfigs.get(i).getUrl()
                                            , networkRequestConfigs.get(i).getMap()
                                            , networkRequestConfigs.get(i).getBody().toJSONString());
                                    js.put("",response);
                                }
                            default:
                                System.out.println("网络请求参数填写错误");
                        }
                    }
                    return js;
                }
            };
            result.add(testPool.submit(callable));
        }
        for(int i = 0;i<networkRequestConfigs.size();i++){
            res.put("",result.get(i).get());
        }
        testPool.shutdown();
        return res;
    }
}

