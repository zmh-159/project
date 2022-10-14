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

import org.parallel.jpa.domain.PmNodeCpuState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

/**
 * @author yuyifade
 * @website https://el-admin.vip
 * @date 2021-06-18
 **/
public interface PmNodeCpuStateRepository extends JpaRepository<PmNodeCpuState, Long>, JpaSpecificationExecutor<PmNodeCpuState> {
    @Query(value = "SELECT AVG(temperature) temperature,AVG(cpu_system) cpu_system,AVG(cpu_user) cpu_user,AVG(cpu_idle) cpu_idle,Avg(frequency) frequency FROM (SELECT ps.temperature,pc.cpu_system,pc.cpu_user,pc.cpu_idle,pc.frequency FROM pm_node_cpu_state ps INNER JOIN pm_node_core_state pc ON pc.cpu_state_id = ps.cpu_state_id WHERE (pc.create_time BETWEEN ?3 AND ?4) AND pc.cpu_id = ?1  ORDER BY pc.create_time LIMIT ?2) AS node;", nativeQuery = true)
    Map<String, Object> getAvgAsc(Long cpuId, int size, String start, String end);

    @Query(value = "SELECT AVG(temperature) temperature,AVG(cpu_system) cpu_system,AVG(cpu_user) cpu_user,AVG(cpu_idle) cpu_idle,Avg(frequency) frequency FROM (SELECT ps.temperature,pc.cpu_system,pc.cpu_user,pc.cpu_idle,pc.frequency FROM pm_node_cpu_state ps INNER JOIN pm_node_core_state pc ON pc.cpu_state_id = ps.cpu_state_id WHERE (pc.create_time BETWEEN ?3 AND ?4) AND pc.cpu_id = ?1  ORDER BY pc.create_time desc LIMIT ?2) AS node;", nativeQuery = true)
    Map<String, Object> getAvgDes(Long cpuId, int size, String start, String end);

    @Query(value = "SELECT COUNT(*) FROM pm_node_cpu_state pc where pc.cpu_id=?1 AND (pc.create_time BETWEEN ?2 AND ?3)", nativeQuery = true)
    Map<String, Object> getNums(Long cpuId, String start, String end);
}