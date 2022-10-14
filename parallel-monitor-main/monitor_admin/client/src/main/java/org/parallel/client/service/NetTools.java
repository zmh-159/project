package org.parallel.client.service;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.parallel.client.config.NetworkConfig;
import org.parallel.common.utils.CommonUtils;
import org.parallel.common.utils.UdpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * @author yuyifade
 * @description 发送udp 和 tcp
 * @date 2021/9/3 上午11:52
 */
@Service
@Slf4j
public class NetTools {
    @Autowired
    private NetworkConfig nc;

    public JSONObject sendTcp(JSONObject message) {
        String server = nc.getServer();
        int port = nc.getStaticInfoPort();
        try (
                Socket socket = new Socket(server, port);
                OutputStream outs = socket.getOutputStream();
                InputStream ins = socket.getInputStream();
        ) {
            //发送
            outs.write(message.toJSONString().getBytes(StandardCharsets.UTF_8));
            socket.shutdownOutput();
            //接收
            byte recBuffer[] = new byte[10240];
            int len = ins.read(recBuffer);
            long localTime = System.currentTimeMillis();
            String recive = new String(recBuffer, 0, len);
            if (-1 == ins.read(recBuffer)) {
                log.info("已连接到服务器：" + recive);
                JSONObject reciveJO = JSONObject.parseObject(recive);
                reciveJO.put("local_time", localTime);
                return reciveJO;
            } else {
                throw new Exception("报文过长");
            }
        } catch (Exception e) {
            log.error("连接服务器异常！");
            CommonUtils.sleep(5000);
            log.info("尝试重连！");
            return sendTcp(message);
        }
    }

    public void sendUdp(byte[] data) {
        UdpUtils.sendMessage(nc.getServer(), nc.getDynamicInfoPort(), nc.getLocalPort(), data);
    }


}
