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

import org.parallel.web.modules.bmc.jpa.domain.PbNodeDisk;
import org.parallel.web.utils.ValidationUtil;
import org.parallel.web.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import org.parallel.web.modules.bmc.jpa.repository.PbNodeDiskRepository;
import org.parallel.web.modules.bmc.jpa.service.PbNodeDiskService;
import org.parallel.web.modules.bmc.jpa.service.dto.PbNodeDiskDto;
import org.parallel.web.modules.bmc.jpa.service.dto.PbNodeDiskQueryCriteria;
import org.parallel.web.modules.bmc.jpa.service.mapstruct.PbNodeDiskMapper;
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
public class PbNodeDiskServiceImpl implements PbNodeDiskService {

    private final PbNodeDiskRepository pbNodeDiskRepository;
    private final PbNodeDiskMapper pbNodeDiskMapper;

    @Override
    public Map<String,Object> queryAll(PbNodeDiskQueryCriteria criteria, Pageable pageable){
        Page<PbNodeDisk> page = pbNodeDiskRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(pbNodeDiskMapper::toDto));
    }

    @Override
    public List<PbNodeDiskDto> queryAll(PbNodeDiskQueryCriteria criteria){
        return pbNodeDiskMapper.toDto(pbNodeDiskRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public PbNodeDiskDto findById(Long diskId) {
        PbNodeDisk pbNodeDisk = pbNodeDiskRepository.findById(diskId).orElseGet(PbNodeDisk::new);
        ValidationUtil.isNull(pbNodeDisk.getDiskId(),"PbNodeDisk","diskId",diskId);
        return pbNodeDiskMapper.toDto(pbNodeDisk);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PbNodeDiskDto create(PbNodeDisk resources) {
        return pbNodeDiskMapper.toDto(pbNodeDiskRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(PbNodeDisk resources) {
        PbNodeDisk pbNodeDisk = pbNodeDiskRepository.findById(resources.getDiskId()).orElseGet(PbNodeDisk::new);
        ValidationUtil.isNull( pbNodeDisk.getDiskId(),"PbNodeDisk","id",resources.getDiskId());
        pbNodeDisk.copy(resources);
        pbNodeDiskRepository.save(pbNodeDisk);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long diskId : ids) {
            pbNodeDiskRepository.deleteById(diskId);
        }
    }

    @Override
    public void download(List<PbNodeDiskDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (PbNodeDiskDto pbNodeDisk : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("外键：表pm_node", pbNodeDisk.getNodeId());
            map.put("磁盘名", pbNodeDisk.getName());
            map.put("磁盘类型", pbNodeDisk.getMediaType());
            map.put("容量", pbNodeDisk.getCapacity());
            map.put("所有信息", pbNodeDisk.getAllInfo());
            map.put("创建时间", pbNodeDisk.getCreateTime());
            map.put("修改时间", pbNodeDisk.getUpdateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}