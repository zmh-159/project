package org.parallel.web.modules.pm.cabinet;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Data
@Table(name = "pm_cabinet")
public class PmCabinet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @ApiModelProperty(value = "主键")
    private Long id;

    @Column(name = "name", nullable = false)
    @NotNull
    @ApiModelProperty(value = "机柜名")
    private String name;

    @Column(name = "location", nullable = false)
    @NotNull
    @ApiModelProperty(value = "机柜位置")
    private String location;

    @Column(name = "width", nullable = false)
    @NotNull
    @ApiModelProperty(value = "机柜宽度")
    private String width;

    @Column(name = "height", nullable = false)
    @NotNull
    @ApiModelProperty(value = "机柜高度")
    private String height;
}
