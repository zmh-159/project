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
import org.parallel.jpa.domain.PmNodeDisk;
import org.parallel.jpa.repository.PmNodeDiskRepository;
import org.parallel.jpa.service.PmNodeDiskService;
import org.parallel.jpa.service.dto.PmNodeDiskDto;
import org.parallel.jpa.service.dto.PmNodeDiskQueryCriteria;
import org.parallel.jpa.service.mapstruct.PmNodeDiskMapper;
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
public class PmNodeDiskServiceImpl implements PmNodeDiskService {

    private final PmNodeDiskRepository pmNodeDiskRepository;
    private final PmNodeDiskMapper pmNodeDiskMapper;

    @Override
    public Map<String,Object> queryAll(PmNodeDiskQueryCriteria criteria, Pageable pageable){
        Page<PmNodeDisk> page = pmNodeDiskRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(pmNodeDiskMapper::toDto));
    }

    @Override
    public List<PmNodeDiskDto> queryAll(PmNodeDiskQueryCriteria criteria){
        return pmNodeDiskMapper.toDto(pmNodeDiskRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public PmNodeDiskDto findById(Long diskId) {
        PmNodeDisk pmNodeDisk = pmNodeDiskRepository.findById(diskId).orElseGet(PmNodeDisk::new);
        ValidationUtil.isNull(pmNodeDisk.getDiskId(),"PmNodeDisk","diskId",diskId);
        return pmNodeDiskMapper.toDto(pmNodeDisk);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PmNodeDiskDto create(PmNodeDisk resources) {
        return pmNodeDiskMapper.toDto(pmNodeDiskRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(PmNodeDisk resources) {
        PmNodeDisk pmNodeDisk = pmNodeDiskRepository.findById(resources.getDiskId()).orElseGet(PmNodeDisk::new);
        ValidationUtil.isNull( pmNodeDisk.getDiskId(),"PmNodeDisk","id",resources.getDiskId());
        pmNodeDisk.copy(resources);
        pmNodeDiskRepository.save(pmNodeDisk);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long diskId : ids) {
            pmNodeDiskRepository.deleteById(diskId);
        }
    }

    @Override
    public void download(List<PmNodeDiskDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (PmNodeDiskDto pmNodeDisk : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("外键：表pm_node", pmNodeDisk.getNodeId());
            map.put("磁盘名", pmNodeDisk.getName());
            map.put("容量", pmNodeDisk.getCapacity());
            map.put("创建时间", pmNodeDisk.getCreateTime());
            map.put("修改时间", pmNodeDisk.getUpdateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}