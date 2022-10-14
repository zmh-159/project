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
package org.parallel.web.modules.pm.config.service.dto;

import lombok.Data;
import org.parallel.jpa.service.dto.PmNodeSimpleDto;

import java.sql.Timestamp;
import java.io.Serializable;
import java.util.Set;

/**
 * @author yuyifade
 * @website https://el-admin.vip
 * @description /
 * @date 2021-09-24
 **/
@Data
public class PmDeployDto implements Serializable {

    /** 主键 */
    private Long deployId;

    /** 软件包名字 */
    private String name;

    private Set<PmNodeSimpleDto> pm;

    /** 版本 */
    private String version;

    /** 操作系统 */
    private String os;

    /** 安装脚本 */
    private String installShell;

    /** 卸载脚本 */
    private String deleteShell;

    /** 创建时间 */
    private Timestamp createTime;

    /** 修改时间 */
    private Timestamp updateTime;
}