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
import java.math.BigDecimal;
import java.io.Serializable;

/**
* @website https://el-admin.vip
* @description /
* @author ruo-yin
* @date 2022-08-09
**/
@Entity
@Data
@Table(name="pb_node_memory")
public class PbNodeMemory implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "memory_id")
    @ApiModelProperty(value = "主键")
    private Long memoryId;

    @Column(name = "node_id",nullable = false)
    @NotNull
    @ApiModelProperty(value = "外键：表pm_node")
    private Long nodeId;

    @Column(name = "manufacturer",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "制造商")
    private String manufacturer;

    @Column(name = "type",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "内存规格")
    private String type;

    @Column(name = "speed",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "速度")
    private String speed;

    @Column(name = "capacity",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "容量")
    private String capacity;

    @Column(name = "health",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "健康状态")
    private String health;

    @Column(name = "socket")
    @ApiModelProperty(value = "内存资源所属CPU槽位号")
    private String socket;

    @Column(name = "slot")
    @ApiModelProperty(value = "内存资源的槽位号")
    private String slot;

    @Column(name = "remaining_service_life")
    @ApiModelProperty(value = "剩余使用寿命百分比")
    private BigDecimal remainingServiceLife;

    @Column(name = "controller_temp")
    @ApiModelProperty(value = "内存资源的控制器温度")
    private BigDecimal controllerTemp;

    @Column(name = "medium_temp")
    @ApiModelProperty(value = "内存资源的介质温度")
    private BigDecimal mediumTemp;

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

    public void copy(PbNodeMemory source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}