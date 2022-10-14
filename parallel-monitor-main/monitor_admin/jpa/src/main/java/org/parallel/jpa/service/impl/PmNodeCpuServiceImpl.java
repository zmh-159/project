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

import lombok.RequiredArgsConstructor;
import org.parallel.jpa.domain.PmNodeCpu;
import org.parallel.jpa.repository.PmNodeCpuRepository;
import org.parallel.jpa.service.PmNodeCpuService;
import org.parallel.jpa.service.dto.PmNodeCpuDto;
import org.parallel.jpa.service.dto.PmNodeCpuQueryCriteria;
import org.parallel.jpa.service.mapstruct.PmNodeCpuMapper;
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
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
* @website https://el-admin.vip
* @description 服务实现
* @author yuyifade
* @date 2021-06-18
**/
@Service
@RequiredArgsConstructor
public class PmNodeCpuServiceImpl implements PmNodeCpuService {

    private final PmNodeCpuRepository pmNodeCpuRepository;
    private final PmNodeCpuMapper pmNodeCpuMapper;

    @Override
    public Map<String,Object> queryAll(PmNodeCpuQueryCriteria criteria, Pageable pageable){
        Page<PmNodeCpu> page = pmNodeCpuRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(pmNodeCpuMapper::toDto));
    }

    @Override
    public List<PmNodeCpuDto> queryAll(PmNodeCpuQueryCriteria criteria){
        return pmNodeCpuMapper.toDto(pmNodeCpuRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public PmNodeCpuDto findById(Long cpuId) {
        PmNodeCpu pmNodeCpu = pmNodeCpuRepository.findById(cpuId).orElseGet(PmNodeCpu::new);
        ValidationUtil.isNull(pmNodeCpu.getCpuId(),"PmNodeCpu","cpuId",cpuId);
        return pmNodeCpuMapper.toDto(pmNodeCpu);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PmNodeCpuDto create(PmNodeCpu resources) {
        return pmNodeCpuMapper.toDto(pmNodeCpuRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(PmNodeCpu resources) {
        PmNodeCpu pmNodeCpu = pmNodeCpuRepository.findById(resources.getCpuId()).orElseGet(PmNodeCpu::new);
        ValidationUtil.isNull( pmNodeCpu.getCpuId(),"PmNodeCpu","id",resources.getCpuId());
        pmNodeCpu.copy(resources);
        pmNodeCpuRepository.save(pmNodeCpu);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long cpuId : ids) {
            pmNodeCpuRepository.deleteById(cpuId);
        }
    }

    @Override
    public void download(List<PmNodeCpuDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (PmNodeCpuDto pmNodeCpu : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("外键：表pm_node", pmNodeCpu.getNodeId());
            map.put("CPU型号", pmNodeCpu.getCpuName());
            map.put("核心数", pmNodeCpu.getCores());
            map.put("线程数", pmNodeCpu.getThreads());
            map.put("架构", pmNodeCpu.getArch());
            map.put("一级指令缓存", pmNodeCpu.getL1ICache());
            map.put("一级数据缓存", pmNodeCpu.getL1DCache());
            map.put("二级缓存", pmNodeCpu.getL2Cache());
            map.put("三级缓存", pmNodeCpu.getL3Cache());
            map.put("主频", pmNodeCpu.getMainFrequency());
            map.put("睿频", pmNodeCpu.getBoostFrequency());
            map.put("创建时间", pmNodeCpu.getCreateTime());
            map.put("修改时间", pmNodeCpu.getUpdateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}