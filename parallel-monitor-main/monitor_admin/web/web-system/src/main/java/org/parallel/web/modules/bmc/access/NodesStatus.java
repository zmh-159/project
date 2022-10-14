package org.parallel.web.modules.bmc.access;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class NodesStatus {
    public Map<Long,String> status = new HashMap<>();
}
