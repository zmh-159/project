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
package org.parallel.web.modules.bmc.jpa.service.impl;

import com.alibaba.fastjson.JSONObject;
import org.parallel.web.modules.bmc.jpa.domain.PbNodeCpu;
import org.parallel.web.modules.bmc.kp.CpuInfo;
import org.parallel.web.utils.ValidationUtil;
import org.parallel.web.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import org.parallel.web.modules.bmc.jpa.repository.PbNodeCpuRepository;
import org.parallel.web.modules.bmc.jpa.service.PbNodeCpuService;
import org.parallel.web.modules.bmc.jpa.service.dto.PbNodeCpuDto;
import org.parallel.web.modules.bmc.jpa.service.dto.PbNodeCpuQueryCriteria;
import org.parallel.web.modules.bmc.jpa.service.mapstruct.PbNodeCpuMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.parallel.web.utils.PageUtil;
import org.parallel.web.utils.QueryHelp;

import java.util.List;
import java.util.Map;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * @author ruo-yin
 * @website https://el-admin.vip
 * @description 服务实现
 * @date 2022-08-09
 **/
@Service
@RequiredArgsConstructor
public class PbNodeCpuServiceImpl implements PbNodeCpuService {

    private final PbNodeCpuRepository pbNodeCpuRepository;
    private final PbNodeCpuMapper pbNodeCpuMapper;
    private final CpuInfo cpuInfo;

    @Override
    public Map<String, Object> queryAll(PbNodeCpuQueryCriteria criteria, Pageable pageable) {
        Page<PbNodeCpu> page = pbNodeCpuRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
        return PageUtil.toPage(page.map(pbNodeCpuMapper::toDto));
    }

    @Override
    public List<PbNodeCpuDto> queryAll(PbNodeCpuQueryCriteria criteria) {
        return pbNodeCpuMapper.toDto(pbNodeCpuRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder)));
    }

    @Override
    @Transactional
    public PbNodeCpuDto findById(Long cpuId) {
        PbNodeCpu pbNodeCpu = pbNodeCpuRepository.findById(cpuId).orElseGet(PbNodeCpu::new);
        ValidationUtil.isNull(pbNodeCpu.getCpuId(), "PbNodeCpu", "cpuId", cpuId);
        return pbNodeCpuMapper.toDto(pbNodeCpu);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PbNodeCpuDto create(PbNodeCpu resources) {
        return pbNodeCpuMapper.toDto(pbNodeCpuRepository.save(resources));
    }

    @Override
    public void createCpu(Long nodeId) {
        JSONObject object = cpuInfo.getCpu(nodeId);
        object.keySet().forEach(key -> {
            PbNodeCpu resources = new PbNodeCpu();
            JSONObject json = object.getJSONObject(key);
            resources.setAllInfo(json.getString("allInfo"));
            resources.setCpuName(json.getString("name"));
            resources.setCores(json.getInteger("cores"));
            resources.setThreads(json.getInteger("threads"));
            resources.setArch(json.getString("arch"));
            resources.setL1Cache(json.getString("L1Cache"));
            resources.setL2Cache(json.getString("L2Cache"));
            resources.setL3Cache(json.getString("L3Cache"));
            resources.setBoostFrequency(json.getString("mainFrequency"));
            create(resources);
        });
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(PbNodeCpu resources) {
        PbNodeCpu pbNodeCpu = pbNodeCpuRepository.findById(resources.getCpuId()).orElseGet(PbNodeCpu::new);
        ValidationUtil.isNull(pbNodeCpu.getCpuId(), "PbNodeCpu", "id", resources.getCpuId());
        pbNodeCpu.copy(resources);
        pbNodeCpuRepository.save(pbNodeCpu);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long cpuId : ids) {
            pbNodeCpuRepository.deleteById(cpuId);
        }
    }

    @Override
    public void download(List<PbNodeCpuDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (PbNodeCpuDto pbNodeCpu : all) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("外键：表pm_node", pbNodeCpu.getNodeId());
            map.put("CPU型号", pbNodeCpu.getCpuName());
            map.put("核心数", pbNodeCpu.getCores());
            map.put("线程数", pbNodeCpu.getThreads());
            map.put("架构", pbNodeCpu.getArch());
            map.put("一级缓存", pbNodeCpu.getL1Cache());
            map.put("二级缓存", pbNodeCpu.getL2Cache());
            map.put("三级缓存", pbNodeCpu.getL3Cache());
            map.put("主频", pbNodeCpu.getMainFrequency());
            map.put("睿频", pbNodeCpu.getBoostFrequency());
            map.put("所有信息", pbNodeCpu.getAllInfo());
            map.put("创建时间", pbNodeCpu.getCreateTime());
            map.put("修改时间", pbNodeCpu.getUpdateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}