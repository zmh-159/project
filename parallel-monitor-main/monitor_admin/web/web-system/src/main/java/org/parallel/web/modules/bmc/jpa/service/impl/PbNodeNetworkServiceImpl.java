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

import org.parallel.web.modules.bmc.jpa.domain.PbNodeNetwork;
import org.parallel.web.utils.ValidationUtil;
import org.parallel.web.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import org.parallel.web.modules.bmc.jpa.repository.PbNodeNetworkRepository;
import org.parallel.web.modules.bmc.jpa.service.PbNodeNetworkService;
import org.parallel.web.modules.bmc.jpa.service.dto.PbNodeNetworkDto;
import org.parallel.web.modules.bmc.jpa.service.dto.PbNodeNetworkQueryCriteria;
import org.parallel.web.modules.bmc.jpa.service.mapstruct.PbNodeNetworkMapper;
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
public class PbNodeNetworkServiceImpl implements PbNodeNetworkService {

    private final PbNodeNetworkRepository pbNodeNetworkRepository;
    private final PbNodeNetworkMapper pbNodeNetworkMapper;

    @Override
    public Map<String,Object> queryAll(PbNodeNetworkQueryCriteria criteria, Pageable pageable){
        Page<PbNodeNetwork> page = pbNodeNetworkRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(pbNodeNetworkMapper::toDto));
    }

    @Override
    public List<PbNodeNetworkDto> queryAll(PbNodeNetworkQueryCriteria criteria){
        return pbNodeNetworkMapper.toDto(pbNodeNetworkRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public PbNodeNetworkDto findById(Long networkId) {
        PbNodeNetwork pbNodeNetwork = pbNodeNetworkRepository.findById(networkId).orElseGet(PbNodeNetwork::new);
        ValidationUtil.isNull(pbNodeNetwork.getNetworkId(),"PbNodeNetwork","networkId",networkId);
        return pbNodeNetworkMapper.toDto(pbNodeNetwork);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PbNodeNetworkDto create(PbNodeNetwork resources) {
        return pbNodeNetworkMapper.toDto(pbNodeNetworkRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(PbNodeNetwork resources) {
        PbNodeNetwork pbNodeNetwork = pbNodeNetworkRepository.findById(resources.getNetworkId()).orElseGet(PbNodeNetwork::new);
        ValidationUtil.isNull( pbNodeNetwork.getNetworkId(),"PbNodeNetwork","id",resources.getNetworkId());
        pbNodeNetwork.copy(resources);
        pbNodeNetworkRepository.save(pbNodeNetwork);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long networkId : ids) {
            pbNodeNetworkRepository.deleteById(networkId);
        }
    }

    @Override
    public void download(List<PbNodeNetworkDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (PbNodeNetworkDto pbNodeNetwork : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("外键：表pm_node", pbNodeNetwork.getNodeId());
            map.put("ipv4地址", pbNodeNetwork.getIp());
            map.put("mac地址", pbNodeNetwork.getMac());
            map.put("网卡名", pbNodeNetwork.getName());
            map.put("地址来源", pbNodeNetwork.getAddressOrigin());
            map.put("所有信息", pbNodeNetwork.getAllInfo());
            map.put("创建时间", pbNodeNetwork.getCreateTime());
            map.put("修改时间", pbNodeNetwork.getUpdateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}