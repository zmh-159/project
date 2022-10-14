package org.parallel.web.modules.pm.webssh;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import javax.websocket.*;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

//TODO 所有websocket继承该类
@Slf4j
public class WebConnect {
    protected static int onlineCount = 0;

    protected static ConcurrentHashMap<String, WebConnect> webSocketMap = new ConcurrentHashMap<>();

    protected Session session;


    protected String uuid = "";

    //    private final PmAlarmService pmAlarmService;
//    private final RestTemplateClient rtc;
    //todo 通过config设置url
    protected static final String url = "http://127.0.0.1:9001/api/realInfo";


    @SneakyThrows
    public static void getRealTimeInfo() {
//        JSONObject res = rtc.send(url);
//        res.put("node_sum", pmNodeService.queryNodeCount());
//        res.put("alarm_un_read_sum", pmAlarmService.queryUnreadAlarmCount());
//        res.put("alarm_read_sum", pmAlarmService.queryReadAlarmCount());
//        res.put("alarm_node_sum", pmAlarmService.queryAlarmNodeCount());
//        int nodeOnline = 0;
//        if (res.containsKey("node_data")) {
//            nodeOnline = res.getJSONObject("node_data").size();
//        }
//        res.put("node_online", nodeOnline);
//        String info = res.toJSONString();
        for (String uuid : webSocketMap.keySet()) {
            sendInfo("sssss", uuid);
        }
    }


    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    @SneakyThrows
    public void onOpen(Session session) {
        this.session = session;
        uuid = UUID.randomUUID().toString().replaceAll("-", "");
        if (webSocketMap.containsKey(uuid)) {
            webSocketMap.remove(uuid);
            webSocketMap.put(uuid, this);
        } else {
            webSocketMap.put(uuid, this);
            addOnlineCount();
        }

        log.info("用户连接:" + uuid + ",当前在线人数为:" + getOnlineCount());
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        if (webSocketMap.containsKey(uuid)) {
            webSocketMap.remove(uuid);
            //从set中删除
            subOnlineCount();
        }
        log.info("用户退出:" + uuid + ",当前在线人数为:" + getOnlineCount());
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    @SneakyThrows
    public void onMessage(String message, Session session) {
        sendMessage("3232434");
        //todo
    }

    /**
     *
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("用户错误:" + this.uuid + ",原因:" + error.getMessage());
        error.printStackTrace();
    }

    /**
     * 实现服务器主动推送
     */
    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }


    /**
     * 发送自定义消息
     */
    public static void sendInfo(String message, String uuid) throws IOException {
        log.info("发送消息到:" + uuid + "，报文:" + message);
        if (StringUtils.isNotBlank(uuid) && webSocketMap.containsKey(uuid)) {
            webSocketMap.get(uuid).sendMessage(message);
        } else {
            log.error("用户" + uuid + ",不在线！");
        }
    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        WebConnect.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        WebConnect.onlineCount--;
    }
}