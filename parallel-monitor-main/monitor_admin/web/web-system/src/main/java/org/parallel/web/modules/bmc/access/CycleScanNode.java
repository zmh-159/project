package org.parallel.web.modules.bmc.access;

import lombok.SneakyThrows;
import org.parallel.web.exception.BadRequestException;
import org.parallel.web.modules.bmc.Ft;
import org.parallel.web.modules.bmc.Kp;
import org.parallel.web.modules.bmc.jpa.domain.PbNode;
import org.parallel.web.modules.bmc.jpa.repository.PbNodeRepository;
import org.parallel.web.modules.bmc.jpa.service.PbNodeService;
import org.parallel.web.modules.bmc.jpa.service.dto.PbNodeDto;
import org.springframework.beans.factory.annotation.Autowired;

import java.security.KeyPair;
import java.util.List;

public class CycleScanNode extends Thread {
    @Autowired
    NodesStatus nodesStatus;
    @Autowired
    PbNodeRepository pbNodeRepository;
    @Autowired
    Kp kp;
    @Autowired
    Ft ft;
    @Autowired
    PbNodeService pbNodeService;
    @SneakyThrows
    @Override
//    定时更新token以及节点状态
    public void run() {
        List<PbNode> nodes = pbNodeRepository.getNodeAll();
        while (true){
            Thread.sleep(5000);
            for (int i=0;i<nodes.size();i++){
                PbNode node = nodes.get(i);
                Long nodeId = node.getNodeId();
                String type = node.getNodeType();
                String username = node.getUser();
                String password = node.getPasswd();
                String ip = node.getBmcIp();
                String token =null;
                switch (type){
                    case "kp":
                        try {
                            token = kp.getToken(username, password, ip);
                        }
                        catch (BadRequestException exception){
                            token = null;
                        }
                        break;
                    case "ft":
                        try {
                            token = ft.getToken(username, password, ip);
                        }
                        catch (BadRequestException exception){
                            token = null;
                        }
                        break;
                    default:
                        break;
                }
                PbNode pbNode = new PbNode();
                if (token==null){
                    pbNode.setNodeType("off");
                }
                pbNode.setNodeType(type);
                pbNode.setPasswd(password);
                pbNode.setUser(username);
                pbNode.setBmcIp(ip);
                pbNode.setNodeId(nodeId);
                pbNodeService.update(pbNode);
                nodesStatus.status.replace(nodeId,token);
            }
        }
    }
}
