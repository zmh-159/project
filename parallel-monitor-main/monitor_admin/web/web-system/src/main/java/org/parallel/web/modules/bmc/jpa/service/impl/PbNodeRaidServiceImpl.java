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

import org.parallel.web.modules.bmc.jpa.domain.PbNodeRaid;
import org.parallel.web.utils.ValidationUtil;
import org.parallel.web.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import org.parallel.web.modules.bmc.jpa.repository.PbNodeRaidRepository;
import org.parallel.web.modules.bmc.jpa.service.PbNodeRaidService;
import org.parallel.web.modules.bmc.jpa.service.dto.PbNodeRaidDto;
import org.parallel.web.modules.bmc.jpa.service.dto.PbNodeRaidQueryCriteria;
import org.parallel.web.modules.bmc.jpa.service.mapstruct.PbNodeRaidMapper;
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
public class PbNodeRaidServiceImpl implements PbNodeRaidService {

    private final PbNodeRaidRepository pbNodeRaidRepository;
    private final PbNodeRaidMapper pbNodeRaidMapper;

    @Override
    public Map<String,Object> queryAll(PbNodeRaidQueryCriteria criteria, Pageable pageable){
        Page<PbNodeRaid> page = pbNodeRaidRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(pbNodeRaidMapper::toDto));
    }

    @Override
    public List<PbNodeRaidDto> queryAll(PbNodeRaidQueryCriteria criteria){
        return pbNodeRaidMapper.toDto(pbNodeRaidRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public PbNodeRaidDto findById(Long raidId) {
        PbNodeRaid pbNodeRaid = pbNodeRaidRepository.findById(raidId).orElseGet(PbNodeRaid::new);
        ValidationUtil.isNull(pbNodeRaid.getRaidId(),"PbNodeRaid","raidId",raidId);
        return pbNodeRaidMapper.toDto(pbNodeRaid);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PbNodeRaidDto create(PbNodeRaid resources) {
        return pbNodeRaidMapper.toDto(pbNodeRaidRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(PbNodeRaid resources) {
        PbNodeRaid pbNodeRaid = pbNodeRaidRepository.findById(resources.getRaidId()).orElseGet(PbNodeRaid::new);
        ValidationUtil.isNull( pbNodeRaid.getRaidId(),"PbNodeRaid","id",resources.getRaidId());
        pbNodeRaid.copy(resources);
        pbNodeRaidRepository.save(pbNodeRaid);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long raidId : ids) {
            pbNodeRaidRepository.deleteById(raidId);
        }
    }

    @Override
    public void download(List<PbNodeRaidDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (PbNodeRaidDto pbNodeRaid : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("外键：表pm_node", pbNodeRaid.getNodeId());
            map.put("阵列卡名", pbNodeRaid.getName());
            map.put("健康状态", pbNodeRaid.getHealth());
            map.put("接口速率", pbNodeRaid.getSpeed());
            map.put("制造商", pbNodeRaid.getManufacturer());
            map.put("支持的RAID级别", pbNodeRaid.getSupportedRaidType());
            map.put("所有信息", pbNodeRaid.getAllInfo());
            map.put("创建时间", pbNodeRaid.getCreateTime());
            map.put("编辑时间", pbNodeRaid.getUpdateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}