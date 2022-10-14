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
package org.parallel.web.modules.bmc.jpa.repository;

import org.parallel.web.modules.bmc.jpa.domain.PbNode;
import org.parallel.web.modules.bmc.jpa.service.dto.PbNodeDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author ruo-yin
 * @website https://el-admin.vip
 * @date 2022-08-09
 **/
public interface PbNodeRepository extends JpaRepository<PbNode, Long>, JpaSpecificationExecutor<PbNode> {
    /**
     * 根据 Uuid 查询
     *
     * @param uuid /
     * @return /
     */
    PbNode findByUuid(String uuid);

    @Query(value = "SELECT node_id FROM pb_node WHERE bmc_ip=?1", nativeQuery = true)
    Long getNodeIdByIp(String ip);

    @Query(value = "SELECT node_id FROM pb_node WHERE bmc_uuid=?1", nativeQuery = true)
    Long getUuidByIp(String ip);

    @Query(value = "SELECT node_id,node_type FROM pb_node", nativeQuery = true)
    List<Map<String, String>> getNodeId();


    @Query(value = "SELECT MAX(serial_number) FROM pb_node", nativeQuery = true)
    Long getMaxSerialNumber();

    @Query(value = "SELECT * FROM pb_node", nativeQuery = true)
    List<PbNode> getNodeAll();



}