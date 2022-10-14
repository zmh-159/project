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
package org.parallel.web.modules.pm.config.domain;

import lombok.Data;
import cn.hutool.core.bean.BeanUtil;
import io.swagger.annotations.ApiModelProperty;
import cn.hutool.core.bean.copier.CopyOptions;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.*;
import org.parallel.jpa.domain.PmNode;

import java.sql.Timestamp;
import java.io.Serializable;
import java.util.Set;

/**
 * @author yuyifade
 * @website https://el-admin.vip
 * @description /
 * @date 2021-09-24
 **/
@Entity
@Data
@Table(name = "pm_deploy")
public class PmDeploy implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "deploy_id")
    @ApiModelProperty(value = "主键")
    private Long deployId;

    @ManyToMany
    @ApiModelProperty(name = "节点", hidden = true)
    @JoinTable(name = "pm_deploy_node",
            joinColumns = {@JoinColumn(name = "deploy_id",referencedColumnName = "deploy_id")},
            inverseJoinColumns = {@JoinColumn(name = "node_id",referencedColumnName = "node_id")})
    private Set<PmNode> pm;

    @Column(name = "name")
    @ApiModelProperty(value = "软件包名字")
    private String name;

    @Column(name = "version")
    @ApiModelProperty(value = "版本")
    private String version;

    @Column(name = "os")
    @ApiModelProperty(value = "操作系统")
    private String os;

    @Column(name = "install_shell")
    @ApiModelProperty(value = "安装脚本")
    private String installShell;

    @Column(name = "delete_shell")
    @ApiModelProperty(value = "卸载脚本")
    private String deleteShell;

    @Column(name = "create_time")
    @CreationTimestamp
    @ApiModelProperty(value = "创建时间")
    private Timestamp createTime;

    @Column(name = "update_time")
    @UpdateTimestamp
    @ApiModelProperty(value = "修改时间")
    private Timestamp updateTime;

    public void copy(PmDeploy source) {
        BeanUtil.copyProperties(source, this, CopyOptions.create().setIgnoreNullValue(true));
    }
}