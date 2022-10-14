package org.parallel.client.kernel.service;

import com.alibaba.fastjson.JSONObject;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author yuyifade
 * @description 数据统一过滤层
 * @date 2022/1/5 下午5:30
 */
@Component
@RequiredArgsConstructor
public class FilterInfo {
    private final AccessInfo accessInfo;

    public JSONObject getInfo(String type, String filter) {
        //todo 添加数据过滤
        return accessInfo.getInfo(type);
    }

}
