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

package org.parallel.web.modules.pm.alarm;

import org.parallel.jpa.service.PmAlarmService;
import org.parallel.jpa.service.dto.PmAlarmQueryCriteria;
import org.springframework.data.domain.Pageable;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.*;

import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

/**
 * @author yuyifade
 * @website https://el-admin.vip
 * @date 2021-11-11
 **/
@RestController
@RequiredArgsConstructor
@Api(tags = "监控：报警记录管理")
@RequestMapping("/api/pmAlarm")
public class PmAlarmController {
    private final PmAlarmService pmAlarmService;

//    @Log("导出数据")
//    @ApiOperation("导出数据")
//    @GetMapping(value = "/download")
//    @PreAuthorize("@el.check('pmAlarm:list')")
//    public void download(HttpServletResponse response, PmAlarmQueryCriteria criteria) throws IOException {
//        pmAlarmService.download(pmAlarmService.queryAll(criteria), response);
//    }

    @GetMapping
    @ApiOperation("查询")
    @PreAuthorize("@el.check('pmAlarm:list')")
    public ResponseEntity<Object> query(PmAlarmQueryCriteria criteria, Pageable pageable) {
        return new ResponseEntity<>(pmAlarmService.queryAll(criteria, pageable), HttpStatus.OK);
    }

    @GetMapping("remark")
    @ApiOperation("备注/已读")
    @PreAuthorize("@el.check('pmAlarm:list')")
    public ResponseEntity<Object> read(String remark, @RequestParam("alarm_id") long alalrmId) {
        boolean result = pmAlarmService.update(alalrmId, remark);
        if (result) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>("更新报警信息失败!", HttpStatus.BAD_REQUEST);
        }
    }
//    @PostMapping
//    @Log("新增test")
//    @ApiOperation("新增test")
//    @PreAuthorize("@el.check('pmAlarm:add')")
//    public ResponseEntity<Object> create(@Validated @RequestBody PmAlarm resources){
//        return new ResponseEntity<>(pmAlarmService.create(resources),HttpStatus.CREATED);
//    }

//    @PutMapping
//    @Log("修改test")
//    @ApiOperation("修改test")
//    @PreAuthorize("@el.check('pmAlarm:edit')")
//    public ResponseEntity<Object> update(@Validated @RequestBody PmAlarm resources){
//        pmAlarmService.update(resources);
//        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//    }

//    @Log("删除报警记录")
//    @ApiOperation("删除报警记录")
//    @PreAuthorize("@el.check('pmAlarm:del')")
//    @DeleteMapping
//    public ResponseEntity<Object> delete(@RequestBody Long[] ids) {
//        pmAlarmService.deleteAll(ids);
//        return new ResponseEntity<>(HttpStatus.OK);
//    }
}