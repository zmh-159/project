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
public class PbNodeBiosDto implements Serializable {

    /** 主键 */
    private Long biosId;

    /** 外键：表pm_node */
    private Long nodeId;

    /** BIOS名 */
    private String name;

    /** 快速启动模式 */
    private String quickBoot;

    /** 静态启动模式 */
    private String quietBoot;

    /** UEFI的PXE功能 */
    private String pxeUefi;

    /** legacy的PXE功能 */
    private String pxeLegacy;

    /** 能效模式 */
    private String customPowerPolicy;

    /** cpu动态加速开关 */
    private String turboMode;

    /** 超线程设置 */
    private String proHyperThread;

    /** 所有信息 */
    private String allInfo;

    /** 创建时间 */
    private Timestamp createTime;

    /** 编辑时间 */
    private Timestamp updateTime;
}