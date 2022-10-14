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
package org.parallel.web.modules.pm.node;

import org.parallel.web.annotation.Log;
import org.parallel.jpa.domain.PmPhysicNetwork;
import org.parallel.jpa.service.PmPhysicNetworkService;
import org.parallel.jpa.service.dto.PmPhysicNetworkQueryCriteria;
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
* @author yuyifade
* @date 2021-06-18
**/
@RestController
@RequiredArgsConstructor
@Api(tags = "监控：物理网卡信息管理")
@RequestMapping("/api/pmNodePhysicNetwork")
public class PmPhysicNetworkController {

    private final PmPhysicNetworkService pmPhysicNetworkService;

    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('pmNode:list')")
    public void download(HttpServletResponse response, PmPhysicNetworkQueryCriteria criteria) throws IOException {
        pmPhysicNetworkService.download(pmPhysicNetworkService.queryAll(criteria), response);
    }

    @GetMapping
    @ApiOperation("查询物理网卡信息")
    @PreAuthorize("@el.check('pmNode:list')")
    public ResponseEntity<Object> query(PmPhysicNetworkQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(pmPhysicNetworkService.queryAll(criteria,pageable),HttpStatus.OK);
    }

//    @PostMapping
//    @Log("新增物理网卡信息")
//    @ApiOperation("新增物理网卡信息")
//    @PreAuthorize("@el.check('pmNodePhysicNetwork:add')")
//    public ResponseEntity<Object> create(@Validated @RequestBody PmPhysicNetwork resources){
//        return new ResponseEntity<>(pmPhysicNetworkService.create(resources),HttpStatus.CREATED);
//    }
//
//    @PutMapping
//    @Log("修改物理网卡信息")
//    @ApiOperation("修改物理网卡信息")
//    @PreAuthorize("@el.check('pmNodePhysicNetwork:edit')")
//    public ResponseEntity<Object> update(@Validated @RequestBody PmPhysicNetwork resources){
//        pmPhysicNetworkService.update(resources);
//        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//    }
//
//    @Log("删除物理网卡信息")
//    @ApiOperation("删除物理网卡信息")
//    @PreAuthorize("@el.check('pmNodePhysicNetwork:del')")
//    @DeleteMapping
//    public ResponseEntity<Object> delete(@RequestBody Long[] ids) {
//        pmPhysicNetworkService.deleteAll(ids);
//        return new ResponseEntity<>(HttpStatus.OK);
//    }
}