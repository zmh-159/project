package org.parallel.server.kernel.udp;

import com.alibaba.fastjson.JSONObject;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.util.CharsetUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@ChannelHandler.Sharable
public class UdpDecoder extends MessageToMessageDecoder<DatagramPacket> {
    private PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);

    @Override
    protected void decode(ChannelHandlerContext ctx, DatagramPacket datagramPacket, List<Object> list) throws Exception {
        ByteBuf buf = datagramPacket.content();
        String ip = datagramPacket.sender().getHostString();
        String msg = buf.toString(CharsetUtil.UTF_8);
        JSONObject recv;
        try {
            recv = JSONObject.parseObject(msg);
        } catch (Exception e) {
            log.warn("udp package format error!");
            return;
        }
        for (String key : recv.keySet()) {
            changeSupport.firePropertyChange(key, ip, recv.getJSONObject(key));
        }
    }


    public boolean addPropertyChangeListener(final String propertyName, final PropertyChangeListener listener) {
        if (changeSupport.hasListeners(propertyName)) {
            return false;
        }
        changeSupport.addPropertyChangeListener(propertyName, listener);
        return true;
    }

    public void removePropertyChangeListener(final PropertyChangeListener listener) {
        changeSupport.removePropertyChangeListener(listener);
    }
}