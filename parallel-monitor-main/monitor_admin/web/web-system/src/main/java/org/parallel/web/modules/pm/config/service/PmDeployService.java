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


import org.parallel.web.modules.pm.config.domain.PmDeploy;
import org.parallel.web.modules.pm.config.service.dto.PmDeployDto;
import org.parallel.web.modules.pm.config.service.dto.PmDeployQueryCriteria;
import org.springframework.data.domain.Pageable;

import java.util.Map;
import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

/**
 * @author yuyifade
 * @website https://el-admin.vip
 * @description 服务接口
 * @date 2021-09-24
 **/
public interface PmDeployService {

    /**
     * 查询数据分页
     *
     * @param criteria 条件
     * @param pageable 分页参数
     * @return Map<String, Object>
     */
    Map<String, Object> queryAll(PmDeployQueryCriteria criteria, Pageable pageable);

    /**
     * 查询所有数据不分页
     *
     * @param criteria 条件参数
     * @return List<PmDeployDto>
     */
    List<PmDeployDto> queryAll(PmDeployQueryCriteria criteria);

    /**
     * 根据ID查询
     *
     * @param deployId ID
     * @return PmDeployDto
     */
    PmDeployDto findById(Long deployId);

    /**
     * 创建
     *
     * @param resources /
     * @return PmDeployDto
     */
    PmDeployDto create(PmDeploy resources);

    /**
     * 编辑
     *
     * @param resources /
     */
    void update(PmDeploy resources);

    /**
     * 添加安装记录
     *
     * @param node_id   /
     * @param deploy_id /
     */
    void installRecord(long deploy_id, long node_id);

    /**
     * 删除安装记录（卸载）
     *
     * @param node_id   /
     * @param deploy_id /
     */
    void deleteRecord(long deploy_id, long node_id);

    /**
     * 多选删除
     *
     * @param ids /
     */
    void deleteAll(Long[] ids);

    /**
     * 导出数据
     *
     * @param all      待导出的数据
     * @param response /
     * @throws IOException /
     */
    void download(List<PmDeployDto> all, HttpServletResponse response) throws IOException;
}