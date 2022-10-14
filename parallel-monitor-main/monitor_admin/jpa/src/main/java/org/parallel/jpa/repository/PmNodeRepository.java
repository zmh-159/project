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
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

/**
 * @author yuyifade
 * @website https://el-admin.vip
 * @date 2021-06-18
 **/
public interface PmNodeRepository extends JpaRepository<PmNode, Long>, JpaSpecificationExecutor<PmNode> {
    /**
     * 根据 Uuid 查询
     * @param uuid /
     * @return /
     */
    PmNode findByUuid(String uuid);

    /***
      * @description  查询最大序号
      * @author yuyifade
      * @date 2021/9/10 上午11:22
      * @return java.lang.Long
      */
    @Query(value = "SELECT MAX(serial_number) FROM pm_node", nativeQuery = true)
    Long getMaxSerialNumber();

}