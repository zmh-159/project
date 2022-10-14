package org.parallel.web.modules.pm.cabinet;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Data
@Table(name = "pm_cabinet_node")
public class PmCabinetNode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @ApiModelProperty(value = "主键")
    private Long id;

    @Column(name = "height", nullable = false)
    @NotNull
    @ApiModelProperty(value = "节点高度")
    private String height;

    @Column(name = "x", nullable = false)
    @NotNull
    @ApiModelProperty(value = "节点横坐标")
    private String x;

    @Column(name = "type", nullable = false)
    @NotNull
    @ApiModelProperty(value = "节点类型")
    private String type;

    @Column(name = "manufacturer", nullable = false)
    @NotNull
    @ApiModelProperty(value = "节点生产商")
    private String manufacturer;

    @Column(name = "date", nullable = false)
    @NotNull
    @ApiModelProperty(value = "节点加入节点时间")
    private String date;

    @Column(name = "name", nullable = false)
    @NotNull
    @ApiModelProperty(value = "节点名")
    private String name;

    @Column(name = "cabinet", nullable = false)
    @NotNull
    @ApiModelProperty(value = "节点所属机柜id")
    private String cabinet;

    @Column(name = "y", nullable = false)
    @NotNull
    @ApiModelProperty(value = "节点纵坐标")
    private String y;

    @Column(name = "width", nullable = false)
    @NotNull
    @ApiModelProperty(value = "节点宽度")
    private String width;
}
