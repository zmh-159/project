package org.parallel.jpa.service.impl;
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

import org.parallel.jpa.domain.PmAlarmScheme;
import org.parallel.jpa.repository.PmAlarmSchemeRepository;
import org.parallel.jpa.service.dto.PmAlarmSchemeDto;
import org.parallel.jpa.service.dto.PmAlarmSchemeQueryCriteria;
import org.parallel.jpa.service.mapstruct.PmAlarmSchemeMapper;
import org.parallel.jpa.service.mapstruct.PmAlarmSchemeService;
import org.parallel.web.utils.ValidationUtil;
import org.parallel.web.utils.FileUtil;
import lombok.RequiredArgsConstructor;
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
 * @author yuyifade
 * @date 2021-11-11
 **/
@Service
@RequiredArgsConstructor
public class PmAlarmSchemeServiceImpl implements PmAlarmSchemeService {

    private final PmAlarmSchemeRepository pmAlarmSchemeRepository;
    private final PmAlarmSchemeMapper pmAlarmSchemeMapper;

    @Override
    public Map<String,Object> queryAll(PmAlarmSchemeQueryCriteria criteria, Pageable pageable){
        Page<PmAlarmScheme> page = pmAlarmSchemeRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(pmAlarmSchemeMapper::toDto));
    }

    @Override
    public List<PmAlarmSchemeDto> queryAll(PmAlarmSchemeQueryCriteria criteria){
        return pmAlarmSchemeMapper.toDto(pmAlarmSchemeRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public PmAlarmSchemeDto findById(Integer alarmSchemeId) {
        PmAlarmScheme pmAlarmScheme = pmAlarmSchemeRepository.findById(alarmSchemeId).orElseGet(PmAlarmScheme::new);
        ValidationUtil.isNull(pmAlarmScheme.getAlarmSchemeId(),"PmAlarmScheme","alarmSchemeId",alarmSchemeId);
        return pmAlarmSchemeMapper.toDto(pmAlarmScheme);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PmAlarmSchemeDto create(PmAlarmScheme resources) {
        return pmAlarmSchemeMapper.toDto(pmAlarmSchemeRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(PmAlarmScheme resources) {
        PmAlarmScheme pmAlarmScheme = pmAlarmSchemeRepository.findById(resources.getAlarmSchemeId()).orElseGet(PmAlarmScheme::new);
        ValidationUtil.isNull( pmAlarmScheme.getAlarmSchemeId(),"PmAlarmScheme","id",resources.getAlarmSchemeId());
        pmAlarmScheme.copy(resources);
        pmAlarmSchemeRepository.save(pmAlarmScheme);
    }

    @Override
    public void deleteAll(Integer[] ids) {
        for (Integer alarmSchemeId : ids) {
            pmAlarmSchemeRepository.deleteById(alarmSchemeId);
        }
    }

    @Override
    public void download(List<PmAlarmSchemeDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (PmAlarmSchemeDto pmAlarmScheme : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("方案名", pmAlarmScheme.getName());
            map.put("配置", pmAlarmScheme.getDetail());
            map.put("生效节点", pmAlarmScheme.getEffectNode());
            map.put("状态", pmAlarmScheme.getStatus());
            map.put("更新时间", pmAlarmScheme.getUpdateTime());
            map.put("创建时间", pmAlarmScheme.getCreateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}