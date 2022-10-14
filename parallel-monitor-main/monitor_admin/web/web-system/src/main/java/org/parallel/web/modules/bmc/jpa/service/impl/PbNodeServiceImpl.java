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

import org.parallel.web.modules.bmc.jpa.domain.PbNode;
import org.parallel.web.exception.EntityExistException;
import org.parallel.web.utils.ValidationUtil;
import org.parallel.web.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import org.parallel.web.modules.bmc.jpa.repository.PbNodeRepository;
import org.parallel.web.modules.bmc.jpa.service.PbNodeService;
import org.parallel.web.modules.bmc.jpa.service.dto.PbNodeDto;
import org.parallel.web.modules.bmc.jpa.service.dto.PbNodeQueryCriteria;
import org.parallel.web.modules.bmc.jpa.service.mapstruct.PbNodeMapper;
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
public class PbNodeServiceImpl implements PbNodeService {

    private final PbNodeRepository pbNodeRepository;
    private final PbNodeMapper pbNodeMapper;

    @Override
    public Map<String,Object> queryAll(PbNodeQueryCriteria criteria, Pageable pageable){
        Page<PbNode> page = pbNodeRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(pbNodeMapper::toDto));
    }

    @Override
    public List<PbNodeDto> queryAll(PbNodeQueryCriteria criteria){
        return pbNodeMapper.toDto(pbNodeRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public PbNodeDto findById(Long nodeId) {
        PbNode pbNode = pbNodeRepository.findById(nodeId).orElseGet(PbNode::new);
        ValidationUtil.isNull(pbNode.getNodeId(),"PbNode","nodeId",nodeId);
        return pbNodeMapper.toDto(pbNode);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PbNodeDto create(PbNode resources) {
        return pbNodeMapper.toDto(pbNodeRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PbNode update(PbNode resources) {
        PbNode pbNode = pbNodeRepository.findById(resources.getNodeId()).orElseGet(PbNode::new);
        ValidationUtil.isNull( pbNode.getNodeId(),"PbNode","id",resources.getNodeId());
        PbNode pbNode1 = null;
        pbNode1 = pbNodeRepository.findByUuid(resources.getUuid());
        if(pbNode1 != null && !pbNode1.getNodeId().equals(pbNode.getNodeId())){
            throw new EntityExistException(PbNode.class,"uuid",resources.getUuid());
        }
        pbNode.copy(resources);
        return pbNodeRepository.save(pbNode);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long nodeId : ids) {
            pbNodeRepository.deleteById(nodeId);
        }
    }

    @Override
    public void download(List<PbNodeDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (PbNodeDto pbNode : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("标识符", pbNode.getUuid());
            map.put("用户名", pbNode.getUser());
            map.put("密码", pbNode.getPasswd());
            map.put("bmc的ip地址", pbNode.getBmcIp());
            map.put("cpu数量", pbNode.getCpuNums());
            map.put("gpu数量", pbNode.getGpuNums());
            map.put("最大扩展的内存容量", pbNode.getMaxExtendMemory());
            map.put("内存", pbNode.getMemory());
            map.put("内存插槽", pbNode.getMemorySlot());
            map.put("可用磁盘", pbNode.getDiskTotal());
            map.put("备注", pbNode.getRemark());
            map.put("序号", pbNode.getSerialNumber());
            map.put("节点类型", pbNode.getNodeType());
            map.put("所有信息", pbNode.getAllInfo());
            map.put("创建时间", pbNode.getCreateTime());
            map.put("修改时间", pbNode.getUpdateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}