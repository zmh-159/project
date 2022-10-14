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
package org.parallel.web.modules.pm.config.service;

import org.parallel.web.modules.pm.config.domain.PmChartScheme;
import org.parallel.web.modules.pm.config.service.dto.PmChartSchemeDto;
import org.parallel.web.modules.pm.config.service.dto.PmChartSchemeQueryCriteria;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

/**
 * @author yuyifade
 * @website https://el-admin.vip
 * @description 服务接口
 * @date 2021-07-07
 **/
public interface PmChartSchemeService {

    /**
     * 查询数据分页
     *
     * @param criteria 条件
     * @param pageable 分页参数
     * @return Map<String, Object>
     */
    Map<String, Object> queryAll(PmChartSchemeQueryCriteria criteria, Pageable pageable);

    /**
     * 查询所有数据不分页
     *
     * @param criteria 条件参数
     * @return List<PmChartSchemeDto>
     */
    List<PmChartSchemeDto> queryAll(PmChartSchemeQueryCriteria criteria);

    /**
     * 根据ID查询
     *
     * @param chartSchemeId ID
     * @return PmChartSchemeDto
     */
    PmChartSchemeDto findById(Long chartSchemeId);

    /**
     * 创建
     *
     * @param resources /
     * @return PmChartSchemeDto
     */
    PmChartSchemeDto create(PmChartScheme resources);

    /**
     * 编辑
     *
     * @param resources /
     */
    void update(PmChartScheme resources);

    /**
     * 多选删除
     *
     * @param ids /
     */
    void deleteAll(Long[] ids,Long userId);

    /**
     * 导出数据
     *
     * @param all      待导出的数据
     * @param response /
     * @throws IOException /
     */
    void download(List<PmChartSchemeDto> all, HttpServletResponse response) throws IOException;
}