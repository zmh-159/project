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
package org.parallel.jpa.service.dto;

import lombok.Data;
import org.parallel.web.annotation.Query;

import java.sql.Timestamp;
import java.util.List;

/**
* @website https://el-admin.vip
 *  @description cpu静态数据查询条件
* @author yuyifade
* @date 2021-06-18
**/
@Data
public class PmNodeCpuQueryCriteria{

    /** 精确 */
    @Query
    private Long cpuId;

    /** 精确 */
    @Query
    private Long nodeId;

    /** 精确 */
    @Query
    private String cpuName;

    /** 精确 */
    @Query
    private Integer cores;

    /** 模糊 */
    @Query(type = Query.Type.INNER_LIKE)
    private String arch;

    /** 大于等于 */
    @Query(type = Query.Type.GREATER_THAN)
    private String l1ICache;

    /** 大于等于 */
    @Query(type = Query.Type.GREATER_THAN)
    private String l1DCache;

    /** 大于等于 */
    @Query(type = Query.Type.GREATER_THAN)
    private String l2Cache;

    /** 大于等于 */
    @Query(type = Query.Type.GREATER_THAN)
    private String l3Cache;

    /** 大于等于 */
    @Query(type = Query.Type.GREATER_THAN)
    private Integer mainFrequency;

    /** 大于等于 */
    @Query(type = Query.Type.GREATER_THAN)
    private Integer boostFrequency;
    /** BETWEEN */
    @Query(type = Query.Type.BETWEEN)
    private List<Timestamp> createTime;
}