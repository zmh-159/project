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
import org.parallel.jpa.domain.PmNodeCpu;
import org.parallel.jpa.service.PmNodeCpuService;
import org.parallel.jpa.service.dto.PmNodeCpuQueryCriteria;
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
@Api(tags = "监控：CPU信息管理")
@RequestMapping("/api/pmNodeCpu")
public class PmNodeCpuController {

    private final PmNodeCpuService pmNodeCpuService;

    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('pmNode:list')")
    public void download(HttpServletResponse response, PmNodeCpuQueryCriteria criteria) throws IOException {
        pmNodeCpuService.download(pmNodeCpuService.queryAll(criteria), response);
    }

    @GetMapping
    @ApiOperation("查询CPU信息")
    @PreAuthorize("@el.check('pmNode:list')")
    public ResponseEntity<Object> query(PmNodeCpuQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(pmNodeCpuService.queryAll(criteria,pageable),HttpStatus.OK);
    }

//    @PostMapping
//    @Log("新增CPU信息")
//    @ApiOperation("新增CPU信息")
//    @PreAuthorize("@el.check('pmNodeCpu:add')")
//    public ResponseEntity<Object> create(@Validated @RequestBody PmNodeCpu resources){
//        return new ResponseEntity<>(pmNodeCpuService.create(resources),HttpStatus.CREATED);
//    }

//    @PutMapping
//    @Log("修改CPU信息")
//    @ApiOperation("修改CPU信息")
//    @PreAuthorize("@el.check('pmNodeCpu:edit')")
//    public ResponseEntity<Object> update(@Validated @RequestBody PmNodeCpu resources){
//        pmNodeCpuService.update(resources);
//        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//    }

//    @Log("删除CPU信息")
//    @ApiOperation("删除CPU信息")
//    @PreAuthorize("@el.check('pmNodeCpu:del')")
//    @DeleteMapping
//    public ResponseEntity<Object> delete(@RequestBody Long[] ids) {
//        pmNodeCpuService.deleteAll(ids);
//        return new ResponseEntity<>(HttpStatus.OK);
//    }
}