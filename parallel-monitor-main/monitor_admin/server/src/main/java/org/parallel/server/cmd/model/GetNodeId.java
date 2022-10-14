package org.parallel.server.cmd.model;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.parallel.server.service.OnlineNode;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@RequiredArgsConstructor
@Component
@Slf4j
public class GetNodeId {
    private final EntityManager em;
    private final OnlineNode nic;

    public List<String> allNodeId() {
        String sql;
        sql = "SELECT node_id FROM pm_node";
        Query query = em.createNativeQuery(sql);

        List<String> node = new ArrayList<>();
        for (int i = 0; i < query.getResultList().size(); i++) {
            node.add(query.getResultList().get(i).toString());
        }
        return node;
    }

    public List<Long> getOnlineId() {
        List<Long> onNodeId = new ArrayList<>();
        String[] onlineId = StringUtils.strip(nic.getOnlineId(), "()").split(",");
        for (String s : onlineId) {
            onNodeId.add(Long.parseLong(s));
        }
        onNodeId.sort(Comparator.naturalOrder());
        return onNodeId;
    }

    public List<Long> getOnlineId(List<Long> nodeId) {
        return nodeId;
    }
}
