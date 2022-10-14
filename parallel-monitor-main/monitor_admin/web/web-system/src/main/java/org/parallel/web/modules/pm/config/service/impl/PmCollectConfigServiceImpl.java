package org.parallel.web.modules.pm.config.service.impl;

import com.alibaba.fastjson.JSONObject;
import lombok.RequiredArgsConstructor;
import org.parallel.web.modules.pm.config.domain.PmCollectConfig;
import org.parallel.web.modules.pm.config.domain.PmConfig;
import org.parallel.web.modules.pm.config.repository.PmConfigRepository;
import org.parallel.web.modules.pm.config.service.PmCollectConfigService;
import org.springframework.stereotype.Service;

/**
 * @author yuyifade
 * @website https://el-admin.vip
 * @description 服务实现
 * @date 2021-11-19
 **/
@RequiredArgsConstructor
@Service
public class PmCollectConfigServiceImpl implements PmCollectConfigService {
    private final PmConfigRepository pcr;

    /***
     * @description 获取采样参数，单位s ;mode（模式）：模式1（高频采集模式）  2,模式2（动态采集模式）
     * @author yuyifade
     * @date 2021/11/9 上午9:04
     * @return com.alibaba.fastjson.JSONObject
     */
    @Override
    public JSONObject getConfig() {
        PmConfig bigInterval = pcr.findByKeyword("big_interval");
        PmConfig smallInterval = pcr.findByKeyword("small_interval");
        PmConfig mode = pcr.findByKeyword("mode");
        int bi = 600;
        int si = 5;
        int m = 0;
        try {
            if (bigInterval != null) {
                bi = Integer.parseInt(bigInterval.getCollocation());
            }
            if (smallInterval != null) {
                si = Integer.parseInt(smallInterval.getCollocation());
            }
            if (mode != null) {
                m = Integer.parseInt(mode.getCollocation());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        JSONObject jo = new JSONObject();
        jo.put("big_interval", bi);
        jo.put("small_interval", si);
        jo.put("mode", m);
        return jo;
    }

    @Override
    public boolean setConfig(PmCollectConfig pmCollectConfig) {
        PmConfig bigInterval = pcr.findByKeyword("big_interval");
        PmConfig smallInterval = pcr.findByKeyword("small_interval");
        PmConfig mode = pcr.findByKeyword("mode");
        if (bigInterval == null) {
            bigInterval = new PmConfig();
            bigInterval.setKeyword("big_interval");
        }
        if (smallInterval == null) {
            smallInterval = new PmConfig();
            smallInterval.setKeyword("small_interval");
        }
        if (mode == null) {
            mode = new PmConfig();
            mode.setKeyword("mode");
        }
        bigInterval.setCollocation(pmCollectConfig.getBigInterval() + "");
        smallInterval.setCollocation(pmCollectConfig.getSmallInterval() + "");
        mode.setCollocation(pmCollectConfig.getMode() + "");

        try {
            pcr.save(bigInterval);
            pcr.save(smallInterval);
            pcr.save(mode);
            return true;
        }catch (Exception e){
            return false;
        }
    }
}
