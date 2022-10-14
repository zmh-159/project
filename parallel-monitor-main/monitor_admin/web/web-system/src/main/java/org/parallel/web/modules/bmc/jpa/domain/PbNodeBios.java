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
@Table(name="pb_node_bios")
public class PbNodeBios implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bios_id")
    @ApiModelProperty(value = "主键")
    private Long biosId;

    @Column(name = "node_id",nullable = false)
    @NotNull
    @ApiModelProperty(value = "外键：表pm_node")
    private Long nodeId;

    @Column(name = "name",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "BIOS名")
    private String name;

    @Column(name = "quick_boot")
    @ApiModelProperty(value = "快速启动模式")
    private String quickBoot;

    @Column(name = "quiet_boot")
    @ApiModelProperty(value = "静态启动模式")
    private String quietBoot;

    @Column(name = "pxe_uefi",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "UEFI的PXE功能")
    private String pxeUefi;

    @Column(name = "pxe_legacy")
    @ApiModelProperty(value = "legacy的PXE功能")
    private String pxeLegacy;

    @Column(name = "custom_power_policy")
    @ApiModelProperty(value = "能效模式")
    private String customPowerPolicy;

    @Column(name = "turbo_mode")
    @ApiModelProperty(value = "cpu动态加速开关")
    private String turboMode;

    @Column(name = "pro_hyper_thread")
    @ApiModelProperty(value = "超线程设置")
    private String proHyperThread;

    @Column(name = "all_info",nullable = false)
    @NotNull
    @ApiModelProperty(value = "所有信息")
    private String allInfo;

    @Column(name = "create_time",nullable = false)
    @CreationTimestamp
    @ApiModelProperty(value = "创建时间")
    private Timestamp createTime;

    @Column(name = "update_time",nullable = false)
    @UpdateTimestamp
    @ApiModelProperty(value = "编辑时间")
    private Timestamp updateTime;

    public void copy(PbNodeBios source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}