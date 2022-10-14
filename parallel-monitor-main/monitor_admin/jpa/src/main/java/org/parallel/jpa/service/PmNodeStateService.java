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
import org.parallel.jpa.domain.PmNodeState;
import org.parallel.jpa.service.dto.PmNodeAvgQueryCriteria;
import org.parallel.jpa.service.dto.PmNodeStateDto;
import org.parallel.jpa.service.dto.PmNodeStateQueryCriteria;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author yuyifade
 * @website https://el-admin.vip
 * @description 服务接口
 * @date 2021-06-18
 **/
public interface PmNodeStateService {

    /**
     * 查询数据分页
     *
     * @param criteria 条件
     * @param pageable 分页参数
     * @return Map<String, Object>
     */
    Map<String, Object> queryAll(PmNodeStateQueryCriteria criteria, Pageable pageable);

    /**
     * 查询所有数据不分页
     *
     * @param criteria 条件参数
     * @return List<PmNodeStateDto>
     */
    List<PmNodeStateDto> queryAll(PmNodeStateQueryCriteria criteria);

    /**
     * 根据ID查询
     *
     * @param nodeStateId ID
     * @return PmNodeStateDto
     */
    PmNodeStateDto findById(Long nodeStateId);

    /**
     * 按天对节点数据进行平均
     *
     * @return List<String>
     */
    Map<String, Object> queryDailyTotalUsage(PmNodeAvgQueryCriteria criteria);

    /**
     * 创建
     *
     * @param resources /
     * @return PmNodeStateDto
     */
    PmNodeStateDto create(PmNodeState resources);

    /**
     * 编辑
     *
     * @param resources /
     */
    void update(PmNodeState resources);

    /**
     * 多选删除
     *
     * @param ids /
     */
    void deleteAll(Long[] ids);

    void deleteByNodeId(Long nodeId);

    /**
     * 导出数据
     *
     * @param all      待导出的数据
     * @param response /
     * @throws IOException /
     */
    void download(List<PmNodeStateDto> all, HttpServletResponse response) throws IOException;
}