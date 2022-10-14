package org.parallel.web.modules.pm.config.service.dto;/*
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

import lombok.Data;
import java.sql.Timestamp;
import java.io.Serializable;

/**
 * @website https://el-admin.vip
 * @description /
 * @author yuyifade
 * @date 2021-07-07
 **/
@Data
public class PmChartSchemeDto implements Serializable {

    /** 主键 */
    private Long chartSchemeId;

    /** 外键：关联用户表 */
    private Long userId;

    /** 描述 */
    private String description;

    /** 方案编号 */
    private Long serialNumber;

    /** 方案具体参数 */
    private String detail;

    /** 位置编号 */
    private Integer position;

    /** 创建日期 */
    private Timestamp createTime;

    /** 更新时间 */
    private Timestamp updateTime;
}