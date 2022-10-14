package org.parallel.web.modules.pm.config;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.parallel.web.annotation.Log;
import org.parallel.common.utils.RestTemplateClient;
import org.parallel.web.modules.pm.bean.ServerConfig;
import org.parallel.web.modules.pm.config.domain.PmCollectConfig;
import org.parallel.web.modules.pm.config.service.PmCollectConfigService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author yuyifade
 * @website https://el-admin.vip
 * @date 2021-11-8
 **/
@RestController
@RequiredArgsConstructor
@Api(tags = "监控：采集参数管理")
@RequestMapping("/api/pmCollectConfig")
public class PmCollectConfigController {
    private final PmCollectConfigService pccs;
    private final RestTemplateClient rtc;
    private final ServerConfig sc;

    @GetMapping
    @ApiOperation("查询配置")
    @PreAuthorize("@el.check('pmCollectConfig:list')")
    public ResponseEntity<Object> query() {
        return new ResponseEntity<>(pccs.getConfig(), HttpStatus.OK);
    }

    @PutMapping
    @Log("修改配置")
    @ApiOperation("修改配置")
    @PreAuthorize("@el.check('pmCollectConfig:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody PmCollectConfig resources) {
        int bi = resources.getBigInterval();
        int si = resources.getSmallInterval();
        int m = resources.getMode();
        //验证数据范围
        if (bi < 5 || bi > 1800 || si < 5 || si > 1800 || si > bi) {
            return new ResponseEntity<>("参数范围错误！5<=big interval<=1800,5<=small interval<=1800,small interval<=big interval", HttpStatus.BAD_REQUEST);
        }
        if (m != 0 && m != 1) {
            return new ResponseEntity<>("模式错误:0（模式1），1（模式2）", HttpStatus.BAD_REQUEST);
        }

        boolean success = pccs.setConfig(resources);
        if (success) {
            try {
                String url = sc.interval();
                rtc.send(url);
            } catch (Exception e) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>("保存失败！", HttpStatus.BAD_REQUEST);
        }
    }
}
