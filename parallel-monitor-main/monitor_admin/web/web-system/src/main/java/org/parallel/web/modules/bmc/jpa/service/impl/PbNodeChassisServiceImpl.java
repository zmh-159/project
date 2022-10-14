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

import org.parallel.web.modules.bmc.jpa.domain.PbNodeChassis;
import org.parallel.web.utils.ValidationUtil;
import org.parallel.web.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import org.parallel.web.modules.bmc.jpa.repository.PbNodeChassisRepository;
import org.parallel.web.modules.bmc.jpa.service.PbNodeChassisService;
import org.parallel.web.modules.bmc.jpa.service.dto.PbNodeChassisDto;
import org.parallel.web.modules.bmc.jpa.service.dto.PbNodeChassisQueryCriteria;
import org.parallel.web.modules.bmc.jpa.service.mapstruct.PbNodeChassisMapper;
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
public class PbNodeChassisServiceImpl implements PbNodeChassisService {

    private final PbNodeChassisRepository pbNodeChassisRepository;
    private final PbNodeChassisMapper pbNodeChassisMapper;

    @Override
    public Map<String,Object> queryAll(PbNodeChassisQueryCriteria criteria, Pageable pageable){
        Page<PbNodeChassis> page = pbNodeChassisRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(pbNodeChassisMapper::toDto));
    }

    @Override
    public List<PbNodeChassisDto> queryAll(PbNodeChassisQueryCriteria criteria){
        return pbNodeChassisMapper.toDto(pbNodeChassisRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public PbNodeChassisDto findById(Long physicNetworkId) {
        PbNodeChassis pbNodeChassis = pbNodeChassisRepository.findById(physicNetworkId).orElseGet(PbNodeChassis::new);
        ValidationUtil.isNull(pbNodeChassis.getPhysicNetworkId(),"PbNodeChassis","physicNetworkId",physicNetworkId);
        return pbNodeChassisMapper.toDto(pbNodeChassis);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PbNodeChassisDto create(PbNodeChassis resources) {
        return pbNodeChassisMapper.toDto(pbNodeChassisRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(PbNodeChassis resources) {
        PbNodeChassis pbNodeChassis = pbNodeChassisRepository.findById(resources.getPhysicNetworkId()).orElseGet(PbNodeChassis::new);
        ValidationUtil.isNull( pbNodeChassis.getPhysicNetworkId(),"PbNodeChassis","id",resources.getPhysicNetworkId());
        pbNodeChassis.copy(resources);
        pbNodeChassisRepository.save(pbNodeChassis);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long physicNetworkId : ids) {
            pbNodeChassisRepository.deleteById(physicNetworkId);
        }
    }

    @Override
    public void download(List<PbNodeChassisDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (PbNodeChassisDto pbNodeChassis : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("外键：表pm_node", pbNodeChassis.getNodeId());
            map.put("机箱名", pbNodeChassis.getName());
            map.put("机箱类型", pbNodeChassis.getType());
            map.put("厂商信息", pbNodeChassis.getManufacturer());
            map.put("机箱序列号", pbNodeChassis.getSn());
            map.put("所有信息", pbNodeChassis.getAllInfo());
            map.put("创建时间", pbNodeChassis.getCreateTime());
            map.put("修改时间", pbNodeChassis.getUpdateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}