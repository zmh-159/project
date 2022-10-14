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
package org.parallel.web.modules.bmc.jpa.service.dto;

import lombok.Data;
import java.sql.Timestamp;
import java.math.BigDecimal;
import java.io.Serializable;

/**
* @website https://el-admin.vip
* @description /
* @author ruo-yin
* @date 2022-08-09
**/
@Data
public class PbNodeMemoryDto implements Serializable {

    /** 主键 */
    private Long memoryId;

    /** 外键：表pm_node */
    private Long nodeId;

    /** 制造商 */
    private String manufacturer;

    /** 内存规格 */
    private String type;

    /** 速度 */
    private String speed;

    /** 容量 */
    private String capacity;

    /** 健康状态 */
    private String health;

    /** 内存资源所属CPU槽位号 */
    private String socket;

    /** 内存资源的槽位号 */
    private String slot;

    /** 剩余使用寿命百分比 */
    private BigDecimal remainingServiceLife;

    /** 内存资源的控制器温度 */
    private BigDecimal controllerTemp;

    /** 内存资源的介质温度 */
    private BigDecimal mediumTemp;

    /** 所有信息 */
    private String allInfo;

    /** 创建时间 */
    private Timestamp createTime;

    /** 修改时间 */
    private Timestamp updateTime;
}