package org.parallel.web.modules.bmc.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.parallel.jpa.service.PmNetworkService;
import org.parallel.jpa.service.dto.PmNetworkQueryCriteria;
import org.parallel.web.modules.bmc.jpa.service.PbNodeNetworkService;
import org.parallel.web.modules.bmc.jpa.service.dto.PbNodeNetworkQueryCriteria;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
@Api(tags = "bmc：风扇静态信息管理")
@RequestMapping("/api/pbNodeFan")
public class pbNodeFanController {
    private final PbNodeFanService pbNodeFanService;

    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    public void download(HttpServletResponse response, PbNodeFanQueryCriteria criteria) throws IOException {
        pbNodeFanService.download(pbNodeFanService.queryAll(criteria), response);
    }

    @GetMapping
    @ApiOperation("查询风扇静态信息")
    public ResponseEntity<Object> query(PbNodeFanQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(pbNodeFanService.queryAll(criteria,pageable), HttpStatus.OK);
    }
}
