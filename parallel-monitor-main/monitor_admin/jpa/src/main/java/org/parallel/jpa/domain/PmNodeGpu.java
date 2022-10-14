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
import java.io.Serializable;

/**
 * @website https://el-admin.vip
 * @description /
 * @author yuyifade
 * @date 2021-08-18
 **/
@Entity
@Data
@Table(name="pm_node_gpu")
public class PmNodeGpu implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "gpu_id")
    @ApiModelProperty(value = "主键")
    private Long gpuId;

    @Column(name = "node_id",nullable = false)
    @NotNull
    @ApiModelProperty(value = "外键：表pm_node")
    private Long nodeId;

    @OneToOne()
    @JoinColumn(name = "node_id",insertable = false,updatable = false)
    private PmNode node;

    @Column(name = "uuid",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "显卡uuid")
    private String uuid;

    @Column(name = "name",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "显卡名")
    private String name;

    @Column(name = "manufacturer",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "制造商")
    private String manufacturer;

    @Column(name = "driver_version",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "驱动版本")
    private String driverVersion;

    @Column(name = "accounting_mode_buffer_size",nullable = false)
    @NotNull
    @ApiModelProperty(value = "记账缓冲区")
    private Long accountingModeBufferSize;

    @Column(name = "cuda")
    @ApiModelProperty(value = "cuda")
    private Integer cuda;

    @Column(name = "width")
    @ApiModelProperty(value = "位宽")
    private Integer width;

    @Column(name = "vbios_version",nullable = false)
    @NotBlank
    @ApiModelProperty(value = " Vbios")
    private String vbiosVersion;

    @Column(name = "memory",nullable = false)
    @NotBlank
    @ApiModelProperty(value = " 显存")
    private String memory;

    @Column(name = "gpu_shutdown_temp",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "关机温度")
    private String gpuShutdownTemp;

    @Column(name = "gpu_slowdown_temp",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "降频温度")
    private String gpuSlowdownTemp;

    @Column(name = "gpu_max_operating_temp",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "gpu最大操作温度")
    private String gpuMaxOperatingTemp;

    @Column(name = "memory_max_operating_temp",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "显存最大操作温度")
    private String memoryMaxOperatingTemp;

    @Column(name = "max_power_limit",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "最大限制功率")
    private String maxPowerLimit;

    @Column(name = "min_power_limit",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "最小限制功率")
    private String minPowerLimit;

    @Column(name = "max_graphics_frequency",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "图像最大限制频率")
    private String maxGraphicsFrequency;

    @Column(name = "max_sm_frequency",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "着色器最大限制频率")
    private String maxSmFrequency;

    @Column(name = "max_memory_frequency",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "显存最大限制频率")
    private String maxMemoryFrequency;

    @Column(name = "max_video_frequency",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "视频最大限制频率")
    private String maxVideoFrequency;

    @Column(name = "create_time")
    @CreationTimestamp
    @ApiModelProperty(value = "创建时间")
    private Timestamp createTime;

    @Column(name = "update_time")
    @UpdateTimestamp
    @ApiModelProperty(value = "修改时间")
    private Timestamp updateTime;

    public void copy(PmNodeGpu source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}