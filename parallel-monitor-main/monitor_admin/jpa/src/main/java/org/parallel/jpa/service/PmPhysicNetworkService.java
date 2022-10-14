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

import org.parallel.jpa.domain.PmPhysicNetwork;
import org.parallel.jpa.service.dto.PmPhysicNetworkDto;
import org.parallel.jpa.service.dto.PmPhysicNetworkQueryCriteria;
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
public interface PmPhysicNetworkService {

    /**
    * 查询数据分页
    * @param criteria 条件
    * @param pageable 分页参数
    * @return Map<String,Object>
    */
    Map<String,Object> queryAll(PmPhysicNetworkQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<PmPhysicNetworkDto>
    */
    List<PmPhysicNetworkDto> queryAll(PmPhysicNetworkQueryCriteria criteria);

    /**
     * 根据ID查询
     * @param physicNetworkId ID
     * @return PmPhysicNetworkDto
     */
    PmPhysicNetworkDto findById(Long physicNetworkId);

    /**
    * 创建
    * @param resources /
    * @return PmPhysicNetworkDto
    */
    PmPhysicNetworkDto create(PmPhysicNetwork resources);

    /**
    * 编辑
    * @param resources /
    */
    void update(PmPhysicNetwork resources);

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
    void download(List<PmPhysicNetworkDto> all, HttpServletResponse response) throws IOException;
}