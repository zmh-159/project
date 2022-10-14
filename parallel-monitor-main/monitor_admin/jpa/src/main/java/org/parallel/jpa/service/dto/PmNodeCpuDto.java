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

import java.io.Serializable;
import java.sql.Timestamp;

/**
* @website https://el-admin.vip
* @description /
* @author yuyifade
* @date 2021-06-18
**/
@Data
public class PmNodeCpuDto implements Serializable {

    /** 主键 */
    private Long cpuId;

    /** 外键：表pm_node */
    private Long nodeId;

    /** 节点数据 **/
    private PmNodeDto node;

    /** CPU型号 */
    private String cpuName;

    /** 核心数 */
    private Integer cores;

    /** 线程数 */
    private Integer threads;

    /** 架构 */
    private String arch;

    /** 一级指令缓存 */
    private String l1ICache;

    /** 一级数据缓存 */
    private String l1DCache;

    /** 二级缓存 */
    private String l2Cache;

    /** 三级缓存 */
    private String l3Cache;

    /** 主频 */
    private String mainFrequency;

    /** 睿频 */
    private String boostFrequency;

    /** 创建时间 */
    private Timestamp createTime;

    /** 修改时间 */
    private Timestamp updateTime;
}