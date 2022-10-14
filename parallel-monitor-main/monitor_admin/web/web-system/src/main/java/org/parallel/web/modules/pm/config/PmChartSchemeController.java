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
package org.parallel.web.modules.pm.config;

import org.parallel.web.annotation.Log;
import org.parallel.web.modules.pm.config.domain.PmChartScheme;
import org.parallel.web.modules.pm.config.service.PmChartSchemeService;
import org.parallel.web.modules.pm.config.service.dto.PmChartSchemeQueryCriteria;
import org.parallel.web.utils.SecurityUtils;
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
 * @date 2021-07-07
 **/
@RestController
@RequiredArgsConstructor
@Api(tags = "监控：方案管理")
@RequestMapping("/api/pmChartScheme")
public class PmChartSchemeController {

    private final PmChartSchemeService pmChartSchemeService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('pmChartScheme:list')")
    public void download(HttpServletResponse response, PmChartSchemeQueryCriteria criteria) throws IOException {
        pmChartSchemeService.download(pmChartSchemeService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询方案")
    @ApiOperation("查询方案")
    @PreAuthorize("@el.check('pmChartScheme:list')")
    public ResponseEntity<Object> query(PmChartSchemeQueryCriteria criteria, Pageable pageable) {
        return new ResponseEntity<>(pmChartSchemeService.queryAll(criteria, pageable), HttpStatus.OK);
    }

    @PostMapping
    @Log("新增方案")
    @ApiOperation("新增方案")
    @PreAuthorize("@el.check('pmChartScheme:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody PmChartScheme resources) {
        resources.setUserId(SecurityUtils.getCurrentUserId());
        return new ResponseEntity<>(pmChartSchemeService.create(resources), HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改方案")
    @ApiOperation("修改方案")
    @PreAuthorize("@el.check('pmChartScheme:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody PmChartScheme resources) {
        resources.setUserId(SecurityUtils.getCurrentUserId());
        pmChartSchemeService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除方案")
    @ApiOperation("删除方案")
    @PreAuthorize("@el.check('pmChartScheme:del')")
    @DeleteMapping
    public ResponseEntity<Object> delete(@RequestBody Long[] ids) {
        pmChartSchemeService.deleteAll(ids,SecurityUtils.getCurrentUserId());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}