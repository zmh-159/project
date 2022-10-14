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
package org.parallel.jpa.service;

import org.parallel.jpa.domain.PmNodeCpuState;
import org.parallel.jpa.service.dto.PmNodeCpuAvgQueryCriteria;
import org.parallel.jpa.service.dto.PmNodeCpuStateDto;
import org.parallel.jpa.service.dto.PmNodeCpuStateQueryCriteria;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
* @website https://el-admin.vip
* @description 服务接口
* @author yuyifade
* @date 2021-06-18
**/
public interface PmNodeCpuStateService {

    /**
    * 查询数据分页
    * @param criteria 条件
    * @param pageable 分页参数
    * @return Map<String,Object>
    */
    Map<String,Object> queryAll(PmNodeCpuStateQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<PmNodeCpuStateDto>
    */
    List<PmNodeCpuStateDto> queryAll(PmNodeCpuStateQueryCriteria criteria);

    /**
     * 根据ID查询
     * @param cpuStateId ID
     * @return PmNodeCpuStateDto
     */
    PmNodeCpuStateDto findById(Long cpuStateId);

    /**
    * 创建
    * @param resources /
    * @return PmNodeCpuStateDto
    */
    PmNodeCpuStateDto create(PmNodeCpuState resources);

    /**
    * 编辑
    * @param resources /
    */
    void update(PmNodeCpuState resources);

    /**
    * 多选删除
    * @param ids /
    */
    void deleteAll(Long[] ids);

    /**
    * 导出数据
    * @param all 待导出的数据
    * @param response /
    * @throws IOException /
    */
    void download(List<PmNodeCpuStateDto> all, HttpServletResponse response) throws IOException;

    /**
     * cpu平均温度 核心使用率 核心用户使用率 核心频率
     */
    Map<String,Object> avg(PmNodeCpuAvgQueryCriteria criteria);

    /**
     * 查询符合条件的条数
     */
    Long queryNums(PmNodeCpuAvgQueryCriteria criteria);
}