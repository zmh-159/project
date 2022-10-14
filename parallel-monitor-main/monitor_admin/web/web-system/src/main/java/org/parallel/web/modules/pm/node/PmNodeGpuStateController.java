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
import org.parallel.jpa.domain.PmNodeGpuState;
import org.parallel.jpa.service.PmNodeGpuStateService;
import org.parallel.jpa.service.dto.PmNodeGpuStateQueryCriteria;
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
@Api(tags = "监控：GPU状态信息管理")
@RequestMapping("/api/pmNodeGpuState")
public class PmNodeGpuStateController {

    private final PmNodeGpuStateService pmNodeGpuStateService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('pmNode:list')")
    public void download(HttpServletResponse response, PmNodeGpuStateQueryCriteria criteria) throws IOException {
        pmNodeGpuStateService.download(pmNodeGpuStateService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询GPU状态信息")
    @ApiOperation("查询GPU状态信息")
    @PreAuthorize("@el.check('pmNode:list')")
    public ResponseEntity<Object> query(PmNodeGpuStateQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(pmNodeGpuStateService.queryAll(criteria,pageable),HttpStatus.OK);
    }

//    @PostMapping
//    @Log("新增test")
//    @ApiOperation("新增test")
//    @PreAuthorize("@el.check('pmNodeGpuState:add')")
//    public ResponseEntity<Object> create(@Validated @RequestBody PmNodeGpuState resources){
//        return new ResponseEntity<>(pmNodeGpuStateService.create(resources),HttpStatus.CREATED);
//    }
//
//    @PutMapping
//    @Log("修改test")
//    @ApiOperation("修改test")
//    @PreAuthorize("@el.check('pmNodeGpuState:edit')")
//    public ResponseEntity<Object> update(@Validated @RequestBody PmNodeGpuState resources){
//        pmNodeGpuStateService.update(resources);
//        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//    }
//
//    @Log("删除test")
//    @ApiOperation("删除test")
//    @PreAuthorize("@el.check('pmNodeGpuState:del')")
//    @DeleteMapping
//    public ResponseEntity<Object> delete(@RequestBody Long[] ids) {
//        pmNodeGpuStateService.deleteAll(ids);
//        return new ResponseEntity<>(HttpStatus.OK);
//    }
}