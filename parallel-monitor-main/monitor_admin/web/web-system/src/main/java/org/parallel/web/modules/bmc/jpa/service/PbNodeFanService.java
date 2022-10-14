package org.parallel.web.modules.bmc.jpa.service;

import org.parallel.web.modules.bmc.jpa.domain.PbNodeFan;
import org.parallel.web.modules.bmc.jpa.service.dto.PbNodeFanDto;
import org.parallel.web.modules.bmc.jpa.service.dto.PbNodeFanQueryCriteria;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface PbNodeFanService {
    /**
     * 查询数据分页
     * @param criteria 条件
     * @param pageable 分页参数
     * @return Map<String,Object>
     */
    Map<String,Object> queryAll(PbNodeFanQueryCriteria criteria, Pageable pageable);

    /**
     * 查询所有数据不分页
     * @param criteria 条件参数
     * @return List<PbNodeDto>
     */
    List<PbNodeFanDto> queryAll(PbNodeFanQueryCriteria criteria);

    /**
     * 根据ID查询
     * @param nodeId ID
     * @return PbNodeDto
     */
    PbNodeFanDto findById(Long nodeId);

    /**
     * 创建
     * @param resources /
     * @return PbNodeDto
     */
    PbNodeFanDto create(PbNodeFan resources);

    /**
     * 编辑
     * @param resources /
     */
    PbNodeFan update(PbNodeFan resources);

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
    void download(List<PbNodeFanDto> all, HttpServletResponse response) throws IOException;
}
