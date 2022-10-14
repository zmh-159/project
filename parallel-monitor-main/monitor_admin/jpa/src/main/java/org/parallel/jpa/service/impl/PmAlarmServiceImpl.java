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

import org.parallel.jpa.domain.PmAlarm;
import org.parallel.jpa.repository.PmAlarmRepository;
import org.parallel.jpa.service.PmAlarmService;
import org.parallel.jpa.service.dto.PmAlarmDto;
import org.parallel.jpa.service.dto.PmAlarmQueryCriteria;
import org.parallel.jpa.service.mapstruct.PmAlarmMapper;
import org.parallel.web.utils.ValidationUtil;
import org.parallel.web.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
 * @date 2021-11-11
 **/
@Service
@RequiredArgsConstructor
public class PmAlarmServiceImpl implements PmAlarmService {

    private final PmAlarmRepository pmAlarmRepository;
    private final PmAlarmMapper pmAlarmMapper;

    @Override
    public Map<String, Object> queryAll(PmAlarmQueryCriteria criteria, Pageable pageable) {
        Page<PmAlarm> page = pmAlarmRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
        return PageUtil.toPage(page.map(pmAlarmMapper::toDto));
    }

    @Override
    public List<PmAlarmDto> queryAll(PmAlarmQueryCriteria criteria) {
        return pmAlarmMapper.toDto(pmAlarmRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder)));
    }

    @Override
    @Transactional
    public PmAlarmDto findById(Long alarmId) {
        PmAlarm pmAlarm = pmAlarmRepository.findById(alarmId).orElseGet(PmAlarm::new);
        ValidationUtil.isNull(pmAlarm.getAlarmId(), "PmAlarm", "alarmId", alarmId);
        return pmAlarmMapper.toDto(pmAlarm);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PmAlarmDto create(PmAlarm resources) {
        return pmAlarmMapper.toDto(pmAlarmRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean update(long alarmId, String remark) {
        try {
            PmAlarm pmAlarm = pmAlarmRepository.getOne(alarmId);
            pmAlarm.setRemark(remark);
            pmAlarm.setStatus(1);
            pmAlarmRepository.save(pmAlarm);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public long queryUnreadAlarmCount() {
        return pmAlarmRepository.getUnreadAlarmCount();
    }

    @Override
    public long queryReadAlarmCount() {
        return pmAlarmRepository.getReadAlarmCount();
    }

    @Override
    public long queryAlarmNodeCount() {
        return pmAlarmRepository.getAlarmNodeCount();
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long alarmId : ids) {
            pmAlarmRepository.deleteById(alarmId);
        }
    }

    @Override
    public void download(List<PmAlarmDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (PmAlarmDto pmAlarm : all) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("外键1", pmAlarm.getAlarmId());
            map.put("外键2", pmAlarm.getNodeId());
            map.put("报警项", pmAlarm.getAlarmOption());
            map.put("阈值", pmAlarm.getThreshold());
            map.put("是否已读", pmAlarm.getStatus());
            map.put("创建时间", pmAlarm.getCreateTime());
            map.put("更新时间", pmAlarm.getUpdateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}