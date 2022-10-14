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
import org.parallel.jpa.domain.PmDiskState;
import org.parallel.jpa.service.PmDiskStateService;
import org.parallel.jpa.service.dto.PmDiskStateQueryCriteria;
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
@Api(tags = "监控：磁盘状态信息管理")
@RequestMapping("/api/pmNodeDiskState")
public class PmDiskStateController {

    private final PmDiskStateService pmDiskStateService;

    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('pmNode:list')")
    public void download(HttpServletResponse response, PmDiskStateQueryCriteria criteria) throws IOException {
        pmDiskStateService.download(pmDiskStateService.queryAll(criteria), response);
    }

    @GetMapping
    @ApiOperation("查询磁盘状态信息")
    @PreAuthorize("@el.check('pmNode:list')")
    public ResponseEntity<Object> query(PmDiskStateQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(pmDiskStateService.queryAll(criteria,pageable),HttpStatus.OK);
    }

//    @PostMapping
//    @Log("新增磁盘状态信息")
//    @ApiOperation("新增磁盘状态信息")
//    @PreAuthorize("@el.check('pmNodeDiskState:add')")
//    public ResponseEntity<Object> create(@Validated @RequestBody PmDiskState resources){
//        return new ResponseEntity<>(pmDiskStateService.create(resources),HttpStatus.CREATED);
//    }

//    @PutMapping
//    @Log("修改磁盘状态信息")
//    @ApiOperation("修改磁盘状态信息")
//    @PreAuthorize("@el.check('pmNodeDiskState:edit')")
//    public ResponseEntity<Object> update(@Validated @RequestBody PmDiskState resources){
//        pmDiskStateService.update(resources);
//        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//    }

//    @Log("删除磁盘状态信息")
//    @ApiOperation("删除磁盘状态信息")
//    @PreAuthorize("@el.check('pmNodeDiskState:del')")
//    @DeleteMapping
//    public ResponseEntity<Object> delete(@RequestBody Long[] ids) {
//        pmDiskStateService.deleteAll(ids);
//        return new ResponseEntity<>(HttpStatus.OK);
//    }
}