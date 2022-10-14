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
import org.parallel.jpa.domain.PmNetwork;
import org.parallel.jpa.service.PmNetworkService;
import org.parallel.jpa.service.dto.PmNetworkQueryCriteria;
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
@Api(tags = "监控：网卡信息管理")
@RequestMapping("/api/pmNodeNetwork")
public class PmNetworkController {

    private final PmNetworkService pmNetworkService;

    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('pmNode:list')")
    public void download(HttpServletResponse response, PmNetworkQueryCriteria criteria) throws IOException {
        pmNetworkService.download(pmNetworkService.queryAll(criteria), response);
    }

    @GetMapping
    @ApiOperation("查询网卡信息")
    @PreAuthorize("@el.check('pmNode:list')")
    public ResponseEntity<Object> query(PmNetworkQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(pmNetworkService.queryAll(criteria,pageable),HttpStatus.OK);
    }

//    @PostMapping
//    @Log("新增网卡信息")
//    @ApiOperation("新增网卡信息")
//    @PreAuthorize("@el.check('pmNodeNetwork:add')")
//    public ResponseEntity<Object> create(@Validated @RequestBody PmNetwork resources){
//        return new ResponseEntity<>(pmNetworkService.create(resources),HttpStatus.CREATED);
//    }

//    @PutMapping
//    @Log("修改网卡信息")
//    @ApiOperation("修改网卡信息")
//    @PreAuthorize("@el.check('pmNodeNetwork:edit')")
//    public ResponseEntity<Object> update(@Validated @RequestBody PmNetwork resources){
//        pmNetworkService.update(resources);
//        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//    }

//    @Log("删除网卡信息")
//    @ApiOperation("删除网卡信息")
//    @PreAuthorize("@el.check('pmNodeNetwork:del')")
//    @DeleteMapping
//    public ResponseEntity<Object> delete(@RequestBody Long[] ids) {
//        pmNetworkService.deleteAll(ids);
//        return new ResponseEntity<>(HttpStatus.OK);
//    }
}