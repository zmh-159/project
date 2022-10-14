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
public class PmNodeDto implements Serializable {

    /** 主键 */
    private Long nodeId;

    /** 标识符 */
    private String uuid;

    /** 备注 */
    private String remark;

    /** cpu数量 */
    private Integer cpuNums;

    /** gpu数量 */
    private Integer gpuNums;

    /** 算力 **/
    private String performance;

    /** ip地址 */
    private String ip;

    /** 最大支持的内存容量 */
    private String maxExtendMemory;

    /** 内存 **/
    private String memory;

    /** 内存插槽 */
    private Integer memorySlot;

    /** 操作系统 */
    private String osName;

    /** 系统版本 */
    private String osVersion;

    /** 位数 */
    private Integer osBit;

    /** 主机名 */
    private String hostName;

    /** 可用磁盘 */
    private String diskTotal;

    /** 序号 */
    private Long serialNumber;

    /** 开机时间 */
    private Timestamp startTime;

    /** 创建时间 */
    private Timestamp createTime;

    /** 修改时间 */
    private Timestamp updateTime;
}