/*
 *  Copyright 2019-2020 Zheng Jie
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.parallel.jpa.service.dto;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import org.parallel.common.utils.CommonUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * @author yuyifade
 * @website https://el-admin.vip
 * @description /
 * @date 2021-06-18
 **/
@Data
public class PmNodeCpuStateDto implements Serializable {

    /** 主键 */
    private Long cpuStateId;

    /** 外键：表pm_node_cpu */
    private Long cpuId;

    /** 外键：表pm_node_state */
    private Long nodeStateId;

    private String coreStateStr;

    private JSONObject coreState;

    private Double cpuSystemAvg = 0.0;

    private Double cpuUserAvg = 0.0;

    private Double cpuIdleAvg = 0.0;

    /** 温度 */
    private BigDecimal temperature;

    /** 采集时间 */
    private Timestamp createTime;

    /** 修改时间 */
    private Timestamp updateTime;

    public void setCoreStateStr(final String s) {
        coreState = JSONObject.parseObject(s);
        for (String key : coreState.keySet()) {
            cpuIdleAvg += coreState.getJSONObject(key).getDouble("cpu_idle");
            cpuSystemAvg += coreState.getJSONObject(key).getDouble("cpu_system");
            cpuUserAvg += coreState.getJSONObject(key).getDouble("cpu_user");
        }
        cpuIdleAvg = CommonUtils.getDoubleWith2(cpuIdleAvg / coreState.size());
        cpuSystemAvg = CommonUtils.getDoubleWith2(cpuSystemAvg / coreState.size());
        cpuUserAvg = CommonUtils.getDoubleWith2(cpuUserAvg / coreState.size());
    }


//    /***
//      * @description 计算当前cpu的平均系统负载，用户负载，空闲值
//      * @author yuyifade
//      * @date 2021/7/12 下午1:13
//      * @param pmCoreStates
//      * @return void
//      */
//    public void setPmCoreStates(final List<PmCoreStateDto> pmCoreStates) {
//        this.pmCoreStates = pmCoreStates;
//        for (PmCoreStateDto p : pmCoreStates) {
//            cpuIdleAvg += p.getCpuIdle().doubleValue();
//            cpuSystemAvg += p.getCpuSystem().doubleValue();
//            cpuUserAvg += p.getCpuUser().doubleValue();
//        }
//        cpuIdleAvg /= pmCoreStates.size();
//        cpuSystemAvg /= pmCoreStates.size();
//        cpuUserAvg /= pmCoreStates.size();
//    }
}