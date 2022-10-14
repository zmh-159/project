package org.parallel.web.modules.bmc.jpa.service.dto;

import com.qiniu.util.Json;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
public class PbNodeFanDto implements Serializable {
    /** 主键 */
    private Long fanId;

    /** 外键：表pb_node */
    private Long nodeId;

    /** 散热资源名 */
    private String name;

    /** 风扇总功耗*/
    private int totalWatts;

    /** 健康状态*/
    private String status;

    /** 创建时间 */
    private Timestamp createTime;

    /** 编辑时间 */
    private Timestamp updateTime;
}
