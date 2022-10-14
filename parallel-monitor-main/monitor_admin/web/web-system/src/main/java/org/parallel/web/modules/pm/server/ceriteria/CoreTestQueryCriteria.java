package org.parallel.web.modules.pm.server.ceriteria;

import lombok.Data;

/**
 * @author yuyifade
 * @description
 * @date 2021/8/3 下午3:46
 */
@Data
//todo 数据校验
public class CoreTestQueryCriteria {
    /**
     * 节点id
     **/
    Long nodeId;
    /**
     * 初始包大小
     **/
    Integer start;
    /**
     * 结束包大小
     **/
    Integer end;
    /**
     * 核心1
     **/
    Integer core1;
    /**
     * 核心2
     **/
    Integer core2;
    /**
     * 步进
     **/
    Integer steps;
    /**
     * 重复次数
     **/
    Integer repeat;

    public String getUrl() {
        String url = "nodeId=" + nodeId;
        url += "&" + "start=" + start;
        url += "&" + "end=" + end;
        url += "&" + "core1=" + core1;
        url += "&" + "core2=" + core2;
        url += "&" + "steps=" + steps;
        url += "&" + "repeat=" + repeat;
        return url;
    }
}
