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

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.parallel.web.exception.BadRequestException;
import org.parallel.web.exception.EntityExistException;
import org.parallel.web.modules.pm.config.domain.PmConfig;
import org.parallel.web.modules.pm.config.repository.PmConfigRepository;
import org.parallel.web.modules.pm.config.service.PmConfigService;
import org.parallel.web.modules.pm.config.service.dto.PmConfigDto;
import org.parallel.web.modules.pm.config.service.dto.PmConfigQueryCriteria;
import org.parallel.web.modules.pm.config.service.mapstruct.PmConfigMapper;
import org.parallel.web.modules.system.repository.UserRepository;
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
 * @author yuyifade
 * @website https://el-admin.vip
 * @description 服务实现
 * @date 2021-06-18
 **/
@Service
@RequiredArgsConstructor
public class PmConfigServiceImpl implements PmConfigService {

    private final PmConfigRepository pmConfigRepository;
    private final PmConfigMapper pmConfigMapper;
    private final UserRepository userRepository;

    @Override
    public Map<String, Object> queryAll(PmConfigQueryCriteria criteria, Pageable pageable) {
        Page<PmConfig> page = pmConfigRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
        return PageUtil.toPage(page.map(pmConfigMapper::toDto));
    }

    @Override
    public List<PmConfigDto> queryAll(PmConfigQueryCriteria criteria) {
        return pmConfigMapper.toDto(pmConfigRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder)));
    }

    @Override
    @Transactional
    public PmConfigDto findById(Long configId) {
        PmConfig pmConfig = pmConfigRepository.findById(configId).orElseGet(PmConfig::new);
        ValidationUtil.isNull(pmConfig.getConfigId(), "PmConfig", "configId", configId);
        return pmConfigMapper.toDto(pmConfig);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PmConfigDto create(PmConfig resources) {
        if (resources.getConfigId() != null) {
            throw new BadRequestException("异常创建！");
        }
        if (pmConfigRepository.findByKeyword(resources.getKeyword()) != null) {
            throw new EntityExistException(PmConfig.class, "key", resources.getKeyword());
        }

        return pmConfigMapper.toDto(pmConfigRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(PmConfig resources) {
        PmConfig pmConfig = pmConfigRepository.findById(resources.getConfigId()).orElseGet(PmConfig::new);
//        if (pmConfig.getUserId() != resources.getUserId()) {
//            throw new BadRequestException("不能修改他人的配置！");
//        }
        ValidationUtil.isNull(pmConfig.getConfigId(), "PmConfig", "id", resources.getConfigId());
        PmConfig pmConfig1 = null;
        pmConfig1 = pmConfigRepository.findByKeyword(resources.getKeyword());
        if (pmConfig1 != null && !pmConfig1.getConfigId().equals(pmConfig.getConfigId())) {
            throw new EntityExistException(PmConfig.class, "key", resources.getKeyword());
        }

        pmConfig.copy(resources);
        pmConfigRepository.save(pmConfig);
    }

    @Override
    public void deleteAll(Long[] ids, Long userId) {
        for (Long configId : ids) {
//            //该配置不属于该用户
//            if (!pmConfigRepository.getOne(configId).getUserId().equals(userId)) {
//                throw new BadRequestException("不能删除他人配置！");
//            }
            pmConfigRepository.deleteById(configId);
        }
    }

    @Override
    public void download(List<PmConfigDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (PmConfigDto pmConfig : all) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put(" key", pmConfig.getKeyword());
            map.put(" collocation", pmConfig.getCollocation());
            map.put(" createTime", pmConfig.getCreateTime());
            map.put(" updateTime", pmConfig.getUpdateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}