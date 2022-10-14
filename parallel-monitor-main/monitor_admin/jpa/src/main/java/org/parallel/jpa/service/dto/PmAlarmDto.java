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
import java.sql.Timestamp;
import java.io.Serializable;
import java.util.List;

/**
 * @author yuyifade
 * @website https://el-admin.vip
 * @description /
 * @date 2021-11-11
 **/
@Data
public class PmAlarmDto implements Serializable {

    /** 主键 */
    private Long alarmId;

    /** 外键 */
    private Long nodeId;

    /** 外键 */
    private Long nodeStateId;

    /** 报警详情 */
    private List<PmAlarmDetailDto> alarmDetails;

    /** 报警项 */
    private String alarmOption;

    /** 报警方式 */
    private Integer alarmType;

    /** 阈值 */
    private Double threshold;

    /** 是否已读 */
    private Integer status;

    /** 备注 */
    private String remark;

    /** 创建时间 */
    private Timestamp createTime;

    /** 更新时间 */
    private Timestamp updateTime;
}