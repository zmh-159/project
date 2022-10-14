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
package org.parallel.jpa.repository;

import org.parallel.jpa.domain.PmNode;
import org.parallel.jpa.domain.PmNodeState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @author yuyifade
 * @website https://el-admin.vip
 * @date 2021-06-18
 **/
public interface PmNodeStateRepository extends JpaRepository<PmNodeState, Long>, JpaSpecificationExecutor<PmNodeState> {
    /**
     * 根据 node Id 删除
     *
     * @param nodeId
     * @return void
     */
    @Transactional
    void deleteByNodeId(Long nodeId);

    @Query(value = "select DATE_FORMAT(create_time,'%Y%m%d') days,avg(cpu_idle) cpu_idle,avg(memory_rate) memory_rate,avg(disk_rate) disk_rate,avg(gpu_utilization) gpu_utilization from pm_node_state where (create_time between ?1 and ?2) group by days;", nativeQuery = true)
    List<Object[]> getAvg(String start, String end);
}