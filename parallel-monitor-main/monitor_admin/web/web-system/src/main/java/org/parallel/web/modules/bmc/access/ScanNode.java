package org.parallel.web.modules.bmc.access;

import org.parallel.web.exception.BadRequestException;
import org.parallel.web.modules.bmc.Ft;
import org.parallel.web.modules.bmc.Kp;
import org.parallel.web.modules.bmc.jpa.domain.PbNode;
import org.parallel.web.modules.bmc.jpa.repository.PbNodeRepository;
import org.parallel.web.modules.bmc.jpa.service.PbNodeService;
import org.parallel.web.modules.bmc.jpa.service.dto.PbNodeDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
@Component
@Order(value = 1)
public class ScanNode implements ApplicationRunner {
    @Autowired
    PbNodeService pbNodeService;
    @Autowired
    Kp kp;

    @Autowired
    Ft ft;
    @Autowired
    PbNodeRepository pbNodeRepository;

    @Autowired
    NodesStatus nodesStatus;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        List<PbNode> nodes = pbNodeRepository.getNodeAll();
        System.out.println("扫描各节点");
        for (int i = 0 ;i<nodes.size();i++){
            PbNode node = nodes.get(i);
            Long id = node.getNodeId();
            String type = node.getNodeType();
            String ip = node.getBmcIp();
            String username = node.getUser();
            String password = node.getPasswd();
            String token = null;
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
            nodesStatus.status.replace(id,token);
            PbNode pbNode = new PbNode();
            if (token==null){
                pbNode.setNodeType("off");
            }
            pbNode.setNodeType(type);
            pbNode.setPasswd(password);
            pbNode.setUser(username);
            pbNode.setBmcIp(ip);
            pbNode.setNodeId(id);
            pbNodeService.update(pbNode);
        }
        CycleScanNode cycleScanNode = new CycleScanNode();
        cycleScanNode.start();
    }
}
