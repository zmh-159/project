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

import com.alibaba.fastjson.JSONObject;
import org.parallel.jpa.domain.PmNode;
import org.parallel.jpa.service.dto.PmNodeDetailDto;
import org.parallel.jpa.service.dto.PmNodeDto;
import org.parallel.jpa.service.dto.PmNodeQueryCriteria;
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
public interface PmNodeService {

    /**
    * 查询数据分页
    * @param criteria 条件
    * @param pageable 分页参数
    * @return Map<String,Object>
    */
    Map<String,Object> queryAll(PmNodeQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<PmNodeDto>
    */
    List<PmNodeDto> queryAll(PmNodeQueryCriteria criteria);

    long queryNodeCount();

    /**
     * 按是否存活分类排序
     * @return List<PmNodeDto>
     */
    JSONObject querySortByAlive(String aliveNodes, int start, int num);



    /**
     * 查询所有详细数据
     * @param criteria 条件参数
     * @return List<PmNodeDetailDto>
     */
    List<PmNodeDetailDto> queryDetail(PmNodeQueryCriteria criteria);

    /**
     * 根据ID查询
     * @param nodeId ID
     * @return PmNodeDto
     */
    PmNodeDto findById(Long nodeId);

    /**
     * 根据ID查询
     * @param uuid ID
     * @return PmNodeDto
     */
    PmNodeDto findByUuid(String uuid);

    Long getMaxSerialNumber();

    JSONObject getOnlineCpus(String aliveNodes);

    JSONObject getOnlineDisk(String aliveNodes);

    JSONObject getRealTimeInfo();

    /**
    * 创建
    * @param resources /
    * @return PmNodeDto
    */
    PmNodeDto create(PmNode resources);

    /**
    * 编辑
    * @param resources /
    */
    PmNode update(PmNode resources);

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
    void download(List<PmNodeDto> all, HttpServletResponse response) throws IOException;
}