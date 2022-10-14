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
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

/**
 * @website https://el-admin.vip
 * @description /
 * @author yuyifade
 * @date 2021-06-18
 **/
@Entity
@Data
@Table(name="pm_node_state")
public class PmNodeState implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "node_state_id")
    @ApiModelProperty(value = "主键")
    private Long nodeStateId;

    @Column(name = "node_id",nullable = false)
    @NotNull
    @ApiModelProperty(value = "外键：表pm_node")
    private Long nodeId;

    @OneToMany(cascade = {CascadeType.REMOVE})
    @JoinColumn(name = "node_state_id",updatable = false)
    private List<PmNetworkState> pmNodeNetworkStates;

    @OneToMany(cascade = {CascadeType.REMOVE})
    @JoinColumn(name = "node_state_id",updatable = false)
    private List<PmNodeCpuState> pmNodeCpuStates;

    @OneToMany(cascade = {CascadeType.REMOVE})
    @JoinColumn(name = "node_state_id",updatable = false)
    private List<PmDiskState> pmNodeDiskStates;

    @OneToMany(cascade = {CascadeType.REMOVE})
    @JoinColumn(name = "node_state_id",updatable = false)
    private List<PmNodeGpuState> pmNodeGpuStates;

    @Column(name = "process_nums",nullable = false)
    @NotNull
    @ApiModelProperty(value = "进程数")
    private Integer processNums;

    @Column(name = "cpu_idle",nullable = false)
    @NotNull
    @ApiModelProperty(value = "cpu空闲率")
    private BigDecimal cpuIdle;

    @Column(name = "cpu_system",nullable = false)
    @NotNull
    @ApiModelProperty(value = "cpu系统占用")
    private BigDecimal cpuSystem;

    @Column(name = "cpu_user",nullable = false)
    @NotNull
    @ApiModelProperty(value = "cpu用户占用")
    private BigDecimal cpuUser;

    @Column(name = "memory_total",nullable = false)
    @NotNull
    @ApiModelProperty(value = "内存")
    private Double memoryTotal;

    @Column(name = "memory_used",nullable = false)
    @NotNull
    @ApiModelProperty(value = "内存使用")
    private Double memoryUsed;

    @Column(name = "memory_rate",nullable = false)
    @NotNull
    @ApiModelProperty(value = "内存使用率")
    private BigDecimal memoryRate;

    @Column(name = "swap_total",nullable = false)
    @NotNull
    @ApiModelProperty(value = "交换区")
    private Double swapTotal;

    @Column(name = "swap_used",nullable = false)
    @NotNull
    @ApiModelProperty(value = "交换区使用")
    private Double swapUsed;

    @Column(name = "swap_rate",nullable = false)
    @NotNull
    @ApiModelProperty(value = "交换区使用率")
    private BigDecimal swapRate;

    @Column(name = "disk_total",nullable = false)
    @NotNull
    @ApiModelProperty(value = "磁盘")
    private Double diskTotal;

    @Column(name = "disk_used",nullable = false)
    @NotNull
    @ApiModelProperty(value = "磁盘使用")
    private Double diskUsed;

    @Column(name = "disk_rate",nullable = false)
    @NotNull
    @ApiModelProperty(value = "磁盘使用率")
    private BigDecimal diskRate;

    @Column(name = "gpu_utilization",nullable = false)
    @NotNull
    @ApiModelProperty(value = "GPU使用率")
    private BigDecimal gpuUtilization;

    @Column(name = "memory_utilization",nullable = false)
    @NotNull
    @ApiModelProperty(value = "内存使用率")
    private BigDecimal memoryUtilization;

    @Column(name = "alarm_state",nullable = false)
    @NotNull
    @ApiModelProperty(value = "报警状态")
    private String alarmState;

    @Column(name = "create_time",nullable = false)
    @NotNull
    @ApiModelProperty(value = "采集时间")
    private Timestamp createTime;

    @Column(name = "update_time",nullable = false)
    @UpdateTimestamp
    @ApiModelProperty(value = "修改时间")
    private Timestamp updateTime;

    public void copy(PmNodeState source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}