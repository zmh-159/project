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
import javax.validation.constraints.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import org.hibernate.annotations.*;
import java.sql.Timestamp;
import java.math.BigDecimal;
import java.io.Serializable;

/**
* @website https://el-admin.vip
* @description /
* @author yuyifade
* @date 2021-08-18
**/
@Entity
@Data
@Table(name="pm_node_gpu_state")
public class PmNodeGpuState implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "gpu_state_id")
    @ApiModelProperty(value = "主键")
    private Long gpuStateId;

    @Column(name = "gpu_id",nullable = false)
    @NotNull
    @ApiModelProperty(value = "外键：表pm_node_gpu")
    private Long gpuId;

    @Column(name = "node_state_id",nullable = false)
    @NotNull
    @ApiModelProperty(value = "外键：表pm_node_state")
    private Long nodeStateId;

    @Column(name = "tx_throughput",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "上行速度")
    private String txThroughput;

    @Column(name = "rx_throughput",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "下行速度")
    private String rxThroughput;

    @Column(name = "fan_speed",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "风扇转速")
    private String fanSpeed;

    @Column(name = "performance_state",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "性能状态")
    private String performanceState;

    @Column(name = "memory_used",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "显存使用")
    private String memoryUsed;

    @Column(name = "gpu_utilization",nullable = false)
    @NotNull
    @ApiModelProperty(value = "gpu利用率")
    private BigDecimal gpuUtilization;

    @Column(name = "memory_utilization",nullable = false)
    @NotNull
    @ApiModelProperty(value = "显存利用率")
    private BigDecimal memoryUtilization;

    @Column(name = "encoder_utilization",nullable = false)
    @NotNull
    @ApiModelProperty(value = "编码器利用率")
    private BigDecimal encoderUtilization;

    @Column(name = "decoder_utilization",nullable = false)
    @NotNull
    @ApiModelProperty(value = "解码器利用率")
    private BigDecimal decoderUtilization;

    @Column(name = "gpu_current_temp",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "gpu当前温度")
    private String gpuCurrentTemp;

    @Column(name = "memory_current_temp",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "显存当前温度")
    private String memoryCurrentTemp;

    @Column(name = "gpu_power_draw",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "功耗")
    private String gpuPowerDraw;

    @Column(name = "current_graphics_frequency",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "图像当前频率")
    private String currentGraphicsFrequency;

    @Column(name = "current_sm_frequency",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "着色器当前频率")
    private String currentSmFrequency;

    @Column(name = "current_memory_frequency",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "显存当前频率")
    private String currentMemoryFrequency;

    @Column(name = "current_video_frequency",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "视频当前频率")
    private String currentVideoFrequency;

    @Column(name = "create_time",nullable = false)
    @NotNull
    @CreationTimestamp
    @ApiModelProperty(value = "创建时间")
    private Timestamp createTime;

    @Column(name = "update_time",nullable = false)
    @NotNull
    @UpdateTimestamp
    @ApiModelProperty(value = "修改时间")
    private Timestamp updateTime;

    public void copy(PmNodeGpuState source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}