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
package org.parallel.jpa.domain;

import lombok.Data;
import cn.hutool.core.bean.BeanUtil;
import io.swagger.annotations.ApiModelProperty;
import cn.hutool.core.bean.copier.CopyOptions;
import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.validation.constraints.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import org.hibernate.annotations.*;
import java.sql.Timestamp;
import java.io.Serializable;
import java.util.List;

/**
 * @website https://el-admin.vip
 * @description /
 * @author yuyifade
 * @date 2021-11-11
 **/
@Entity
@Data
@Table(name="pm_alarm")
public class PmAlarm implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "alarm_id")
    @ApiModelProperty(value = "主键")
    private Long alarmId;

    @Column(name = "node_id",nullable = false)
    @NotNull
    @ApiModelProperty(value = "外键")
    private Long nodeId;

    @Column(name = "node_state_id",nullable = false)
    @NotNull
    @ApiModelProperty(value = "外键")
    private Long nodeStateId;

    //关联报警详情
    @OneToMany(cascade = {CascadeType.REMOVE})
    @JoinColumn(name = "alarm_id",updatable = false)
    private List<PmAlarmDetail> alarmDetails;

    @Column(name = "alarm_option",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "报警项")
    private String alarmOption;

    @Column(name = "alarm_type",nullable = false)
    @NotNull
    @ApiModelProperty(value = "报警方式")
    private Integer alarmType;

    @Column(name = "threshold",nullable = false)
    @NotNull
    @ApiModelProperty(value = "阈值")
    private Double threshold;

    @Column(name = "remark",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "备注")
    private String remark;

    @Column(name = "status",nullable = false)
    @NotNull
    @ApiModelProperty(value = "是否已读")
    private Integer status;

    @Column(name = "create_time",nullable = false)
    @NotNull
    @CreationTimestamp
    @ApiModelProperty(value = "创建时间")
    private Timestamp createTime;

    @Column(name = "update_time",nullable = false)
    @NotNull
    @UpdateTimestamp
    @ApiModelProperty(value = "更新时间")
    private Timestamp updateTime;

    public void copy(PmAlarm source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}