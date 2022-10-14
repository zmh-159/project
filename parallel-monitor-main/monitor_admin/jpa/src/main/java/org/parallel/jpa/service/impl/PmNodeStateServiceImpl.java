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
package org.parallel.jpa.service.impl;

import com.alibaba.fastjson.JSONObject;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.parallel.jpa.domain.PmNodeState;
import org.parallel.jpa.repository.PmNodeStateRepository;
import org.parallel.jpa.service.PmNodeStateService;
import org.parallel.jpa.service.dto.PmNodeAvgQueryCriteria;
import org.parallel.jpa.service.dto.PmNodeStateDto;
import org.parallel.jpa.service.dto.PmNodeStateQueryCriteria;
import org.parallel.jpa.service.mapstruct.PmNodeStateMapper;
import org.parallel.web.utils.FileUtil;
import org.parallel.web.utils.PageUtil;
import org.parallel.web.utils.QueryHelp;
import org.parallel.web.utils.ValidationUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Query;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * @author yuyifade
 * @website https://el-admin.vip
 * @description 服务实现
 * @date 2021-06-18
 **/
@Service
@RequiredArgsConstructor
public class PmNodeStateServiceImpl implements PmNodeStateService {

    private final PmNodeStateRepository pmNodeStateRepository;
    private final PmNodeStateMapper pmNodeStateMapper;

    @Override
    public Map<String, Object> queryAll(PmNodeStateQueryCriteria criteria, Pageable pageable) {
        Page<PmNodeState> page = pmNodeStateRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
        return PageUtil.toPage(page.map(pmNodeStateMapper::toDto));
    }

    @Override
    public List<PmNodeStateDto> queryAll(PmNodeStateQueryCriteria criteria) {
        return pmNodeStateMapper.toDto(pmNodeStateRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder)));
    }

    @Override
    @Transactional
    public PmNodeStateDto findById(Long nodeStateId) {
        PmNodeState pmNodeState = pmNodeStateRepository.findById(nodeStateId).orElseGet(PmNodeState::new);
        ValidationUtil.isNull(pmNodeState.getNodeStateId(), "PmNodeState", "nodeStateId", nodeStateId);
        return pmNodeStateMapper.toDto(pmNodeState);
    }

    @Override
    public Map<String, Object> queryDailyTotalUsage(PmNodeAvgQueryCriteria criteria) {
        String start = criteria.getStartTime();
        String end = criteria.getEndTime();

        List<Object[]> li = pmNodeStateRepository.getAvg(start, end);

        JSONObject res = new JSONObject();
        for (Object[] s : li) {
            JSONObject jo = new JSONObject();
            String str = StringUtils.strip(Arrays.toString(s), "[]");
            jo.put("cpu_idle", str.split(",")[1]);
            jo.put("memory_rate", str.split(",")[2]);
            jo.put("disk_rate", str.split(",")[3]);
            jo.put("gpu_utilization", str.split(",")[4]);
            res.put(str.split(",")[0], jo);
        }

        return res;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PmNodeStateDto create(PmNodeState resources) {
        return pmNodeStateMapper.toDto(pmNodeStateRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(PmNodeState resources) {
        PmNodeState pmNodeState = pmNodeStateRepository.findById(resources.getNodeStateId()).orElseGet(PmNodeState::new);
        ValidationUtil.isNull(pmNodeState.getNodeStateId(), "PmNodeState", "id", resources.getNodeStateId());
        pmNodeState.copy(resources);
        pmNodeStateRepository.save(pmNodeState);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long nodeStateId : ids) {
            pmNodeStateRepository.deleteById(nodeStateId);
        }
    }

    @Override
    public void deleteByNodeId(Long nodeId) {
        pmNodeStateRepository.deleteByNodeId(nodeId);
    }


    @Override
    public void download(List<PmNodeStateDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (PmNodeStateDto pmNodeState : all) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("外键：表pm_node", pmNodeState.getNodeId());
            map.put("进程数", pmNodeState.getProcessNums());
            map.put("cpu空闲率", pmNodeState.getCpuIdle());
            map.put("cpu系统占用", pmNodeState.getCpuSystem());
            map.put("cpu用户占用", pmNodeState.getCpuUser());
            map.put("内存", pmNodeState.getMemoryTotal());
            map.put("内存使用", pmNodeState.getMemoryUsed());
            map.put("内存使用率", pmNodeState.getMemoryRate());
            map.put("交换区", pmNodeState.getSwapTotal());
            map.put("交换区使用", pmNodeState.getSwapUsed());
            map.put("交换区使用率", pmNodeState.getSwapRate());
            map.put("磁盘", pmNodeState.getDiskTotal());
            map.put("磁盘使用", pmNodeState.getDiskUsed());
            map.put("磁盘使用率", pmNodeState.getDiskRate());
            map.put("GPU使用率", pmNodeState.getGpuUtilization());
            map.put("内存使用率", pmNodeState.getMemoryUtilization());
            map.put("采集时间", pmNodeState.getCreateTime());
            map.put("修改时间", pmNodeState.getUpdateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}