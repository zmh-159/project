package org.parallel.web.modules.pm.config.domain;

import lombok.Data;

@Data
public class PmCollectConfig {
    private Integer bigInterval;
    private Integer smallInterval;
    private Integer mode;
}
