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

import com.alibaba.fastjson.JSONObject;
import org.parallel.web.modules.bmc.jpa.domain.PbNodeCpu;
import org.parallel.web.modules.bmc.jpa.domain.PbNodeMemory;
import org.parallel.web.modules.bmc.kp.MemoryInfo;
import org.parallel.web.utils.ValidationUtil;
import org.parallel.web.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import org.parallel.web.modules.bmc.jpa.repository.PbNodeMemoryRepository;
import org.parallel.web.modules.bmc.jpa.service.PbNodeMemoryService;
import org.parallel.web.modules.bmc.jpa.service.dto.PbNodeMemoryDto;
import org.parallel.web.modules.bmc.jpa.service.dto.PbNodeMemoryQueryCriteria;
import org.parallel.web.modules.bmc.jpa.service.mapstruct.PbNodeMemoryMapper;
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
 * @author ruo-yin
 * @website https://el-admin.vip
 * @description 服务实现
 * @date 2022-08-09
 **/
@Service
@RequiredArgsConstructor
public class PbNodeMemoryServiceImpl implements PbNodeMemoryService {

    private final PbNodeMemoryRepository pbNodeMemoryRepository;
    private final PbNodeMemoryMapper pbNodeMemoryMapper;
    private final MemoryInfo memoryInfo;

    @Override
    public Map<String, Object> queryAll(PbNodeMemoryQueryCriteria criteria, Pageable pageable) {
        Page<PbNodeMemory> page = pbNodeMemoryRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
        return PageUtil.toPage(page.map(pbNodeMemoryMapper::toDto));
    }

    @Override
    public List<PbNodeMemoryDto> queryAll(PbNodeMemoryQueryCriteria criteria) {
        return pbNodeMemoryMapper.toDto(pbNodeMemoryRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder)));
    }

    @Override
    @Transactional
    public PbNodeMemoryDto findById(Long memoryId) {
        PbNodeMemory pbNodeMemory = pbNodeMemoryRepository.findById(memoryId).orElseGet(PbNodeMemory::new);
        ValidationUtil.isNull(pbNodeMemory.getMemoryId(), "PbNodeMemory", "memoryId", memoryId);
        return pbNodeMemoryMapper.toDto(pbNodeMemory);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PbNodeMemoryDto create(PbNodeMemory resources) {
        return pbNodeMemoryMapper.toDto(pbNodeMemoryRepository.save(resources));
    }

    @Override
    public void createMemory(Long nodeId) {
        JSONObject object = memoryInfo.getMemory(nodeId);
        object.keySet().forEach(key -> {
            PbNodeMemory resources = new PbNodeMemory();
            JSONObject json = object.getJSONObject(key);
            resources.setAllInfo(json.getString("allInfo"));
            resources.setManufacturer(json.getString("manufacturer"));
            resources.setType(json.getString("type"));
            resources.setSpeed(json.getString("speed"));
            resources.setCapacity(json.getString("capacity"));

            create(resources);
        });
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(PbNodeMemory resources) {
        PbNodeMemory pbNodeMemory = pbNodeMemoryRepository.findById(resources.getMemoryId()).orElseGet(PbNodeMemory::new);
        ValidationUtil.isNull(pbNodeMemory.getMemoryId(), "PbNodeMemory", "id", resources.getMemoryId());
        pbNodeMemory.copy(resources);
        pbNodeMemoryRepository.save(pbNodeMemory);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long memoryId : ids) {
            pbNodeMemoryRepository.deleteById(memoryId);
        }
    }

    @Override
    public void download(List<PbNodeMemoryDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (PbNodeMemoryDto pbNodeMemory : all) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("外键：表pm_node", pbNodeMemory.getNodeId());
            map.put("制造商", pbNodeMemory.getManufacturer());
            map.put("内存规格", pbNodeMemory.getType());
            map.put("速度", pbNodeMemory.getSpeed());
            map.put("容量", pbNodeMemory.getCapacity());
            map.put("健康状态", pbNodeMemory.getHealth());
            map.put("内存资源所属CPU槽位号", pbNodeMemory.getSocket());
            map.put("内存资源的槽位号", pbNodeMemory.getSlot());
            map.put("剩余使用寿命百分比", pbNodeMemory.getRemainingServiceLife());
            map.put("内存资源的控制器温度", pbNodeMemory.getControllerTemp());
            map.put("内存资源的介质温度", pbNodeMemory.getMediumTemp());
            map.put("所有信息", pbNodeMemory.getAllInfo());
            map.put("创建时间", pbNodeMemory.getCreateTime());
            map.put("修改时间", pbNodeMemory.getUpdateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}