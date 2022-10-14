package org.parallel.server.kernel.udp.observe;

import com.alibaba.fastjson.JSONObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.parallel.common.utils.RestTemplateClient;
import org.parallel.server.bean.ClientConfig;
import org.parallel.server.service.NodeSave;
import org.parallel.server.service.OnlineNode;
import org.springframework.stereotype.Component;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.LinkedList;

@Slf4j
@Component
@RequiredArgsConstructor
public class DynamicObserve implements PropertyChangeListener {
    private final NodeSave node;
    private final OnlineNode nic;

    private final RestTemplateClient rtc;
    private final ClientConfig clientConfig;
    private JSONObject nodeBuffer = new JSONObject();
    private long preTime = 0;
    private long index = 0;

    @Override
    public void propertyChange(PropertyChangeEvent event) {
        JSONObject receive = (JSONObject) event.getNewValue();
        //todo 优化读写锁住
        synchronized (nodeBuffer) {
            nodeBuffer.put(index + "", receive);
            index++;
            long now = System.currentTimeMillis();
            if (nodeBuffer.size() > 100 || now - preTime > 5000) {
                index = 0;
                String nodeInfo = nodeBuffer.toJSONString();
                nodeBuffer.clear();
                preTime = now;
                rtc.sendPostString(clientConfig.upNodeDynamicInfo(), "nodeInfo", nodeInfo);
            }
        }
        node.fastInsert(receive);
        nic.updateNodeInfo(receive, (String) event.getOldValue());
    }

    public static void main(String[] args) {
        LinkedList<Integer> link = new LinkedList<>();
        LinkedList<Integer> linkB = new LinkedList<>();
        long t1 = System.currentTimeMillis();
        for (int i = 0; i < 10; ++i) {
            link.add(i);
        }
        long t2 = System.currentTimeMillis();
        for (int i = 0; i < 100000; ++i) {
            linkB.add(0, i);
        }
        long t3 = System.currentTimeMillis();
        System.out.println(t2 - t1 + "   " + (t3 - t2));
//        System.out.println(link.to);
//        System.out.println(link);
//        System.out.println(linkB);

    }
}
