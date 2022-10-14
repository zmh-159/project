package org.parallel.web.modules.bmc.access;

import org.parallel.web.modules.bmc.Ft;
import org.parallel.web.modules.bmc.Kp;
import org.parallel.web.modules.bmc.jpa.domain.PbNode;
import org.parallel.web.modules.bmc.jpa.repository.PbNodeRepository;
import org.parallel.web.modules.bmc.jpa.service.PbNodeService;
import org.parallel.web.modules.bmc.jpa.service.dto.PbNodeDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class Access {
    @Autowired
    PbNodeRepository pbNodeRepository;
    @Autowired
    PbNodeService pbNodeService;
    @Autowired
    Kp kp;
    @Autowired
    Ft ft;
    @Autowired
    NodesStatus nodesStatus;
    PbNodeDto pbNodeDto = new PbNodeDto();
    public String getToken(String username,String password,String ip,String type){
        String token = null;
        if (pbNodeRepository.getUuidByIp(ip)!=null){
            return "repeat one";
        }
        switch (type){
            case "kp":
                token = kp.getToken(username, password, ip);
                break;
            case "ft":
                token = ft.getToken(username, password, ip);
            default:
                System.out.println("机器类型输入错误");
        }
        return token;
    }
    public String firstPutNodeInfo(String username,String password,String ip,String type){
        PbNode pbNode = new PbNode();
        pbNode.setNodeType(type);
        pbNode.setPasswd(password);
        pbNode.setUser(username);
        pbNode.setBmcIp(ip);
        pbNodeDto=pbNodeService.create(pbNode);
        String token = getToken(username,password,ip,type);
        nodesStatus.status.put(pbNodeDto.getNodeId(), token);
        if (token.equals("repeat one") ){
            return "repeat one";
        }
        else return token;
    }
}
