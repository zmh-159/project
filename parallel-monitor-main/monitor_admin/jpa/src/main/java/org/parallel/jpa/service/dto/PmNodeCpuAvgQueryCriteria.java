package org.parallel.jpa.service.dto;

import lombok.Data;
import org.parallel.web.annotation.Query;


import java.sql.Timestamp;
import java.util.List;
import java.util.Locale;

/**
 * @author yuyifade
 * @description
 * @date 2021/7/7 下午7:20
 */
@Data
public class PmNodeCpuAvgQueryCriteria {
    private Long cpuId;

    private Integer size;

    private String startTime;

    private String endTime;

    private String sortType;


    public Long getCpuId() {
        if (cpuId == null) {
            cpuId = 0L;
        }
        return cpuId;
    }

    public Integer getSize() {
        if (size == null) {
            size = 100;
        }
        return size;
    }

    public String getStartTime() {
        return startTime == null ? "1970-01-01 01:01:01" : startTime;
    }

    public String getEndTime() {
        return endTime == null ? "3000-01-01 01:01:01" : endTime;
    }


    public String getSortType() {
        if (sortType != null) {
            String low = sortType.toLowerCase(Locale.ROOT);
            if (low.equals("asc") || low.equals("desc")) {
                return low;
            }
        }
        return "desc";
    }


}
