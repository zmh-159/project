package org.parallel.web.modules.pm.alarm;
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

import org.parallel.jpa.domain.PmAlarmScheme;
import org.parallel.jpa.service.dto.PmAlarmSchemeQueryCriteria;
import org.parallel.jpa.service.mapstruct.PmAlarmSchemeService;
import org.parallel.web.annotation.Log;
import org.parallel.common.utils.RestTemplateClient;
import org.parallel.web.modules.pm.bean.ServerConfig;
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
 * @date 2021-11-11
 **/
@RestController
@RequiredArgsConstructor
@Api(tags = "监控：报警方案管理")
@RequestMapping("/api/pmAlarmScheme")
public class PmAlarmSchemeController {
    private final RestTemplateClient rtc;
    private final ServerConfig sc;
    private final Option option;
    private final PmAlarmSchemeService pmAlarmSchemeService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('pmAlarmScheme:list')")
    public void download(HttpServletResponse response, PmAlarmSchemeQueryCriteria criteria) throws IOException {
        pmAlarmSchemeService.download(pmAlarmSchemeService.queryAll(criteria), response);
    }

    @GetMapping
//    @Log("查询报警方案")
    @ApiOperation("查询报警方案")
    @PreAuthorize("@el.check('pmAlarmScheme:list')")
    public ResponseEntity<Object> query(PmAlarmSchemeQueryCriteria criteria, Pageable pageable) {
        return new ResponseEntity<>(pmAlarmSchemeService.queryAll(criteria, pageable), HttpStatus.OK);
    }

    @PostMapping
    @Log("新增报警方案")
    @ApiOperation("新增报警方案")
    @PreAuthorize("@el.check('pmAlarmScheme:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody PmAlarmScheme resources) {
        return new ResponseEntity<>(pmAlarmSchemeService.create(resources), HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改报警方案")
    @ApiOperation("修改报警方案")
    @PreAuthorize("@el.check('pmAlarmScheme:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody PmAlarmScheme resources) {
        pmAlarmSchemeService.update(resources);
        String url = sc.alarm() + "?alarm_scheme_id=" + resources.getAlarmSchemeId();
        rtc.send(url);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除报警方案")
    @ApiOperation("删除报警方案")
    @PreAuthorize("@el.check('pmAlarmScheme:del')")
    @DeleteMapping
    public ResponseEntity<Object> delete(@RequestBody Integer[] ids) {
        pmAlarmSchemeService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @ApiOperation("查询报警项")
    @GetMapping(value = "/option")
    @PreAuthorize("@el.check('pmAlarmScheme:list')")
    public ResponseEntity<Object> option() {
        return new ResponseEntity<>(option.getOption(), HttpStatus.OK);
    }
}