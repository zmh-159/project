package org.parallel.client.kernel.model.obtain;

import com.alibaba.fastjson.JSONObject;

import java.util.concurrent.Future;

/**
 * @author yuyifade
 * @description 获取动态接口(有延时)
 * @date 2021/7/14 上午12:19
 */
public interface DynamicInfoWithDelay {
    Future<JSONObject> getDynamicWithDelay(int delay);
}
