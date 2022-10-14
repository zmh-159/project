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

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.parallel.web.annotation.Log;
import org.parallel.web.modules.pm.config.domain.PmConfig;
import org.parallel.web.modules.pm.config.service.PmConfigService;
import org.parallel.web.modules.pm.config.service.dto.PmConfigQueryCriteria;
import org.parallel.web.utils.SecurityUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
* @website https://el-admin.vip
* @author yuyifade
* @date 2021-06-18
**/
@RestController
@RequiredArgsConstructor
@Api(tags = "监控：配置管理")
@RequestMapping("/api/pmConfig")
public class PmConfigController {

    private final PmConfigService pmConfigService;

//    @ApiOperation("导出数据")
//    @GetMapping(value = "/download")
//    @PreAuthorize("@el.check('pmConfig:list')")
//    public void download(HttpServletResponse response, PmConfigQueryCriteria criteria) throws IOException {
//        pmConfigService.download(pmConfigService.queryAll(criteria), response);
//    }

    @GetMapping
    @ApiOperation("查询配置")
    @PreAuthorize("@el.check('pmConfig:list')")
    public ResponseEntity<Object> query(PmConfigQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(pmConfigService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增配置")
    @ApiOperation("新增配置")
    @PreAuthorize("@el.check('pmConfig:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody PmConfig resources){
        pmConfigService.create(resources);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改配置")
    @ApiOperation("修改配置")
    @PreAuthorize("@el.check('pmConfig:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody PmConfig resources){
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除配置")
    @ApiOperation("删除配置")
    @PreAuthorize("@el.check('pmConfig:del')")
    @DeleteMapping
    public ResponseEntity<Object> delete(@RequestBody Long[] ids) {
        pmConfigService.deleteAll(ids,SecurityUtils.getCurrentUserId());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}