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

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.parallel.jpa.service.PmAlarmService;
import org.parallel.jpa.service.dto.PmNodeDetailDto;
import org.parallel.web.annotation.Log;
import org.parallel.jpa.domain.PmNode;
import org.parallel.jpa.service.PmNodeService;
import org.parallel.jpa.service.dto.PmNodeQueryCriteria;
import org.parallel.common.utils.RestTemplateClient;
import org.springframework.data.domain.Pageable;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.*;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpServletResponse;

/**
 * @author yuyifade
 * @website https://el-admin.vip
 * @date 2021-06-18
 **/

@RestController
@RequiredArgsConstructor
@Api(tags = "监控：节点信息管理")
@RequestMapping("/api/pmNode")
public class PmNodeController {

    private final PmNodeService pmNodeService;
    private final PmAlarmService pmAlarmService;
    private final RestTemplateClient rtc;
    //todo 通过config设置url
    private static final String url = "http://127.0.0.1:9001/api/realInfo";

    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('pmNode:list')")
    public void download(HttpServletResponse response, PmNodeQueryCriteria criteria) throws IOException {
        pmNodeService.download(pmNodeService.queryAll(criteria), response);
    }

    @GetMapping
    @ApiOperation("查询节点信息")
    @PreAuthorize("@el.check('pmNode:list')")
    public ResponseEntity<Object> query(PmNodeQueryCriteria criteria, Pageable pageable) {
//        if (criteria.getAlive() != null && criteria.getAlive() == 1) {
        JSONObject aliveNode = rtc.send(url);
        StringBuilder ids = new StringBuilder();
        if (aliveNode.size() > 0 && aliveNode.getJSONObject("node_data").size() > 0) {
            Set<String> aliveNodeIds = aliveNode.getJSONObject("node_data").keySet();
            ids.append('(');
            for (String id : aliveNodeIds) {
                ids.append(id);
                ids.append(',');
            }
            ids.setCharAt(ids.length() - 1, ')');
        }

        int num = pageable.getPageSize();
        int start = pageable.getPageNumber() * num;
        JSONObject res = pmNodeService.querySortByAlive(ids.toString(), start, num);
        return new ResponseEntity<>(res, HttpStatus.OK);
//        } else {
//            return new ResponseEntity<>(pmNodeService.queryAll(criteria, pageable), HttpStatus.OK);
//        }
    }

    @ApiOperation("查询节点总数量")
    @GetMapping(value = "nodeSum")
    @PreAuthorize("@el.check('pmNode:list')")
    public ResponseEntity<Object> nodeSum() {
        return new ResponseEntity<>(pmNodeService.queryNodeCount(), HttpStatus.OK);
    }

    @ApiOperation("查询节点详细信息")
    @GetMapping(value = "/detail")
    @PreAuthorize("@el.check('pmNode:list')")
    public ResponseEntity<Object> detail(PmNodeQueryCriteria criteria, Pageable pageable) {
        return new ResponseEntity<>(pmNodeService.queryDetail(criteria), HttpStatus.OK);
    }

    @GetMapping(value = "statusInfo")
    @ApiOperation("查询节点状态信息")
    @PreAuthorize("@el.check('pmNode:list')")
    public ResponseEntity<Object> statusInfo() {
        /** 获取总节点 **/
        List<PmNodeDetailDto> lpd = pmNodeService.queryDetail(new PmNodeQueryCriteria());
        Set<Long> nodeIds = new HashSet<>();
        for (PmNodeDetailDto p : lpd) {
            nodeIds.add(p.getNodeId());
        }

        /** 获取存活的节点 **/
        JSONObject aliveNode = rtc.send(url);
        Set<String> aliveNodeIdsStr = aliveNode.keySet();
        Set<Long> aliveNodeIds = new HashSet<>();
        for (String idStr : aliveNodeIdsStr) {
            if (StringUtils.isNumeric(idStr)) {
                aliveNodeIds.add(Long.parseLong(idStr));
            }
        }

        /** 获取离线的节点 **/
        nodeIds.removeAll(aliveNodeIds);
        JSONObject res = new JSONObject();
        int i = 0;
        for (long p : nodeIds) {
            res.put(i + "", p);
            i++;
        }

        return new ResponseEntity<>(res, HttpStatus.OK);
    }


    @GetMapping(value = "realTimeInfo")
    @ApiOperation("查询节点实时信息")
    @PreAuthorize("@el.check('pmNode:list')")
    public ResponseEntity<Object> realTimeInfo() {
        return new ResponseEntity<>(pmNodeService.getRealTimeInfo(), HttpStatus.OK);
    }

//    @PostMapping
//    @Log("新增节点信息")
//    @ApiOperation("新增节点信息")
//    @PreAuthorize("@el.check('pmNode:add')")
//    public ResponseEntity<Object> create(@Validated @RequestBody PmNode resources) {
//        return new ResponseEntity<>(pmNodeService.create(resources), HttpStatus.CREATED);
//    }

    @PutMapping
    @Log("修改节点信息")
    @ApiOperation("修改节点信息")
    @PreAuthorize("@el.check('pmNode:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody PmNode resources) {
        pmNodeService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


//    @Log("删除节点信息")
//    @ApiOperation("删除节点信息")
//    @PreAuthorize("@el.check('pmNode:del')")
//    @DeleteMapping
//    public ResponseEntity<Object> delete(@RequestBody Long[] ids) {
//        pmNodeService.deleteAll(ids);
//        return new ResponseEntity<>(HttpStatus.OK);
//    }
}
