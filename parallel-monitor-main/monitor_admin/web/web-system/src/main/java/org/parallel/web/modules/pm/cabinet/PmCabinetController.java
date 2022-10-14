package org.parallel.web.modules.pm.cabinet;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
@Api(tags = "监控：机柜")
@RequestMapping("/api/pmCabinet")
public class PmCabinetController {
    private String HttpServletRequest;
    private final PmCabinetQuery pmCabinetQuery;


    @ApiOperation("查询所有机柜信息")
    @GetMapping(value = "getCabinets")
    @PreAuthorize("@el.check('pmNode:list')")
    public ResponseEntity<JSONObject> getCabinet() {
        JSONObject jo = pmCabinetQuery.getCabinet();
        return new ResponseEntity<>(jo,HttpStatus.OK);
    }

    @ApiOperation("查询单个机柜信息")
    @GetMapping(value = "getCabinet")
    @PreAuthorize("@el.check('pmNode:list')")
    public ResponseEntity<JSONObject>  getCabinet(HttpServletRequest request) {
        String id  = request.getParameter("id");
        JSONObject jo = pmCabinetQuery.getCabinet(id);
        return new ResponseEntity<>(jo,HttpStatus.OK);
    }


    @ApiOperation("查询单个机柜节点信息")
    @GetMapping(value = "getCabinetNode")
    @PreAuthorize("@el.check('pmNode:list')")
    public ResponseEntity<JSONObject> getCabinetNode(HttpServletRequest request) {
        String id  = request.getParameter("cabinet");
        JSONObject jo = pmCabinetQuery.getCabinetNode(id);
        return new ResponseEntity<>(jo,HttpStatus.OK);
    }


    @ApiOperation("添加机柜信息")
    @PostMapping(value = "postCabinet")
    @PreAuthorize("@el.check('pmNode:list')")
    public ResponseEntity<Object> postCabinet(HttpServletRequest request) throws IOException {
        JSONObject jo = JSONObject.parseObject(request.getParameter("cabinet"));
        pmCabinetQuery.postCabinet(jo);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation("添加节点信息")
    @PostMapping(value = "postCabinetNode")
    @PreAuthorize("@el.check('pmNode:list')")
    public ResponseEntity<Object> postCabinetNode(HttpServletRequest request){
        JSONObject jo = JSONObject.parseObject(request.getParameter("id"));
        pmCabinetQuery.postCabinetNode(jo);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation("删除机柜")
    @DeleteMapping(value = "delCabinet")
    @PreAuthorize("@el.check('pmNode:list')")
    public ResponseEntity<Object> delCabinet(HttpServletRequest request) throws IOException {
        JSONObject jo = JSONObject.parseObject(request.getParameter("cabinet"));
        pmCabinetQuery.delCabinet(jo);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation("删除节点")
    @DeleteMapping(value = "delCabinetNode")
    @PreAuthorize("@el.check('pmNode:list')")
    public ResponseEntity<Object> delCabinetNode(HttpServletRequest request){
        JSONObject jo = JSONObject.parseObject(request.getParameter("id"));
        pmCabinetQuery.delCabinetNode(jo);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping(value = "putCabinet")
    @ApiOperation("修改机柜信息")
    @PreAuthorize("@el.check('pmNode:list')")
    public ResponseEntity<Object> putCabinet(HttpServletRequest request){
        JSONObject jo = JSONObject.parseObject(request.getParameter("cabinet"));
        pmCabinetQuery.putCabinet(jo);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping(value = "putCabinetNode")
    @ApiOperation("修改节点信息")
    @PreAuthorize("@el.check('pmNode:list')")
    public ResponseEntity<Object> putCabinetNode(HttpServletRequest request){
        JSONObject jo = JSONObject.parseObject(request.getParameter("id"));
        pmCabinetQuery.putCabinetNode(jo);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}