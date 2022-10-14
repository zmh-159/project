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
import org.parallel.web.modules.bmc.jpa.domain.PbNodePhysicNetwork;
import org.parallel.web.modules.bmc.jpa.service.PbNodePhysicNetworkService;
import org.parallel.web.modules.bmc.jpa.service.dto.PbNodePhysicNetworkQueryCriteria;
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
* @website https://el-admin.vip
* @author ruo-yin
* @date 2022-08-09
**/
@RestController
@RequiredArgsConstructor
@Api(tags = "bmc管理")
@RequestMapping("/api/pbNodePhysicNetwork")
public class PbNodePhysicNetworkController {

    private final PbNodePhysicNetworkService pbNodePhysicNetworkService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('pbNodePhysicNetwork:list')")
    public void download(HttpServletResponse response, PbNodePhysicNetworkQueryCriteria criteria) throws IOException {
        pbNodePhysicNetworkService.download(pbNodePhysicNetworkService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询bmc")
    @ApiOperation("查询bmc")
    @PreAuthorize("@el.check('pbNodePhysicNetwork:list')")
    public ResponseEntity<Object> query(PbNodePhysicNetworkQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(pbNodePhysicNetworkService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增bmc")
    @ApiOperation("新增bmc")
    @PreAuthorize("@el.check('pbNodePhysicNetwork:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody PbNodePhysicNetwork resources){
        return new ResponseEntity<>(pbNodePhysicNetworkService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改bmc")
    @ApiOperation("修改bmc")
    @PreAuthorize("@el.check('pbNodePhysicNetwork:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody PbNodePhysicNetwork resources){
        pbNodePhysicNetworkService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除bmc")
    @ApiOperation("删除bmc")
    @PreAuthorize("@el.check('pbNodePhysicNetwork:del')")
    @DeleteMapping
    public ResponseEntity<Object> delete(@RequestBody Long[] ids) {
        pbNodePhysicNetworkService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}