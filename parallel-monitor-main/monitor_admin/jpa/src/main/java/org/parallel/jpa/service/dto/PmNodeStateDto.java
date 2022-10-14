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
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * @website https://el-admin.vip
 * @description /
 * @author yuyifade
 * @date 2021-06-18
 **/
@Data
public class PmNodeStateDto implements Serializable {

    /** 主键 */
    private Long nodeStateId;

    /** 外键：表pm_node */
    private Long nodeId;

    /** 进程数 */
    private Integer processNums;

    /** cpu占用率 */
    private BigDecimal cpuIdle;

    /** cpu系统占用 */
    private BigDecimal cpuSystem;

    /** cpu用户占用 */
    private BigDecimal cpuUser;

    /** 内存 */
    private Double memoryTotal;

    /** 内存使用 */
    private Double memoryUsed;

    /** 内存使用率 */
    private BigDecimal memoryRate;

    /** 交换区 */
    private Double swapTotal;

    /** 交换区使用 */
    private Double swapUsed;

    /** 交换区使用率 */
    private BigDecimal swapRate;

    /** 磁盘 */
    private Double diskTotal;

    /** 磁盘使用 */
    private Double diskUsed;

    /** 磁盘使用率 */
    private BigDecimal diskRate;

    /** 磁盘使用率 */
    private BigDecimal gpuUtilization;

    /** 磁盘使用率 */
    private BigDecimal memoryUtilization;

    /** 报警状态 */
    private String alarmState;

    /** 采集时间 */
    private Timestamp createTime;

    /** 修改时间 */
    private Timestamp updateTime;
}