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
package org.parallel.web.modules.pm.config.repository;


import org.parallel.web.modules.pm.config.domain.PmDeploy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * @author yuyifade
 * @website https://el-admin.vip
 * @date 2021-09-24
 **/
public interface PmDeployRepository extends JpaRepository<PmDeploy, Long>, JpaSpecificationExecutor<PmDeploy> {
    @Modifying
    @Query(value = "INSERT INTO pm_deploy_node (deploy_id,node_id) VALUES (?1,?2)", nativeQuery = true)
    void addDeployNode(Long deployId, Long nodeId);

    @Modifying
    @Query(value = "DELETE FROM pm_deploy_node WHERE deploy_id=?1 AND node_id=?2", nativeQuery = true)
    void deleteDeployNode(Long deployId, Long nodeId);
}