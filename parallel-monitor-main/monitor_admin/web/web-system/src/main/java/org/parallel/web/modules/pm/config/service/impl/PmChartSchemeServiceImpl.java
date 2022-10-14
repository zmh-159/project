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
package org.parallel.web.modules.pm.config.service.impl;

import org.parallel.web.exception.BadRequestException;
import org.parallel.web.modules.pm.config.domain.PmChartScheme;
import org.parallel.web.modules.pm.config.repository.PmChartSchemeRepository;
import org.parallel.web.modules.pm.config.service.PmChartSchemeService;
import org.parallel.web.modules.pm.config.service.dto.PmChartSchemeDto;
import org.parallel.web.modules.pm.config.service.dto.PmChartSchemeQueryCriteria;
import org.parallel.web.modules.pm.config.service.mapstruct.PmChartSchemeMapper;
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
 * @author yuyifade
 * @website https://el-admin.vip
 * @description 服务实现
 * @date 2021-07-07
 **/
@Service
@RequiredArgsConstructor
public class PmChartSchemeServiceImpl implements PmChartSchemeService {

    private final PmChartSchemeRepository pmChartSchemeRepository;
    private final PmChartSchemeMapper pmChartSchemeMapper;

    @Override
    public Map<String, Object> queryAll(PmChartSchemeQueryCriteria criteria, Pageable pageable) {
        Page<PmChartScheme> page = pmChartSchemeRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
        return PageUtil.toPage(page.map(pmChartSchemeMapper::toDto));
    }

    @Override
    public List<PmChartSchemeDto> queryAll(PmChartSchemeQueryCriteria criteria) {
        return pmChartSchemeMapper.toDto(pmChartSchemeRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder)));
    }

    @Override
    @Transactional
    public PmChartSchemeDto findById(Long chartSchemeId) {
        PmChartScheme pmChartScheme = pmChartSchemeRepository.findById(chartSchemeId).orElseGet(PmChartScheme::new);
        ValidationUtil.isNull(pmChartScheme.getChartSchemeId(), "PmChartScheme", "chartSchemeId", chartSchemeId);
        return pmChartSchemeMapper.toDto(pmChartScheme);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PmChartSchemeDto create(PmChartScheme resources) {
        return pmChartSchemeMapper.toDto(pmChartSchemeRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(PmChartScheme resources) {
        PmChartScheme pmChartScheme = pmChartSchemeRepository.findById(resources.getChartSchemeId()).orElseGet(PmChartScheme::new);
        if (pmChartScheme.getUserId() != resources.getUserId()) {
            throw new BadRequestException("不能修改他人的方案！");
        }
        ValidationUtil.isNull(pmChartScheme.getChartSchemeId(), "PmChartScheme", "id", resources.getChartSchemeId());
        pmChartScheme.copy(resources);
        pmChartSchemeRepository.save(pmChartScheme);
    }

    @Override
    public void deleteAll(Long[] ids,Long userId) {
        for (Long chartSchemeId : ids) {
            if (!pmChartSchemeRepository.getOne(chartSchemeId).getUserId().equals(userId)) {
                throw new BadRequestException("不能删除他人配置！");
            }
            pmChartSchemeRepository.deleteById(chartSchemeId);
        }
    }

    @Override
    public void download(List<PmChartSchemeDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (PmChartSchemeDto pmChartScheme : all) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("外键：关联用户表", pmChartScheme.getUserId());
            map.put("描述", pmChartScheme.getDescription());
            map.put("方案编号", pmChartScheme.getSerialNumber());
            map.put("方案具体参数", pmChartScheme.getDetail());
            map.put("位置编号", pmChartScheme.getPosition());
            map.put("创建日期", pmChartScheme.getCreateTime());
            map.put("更新时间", pmChartScheme.getUpdateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}