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
package org.parallel.web.modules.bmc.jpa.rest;

import org.parallel.web.annotation.Log;
import org.parallel.web.modules.bmc.jpa.domain.PbNode;
import org.parallel.web.modules.bmc.jpa.repository.PbNodeRepository;
import org.parallel.web.modules.bmc.jpa.service.PbNodeCpuService;
import org.parallel.web.modules.bmc.jpa.service.PbNodeMemoryService;
import org.parallel.web.modules.bmc.jpa.service.PbNodeService;
import org.parallel.web.modules.bmc.jpa.service.dto.PbNodeDto;
import org.parallel.web.modules.bmc.jpa.service.dto.PbNodeQueryCriteria;
import org.springframework.data.domain.Pageable;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.*;

import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

/**
 * @author ruo-yin
 * @website https://el-admin.vip
 * @date 2022-08-09
 **/
@RestController
@RequiredArgsConstructor
@Api(tags = "BMC节点管理")
@RequestMapping("/api/pbNode")
public class PbNodeController {

    private final PbNodeService pbNodeService;
    private final PbNodeRepository pbNodeRepository;
    private final PbNodeCpuService pbNodeCpuService;
    private final PbNodeMemoryService pbNodeMemoryService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('pbNode:list')")
    public void download(HttpServletResponse response, PbNodeQueryCriteria criteria) throws IOException {
        pbNodeService.download(pbNodeService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询bmc节点")
    @ApiOperation("查询bmc节点")
    @PreAuthorize("@el.check('pbNode:list')")
    public ResponseEntity<Object> query(PbNodeQueryCriteria criteria, Pageable pageable) {
        return new ResponseEntity<>(pbNodeService.queryAll(criteria, pageable), HttpStatus.OK);
    }

    @PostMapping
    @Log("新增bmc节点")
    @ApiOperation("新增bmc节点")
    @PreAuthorize("@el.check('pbNode:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody PbNode resources) {
        String ip = resources.getBmcIp();
        PbNodeDto pbNode = pbNodeService.create(resources);
        if (pbNode != null) {
            Long nodeId = pbNodeRepository.getNodeIdByIp(ip);
            pbNodeCpuService.createCpu(nodeId);
            pbNodeMemoryService.createMemory(nodeId);
        }

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改bmc节点")
    @ApiOperation("修改bmc节点")
    @PreAuthorize("@el.check('pbNode:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody PbNode resources) {
        pbNodeService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除bmc节点")
    @ApiOperation("删除bmc节点")
    @PreAuthorize("@el.check('pbNode:del')")
    @DeleteMapping
    public ResponseEntity<Object> delete(@RequestBody Long[] ids) {
        pbNodeService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}