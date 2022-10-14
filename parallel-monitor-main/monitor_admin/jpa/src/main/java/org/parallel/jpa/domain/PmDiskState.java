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

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Timestamp;

/**
* @website https://el-admin.vip
* @description /
* @author yuyifade
* @date 2021-06-18
**/
@Entity
@Data
@Table(name="pm_node_disk_state")
public class PmDiskState implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "disk_state_id")
    @ApiModelProperty(value = "主键")
    private Long diskStateId;

    @Column(name = "disk_id",nullable = false)
    @NotNull
    @ApiModelProperty(value = "外键：表pm_node_disk")
    private Long diskId;

    @Column(name = "node_state_id",nullable = false)
    @NotNull
    @ApiModelProperty(value = "外键：表pm_node_state")
    private Long nodeStateId;

    @Column(name = "read_speed",nullable = false)
    @NotNull
    @ApiModelProperty(value = "读速度")
    private Double readSpeed;

    @Column(name = "write_speed",nullable = false)
    @NotNull
    @ApiModelProperty(value = "写速度")
    private Double writeSpeed;

    @Column(name = "create_time",nullable = false)
    @NotNull
    @ApiModelProperty(value = "创建时间")
    private Timestamp createTime;

    @Column(name = "update_time",nullable = false)
    @UpdateTimestamp
    @ApiModelProperty(value = "修改时间")
    private Timestamp updateTime;

    public void copy(PmDiskState source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}