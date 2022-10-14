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
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
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
@Table(name="pm_node_cpu")
public class PmNodeCpu implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cpu_id")
    @ApiModelProperty(value = "主键")
    private Long cpuId;

    @Column(name = "node_id",nullable = false)
    @NotNull
    @ApiModelProperty(value = "外键：表pm_node")
    private Long nodeId;

    @OneToOne()
    @JoinColumn(name = "node_id",insertable = false,updatable = false)
    private PmNode node;

    @Column(name = "cpu_name", nullable = false)
    @NotBlank
    @ApiModelProperty(value = "CPU型号")
    private String cpuName;

    @Column(name = "cores",nullable = false)
    @NotNull
    @ApiModelProperty(value = "核心数")
    private Integer cores;

    @Column(name = "threads",nullable = false)
    @NotNull
    @ApiModelProperty(value = "线程数")
    private Integer threads;

    @Column(name = "arch",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "架构")
    private String arch;

    @Column(name = "l1_i_cache",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "一级指令缓存")
    private String l1ICache;

    @Column(name = "l1_d_cache",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "一级数据缓存")
    private String l1DCache;

    @Column(name = "l2_cache",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "二级缓存")
    private String l2Cache;

    @Column(name = "l3_cache",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "三级缓存")
    private String l3Cache;

    @Column(name = "main_frequency",nullable = false)
    @NotNull
    @ApiModelProperty(value = "主频")
    private String mainFrequency;

    @Column(name = "boost_frequency",nullable = false)
    @NotNull
    @ApiModelProperty(value = "睿频")
    private String boostFrequency;

    @Column(name = "create_time",nullable = false)
    @CreationTimestamp
    @ApiModelProperty(value = "创建时间")
    private Timestamp createTime;

    @Column(name = "update_time",nullable = false)
    @UpdateTimestamp
    @ApiModelProperty(value = "修改时间")
    private Timestamp updateTime;

    public void copy(PmNodeCpu source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}