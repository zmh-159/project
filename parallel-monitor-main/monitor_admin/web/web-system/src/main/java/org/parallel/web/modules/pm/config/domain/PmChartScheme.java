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
package org.parallel.web.modules.pm.config.domain;

import lombok.Data;
import cn.hutool.core.bean.BeanUtil;
import io.swagger.annotations.ApiModelProperty;
import cn.hutool.core.bean.copier.CopyOptions;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.sql.Timestamp;
import java.io.Serializable;

/**
 * @website https://el-admin.vip
 * @description /
 * @author yuyifade
 * @date 2021-07-07
 **/
@Entity
@Data
@Table(name="pm_chart_scheme")
public class PmChartScheme implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chart_scheme_id")
    @ApiModelProperty(value = "主键",hidden = true)
    private Long chartSchemeId;

    @Column(name = "user_id")
    @ApiModelProperty(value = "外键：关联用户表",hidden = true)
    private Long userId;

    @Column(name = "description",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "描述")
    private String description;

    @Column(name = "serial_number",nullable = false)
    @NotNull
    @ApiModelProperty(value = "方案编号")
    private Long serialNumber;

    @Column(name = "detail",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "方案具体参数")
    private String detail;

    @Column(name = "position",nullable = false)
    @NotNull
    @ApiModelProperty(value = "位置编号")
    private Integer position;

    @Column(name = "create_time")
    @CreationTimestamp
    @ApiModelProperty(value = "创建日期",hidden = true)
    private Timestamp createTime;

    @Column(name = "update_time")
    @UpdateTimestamp
    @ApiModelProperty(value = "更新时间",hidden = true)
    private Timestamp updateTime;

    public void copy(PmChartScheme source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}