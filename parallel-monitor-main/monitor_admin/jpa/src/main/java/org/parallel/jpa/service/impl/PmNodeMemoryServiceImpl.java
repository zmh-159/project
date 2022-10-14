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
import org.parallel.jpa.domain.PmNodeMemory;
import org.parallel.jpa.repository.PmNodeMemoryRepository;
import org.parallel.jpa.service.PmNodeMemoryService;
import org.parallel.jpa.service.dto.PmNodeMemoryDto;
import org.parallel.jpa.service.dto.PmNodeMemoryQueryCriteria;
import org.parallel.jpa.service.mapstruct.PmNodeMemoryMapper;
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
public class PmNodeMemoryServiceImpl implements PmNodeMemoryService {

    private final PmNodeMemoryRepository pmNodeMemoryRepository;
    private final PmNodeMemoryMapper pmNodeMemoryMapper;

    @Override
    public Map<String,Object> queryAll(PmNodeMemoryQueryCriteria criteria, Pageable pageable){
        Page<PmNodeMemory> page = pmNodeMemoryRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(pmNodeMemoryMapper::toDto));
    }

    @Override
    public List<PmNodeMemoryDto> queryAll(PmNodeMemoryQueryCriteria criteria){
        return pmNodeMemoryMapper.toDto(pmNodeMemoryRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public PmNodeMemoryDto findById(Long memoryId) {
        PmNodeMemory pmNodeMemory = pmNodeMemoryRepository.findById(memoryId).orElseGet(PmNodeMemory::new);
        ValidationUtil.isNull(pmNodeMemory.getMemoryId(),"PmNodeMemory","memoryId",memoryId);
        return pmNodeMemoryMapper.toDto(pmNodeMemory);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PmNodeMemoryDto create(PmNodeMemory resources) {
        return pmNodeMemoryMapper.toDto(pmNodeMemoryRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(PmNodeMemory resources) {
        PmNodeMemory pmNodeMemory = pmNodeMemoryRepository.findById(resources.getMemoryId()).orElseGet(PmNodeMemory::new);
        ValidationUtil.isNull( pmNodeMemory.getMemoryId(),"PmNodeMemory","id",resources.getMemoryId());
        pmNodeMemory.copy(resources);
        pmNodeMemoryRepository.save(pmNodeMemory);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long memoryId : ids) {
            pmNodeMemoryRepository.deleteById(memoryId);
        }
    }

    @Override
    public void download(List<PmNodeMemoryDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (PmNodeMemoryDto pmNodeMemory : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("外键：表pm_node", pmNodeMemory.getNodeId());
            map.put("制造商", pmNodeMemory.getManufacturer());
            map.put("内存规格", pmNodeMemory.getMemoryType());
            map.put("速度", pmNodeMemory.getSpeed());
            map.put("容量", pmNodeMemory.getCapacity());
            map.put("创建时间", pmNodeMemory.getCreateTime());
            map.put("修改时间", pmNodeMemory.getUpdateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}