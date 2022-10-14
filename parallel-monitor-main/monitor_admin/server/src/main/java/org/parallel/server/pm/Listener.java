package org.parallel.server.pm;

/**
 * @author yuyifade
 * @description 动态及静态信息监听器
 * @date 2021/6/25 下午2:59
 */

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.parallel.server.service.AlarmConfig;
import org.parallel.server.service.IntervalConfig;
import org.parallel.server.service.NodeSave;
import org.parallel.server.service.OnlineNode;
import org.parallel.web.utils.CommonUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

@Slf4j
@Service
@Data
@RequiredArgsConstructor
public class Listener {
    @Value("${pm.udp.udpPort}")
    private int udpPort;
    @Value("${pm.udp.tcpPort}")
    private int tcpPort;
    @Value("${pm.udp.maxUdpDataSize}")
    private int maxSize;
    //节点信息存储
    private final NodeSave node;

    private final OnlineNode nic;
    private final IntervalConfig interValConfig;
    private final AlarmConfig alarmConfig;

    /***
     * @description 开启udp监听线程获取节点动态信息
     * @author yuyifade
     * @date 2021/7/3 下午3:47
     * @return void
     */
    @Async
    public void nodeDynamicInfo() {
        try (DatagramSocket socket = new DatagramSocket(udpPort)) {
            log.info("=======开启UDP监听线程端口{}：采集节点动态信息======", udpPort);
            while (true) {
                /** 接收数据格式 **/
                byte buffer[] = new byte[maxSize];
                DatagramPacket p = new DatagramPacket(buffer, buffer.length);
                socket.receive(p);
                String ip = p.getAddress().getHostAddress();

                /** 动态数据格式转换 **/
                buffer = p.getData();
                JSONObject recive = (JSONObject) CommonUtils.getObject(buffer);
                /** 存储更新动态数据 **/
                node.fastInsert(recive);
                nic.updateNodeInfo(recive, ip);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /***
     * @description 开启tcp监听线程获取节点静态信息
     * @author yuyifade
     * @date 2021/7/3 下午3:47
     * @return void
     */
    @Async
    @SneakyThrows
    public void nodeStaticInfo() {
        log.info("=======开启TCP监听线程端口{}：采集节点静态信息======", tcpPort);
        try (
                ServerSocket serverSocket = new ServerSocket(tcpPort);
                Socket conn = serverSocket.accept();//接受一个连接
                InputStream in = conn.getInputStream();//输入流
                OutputStream os = conn.getOutputStream();
        ) {
            log.info("=======新连接！来自:{}======", conn.getInetAddress().getHostAddress());
            byte[] buffer = in.readAllBytes();
            //time2
            long server_recieve_time = System.currentTimeMillis();
            String receive = new String(buffer, 0, buffer.length);
            log.info(receive);
            if (buffer.length < maxSize) {//判断数据长度是否越界
                JSONObject info = JSONObject.parseObject(receive);
                String ip = conn.getInetAddress().getHostAddress();
                JSONObject res = node.saveStaticInfo(info, ip);
                //time3
                res.put("server_recieve_time", server_recieve_time);
                res.put("server_send_time", System.currentTimeMillis());
                //传输采样模式，采样间隔相关参数
                JSONObject interval = interValConfig.getConfig();
                res.put("big_interval", interval.getIntValue("big_interval"));
                res.put("small_interval", interval.getIntValue("small_interval"));
                res.put("mode", interval.getIntValue("mode"));
                //报警参数
                JSONObject test = alarmConfig.getConfig(res.getLongValue("node_id"));
                res.put("alarm", test);
                os.write(res.toJSONString().getBytes(StandardCharsets.UTF_8));
                conn.shutdownOutput();
            } else {
                throw new Exception("数据长度越界！");
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}