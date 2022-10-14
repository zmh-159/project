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

import org.parallel.jpa.service.dto.*;
import org.parallel.web.annotation.Log;
import org.parallel.jpa.domain.PmNodeCpuState;
import org.parallel.jpa.service.PmNodeCpuStateService;
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
 * @author yuyifade
 * @website https://el-admin.vip
 * @date 2021-06-18
 **/
@RestController
@RequiredArgsConstructor
@Api(tags = "监控：CPU状态信息管理")
@RequestMapping("/api/pmNodeCpuState")
public class PmNodeCpuStateController {

    private final PmNodeCpuStateService pmNodeCpuStateService;

    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('pmNode:list')")
    public void download(HttpServletResponse response, PmNodeCpuStateQueryCriteria criteria) throws IOException {
        pmNodeCpuStateService.download(pmNodeCpuStateService.queryAll(criteria), response);
    }

    /**
     * @description 根据查询条件返回cpu 温度，系统占用，用户占用平均值
     * @author yuyifade
     * @date 2021/7/6 下午4:17
     */
    @ApiOperation("查询CPU平均负载")
    @GetMapping(value = "/loadAvg")
    @PreAuthorize("@el.check('pmNode:list')")
    public ResponseEntity<Object> avg(PmNodeCpuAvgQueryCriteria criteria) {
        return new ResponseEntity<>(pmNodeCpuStateService.avg(criteria), HttpStatus.OK);
    }

    /***
     * @description 获取符合条件的数量
     * @author yuyifade
     * @date 2021/7/16 下午2:38
     * @param criteria
     */
    @ApiOperation("查询符合条件的数量")
    @GetMapping(value = "/nums")
    @PreAuthorize("@el.check('pmNode:list')")
    public ResponseEntity<Object> queryNums(PmNodeCpuAvgQueryCriteria criteria) {
        return new ResponseEntity<>(pmNodeCpuStateService.queryNums(criteria), HttpStatus.OK);
    }

    @GetMapping
    @ApiOperation("查询CPU状态信息")
    @PreAuthorize("@el.check('pmNode:list')")
    public ResponseEntity<Object> query(PmNodeCpuStateQueryCriteria criteria, Pageable pageable) {
        return new ResponseEntity<>(pmNodeCpuStateService.queryAll(criteria, pageable), HttpStatus.OK);
    }

//    @PostMapping
//    @Log("新增CPU状态信息")
//    @ApiOperation("新增CPU状态信息")
//    @PreAuthorize("@el.check('pmNodeCpuState:add')")
//    public ResponseEntity<Object> create(@Validated @RequestBody PmNodeCpuState resources) {
//        return new ResponseEntity<>(pmNodeCpuStateService.create(resources), HttpStatus.CREATED);
//    }

//    @PutMapping
//    @Log("修改CPU状态信息")
//    @ApiOperation("修改CPU状态信息")
//    @PreAuthorize("@el.check('pmNodeCpuState:edit')")
//    public ResponseEntity<Object> update(@Validated @RequestBody PmNodeCpuState resources) {
//        pmNodeCpuStateService.update(resources);
//        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//    }

//    @Log("删除CPU状态信息")
//    @ApiOperation("删除CPU状态信息")
//    @PreAuthorize("@el.check('pmNodeCpuState:del')")
//    @DeleteMapping
//    public ResponseEntity<Object> delete(@RequestBody Long[] ids) {
//        pmNodeCpuStateService.deleteAll(ids);
//        return new ResponseEntity<>(HttpStatus.OK);
//    }
}