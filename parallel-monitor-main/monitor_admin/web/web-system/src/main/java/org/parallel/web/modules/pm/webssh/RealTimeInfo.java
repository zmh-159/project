package org.parallel.web.modules.pm.webssh;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.parallel.web.modules.security.security.TokenProvider;
import org.parallel.web.utils.SpringContextHolder;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

import lombok.SneakyThrows;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.StringUtils;

import java.util.UUID;

//TODO 继承WebConnect

/**
 * @author yuyifade
 * @description 转发指令到数据存储端
 * @date 2021/8/15 上午10:21
 */
@ServerEndpoint("/webSocket/realTimeInfo")
@Component
@Slf4j
public class RealTimeInfo {
    private static int onlineCount = 0;

    private static ConcurrentHashMap<String, RealTimeInfo> webSocketMap = new ConcurrentHashMap<>();

    private Session session;

    private String token;

    private String uuid = "";

    @SneakyThrows
    public static void trigger(JSONObject info) {
        String infoStr = info.toJSONString();
        for (String uuid : webSocketMap.keySet()) {
            sendInfo(infoStr, uuid);
        }
    }


    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    @SneakyThrows
    public void onOpen(Session session) {
        this.session = session;
        String bearToken = session.getRequestParameterMap().get("token").get(0);
        token = bearToken.replace("Bearer ", "");

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
        TokenProvider t = SpringContextHolder.getBean(TokenProvider.class);
        //todo 减少token检查频率
        t.checkRenewal(token);
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
        RealTimeInfo.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        RealTimeInfo.onlineCount--;
    }
}
