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

import org.parallel.web.modules.bmc.jpa.domain.PbNodeGpu;
import org.parallel.web.utils.ValidationUtil;
import org.parallel.web.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import org.parallel.web.modules.bmc.jpa.repository.PbNodeGpuRepository;
import org.parallel.web.modules.bmc.jpa.service.PbNodeGpuService;
import org.parallel.web.modules.bmc.jpa.service.dto.PbNodeGpuDto;
import org.parallel.web.modules.bmc.jpa.service.dto.PbNodeGpuQueryCriteria;
import org.parallel.web.modules.bmc.jpa.service.mapstruct.PbNodeGpuMapper;
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
* @author ruo-yin
* @date 2022-08-09
**/
@Service
@RequiredArgsConstructor
public class PbNodeGpuServiceImpl implements PbNodeGpuService {

    private final PbNodeGpuRepository pbNodeGpuRepository;
    private final PbNodeGpuMapper pbNodeGpuMapper;

    @Override
    public Map<String,Object> queryAll(PbNodeGpuQueryCriteria criteria, Pageable pageable){
        Page<PbNodeGpu> page = pbNodeGpuRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(pbNodeGpuMapper::toDto));
    }

    @Override
    public List<PbNodeGpuDto> queryAll(PbNodeGpuQueryCriteria criteria){
        return pbNodeGpuMapper.toDto(pbNodeGpuRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public PbNodeGpuDto findById(Long gpuId) {
        PbNodeGpu pbNodeGpu = pbNodeGpuRepository.findById(gpuId).orElseGet(PbNodeGpu::new);
        ValidationUtil.isNull(pbNodeGpu.getGpuId(),"PbNodeGpu","gpuId",gpuId);
        return pbNodeGpuMapper.toDto(pbNodeGpu);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PbNodeGpuDto create(PbNodeGpu resources) {
        return pbNodeGpuMapper.toDto(pbNodeGpuRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(PbNodeGpu resources) {
        PbNodeGpu pbNodeGpu = pbNodeGpuRepository.findById(resources.getGpuId()).orElseGet(PbNodeGpu::new);
        ValidationUtil.isNull( pbNodeGpu.getGpuId(),"PbNodeGpu","id",resources.getGpuId());
        pbNodeGpu.copy(resources);
        pbNodeGpuRepository.save(pbNodeGpu);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long gpuId : ids) {
            pbNodeGpuRepository.deleteById(gpuId);
        }
    }

    @Override
    public void download(List<PbNodeGpuDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (PbNodeGpuDto pbNodeGpu : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("外键：表pm_node", pbNodeGpu.getNodeId());
            map.put("显卡uuid", pbNodeGpu.getUuid());
            map.put("显卡名", pbNodeGpu.getName());
            map.put("制造商", pbNodeGpu.getManufacturer());
            map.put("固件版本", pbNodeGpu.getFirmwareVersion());
            map.put("槽位号", pbNodeGpu.getSlotNumber());
            map.put("所有信息", pbNodeGpu.getAllInfo());
            map.put("创建时间", pbNodeGpu.getCreateTime());
            map.put("修改时间", pbNodeGpu.getUpdateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}