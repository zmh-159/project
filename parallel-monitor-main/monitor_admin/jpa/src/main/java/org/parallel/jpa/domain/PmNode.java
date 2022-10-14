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
import java.util.List;

/**
 * @author yuyifade
 * @website https://el-admin.vip
 * @description /
 * @date 2021-06-18
 **/
@Entity
@Data
@Table(name = "pm_node")
public class PmNode implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "node_id")
    @ApiModelProperty(value = "主键")
    private Long nodeId;

    @Column(name = "uuid", unique = true, nullable = false)
    @NotBlank
    @ApiModelProperty(value = "标识符")
    private String uuid;

    @OneToMany(cascade = {CascadeType.REMOVE})
    @JoinColumn(name = "node_id",updatable = false)
    private List<PmNodeMemory> memoryMoudles;

    @OneToMany(cascade = {CascadeType.REMOVE})
    @JoinColumn(name = "node_id",updatable = false)
    private List<PmNetwork> networks;

    @OneToMany(cascade = {CascadeType.REMOVE})
    @JoinColumn(name = "node_id",updatable = false)
    private List<PmNodeCpu> cpus;

    @OneToMany(cascade = {CascadeType.REMOVE})
    @JoinColumn(name = "node_id",updatable = false)
    private List<PmPhysicNetwork> physicNetworks;

    @OneToMany(cascade = {CascadeType.REMOVE})
    @JoinColumn(name = "node_id",updatable = false)
    private List<PmNodeDisk> disks;

    @OneToMany(cascade = {CascadeType.REMOVE})
    @JoinColumn(name = "node_id",updatable = false)
    private List<PmNodeGpu> gpus;

    @Column(name = "remark")
    @ApiModelProperty(value = "备注")
    private String remark;

    @Column(name = "ip", nullable = false)
    @NotBlank
    @ApiModelProperty(value = "ip地址")
    private String ip;

    @Column(name = "cpu_nums", nullable = false)
    @NotNull
    @ApiModelProperty(value = "cpu数量")
    private Integer cpuNums;

    @Column(name = "gpu_nums", nullable = false)
    @NotNull
    @ApiModelProperty(value = "gpu数量")
    private Integer gpuNums;

    @Column(name = "performance", nullable = false)
    @NotBlank
    @ApiModelProperty(value = "算力")
    private String performance;

    @Column(name = "max_extend_memory", nullable = false)
    @NotBlank
    @ApiModelProperty(value = "最大扩展内存容量")
    private String maxExtendMemory;

    @Column(name = "memory", nullable = false)
    @NotBlank
    @ApiModelProperty(value = "内存")
    private String memory;

    @Column(name = "memory_slot", nullable = false)
    @NotNull
    @ApiModelProperty(value = "内存插槽")
    private Integer memorySlot;

    @Column(name = "os_name", nullable = false)
    @NotBlank
    @ApiModelProperty(value = "操作系统")
    private String osName;

    @Column(name = "os_version", nullable = false)
    @NotBlank
    @ApiModelProperty(value = "系统版本")
    private String osVersion;

    @Column(name = "os_bit", nullable = false)
    @NotNull
    @ApiModelProperty(value = "位数")
    private Integer osBit;

    @Column(name = "host_name", nullable = false)
    @NotBlank
    @ApiModelProperty(value = "主机名")
    private String hostName;

    @Column(name = "disk_total", nullable = false)
    @NotBlank
    @ApiModelProperty(value = "可用磁盘")
    private String diskTotal;


    @Column(name = "serial_number")
    @ApiModelProperty(value = "序号")
    private Long serialNumber;

    @Column(name = "start_time", nullable = false)
    @NotNull
    @ApiModelProperty(value = "开机时间")
    private Timestamp startTime;

    @Column(name = "create_time")
    @CreationTimestamp
    @ApiModelProperty(value = "创建时间")
    private Timestamp createTime;

    @Column(name = "update_time")
    @UpdateTimestamp
    @ApiModelProperty(value = "修改时间")
    private Timestamp updateTime;

    public void copy(PmNode source) {
        BeanUtil.copyProperties(source, this, CopyOptions.create().setIgnoreNullValue(true));
    }
}