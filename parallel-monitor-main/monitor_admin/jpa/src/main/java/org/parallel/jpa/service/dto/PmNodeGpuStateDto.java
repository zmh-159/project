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

import lombok.Data;
import java.sql.Timestamp;
import java.math.BigDecimal;
import java.io.Serializable;

/**
* @website https://el-admin.vip
* @description /
* @author yuyifade
* @date 2021-08-18
**/
@Data
public class PmNodeGpuStateDto implements Serializable {

    /** 主键 */
    private Long gpuStateId;

    /** 外键：表pm_node_gpu */
    private Long gpuId;

    /** 外键：表pm_node_state */
    private Long nodeStateId;

    /** 上行速度 */
    private String txThroughput;

    /** 下行速度 */
    private String rxThroughput;

    /** 风扇转速 */
    private String fanSpeed;

    /** 性能状态 */
    private String performanceState;

    /** 显存使用 */
    private String memoryUsed;

    /** gpu利用率 */
    private BigDecimal gpuUtilization;

    /** 显存利用率 */
    private BigDecimal memoryUtilization;

    /** 编码器利用率 */
    private BigDecimal encoderUtilization;

    /** 解码器利用率 */
    private BigDecimal decoderUtilization;

    /** gpu当前温度 */
    private String gpuCurrentTemp;

    /** 显存当前温度 */
    private String memoryCurrentTemp;

    /** 功耗 */
    private String gpuPowerDraw;

    /** 图像当前频率 */
    private String currentGraphicsFrequency;

    /** 着色器当前频率 */
    private String currentSmFrequency;

    /** 显存当前频率 */
    private String currentMemoryFrequency;

    /** 视频当前频率 */
    private String currentVideoFrequency;

    /** 创建时间 */
    private Timestamp createTime;

    /** 修改时间 */
    private Timestamp updateTime;
}