package org.parallel.web.modules.pm.server;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.parallel.common.utils.RestTemplateClient;
import org.parallel.jpa.service.PmNodeService;
import org.parallel.web.annotation.AnonymousAccess;
import org.parallel.web.modules.pm.webssh.RealTimeInfo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author yuyifade
 * @description 节点信息收集信息接收
 * @date 2022/2/20 下午4:44
 */
@RestController
@RequiredArgsConstructor
@Api(tags = "监控：节点信息收集")
@RequestMapping("/api/nodeInfo")
@Slf4j
public class ServerInfoController {
    //todo 通过config设置url
    private static final String url = "http://127.0.0.1:9001/api/cluser";
    private final RestTemplateClient rtc;
    private final NodeSave nodeSave;
    private final PmNodeService pmNodeService;

//    @ApiOperation("集群控制")
//    @GetMapping(value = "/shutdown")
//    @PreAuthorize("@el.check('cluserController:shutdown')")
//    public ResponseEntity<Object> index() {
//        //todo 添加各类参数
//        String urlWithPara = url + "/shutdown";
//        JSONObject res = rtc.send(urlWithPara);
//        return new ResponseEntity<>(res,HttpStatus.OK);
//    }

    @ApiOperation("收集动态信息")
    @PostMapping("dynamicInfo")
    @AnonymousAccess
    /** 接收server发送的节点动态信息 **/
    public ResponseEntity<Object> index(HttpServletRequest request) {
        JSONObject infos = JSONObject.parseObject(request.getParameter("nodeInfo"));
        for (String key : infos.keySet()) {
            //TODO 存储节点动态信息到数据库
        }
        /** 触发实时数据传输 **/
        if (RealTimeInfo.getOnlineCount() > 0) {
            RealTimeInfo.trigger(pmNodeService.getRealTimeInfo());
        }
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @ApiOperation("收集静态信息")
    @PostMapping("staticInfo")
    @AnonymousAccess
    /** 接收server发送的节点静态信息 **/
    public ResponseEntity<Object> staticInfo(HttpServletRequest request) {
        JSONObject infos = JSONObject.parseObject(request.getParameter("nodeInfo"));
        String ip = request.getParameter("ip");
        System.out.println(infos);
        System.out.println(ip);

        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
}
