package org.parallel.web.modules.bmc.access;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.models.Response;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.parallel.web.annotation.Log;
import org.parallel.web.modules.bmc.Ft;
import org.parallel.web.modules.bmc.Kp;
import org.parallel.web.modules.bmc.jpa.domain.PbNode;
import org.parallel.web.modules.bmc.jpa.repository.PbNodeRepository;
import org.parallel.web.modules.bmc.jpa.service.PbNodeService;
import org.parallel.web.modules.bmc.jpa.service.dto.PbNodeDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;


@Component
@RestController
@RequestMapping("/api/access")
@RequiredArgsConstructor
public class AccessController {
    @Autowired
    Kp kp;

    @Autowired
    Ft ft;

    @Autowired
    PbNodeRepository pbNodeRepository;
    @Autowired
    Access access;
    @Log("接入新节点")
    @ApiOperation(value = "接入新节点",httpMethod = "GET")
    @ApiParam(required = true,name = "ip 用户名 密码",value = "只能添加新节点，重复添加会返回\"repeat node\"")
    @SneakyThrows
    @GetMapping(value = "/newNode")
    @PreAuthorize("@el.check('pbNode:list')")
    public ResponseEntity<Object> newNode(String ip,String username,String password,String type){
        String result = access.firstPutNodeInfo(username,password,password,type);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Log("获取接入节点列表")
    @ApiOperation(value = "接入新节点",httpMethod = "GET")
    @ApiParam(required = true,name = "nodesId[]",value = "返回已接入列表信息")
    @SneakyThrows
    @GetMapping(value = "/nodesList")
    @PreAuthorize("@el.check('pbNode:list')")
    public ResponseEntity<Object> nodesList(){
        List<PbNode> nodeLists = pbNodeRepository.getNodeAll();
        return new ResponseEntity<>(nodeLists, HttpStatus.OK);
    }
}
