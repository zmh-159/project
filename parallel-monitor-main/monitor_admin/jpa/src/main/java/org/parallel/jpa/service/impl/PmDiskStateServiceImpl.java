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
import org.parallel.jpa.domain.PmDiskState;
import org.parallel.jpa.repository.PmDiskStateRepository;
import org.parallel.jpa.service.PmDiskStateService;
import org.parallel.jpa.service.dto.PmDiskStateDto;
import org.parallel.jpa.service.dto.PmDiskStateQueryCriteria;
import org.parallel.jpa.service.mapstruct.PmDiskStateMapper;
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
public class PmDiskStateServiceImpl implements PmDiskStateService {

    private final PmDiskStateRepository pmDiskStateRepository;
    private final PmDiskStateMapper pmDiskStateMapper;

    @Override
    public Map<String,Object> queryAll(PmDiskStateQueryCriteria criteria, Pageable pageable){
        Page<PmDiskState> page = pmDiskStateRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(pmDiskStateMapper::toDto));
    }

    @Override
    public List<PmDiskStateDto> queryAll(PmDiskStateQueryCriteria criteria){
        return pmDiskStateMapper.toDto(pmDiskStateRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public PmDiskStateDto findById(Long diskStateId) {
        PmDiskState pmDiskState = pmDiskStateRepository.findById(diskStateId).orElseGet(PmDiskState::new);
        ValidationUtil.isNull(pmDiskState.getDiskStateId(),"PmDiskState","diskStateId",diskStateId);
        return pmDiskStateMapper.toDto(pmDiskState);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PmDiskStateDto create(PmDiskState resources) {
        return pmDiskStateMapper.toDto(pmDiskStateRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(PmDiskState resources) {
        PmDiskState pmDiskState = pmDiskStateRepository.findById(resources.getDiskStateId()).orElseGet(PmDiskState::new);
        ValidationUtil.isNull( pmDiskState.getDiskStateId(),"PmDiskState","id",resources.getDiskStateId());
        pmDiskState.copy(resources);
        pmDiskStateRepository.save(pmDiskState);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long diskStateId : ids) {
            pmDiskStateRepository.deleteById(diskStateId);
        }
    }

    @Override
    public void download(List<PmDiskStateDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (PmDiskStateDto pmDiskState : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("外键：表pm_node_disk", pmDiskState.getDiskId());
            map.put("外键：表pm_node_state", pmDiskState.getNodeStateId());
            map.put("读速度", pmDiskState.getReadSpeed());
            map.put("写速度", pmDiskState.getWriteSpeed());
            map.put("创建时间", pmDiskState.getCreateTime());
            map.put("修改时间", pmDiskState.getUpdateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}