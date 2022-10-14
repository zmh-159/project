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

import com.alibaba.fastjson.JSONObject;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.parallel.common.utils.RestTemplateClient;
import org.parallel.jpa.bean.PmConnect;
import org.parallel.jpa.domain.PmAlarm;
import org.parallel.jpa.repository.PmNodeStateRepository;
import org.parallel.jpa.service.PmAlarmService;
import org.parallel.web.exception.EntityExistException;
import org.parallel.jpa.domain.PmNode;
import org.parallel.jpa.repository.PmNodeRepository;
import org.parallel.jpa.service.PmNodeService;
import org.parallel.jpa.service.dto.PmNodeDetailDto;
import org.parallel.jpa.service.dto.PmNodeDto;
import org.parallel.jpa.service.dto.PmNodeQueryCriteria;
import org.parallel.jpa.service.mapstruct.PmNodeDetailMapper;
import org.parallel.jpa.service.mapstruct.PmNodeMapper;
import org.parallel.web.utils.FileUtil;
import org.parallel.web.utils.PageUtil;
import org.parallel.web.utils.QueryHelp;
import org.parallel.web.utils.ValidationUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

/**
 * @author yuyifade
 * @website https://el-admin.vip
 * @description 服务实现
 * @date 2021-06-18
 **/
@Service
@RequiredArgsConstructor
public class PmNodeServiceImpl implements PmNodeService {

    private final PmNodeRepository pmNodeRepository;
    private final PmNodeStateRepository pmNodeStateRepository;
    private final PmNodeMapper pmNodeMapper;
    private final PmNodeDetailMapper pmNodeDetailMapper;
    private final EntityManager em;
    private final PmConnect pmConnect;
    private final PmAlarmService pmAlarmService;
    private final RestTemplateClient rtc;
    //todo 通过config设置url
    private static final String url = "http://127.0.0.1:9001/api/realInfo";


    @Override
    public Long getMaxSerialNumber() {
        return pmNodeRepository.getMaxSerialNumber();
    }

    @Override
    public Map<String, Object> queryAll(PmNodeQueryCriteria criteria, Pageable pageable) {
        Page<PmNode> page = pmNodeRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
        return PageUtil.toPage(page.map(pmNodeMapper::toDto));
    }

    @Override
    public long queryNodeCount() {
        return pmNodeRepository.count();
    }

