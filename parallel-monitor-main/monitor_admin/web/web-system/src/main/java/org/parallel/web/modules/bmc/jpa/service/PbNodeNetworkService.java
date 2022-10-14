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
package org.parallel.web.modules.bmc.jpa.service;

import org.parallel.web.modules.bmc.jpa.domain.PbNodeNetwork;
import org.parallel.web.modules.bmc.jpa.service.dto.PbNodeNetworkDto;
import org.parallel.web.modules.bmc.jpa.service.dto.PbNodeNetworkQueryCriteria;
import org.springframework.data.domain.Pageable;
import java.util.Map;
import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

/**
* @website https://el-admin.vip
* @description 服务接口
* @author ruo-yin
* @date 2022-08-09
**/
public interface PbNodeNetworkService {

    /**
    * 查询数据分页
    * @param criteria 条件
    * @param pageable 分页参数
    * @return Map<String,Object>
    */
    Map<String,Object> queryAll(PbNodeNetworkQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<PbNodeNetworkDto>
    */
    List<PbNodeNetworkDto> queryAll(PbNodeNetworkQueryCriteria criteria);

    /**
     * 根据ID查询
     * @param networkId ID
     * @return PbNodeNetworkDto
     */
    PbNodeNetworkDto findById(Long networkId);

    /**
    * 创建
    * @param resources /
    * @return PbNodeNetworkDto
    */
    PbNodeNetworkDto create(PbNodeNetwork resources);

    /**
    * 编辑
    * @param resources /
    */
    void update(PbNodeNetwork resources);

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
    void download(List<PbNodeNetworkDto> all, HttpServletResponse response) throws IOException;
}