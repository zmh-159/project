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

import org.parallel.web.modules.bmc.jpa.domain.PbNodeBios;
import org.parallel.web.utils.ValidationUtil;
import org.parallel.web.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import org.parallel.web.modules.bmc.jpa.repository.PbNodeBiosRepository;
import org.parallel.web.modules.bmc.jpa.service.PbNodeBiosService;
import org.parallel.web.modules.bmc.jpa.service.dto.PbNodeBiosDto;
import org.parallel.web.modules.bmc.jpa.service.dto.PbNodeBiosQueryCriteria;
import org.parallel.web.modules.bmc.jpa.service.mapstruct.PbNodeBiosMapper;
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
public class PbNodeBiosServiceImpl implements PbNodeBiosService {

    private final PbNodeBiosRepository pbNodeBiosRepository;
    private final PbNodeBiosMapper pbNodeBiosMapper;

    @Override
    public Map<String,Object> queryAll(PbNodeBiosQueryCriteria criteria, Pageable pageable){
        Page<PbNodeBios> page = pbNodeBiosRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(pbNodeBiosMapper::toDto));
    }

    @Override
    public List<PbNodeBiosDto> queryAll(PbNodeBiosQueryCriteria criteria){
        return pbNodeBiosMapper.toDto(pbNodeBiosRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public PbNodeBiosDto findById(Long biosId) {
        PbNodeBios pbNodeBios = pbNodeBiosRepository.findById(biosId).orElseGet(PbNodeBios::new);
        ValidationUtil.isNull(pbNodeBios.getBiosId(),"PbNodeBios","biosId",biosId);
        return pbNodeBiosMapper.toDto(pbNodeBios);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PbNodeBiosDto create(PbNodeBios resources) {
        return pbNodeBiosMapper.toDto(pbNodeBiosRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(PbNodeBios resources) {
        PbNodeBios pbNodeBios = pbNodeBiosRepository.findById(resources.getBiosId()).orElseGet(PbNodeBios::new);
        ValidationUtil.isNull( pbNodeBios.getBiosId(),"PbNodeBios","id",resources.getBiosId());
        pbNodeBios.copy(resources);
        pbNodeBiosRepository.save(pbNodeBios);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long biosId : ids) {
            pbNodeBiosRepository.deleteById(biosId);
        }
    }

    @Override
    public void download(List<PbNodeBiosDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (PbNodeBiosDto pbNodeBios : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("外键：表pm_node", pbNodeBios.getNodeId());
            map.put("BIOS名", pbNodeBios.getName());
            map.put("快速启动模式", pbNodeBios.getQuickBoot());
            map.put("静态启动模式", pbNodeBios.getQuietBoot());
            map.put("UEFI的PXE功能", pbNodeBios.getPxeUefi());
            map.put("legacy的PXE功能", pbNodeBios.getPxeLegacy());
            map.put("能效模式", pbNodeBios.getCustomPowerPolicy());
            map.put("cpu动态加速开关", pbNodeBios.getTurboMode());
            map.put("超线程设置", pbNodeBios.getProHyperThread());
            map.put("所有信息", pbNodeBios.getAllInfo());
            map.put("创建时间", pbNodeBios.getCreateTime());
            map.put("编辑时间", pbNodeBios.getUpdateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}