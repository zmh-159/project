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
import org.parallel.jpa.domain.PmPhysicNetwork;
import org.parallel.jpa.repository.PmPhysicNetworkRepository;
import org.parallel.jpa.service.PmPhysicNetworkService;
import org.parallel.jpa.service.dto.PmPhysicNetworkDto;
import org.parallel.jpa.service.dto.PmPhysicNetworkQueryCriteria;
import org.parallel.jpa.service.mapstruct.PmPhysicNetworkMapper;
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
public class PmPhysicNetworkServiceImpl implements PmPhysicNetworkService {

    private final PmPhysicNetworkRepository pmPhysicNetworkRepository;
    private final PmPhysicNetworkMapper pmPhysicNetworkMapper;

    @Override
    public Map<String,Object> queryAll(PmPhysicNetworkQueryCriteria criteria, Pageable pageable){
        Page<PmPhysicNetwork> page = pmPhysicNetworkRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(pmPhysicNetworkMapper::toDto));
    }

    @Override
    public List<PmPhysicNetworkDto> queryAll(PmPhysicNetworkQueryCriteria criteria){
        return pmPhysicNetworkMapper.toDto(pmPhysicNetworkRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public PmPhysicNetworkDto findById(Long physicNetworkId) {
        PmPhysicNetwork pmPhysicNetwork = pmPhysicNetworkRepository.findById(physicNetworkId).orElseGet(PmPhysicNetwork::new);
        ValidationUtil.isNull(pmPhysicNetwork.getPhysicNetworkId(),"PmPhysicNetwork","physicNetworkId",physicNetworkId);
        return pmPhysicNetworkMapper.toDto(pmPhysicNetwork);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PmPhysicNetworkDto create(PmPhysicNetwork resources) {
        return pmPhysicNetworkMapper.toDto(pmPhysicNetworkRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(PmPhysicNetwork resources) {
        PmPhysicNetwork pmPhysicNetwork = pmPhysicNetworkRepository.findById(resources.getPhysicNetworkId()).orElseGet(PmPhysicNetwork::new);
        ValidationUtil.isNull( pmPhysicNetwork.getPhysicNetworkId(),"PmPhysicNetwork","id",resources.getPhysicNetworkId());
        pmPhysicNetwork.copy(resources);
        pmPhysicNetworkRepository.save(pmPhysicNetwork);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long physicNetworkId : ids) {
            pmPhysicNetworkRepository.deleteById(physicNetworkId);
        }
    }

    @Override
    public void download(List<PmPhysicNetworkDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (PmPhysicNetworkDto pmPhysicNetwork : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("外键：表pm_node", pmPhysicNetwork.getNodeId());
            map.put("物理网卡名", pmPhysicNetwork.getName());
            map.put("创建时间", pmPhysicNetwork.getCreateTime());
            map.put("修改时间", pmPhysicNetwork.getUpdateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}