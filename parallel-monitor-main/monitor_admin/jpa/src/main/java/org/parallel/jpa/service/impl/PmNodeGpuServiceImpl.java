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

import org.parallel.jpa.domain.PmNodeGpu;
import org.parallel.web.utils.ValidationUtil;
import org.parallel.web.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import org.parallel.jpa.repository.PmNodeGpuRepository;
import org.parallel.jpa.service.PmNodeGpuService;
import org.parallel.jpa.service.dto.PmNodeGpuDto;
import org.parallel.jpa.service.dto.PmNodeGpuQueryCriteria;
import org.parallel.jpa.service.mapstruct.PmNodeGpuMapper;
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
* @website https://el-admin.vip
* @description 服务实现
* @author yuyifade
* @date 2021-08-18
**/
@Service
@RequiredArgsConstructor
public class PmNodeGpuServiceImpl implements PmNodeGpuService {

    private final PmNodeGpuRepository pmNodeGpuRepository;
    private final PmNodeGpuMapper pmNodeGpuMapper;

    @Override
    public Map<String,Object> queryAll(PmNodeGpuQueryCriteria criteria, Pageable pageable){
        Page<PmNodeGpu> page = pmNodeGpuRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(pmNodeGpuMapper::toDto));
    }

    @Override
    public List<PmNodeGpuDto> queryAll(PmNodeGpuQueryCriteria criteria){
        return pmNodeGpuMapper.toDto(pmNodeGpuRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public PmNodeGpuDto findById(Long gpuId) {
        PmNodeGpu pmNodeGpu = pmNodeGpuRepository.findById(gpuId).orElseGet(PmNodeGpu::new);
        ValidationUtil.isNull(pmNodeGpu.getGpuId(),"PmNodeGpu","gpuId",gpuId);
        return pmNodeGpuMapper.toDto(pmNodeGpu);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PmNodeGpuDto create(PmNodeGpu resources) {
        return pmNodeGpuMapper.toDto(pmNodeGpuRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(PmNodeGpu resources) {
        PmNodeGpu pmNodeGpu = pmNodeGpuRepository.findById(resources.getGpuId()).orElseGet(PmNodeGpu::new);
        ValidationUtil.isNull( pmNodeGpu.getGpuId(),"PmNodeGpu","id",resources.getGpuId());
        pmNodeGpu.copy(resources);
        pmNodeGpuRepository.save(pmNodeGpu);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long gpuId : ids) {
            pmNodeGpuRepository.deleteById(gpuId);
        }
    }

    @Override
    public void download(List<PmNodeGpuDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (PmNodeGpuDto pmNodeGpu : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("外键：表pm_node", pmNodeGpu.getNodeId());
            map.put("显卡uuid", pmNodeGpu.getUuid());
            map.put("显卡名", pmNodeGpu.getName());
            map.put("制造商", pmNodeGpu.getManufacturer());
            map.put("驱动版本", pmNodeGpu.getDriverVersion());
            map.put("记账缓冲区", pmNodeGpu.getAccountingModeBufferSize());
            map.put("cuda", pmNodeGpu.getCuda());
            map.put("位宽", pmNodeGpu.getWidth());
            map.put(" Vbios", pmNodeGpu.getVbiosVersion());
            map.put(" 显存", pmNodeGpu.getMemory());
            map.put("关机温度", pmNodeGpu.getGpuShutdownTemp());
            map.put("降频温度", pmNodeGpu.getGpuSlowdownTemp());
            map.put("gpu最大操作温度", pmNodeGpu.getGpuMaxOperatingTemp());
            map.put("显存最大操作温度", pmNodeGpu.getMemoryMaxOperatingTemp());
            map.put("最大限制功率", pmNodeGpu.getMaxPowerLimit());
            map.put("最小限制功率", pmNodeGpu.getMinPowerLimit());
            map.put("图像最大限制频率", pmNodeGpu.getMaxGraphicsFrequency());
            map.put("着色器最大限制频率", pmNodeGpu.getMaxSmFrequency());
            map.put("显存最大限制频率", pmNodeGpu.getMaxMemoryFrequency());
            map.put("视频最大限制频率", pmNodeGpu.getMaxVideoFrequency());
            map.put("创建时间", pmNodeGpu.getCreateTime());
            map.put("修改时间", pmNodeGpu.getUpdateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}