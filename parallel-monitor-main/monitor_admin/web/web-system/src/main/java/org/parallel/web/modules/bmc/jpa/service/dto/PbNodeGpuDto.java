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
public class PbNodeGpuDto implements Serializable {

    /** 主键 */
    private Long gpuId;

    /** 外键：表pm_node */
    private Long nodeId;

    /** 显卡uuid */
    private String uuid;

    /** 显卡名 */
    private String name;

    /** 制造商 */
    private String manufacturer;

    /** 固件版本 */
    private String firmwareVersion;

    /** 槽位号 */
    private Integer slotNumber;

    /** 所有信息 */
    private String allInfo;

    /** 创建时间 */
    private Timestamp createTime;

    /** 修改时间 */
    private Timestamp updateTime;
}