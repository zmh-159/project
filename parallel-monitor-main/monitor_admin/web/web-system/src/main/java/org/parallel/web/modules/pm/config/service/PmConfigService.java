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

import org.parallel.web.modules.pm.config.domain.PmConfig;
import org.parallel.web.modules.pm.config.service.dto.PmConfigDto;
import org.parallel.web.modules.pm.config.service.dto.PmConfigQueryCriteria;
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
public interface PmConfigService {

    /**
    * 查询数据分页
    * @param criteria 条件
    * @param pageable 分页参数
    * @return Map<String,Object>
    */
    Map<String,Object> queryAll(PmConfigQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<PmConfigDto>
    */
    List<PmConfigDto> queryAll(PmConfigQueryCriteria criteria);

    /**
     * 根据ID查询
     * @param configId ID
     * @return PmConfigDto
     */
    PmConfigDto findById(Long configId);

    /**
    * 创建
    * @param resources /
    * @return PmConfigDto
    */
    PmConfigDto create(PmConfig resources);

    /**
    * 编辑
    * @param resources /
    */
    void update(PmConfig resources);

    /**
    * 多选删除
    * @param ids /
    */
    void deleteAll(Long[] ids,Long userId);

    /**
    * 导出数据
    * @param all 待导出的数据
    * @param response /
    * @throws IOException /
    */
    void download(List<PmConfigDto> all, HttpServletResponse response) throws IOException;
}