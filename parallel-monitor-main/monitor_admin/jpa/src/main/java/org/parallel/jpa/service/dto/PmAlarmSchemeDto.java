package org.parallel.jpa.service.dto;
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

import lombok.Data;
import java.sql.Timestamp;
import java.io.Serializable;

/**
 * @website https://el-admin.vip
 * @description /
 * @author yuyifade
 * @date 2021-11-11
 **/
@Data
public class PmAlarmSchemeDto implements Serializable {

    /** 主键 */
    private Integer alarmSchemeId;

    /** 方案名 */
    private String name;

    /** 配置 */
    private String detail;

    /** 生效节点 */
    private String effectNode;

    /** 状态 */
    private String status;

    /** 更新时间 */
    private Timestamp updateTime;

    /** 创建时间 */
    private Timestamp createTime;
}