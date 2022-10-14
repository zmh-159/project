package org.parallel.web.modules.bmc.jpa.domain;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Data
@Table(name="pb_node_fan")
public class PbNodeFan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fan_id")
    @ApiModelProperty(value = "主键")
    private Long fanId;

    @Column(name = "node_id",unique = true,nullable = false)
    @ApiModelProperty(value = "外键：表pb_node")
    private String nodeId;

    @Column(name = "name",unique = true,nullable = false)
    @ApiModelProperty(value = "散热资源名")
    private String name;

    @Column(name = "total_watts",unique = true,nullable = false)
    @ApiModelProperty(value = "风扇总功耗")
    private String totalWatts;

    @Column(name = "status",unique = true,nullable = false)
    @ApiModelProperty(value = "健康状态")
    private String status;

    @Column(name = "create_time",nullable = false)
    @CreationTimestamp
    @ApiModelProperty(value = "创建时间")
    private Timestamp createTime;

    @Column(name = "update_time",nullable = false)
    @UpdateTimestamp
    @ApiModelProperty(value = "修改时间")
    private Timestamp updateTime;

    public void copy(PbNodeFan source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
