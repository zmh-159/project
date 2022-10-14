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
package org.parallel.web.modules.bmc.jpa.rest;

import org.parallel.web.annotation.Log;
import org.parallel.web.modules.bmc.jpa.domain.PbNodeBios;
import org.parallel.web.modules.bmc.jpa.service.PbNodeBiosService;
import org.parallel.web.modules.bmc.jpa.service.dto.PbNodeBiosQueryCriteria;
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
* @author ruo-yin
* @date 2022-08-09
**/
@RestController
@RequiredArgsConstructor
@Api(tags = "bmc管理")
@RequestMapping("/api/pbNodeBios")
public class PbNodeBiosController {

    private final PbNodeBiosService pbNodeBiosService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('pbNodeBios:list')")
    public void download(HttpServletResponse response, PbNodeBiosQueryCriteria criteria) throws IOException {
        pbNodeBiosService.download(pbNodeBiosService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询bmc")
    @ApiOperation("查询bmc")
    @PreAuthorize("@el.check('pbNodeBios:list')")
    public ResponseEntity<Object> query(PbNodeBiosQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(pbNodeBiosService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增bmc")
    @ApiOperation("新增bmc")
    @PreAuthorize("@el.check('pbNodeBios:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody PbNodeBios resources){
        return new ResponseEntity<>(pbNodeBiosService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改bmc")
    @ApiOperation("修改bmc")
    @PreAuthorize("@el.check('pbNodeBios:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody PbNodeBios resources){
        pbNodeBiosService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除bmc")
    @ApiOperation("删除bmc")
    @PreAuthorize("@el.check('pbNodeBios:del')")
    @DeleteMapping
    public ResponseEntity<Object> delete(@RequestBody Long[] ids) {
        pbNodeBiosService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}