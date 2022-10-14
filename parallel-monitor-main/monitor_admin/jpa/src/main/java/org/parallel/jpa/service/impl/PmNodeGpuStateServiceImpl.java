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

import org.parallel.jpa.domain.PmNodeGpuState;
import org.parallel.web.utils.ValidationUtil;
import org.parallel.web.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import org.parallel.jpa.repository.PmNodeGpuStateRepository;
import org.parallel.jpa.service.PmNodeGpuStateService;
import org.parallel.jpa.service.dto.PmNodeGpuStateDto;
import org.parallel.jpa.service.dto.PmNodeGpuStateQueryCriteria;
import org.parallel.jpa.service.mapstruct.PmNodeGpuStateMapper;
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
public class PmNodeGpuStateServiceImpl implements PmNodeGpuStateService {

    private final PmNodeGpuStateRepository pmNodeGpuStateRepository;
    private final PmNodeGpuStateMapper pmNodeGpuStateMapper;

    @Override
    public Map<String,Object> queryAll(PmNodeGpuStateQueryCriteria criteria, Pageable pageable){
        Page<PmNodeGpuState> page = pmNodeGpuStateRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(pmNodeGpuStateMapper::toDto));
    }

    @Override
    public List<PmNodeGpuStateDto> queryAll(PmNodeGpuStateQueryCriteria criteria){
        return pmNodeGpuStateMapper.toDto(pmNodeGpuStateRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public PmNodeGpuStateDto findById(Long gpuStateId) {
        PmNodeGpuState pmNodeGpuState = pmNodeGpuStateRepository.findById(gpuStateId).orElseGet(PmNodeGpuState::new);
        ValidationUtil.isNull(pmNodeGpuState.getGpuStateId(),"PmNodeGpuState","gpuStateId",gpuStateId);
        return pmNodeGpuStateMapper.toDto(pmNodeGpuState);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PmNodeGpuStateDto create(PmNodeGpuState resources) {
        return pmNodeGpuStateMapper.toDto(pmNodeGpuStateRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(PmNodeGpuState resources) {
        PmNodeGpuState pmNodeGpuState = pmNodeGpuStateRepository.findById(resources.getGpuStateId()).orElseGet(PmNodeGpuState::new);
        ValidationUtil.isNull( pmNodeGpuState.getGpuStateId(),"PmNodeGpuState","id",resources.getGpuStateId());
        pmNodeGpuState.copy(resources);
        pmNodeGpuStateRepository.save(pmNodeGpuState);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long gpuStateId : ids) {
            pmNodeGpuStateRepository.deleteById(gpuStateId);
        }
    }

    @Override
    public void download(List<PmNodeGpuStateDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (PmNodeGpuStateDto pmNodeGpuState : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("外键：表pm_node_gpu", pmNodeGpuState.getGpuId());
            map.put("外键：表pm_node_state", pmNodeGpuState.getNodeStateId());
            map.put("上行速度", pmNodeGpuState.getTxThroughput());
            map.put("下行速度", pmNodeGpuState.getRxThroughput());
            map.put("风扇转速", pmNodeGpuState.getFanSpeed());
            map.put("性能状态", pmNodeGpuState.getPerformanceState());
            map.put("显存使用", pmNodeGpuState.getMemoryUsed());
            map.put("gpu利用率", pmNodeGpuState.getGpuUtilization());
            map.put("显存利用率", pmNodeGpuState.getMemoryUtilization());
            map.put("编码器利用率", pmNodeGpuState.getEncoderUtilization());
            map.put("解码器利用率", pmNodeGpuState.getDecoderUtilization());
            map.put("gpu当前温度", pmNodeGpuState.getGpuCurrentTemp());
            map.put("显存当前温度", pmNodeGpuState.getMemoryCurrentTemp());
            map.put("功耗", pmNodeGpuState.getGpuPowerDraw());
            map.put("图像当前频率", pmNodeGpuState.getCurrentGraphicsFrequency());
            map.put("着色器当前频率", pmNodeGpuState.getCurrentSmFrequency());
            map.put("显存当前频率", pmNodeGpuState.getCurrentMemoryFrequency());
            map.put("视频当前频率", pmNodeGpuState.getCurrentVideoFrequency());
            map.put("创建时间", pmNodeGpuState.getCreateTime());
            map.put("修改时间", pmNodeGpuState.getUpdateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}