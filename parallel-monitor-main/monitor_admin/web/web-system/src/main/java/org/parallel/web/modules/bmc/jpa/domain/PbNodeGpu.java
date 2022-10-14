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
@Table(name="pb_node_gpu")
public class PbNodeGpu implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "gpu_id")
    @ApiModelProperty(value = "主键")
    private Long gpuId;

    @Column(name = "node_id",nullable = false)
    @NotNull
    @ApiModelProperty(value = "外键：表pm_node")
    private Long nodeId;

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

    @Column(name = "firmware_version",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "固件版本")
    private String firmwareVersion;

    @Column(name = "slot_number",nullable = false)
    @NotNull
    @ApiModelProperty(value = "槽位号")
    private Integer slotNumber;

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
    @ApiModelProperty(value = "修改时间")
    private Timestamp updateTime;

    public void copy(PbNodeGpu source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}