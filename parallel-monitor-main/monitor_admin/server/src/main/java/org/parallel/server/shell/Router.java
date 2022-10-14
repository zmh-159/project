package org.parallel.server.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Router {
    private final Terminal t;

    public String route(String cmds) {
        String[] arr = cmds.split(" ");
        if (arr[0].equals("cpuinfo")) {
            double cpuLimit = 0;
            if (arr.length > 1) {
                try {
                    cpuLimit = Double.parseDouble(arr[1]);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return t.getCpuInfo(cpuLimit);
        }

        if(arr[0].equals("networkinfo")){
            return t.getNetworkInfo();
        }
        else if(arr[0].equals("nodeinfo")){
            return t.getNodesInfo();
        }
        else if(arr[0].equals("diskinfo")){
            return t.getDiskInfo();
        }
        return "";
    }

}
