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
import org.parallel.jpa.domain.PmNodeCpuState;
import org.parallel.jpa.repository.PmNodeCpuStateRepository;
import org.parallel.jpa.service.PmNodeCpuStateService;
import org.parallel.jpa.service.dto.PmNodeCpuAvgQueryCriteria;
import org.parallel.jpa.service.dto.PmNodeCpuStateDto;
import org.parallel.jpa.service.dto.PmNodeCpuStateQueryCriteria;
import org.parallel.jpa.service.mapstruct.PmNodeCpuStateMapper;
import org.parallel.web.utils.FileUtil;
import org.parallel.web.utils.PageUtil;
import org.parallel.web.utils.QueryHelp;
import org.parallel.web.utils.ValidationUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.*;

/**
 * @author yuyifade
 * @website https://el-admin.vip
 * @description 服务实现
 * @date 2021-06-18
 **/
@Service
@RequiredArgsConstructor
public class PmNodeCpuStateServiceImpl implements PmNodeCpuStateService {

    private final PmNodeCpuStateRepository pmNodeCpuStateRepository;
    private final PmNodeCpuStateMapper pmNodeCpuStateMapper;

    @Override
    public Map<String, Object> queryAll(PmNodeCpuStateQueryCriteria criteria, Pageable pageable) {
        Page<PmNodeCpuState> page = pmNodeCpuStateRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
        return PageUtil.toPage(page.map(pmNodeCpuStateMapper::toDto));
    }

    @Override
    public List<PmNodeCpuStateDto> queryAll(PmNodeCpuStateQueryCriteria criteria) {
        return pmNodeCpuStateMapper.toDto(pmNodeCpuStateRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder)));
    }

    @Override
    @Transactional
    public PmNodeCpuStateDto findById(Long cpuStateId) {
        PmNodeCpuState pmNodeCpuState = pmNodeCpuStateRepository.findById(cpuStateId).orElseGet(PmNodeCpuState::new);
        ValidationUtil.isNull(pmNodeCpuState.getCpuStateId(), "PmNodeCpuState", "cpuStateId", cpuStateId);
        return pmNodeCpuStateMapper.toDto(pmNodeCpuState);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PmNodeCpuStateDto create(PmNodeCpuState resources) {
        return pmNodeCpuStateMapper.toDto(pmNodeCpuStateRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(PmNodeCpuState resources) {
        PmNodeCpuState pmNodeCpuState = pmNodeCpuStateRepository.findById(resources.getCpuStateId()).orElseGet(PmNodeCpuState::new);
        ValidationUtil.isNull(pmNodeCpuState.getCpuStateId(), "PmNodeCpuState", "id", resources.getCpuStateId());
        pmNodeCpuState.copy(resources);
        pmNodeCpuStateRepository.save(pmNodeCpuState);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long cpuStateId : ids) {
            pmNodeCpuStateRepository.deleteById(cpuStateId);
        }
    }

    @Override
    public Map<String, Object> avg(PmNodeCpuAvgQueryCriteria criteria) {
        long cpuId = criteria.getCpuId();
        int size = criteria.getSize();
        String s = criteria.getStartTime();
        String e = criteria.getEndTime();
        String sort = criteria.getSortType();
        Map<String, Object> res;

        if (sort.equals("desc")) {
            res = new HashMap<>(pmNodeCpuStateRepository.getAvgDes(cpuId, size, s, e));
        } else {
            res = new HashMap<>(pmNodeCpuStateRepository.getAvgAsc(cpuId, size, s, e));
        }
        return res;
    }

    /**
     * 查询符合条件的条数
     *
     * @param criteria
     */
    @Override
    public Long queryNums(PmNodeCpuAvgQueryCriteria criteria) {
        long cpuId = criteria.getCpuId();
        String s = criteria.getStartTime();
        String e = criteria.getEndTime();
        Map<String, Object> res = pmNodeCpuStateRepository.getNums(cpuId, s, e);

        for (String ss : res.keySet()) {
            BigInteger bi = (BigInteger) res.get(ss);
            return bi.longValue();
        }
        return 0L;
    }

    @Override
    public void download(List<PmNodeCpuStateDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (PmNodeCpuStateDto pmNodeCpuState : all) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("外键：表pm_node_cpu", pmNodeCpuState.getCpuId());
            map.put("外键：表pm_node_state", pmNodeCpuState.getNodeStateId());
            map.put("温度", pmNodeCpuState.getTemperature());
            map.put("采集时间", pmNodeCpuState.getCreateTime());
            map.put("修改时间", pmNodeCpuState.getUpdateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}