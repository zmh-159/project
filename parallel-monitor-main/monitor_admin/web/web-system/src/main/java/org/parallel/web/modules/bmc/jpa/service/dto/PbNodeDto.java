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
import java.io.Serializable;

/**
* @website https://el-admin.vip
* @description /
* @author ruo-yin
* @date 2022-08-09
**/
@Data
public class PbNodeDto implements Serializable {

    /** 主键 */
    private Long nodeId;

    /** 标识符 */
    private String uuid;

    /** 用户名 */
    private String user;

    /** 密码 */
    private String passwd;

    /** bmc的ip地址 */
    private String bmcIp;

    /** cpu数量 */
    private Integer cpuNums;

    /** gpu数量 */
    private Integer gpuNums;

    /** 最大扩展的内存容量 */
    private String maxExtendMemory;

    /** 内存 */
    private String memory;

    /** 内存插槽 */
    private Integer memorySlot;

    /** 可用磁盘 */
    private String diskTotal;

    /** 备注 */
    private String remark;

    /** 序号 */
    private Long serialNumber;

    /** 节点类型 */
    private String nodeType;

    /** 所有信息 */
    private String allInfo;

    /** 创建时间 */
    private Timestamp createTime;

    /** 修改时间 */
    private Timestamp updateTime;
}