    @Override
    public List<PmNodeDto> queryAll(PmNodeQueryCriteria criteria) {
        return pmNodeMapper.toDto(pmNodeRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder)));
    }

    @Override
    public JSONObject querySortByAlive(String aliveNodes, int start, int num) {
        String sql;
        if (aliveNodes.length() > 0) {
            sql = "SELECT * FROM pm_node ORDER BY CASE WHEN node_id IN " + aliveNodes + " THEN 0 ELSE 1 END,node_id DESC LIMIT " + start + "," + num;
        } else {
            sql = "SELECT * FROM pm_node ORDER BY node_id DESC LIMIT " + start + "," + num;
        }
        Query query = em.createNativeQuery(sql, PmNode.class);
        List<PmNode> res = query.getResultList();
        JSONObject jo = new JSONObject();
        jo.put("content", pmNodeMapper.toDto(res));
        jo.put("totalElements", pmNodeRepository.count());
        return jo;
    }


    @Override
    public List<PmNodeDetailDto> queryDetail(PmNodeQueryCriteria criteria) {
        return pmNodeDetailMapper.toDto(pmNodeRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder)));
    }

    @SneakyThrows
    @Override
    public JSONObject getOnlineCpus(String aliveNodes) {
        JSONObject res = new JSONObject();
        if (aliveNodes.length() > 0) {
            Connection connection = pmConnect.getCon();
            if (connection != null) {
                String sql = "SELECT node_id,cpu_id,threads FROM pm_node_cpu WHERE node_id IN " + aliveNodes;
                Statement statement = connection.createStatement();
                ResultSet cpus = statement.executeQuery(sql);
                while (cpus.next()) {
                    long nodeId = cpus.getLong(1);
                    if (res.containsKey(nodeId)) {
                        JSONObject cpu = res.getJSONObject(nodeId + "");
                        cpu.put("" + cpu.size(), cpus.getInt(3));
                    } else {
                        JSONObject cpu = new JSONObject();
                        cpu.put("0", cpus.getInt(3));
                        res.put(nodeId + "", cpu);
                    }
                }
                statement.close();
                connection.close();
            }
        }
        return res;
    }

    @SneakyThrows
    @Override
    public JSONObject getOnlineDisk(String aliveNodes) {
        JSONObject res = new JSONObject();
        if (aliveNodes.length() > 0) {
            Connection connection = pmConnect.getCon();
            if (connection != null) {
                String sql = "SELECT node_id,disk_id,name FROM pm_node_disk WHERE node_id IN " + aliveNodes;
                Statement statement = connection.createStatement();
                ResultSet disks = statement.executeQuery(sql);
                while (disks.next()) {
                    long nodeId = disks.getLong(1);
                    if (res.containsKey(nodeId)) {
                        JSONObject disk = res.getJSONObject(nodeId + "");
                        disk.put("" + disk.size(), disks.getString(3));
                    } else {
                        JSONObject disk = new JSONObject();
                        disk.put("0", disks.getString(3));
                        res.put(nodeId + "", disk);
                    }
                }
                statement.close();
                connection.close();
            }
        }
        return res;
    }

    @Override
    public JSONObject getRealTimeInfo() {
        JSONObject res = rtc.send(url);
        res.put("node_sum", queryNodeCount());
        res.put("alarm_un_read_sum", pmAlarmService.queryUnreadAlarmCount());
        res.put("alarm_read_sum", pmAlarmService.queryReadAlarmCount());
        res.put("alarm_node_sum", pmAlarmService.queryAlarmNodeCount());
        int nodeOnline = 0;
        if (res.containsKey("node_data")) {
            nodeOnline = res.getJSONObject("node_data").size();
        }
        res.put("node_online", nodeOnline);
        return res;
    }

    @Override
    @Transactional
    public PmNodeDto findById(Long nodeId) {
        PmNode pmNode = pmNodeRepository.findById(nodeId).orElseGet(PmNode::new);
        ValidationUtil.isNull(pmNode.getNodeId(), "PmNode", "nodeId", nodeId);
        return pmNodeMapper.toDto(pmNode);
    }

    @Override
    @Transactional
    public PmNodeDto findByUuid(String uuid) {
        PmNode pmNode = pmNodeRepository.findByUuid(uuid);
        if (pmNode == null) {
            return null;
        } else {
            return pmNodeMapper.toDto(pmNode);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PmNodeDto create(PmNode resources) {
        if (pmNodeRepository.findByUuid(resources.getUuid()) != null) {
            throw new EntityExistException(PmNode.class, "uuid", resources.getUuid());
        }
        return pmNodeMapper.toDto(pmNodeRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)//添加事物
    public PmNode update(PmNode resources) {
        PmNode pmNode = pmNodeRepository.findById(resources.getNodeId()).orElseGet(PmNode::new);
        ValidationUtil.isNull(pmNode.getNodeId(), "PmNode", "id", resources.getNodeId());
        PmNode pmNode1 = null;
        pmNode1 = pmNodeRepository.findByUuid(resources.getUuid());
        if (pmNode1 != null && !pmNode1.getNodeId().equals(pmNode.getNodeId())) {
            throw new EntityExistException(PmNode.class, "uuid", resources.getUuid());
        }
        pmNode.copy(resources);
        return pmNodeRepository.save(pmNode);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long nodeId : ids) {
            pmNodeRepository.deleteById(nodeId);
            pmNodeStateRepository.deleteByNodeId(nodeId);
        }
    }

    @Override
    public void download(List<PmNodeDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (PmNodeDto pmNode : all) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("标识符", pmNode.getUuid());
            map.put("备注", pmNode.getRemark());
            map.put("序号", pmNode.getSerialNumber());
            map.put("主机名", pmNode.getHostName());
            map.put("ip地址", pmNode.getIp());
            map.put("操作系统", pmNode.getOsName());
            map.put("系统版本", pmNode.getOsVersion());
            map.put("位数", pmNode.getOsBit());
            map.put("算力", pmNode.getPerformance());
            map.put("cpu个数", pmNode.getCpuNums());
            map.put("gpu个数", pmNode.getGpuNums());
            map.put("最大扩展内存容量", pmNode.getMaxExtendMemory());
            map.put("内存", pmNode.getMemory());
            map.put("内存插槽", pmNode.getMemorySlot());
            map.put("磁盘容量", pmNode.getDiskTotal());
            map.put("开机时间", pmNode.getStartTime());
            map.put("创建时间", pmNode.getCreateTime());
            map.put("修改时间", pmNode.getUpdateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
    }
