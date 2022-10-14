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
package org.parallel.web.modules.bmc.jpa.domain;

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
* @author ruo-yin
* @date 2022-08-09
**/
@Entity
@Data
@Table(name="pb_node")
public class PbNode implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "node_id")
    @ApiModelProperty(value = "主键")
    private Long nodeId;

    @Column(name = "uuid",unique = true,nullable = false)
    @ApiModelProperty(value = "标识符")
    private String uuid;

    @Column(name = "user",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "用户名")
    private String user;

    @Column(name = "passwd",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "密码")
    private String passwd;

    @Column(name = "bmc_ip",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "bmc的ip地址")
    private String bmcIp;

    @Column(name = "cpu_nums",nullable = false)
    @ApiModelProperty(value = "cpu数量")
    private Integer cpuNums;

    @Column(name = "gpu_nums",nullable = false)
    @ApiModelProperty(value = "gpu数量")
    private Integer gpuNums;

    @Column(name = "max_extend_memory",nullable = false)
    @ApiModelProperty(value = "最大扩展的内存容量")
    private String maxExtendMemory;

    @Column(name = "memory",nullable = false)
    @ApiModelProperty(value = "内存")
    private String memory;

    @Column(name = "memory_slot",nullable = false)
    @ApiModelProperty(value = "内存插槽")
    private Integer memorySlot;

    @Column(name = "disk_total",nullable = false)
    @ApiModelProperty(value = "可用磁盘")
    private String diskTotal;

    @Column(name = "remark")
    @ApiModelProperty(value = "备注")
    private String remark;

    @Column(name = "serial_number")
    @ApiModelProperty(value = "序号")
    private Long serialNumber;

    @Column(name = "node_type",nullable = false)
    @ApiModelProperty(value = "节点类型")
    private String nodeType;

    @Column(name = "all_info",nullable = false)
    @ApiModelProperty(value = "所有信息")
    private String allInfo;

    @Column(name = "create_time",nullable = false)
    @CreationTimestamp
    @ApiModelProperty(value = "创建时间")
    private Timestamp createTime;

    @Column(name = "update_time",nullable = false)
    @UpdateTimestamp
    @ApiModelProperty(value = "修改时间")
    private Timestamp updateTime;

    public void copy(PbNode source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}