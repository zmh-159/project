package org.parallel.web.modules.pm.config.service;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import org.parallel.web.modules.pm.config.domain.PmCollectConfig;

/**
 * @website https://el-admin.vip
 * @description 服采集间隔相关参数
 * @author yuyifade
 * @date 2021-11-9
 **/
public interface PmCollectConfigService {
    /**
     * 查询参数
     * @return JSONObject
     */
    JSONObject getConfig();
    /**
     * 设置参数
     * @return JSONObject
     */
    boolean setConfig(PmCollectConfig pmCollectConfig);
}
