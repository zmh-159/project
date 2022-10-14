package org.parallel.jpa.service.dto;

import lombok.Data;

@Data
public class PmNodeAvgQueryCriteria {
    private String startTime;

    private String endTime;

    public String getStartTime() {
        return startTime == null ? "19700101" : startTime;
    }

    public String getEndTime() {
        return endTime == null ? "30000101" : endTime;
    }
}
