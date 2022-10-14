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

/**
 * @author yuyifade
 * @website https://el-admin.vip
 * @description /
 * @date 2021-06-18
 **/
@Entity
@Data
@Table(name = "pm_node_cpu_state")
public class PmNodeCpuState implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cpu_state_id")
    @ApiModelProperty(value = "主键")
    private Long cpuStateId;

    @Column(name = "cpu_id", nullable = false)
    @NotNull
    @ApiModelProperty(value = "外键：表pm_node_cpu")
    private Long cpuId;

    @Column(name = "node_state_id", nullable = false)
    @NotNull
    @ApiModelProperty(value = "外键：表pm_node_state")
    private Long nodeStateId;

    @Column(name = "core_state", nullable = false)
    @NotNull
    @ApiModelProperty(value = "核心状态")
    private String coreStateStr;

    @Column(name = "temperature", nullable = false)
    @NotNull
    @ApiModelProperty(value = "温度")
    private BigDecimal temperature;

    @Column(name = "create_time", nullable = false)
    @NotNull
    @ApiModelProperty(value = "采集时间")
    private Timestamp createTime;

    @Column(name = "update_time", nullable = false)
    @UpdateTimestamp
    @ApiModelProperty(value = "修改时间")
    private Timestamp updateTime;

    public void copy(PmNodeCpuState source) {
        BeanUtil.copyProperties(source, this, CopyOptions.create().setIgnoreNullValue(true));
    }
}