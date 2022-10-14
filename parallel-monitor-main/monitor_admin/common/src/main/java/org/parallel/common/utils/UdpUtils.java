package org.parallel.common.utils;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;


/**
 * @author yuyifade
 * @description 发送udp包
 * @date 2020/9/22 上午9:52
 */
public class UdpUtils {
    /**
     * @param [server:目标ip, serverPort:目标端口, localPort:本地socket端口, data:需要发送的数据]
     * @description 发送加密字节数组 udp包
     * @author yuyifade
     * @date 2020/9/22 上午10:06
     * @returnType java.lang.Boolean
     */
    public static Boolean sendMessage(String server, int serverPort, int localPort, byte [] dataByte) {
        try {
            DatagramSocket datagramSocket = new DatagramSocket(localPort);
            DatagramPacket datagramPacket = new DatagramPacket(dataByte, dataByte.length, InetAddress.getByName(server), serverPort);
            datagramSocket.send(datagramPacket);
            datagramSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
