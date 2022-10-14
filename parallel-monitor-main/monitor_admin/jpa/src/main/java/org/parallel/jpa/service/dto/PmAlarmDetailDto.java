package org.parallel.jpa.service.dto;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
public class PmAlarmDetailDto implements Serializable {
    private double outlier;
    private Timestamp createTime;
}
