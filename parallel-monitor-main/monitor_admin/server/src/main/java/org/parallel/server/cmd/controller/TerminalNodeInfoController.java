package org.parallel.server.cmd.controller;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.parallel.server.cmd.model.GetNodeId;
import org.parallel.server.cmd.service.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("/api/cmdRun")
@RequiredArgsConstructor
@Slf4j
public class TerminalNodeInfoController {
    private final NodeService nodeService;
    private final CpuService cpuService;
    private final MemService memService;
    private final DiskService diskService;
    private final NetService netService;
    private final ProcessService processService;
    private final ShutdownService shutdownService;
    private final GetNodeId getNodeId;
    private final NodeStatus nodeStatus;

    @SneakyThrows
    @GetMapping(value = "/sendCmd")
    public ResponseEntity<Object> sendCmd(HttpServletRequest request) {
        StringBuilder res = new StringBuilder();
        String cmd = request.getParameter("cmd");
        String[] arr = cmd.split(" ");
        switch (arr[0]) {
            case "clusterInfo":
                if (arr.length > 1) {
                    res.append(nodeService.getClusterInfo(arr[1]));
                } else {
                    res.append(nodeService.getClusterInfo());
                }
                break;
            case "cpuStatic":
                if (arr.length > 1) {
                    res.append(cpuService.cpuStatic(arr[1]));
                } else {
                    res.append(cpuService.cpuStatic(""));
                }
                break;
            case "cpuDynamic":
                if (arr.length > 1) {
                    res.append(cpuService.cpuDynamic(arr[1]));
                } else {
                    res.append(cpuService.cpuDynamic(""));
                }
                break;
            case "memoryStatic":
                if (arr.length > 1) {
                    res.append(memService.memoryStatic(arr[1]));
                } else {
                    res.append(memService.memoryStatic(""));
                }
                break;
            case "diskStat":
                if (arr.length > 1) {
                    res.append(diskService.diskStat(arr[1]));
                } else {
                    res.append(diskService.diskStat(""));
                }
                break;
            case "netStat":
                if (arr.length > 1) {
                    res.append(netService.netStat(arr[1]));
                } else {
                    res.append(netService.netStat(""));
                }
                break;
            case "proDynamic":
                if (arr.length > 1) {
                    try {
                        res.append(processService.processStatic(arr[1]));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    res.append(processService.processStatic());
                }
                break;
            case "nodeId":
                res.append(getNodeId.getOnlineId());
                break;
            case "nodeStatus":
                res.append(nodeStatus.nodeStatus());
                break;
            case "shutdown":
                if (arr.length > 1) {
                    res.append(shutdownService.shutdown(arr[1]));
                } else {
                    res.append(shutdownService.shutdown(""));
                }
        }

        return new ResponseEntity<>(res.toString(), HttpStatus.OK);
    }
}
