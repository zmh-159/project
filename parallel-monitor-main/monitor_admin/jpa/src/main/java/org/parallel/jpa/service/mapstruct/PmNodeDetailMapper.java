
package org.parallel.jpa.service.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.parallel.web.base.BaseMapper;
import org.parallel.jpa.domain.PmNode;
import org.parallel.jpa.service.dto.PmNodeDetailDto;

/**
 * @author yuyifade
 * @description
 * @date 2021/6/30 下午6:51
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PmNodeDetailMapper extends BaseMapper<PmNodeDetailDto, PmNode> {

}