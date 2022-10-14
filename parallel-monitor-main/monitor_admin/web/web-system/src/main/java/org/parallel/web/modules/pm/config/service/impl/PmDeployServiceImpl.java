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


import org.parallel.web.modules.pm.config.domain.PmDeploy;
import org.parallel.web.modules.pm.config.repository.PmDeployRepository;
import org.parallel.web.modules.pm.config.service.PmDeployService;
import org.parallel.web.modules.pm.config.service.dto.PmDeployDto;
import org.parallel.web.modules.pm.config.service.dto.PmDeployQueryCriteria;
import org.parallel.web.modules.pm.config.service.mapstruct.PmDeployMapper;
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
 * @date 2021-09-24
 **/
@Service
@RequiredArgsConstructor
public class PmDeployServiceImpl implements PmDeployService {

    private final PmDeployRepository pmDeployRepository;
    private final PmDeployMapper pmDeployMapper;

    @Override
    public Map<String, Object> queryAll(PmDeployQueryCriteria criteria, Pageable pageable) {
        Page<PmDeploy> page = pmDeployRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
        return PageUtil.toPage(page.map(pmDeployMapper::toDto));
    }

    @Override
    public List<PmDeployDto> queryAll(PmDeployQueryCriteria criteria) {
        return pmDeployMapper.toDto(pmDeployRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder)));
    }

    @Override
    @Transactional
    public PmDeployDto findById(Long deployId) {
        PmDeploy pmDeploy = pmDeployRepository.findById(deployId).orElseGet(PmDeploy::new);
        ValidationUtil.isNull(pmDeploy.getDeployId(), "PmDeploy", "deployId", deployId);
        return pmDeployMapper.toDto(pmDeploy);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PmDeployDto create(PmDeploy resources) {
        return pmDeployMapper.toDto(pmDeployRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(PmDeploy resources) {
        PmDeploy pmDeploy = pmDeployRepository.findById(resources.getDeployId()).orElseGet(PmDeploy::new);
        ValidationUtil.isNull(pmDeploy.getDeployId(), "PmDeploy", "id", resources.getDeployId());
        pmDeploy.copy(resources);
        pmDeployRepository.save(pmDeploy);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long deployId : ids) {
            pmDeployRepository.deleteById(deployId);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void installRecord(long deploy_id, long node_id) {
        pmDeployRepository.addDeployNode(deploy_id, node_id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteRecord(long deploy_id, long node_id) {
        pmDeployRepository.deleteDeployNode(deploy_id, node_id);
    }


    @Override
    public void download(List<PmDeployDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (PmDeployDto pmDeploy : all) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("软件包名字", pmDeploy.getName());
            map.put("版本", pmDeploy.getVersion());
            map.put("操作系统", pmDeploy.getOs());
            map.put("安装脚本", pmDeploy.getInstallShell());
            map.put("卸载脚本", pmDeploy.getDeleteShell());
            map.put("创建时间", pmDeploy.getCreateTime());
            map.put("修改时间", pmDeploy.getUpdateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}