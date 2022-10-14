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
import org.parallel.jpa.domain.PmNodeGpu;
import org.parallel.jpa.service.PmNodeGpuService;
import org.parallel.jpa.service.dto.PmNodeGpuQueryCriteria;
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
* @date 2021-08-18
**/
@RestController
@RequiredArgsConstructor
@Api(tags = "监控：GPU信息管理")
@RequestMapping("/api/pmNodeGpu")
public class PmNodeGpuController {

    private final PmNodeGpuService pmNodeGpuService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('pmNode:list')")
    public void download(HttpServletResponse response, PmNodeGpuQueryCriteria criteria) throws IOException {
        pmNodeGpuService.download(pmNodeGpuService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询GPU")
    @ApiOperation("查询GPU")
    @PreAuthorize("@el.check('pmNode:list')")
    public ResponseEntity<Object> query(PmNodeGpuQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(pmNodeGpuService.queryAll(criteria,pageable),HttpStatus.OK);
    }

//    @PostMapping
//    @Log("新增test")
//    @ApiOperation("新增test")
//    @PreAuthorize("@el.check('pmNodeGpu:add')")
//    public ResponseEntity<Object> create(@Validated @RequestBody PmNodeGpu resources){
//        return new ResponseEntity<>(pmNodeGpuService.create(resources),HttpStatus.CREATED);
//    }

//    @PutMapping
//    @Log("修改test")
//    @ApiOperation("修改test")
//    @PreAuthorize("@el.check('pmNodeGpu:edit')")
//    public ResponseEntity<Object> update(@Validated @RequestBody PmNodeGpu resources){
//        pmNodeGpuService.update(resources);
//        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//    }

//    @Log("删除test")
//    @ApiOperation("删除test")
//    @PreAuthorize("@el.check('pmNodeGpu:del')")
//    @DeleteMapping
//    public ResponseEntity<Object> delete(@RequestBody Long[] ids) {
//        pmNodeGpuService.deleteAll(ids);
//        return new ResponseEntity<>(HttpStatus.OK);
//    }
}