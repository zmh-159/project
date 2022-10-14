package org.parallel.jpa.domain;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Timestamp;
/**
 * @website https://el-admin.vip
 * @description /
 * @author yuyifade
 * @date 2021-11-11
 **/
@Entity
@Data
@Table(name="pm_alarm_detail")
public class PmAlarmDetail implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "alarm_detail_id")
    @ApiModelProperty(value = "主键")
    private Long alarmDetailId;

    @Column(name = "alarm_id",nullable = false)
    @NotNull
    @ApiModelProperty(value = "外键")
    private Long alarmId;

    @Column(name = "outlier",nullable = false)
    @NotNull
    @ApiModelProperty(value = "异常值")
    private double outlier;

    @Column(name = "create_time",nullable = false)
    @NotNull
    @ApiModelProperty(value = "触发时间")
    private Timestamp createTime;

    public void copy(PmAlarm source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}