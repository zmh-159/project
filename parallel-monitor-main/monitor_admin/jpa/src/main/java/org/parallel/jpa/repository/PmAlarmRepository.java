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

import org.parallel.jpa.domain.PmAlarm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

/**
 * @website https://el-admin.vip
 * @author yuyifade
 * @date 2021-11-11
 **/
public interface PmAlarmRepository extends JpaRepository<PmAlarm, Long>, JpaSpecificationExecutor<PmAlarm> {
    /***
     * @description  查询未读
     * @author yuyifade
     * @date 2021/9/10 上午11:22
     * @return java.lang.Long
     */
    @Query(value = "SELECT COUNT(*) FROM pm_alarm WHERE `status` = '0'", nativeQuery = true)
    Long getUnreadAlarmCount();

    /***
     * @description  查询已读
     * @author yuyifade
     * @date 2022/1/10 上午11:22
     * @return java.lang.Long
     */
    @Query(value = "SELECT COUNT(*) FROM pm_alarm WHERE `status` = '1'", nativeQuery = true)
    Long getReadAlarmCount();

    @Query(value = "SELECT COUNT(*) FROM (SELECT COUNT(node_id) FROM pm_alarm WHERE `status`=1  GROUP BY node_id) as table2", nativeQuery = true)
    Long getAlarmNodeCount();
}