package org.parallel.client.controller;

import lombok.extern.slf4j.Slf4j;
import org.parallel.client.config.NetworkConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;



/**
 * @author yuyifade
 * @description 发送节点信息
 * @date 2020/9/22 上午9:40
 */
@Component
@Slf4j
public class SendNodeInfo {
    @Autowired
    private NetworkConfig networkConfig;
    /**
     * @param
     * @description 定时发送信息给server
     * ×@author yuyifade
     * ×@date 2020/9/22 上午9:45
     * @returnType void
     */
//    @Scheduled(cron = "0/5 * * * * ?")
//    private void sendNodeInfo() throws JSONException {
//        byte [] dataInfo = CommonUtils.getByteArrayOutputStream(OshiUtils.getInfo());
//        UdpUtils.sendMessage(udpConfig.getServer(), udpConfig.getServerPort(), udpConfig.getLocalPort(), dataInfo);
//    }
}
