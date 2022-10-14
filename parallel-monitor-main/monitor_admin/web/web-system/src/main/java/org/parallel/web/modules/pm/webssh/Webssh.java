package org.parallel.web.modules.pm.webssh;

import lombok.extern.slf4j.Slf4j;
import org.java_websocket.handshake.ServerHandshake;
import org.parallel.common.utils.RestTemplateClient;
import org.parallel.web.modules.pm.bean.ServerConfig;
import org.parallel.web.utils.SpringContextHolder;
import org.springframework.stereotype.Component;
import org.java_websocket.drafts.Draft;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

import lombok.SneakyThrows;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.StringUtils;
import org.java_websocket.client.WebSocketClient;


import java.net.URI;

import java.util.UUID;


/**
 * @author yuyifade
 * @description 转发指令到数据存储端
 * @date 2021/8/15 上午10:21
 */
@ServerEndpoint("/webSocket/webssh")
@Component
@Slf4j
public class Webssh {
    private ServerConfig sc;
    private RestTemplateClient rtc;

    private static int onlineCount = 0;

    private static ConcurrentHashMap<String, Webssh> webSocketMap = new ConcurrentHashMap<>();

    private Session session;

    private String uuid = "";

    private WebsocketClient wsClient;

    private String getAuthorization() {
        if (rtc == null) {
            rtc = SpringContextHolder.getBean(RestTemplateClient.class);
        }
        return rtc.getAuthorization();
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
        //todo 通过配置获取
        wsClient = new WebsocketClient(new URI("ws://localhost:9001/webssh"));
        wsClient.addHeader("authorization", getAuthorization());
//        wsClient = new WebsocketClient(new URI(sc.websshUrl()));
        wsClient.connect();

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
        wsClient.close();
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
        while (!wsClient.isOpen()) {
            Thread.sleep(10);
        }
        wsClient.send(message);
    }

    /**
     * @param session
     * @param error
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
        Webssh.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        Webssh.onlineCount--;
    }

    class WebsocketClient extends WebSocketClient {

        public WebsocketClient(URI serverUri, Draft draft) {
            super(serverUri, draft);
        }

        public WebsocketClient(URI serverURI) {
            super(serverURI);
        }

        public WebsocketClient(URI serverUri, Map<String, String> httpHeaders) {
            super(serverUri, httpHeaders);
        }

        @Override
        public void onOpen(ServerHandshake handshakedata) {

        }

        @SneakyThrows
        @Override
        public void onMessage(String message) {
            log.info("发出：\n" + message);
            sendMessage(message);
        }

        @SneakyThrows
        @Override
        public void onClose(int code, String reason, boolean remote) {
            log.info("webclient 退出！");
            session.close();
        }

        @Override
        public void onError(Exception ex) {
            ex.printStackTrace();
        }
    }
}
