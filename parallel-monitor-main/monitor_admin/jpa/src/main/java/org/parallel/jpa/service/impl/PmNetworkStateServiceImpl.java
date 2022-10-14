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
import org.parallel.jpa.domain.PmNetworkState;
import org.parallel.jpa.repository.PmNetworkStateRepository;
import org.parallel.jpa.service.PmNetworkStateService;
import org.parallel.jpa.service.dto.PmNetworkStateDto;
import org.parallel.jpa.service.dto.PmNetworkStateQueryCriteria;
import org.parallel.jpa.service.mapstruct.PmNetworkStateMapper;
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
public class PmNetworkStateServiceImpl implements PmNetworkStateService {

    private final PmNetworkStateRepository pmNetworkStateRepository;
    private final PmNetworkStateMapper pmNetworkStateMapper;

    @Override
    public Map<String,Object> queryAll(PmNetworkStateQueryCriteria criteria, Pageable pageable){
        Page<PmNetworkState> page = pmNetworkStateRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(pmNetworkStateMapper::toDto));
    }

    @Override
    public List<PmNetworkStateDto> queryAll(PmNetworkStateQueryCriteria criteria){
        return pmNetworkStateMapper.toDto(pmNetworkStateRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public PmNetworkStateDto findById(Long networkStateId) {
        PmNetworkState pmNetworkState = pmNetworkStateRepository.findById(networkStateId).orElseGet(PmNetworkState::new);
        ValidationUtil.isNull(pmNetworkState.getNetworkStateId(),"PmNetworkState","networkStateId",networkStateId);
        return pmNetworkStateMapper.toDto(pmNetworkState);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PmNetworkStateDto create(PmNetworkState resources) {
        return pmNetworkStateMapper.toDto(pmNetworkStateRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(PmNetworkState resources) {
        PmNetworkState pmNetworkState = pmNetworkStateRepository.findById(resources.getNetworkStateId()).orElseGet(PmNetworkState::new);
        ValidationUtil.isNull( pmNetworkState.getNetworkStateId(),"PmNetworkState","id",resources.getNetworkStateId());
        pmNetworkState.copy(resources);
        pmNetworkStateRepository.save(pmNetworkState);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long networkStateId : ids) {
            pmNetworkStateRepository.deleteById(networkStateId);
        }
    }

    @Override
    public void download(List<PmNetworkStateDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (PmNetworkStateDto pmNetworkState : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("外键：表pm_network", pmNetworkState.getNetworkId());
            map.put("外键：表pm_node_state", pmNetworkState.getNodeStateId());
            map.put("总上传", pmNetworkState.getUpPackage());
            map.put("总下载", pmNetworkState.getDownPackage());
            map.put("上传速度", pmNetworkState.getUpSpeed());
            map.put("下载速度", pmNetworkState.getDownSpeed());
            map.put("创建时间", pmNetworkState.getCreateTime());
            map.put("修改时间", pmNetworkState.getUpdateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}