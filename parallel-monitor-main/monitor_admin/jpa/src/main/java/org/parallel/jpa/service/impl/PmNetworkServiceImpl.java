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
import org.parallel.jpa.domain.PmNetwork;
import org.parallel.jpa.repository.PmNetworkRepository;
import org.parallel.jpa.service.PmNetworkService;
import org.parallel.jpa.service.dto.PmNetworkDto;
import org.parallel.jpa.service.dto.PmNetworkQueryCriteria;
import org.parallel.jpa.service.mapstruct.PmNetworkMapper;
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
public class PmNetworkServiceImpl implements PmNetworkService {

    private final PmNetworkRepository pmNetworkRepository;
    private final PmNetworkMapper pmNetworkMapper;

    @Override
    public Map<String,Object> queryAll(PmNetworkQueryCriteria criteria, Pageable pageable){
        Page<PmNetwork> page = pmNetworkRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(pmNetworkMapper::toDto));
    }

    @Override
    public List<PmNetworkDto> queryAll(PmNetworkQueryCriteria criteria){
        return pmNetworkMapper.toDto(pmNetworkRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public PmNetworkDto findById(Long networkId) {
        PmNetwork pmNetwork = pmNetworkRepository.findById(networkId).orElseGet(PmNetwork::new);
        ValidationUtil.isNull(pmNetwork.getNetworkId(),"PmNetwork","networkId",networkId);
        return pmNetworkMapper.toDto(pmNetwork);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PmNetworkDto create(PmNetwork resources) {
        return pmNetworkMapper.toDto(pmNetworkRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(PmNetwork resources) {
        PmNetwork pmNetwork = pmNetworkRepository.findById(resources.getNetworkId()).orElseGet(PmNetwork::new);
        ValidationUtil.isNull( pmNetwork.getNetworkId(),"PmNetwork","id",resources.getNetworkId());
        pmNetwork.copy(resources);
        pmNetworkRepository.save(pmNetwork);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long networkId : ids) {
            pmNetworkRepository.deleteById(networkId);
        }
    }

    @Override
    public void download(List<PmNetworkDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (PmNetworkDto pmNetwork : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("外键：表pm_node", pmNetwork.getNodeId());
            map.put("ip地址", pmNetwork.getIp());
            map.put("mac地址", pmNetwork.getMac());
            map.put("网卡名", pmNetwork.getName());
            map.put("创建时间", pmNetwork.getCreateTime());
            map.put("修改时间", pmNetwork.getUpdateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}