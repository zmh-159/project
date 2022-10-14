/*
 *  Copyright 2019-2020 Zheng Jie
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.parallel.web.modules.pm.config;

import org.parallel.web.annotation.Log;
import org.parallel.common.utils.RestTemplateClient;
import org.parallel.web.modules.pm.bean.ServerConfig;
import org.parallel.web.modules.pm.config.domain.PmDeploy;
import org.parallel.web.modules.pm.config.service.PmDeployService;
import org.parallel.web.modules.pm.config.service.dto.PmDeployDto;
import org.parallel.web.modules.pm.config.service.dto.PmDeployQueryCriteria;
import org.springframework.data.domain.Pageable;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.*;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author yuyifade
 * @website https://el-admin.vip
 * @date 2021-09-24
 **/
@RestController
@RequiredArgsConstructor
@Api(tags = "监控：脚本管理")
@RequestMapping("/api/pmDeploy")
public class PmDeployController {

    private final PmDeployService pmDeployService;


    private final ServerConfig sc;
    private final RestTemplateClient rtc;

    @ApiOperation("安装/卸载脚本")
    @PostMapping(value = "/install")
    @PreAuthorize("@el.check('pmDeploy:install')")
    public ResponseEntity<Object> index(HttpServletRequest request) {
        try {
            long nodeId = Long.parseLong(request.getParameter("node_id"));
            long deployId = Long.parseLong(request.getParameter("deploy_id"));
            String type = request.getParameter("type");
            //获取对应脚本
            PmDeployDto p = pmDeployService.findById(deployId);
            if (p == null) {
                return new ResponseEntity<>("脚本不存在！", HttpStatus.BAD_REQUEST);
            }
            String shell;
            if (type.equals("install")) {
                pmDeployService.installRecord(deployId, nodeId);
                shell = p.getInstallShell();
            } else {
                pmDeployService.deleteRecord(deployId, nodeId);
                shell = p.getDeleteShell();
            }
            MultiValueMap<String, String> mss = new LinkedMultiValueMap<>();

            mss.add("node_id", nodeId + "");
            mss.add("shell", shell);
            return new ResponseEntity<>(rtc.sendPost(sc.appInstall(), mss), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @ApiOperation("查询脚本运行状态")
    @GetMapping(value = "/query")
    @PreAuthorize("@el.check('pmDeploy:query')")
    public ResponseEntity<Object> result(HttpServletRequest request) {
        try {
            long shellId = Long.parseLong(request.getParameter("shell_id"));
            long nodeId = Long.parseLong(request.getParameter("node_id"));

            String url = sc.appResult() + "?shell_id=" + shellId + "&node_id=" + nodeId;
            return new ResponseEntity<>(rtc.send(url), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

//    @PostMapping(value = "/addNode")
//    @Log("新增脚本安装记录")
//    @ApiOperation("新增脚本安装记录")
//    @PreAuthorize("@el.check('pmDeploy:addNode')")
//    public ResponseEntity<Object> addNode(Long deploy_id, Long node_id) {
//        pmDeployService.installRecord(deploy_id, node_id);
//        return new ResponseEntity<>(HttpStatus.CREATED);
//    }
//
//    @PostMapping(value = "/deleteNode")
//    @Log("删除脚本安装记录")
//    @ApiOperation("删除脚本安装记录")
//    @PreAuthorize("@el.check('pmDeploy:deleteNode')")
//    public ResponseEntity<Object> deleteNode(Long deploy_id, Long node_id) {
//        pmDeployService.deleteRecord(deploy_id, node_id);
//        return new ResponseEntity<>(HttpStatus.OK);
//    }

    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('pmDeploy:list')")
    public void download(HttpServletResponse response, PmDeployQueryCriteria criteria) throws IOException {
        pmDeployService.download(pmDeployService.queryAll(criteria), response);
    }

    @GetMapping
    @ApiOperation("查询脚本")
    @PreAuthorize("@el.check('pmDeploy:list')")
    public ResponseEntity<Object> query(PmDeployQueryCriteria criteria, Pageable pageable) {
        return new ResponseEntity<>(pmDeployService.queryAll(criteria, pageable), HttpStatus.OK);
    }

    @PostMapping
    @Log("新增脚本")
    @ApiOperation("新增脚本")
    @PreAuthorize("@el.check('pmDeploy:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody PmDeploy resources) {
        return new ResponseEntity<>(pmDeployService.create(resources), HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改脚本")
    @ApiOperation("修改脚本")
    @PreAuthorize("@el.check('pmDeploy:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody PmDeploy resources) {
        pmDeployService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除脚本")
    @ApiOperation("删除脚本")
    @PreAuthorize("@el.check('pmDeploy:del')")
    @DeleteMapping
    public ResponseEntity<Object> delete(@RequestBody Long[] ids) {
        pmDeployService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}