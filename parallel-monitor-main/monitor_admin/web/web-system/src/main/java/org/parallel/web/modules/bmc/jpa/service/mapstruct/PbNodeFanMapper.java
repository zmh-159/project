package org.parallel.web.modules.bmc.jpa.service.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.parallel.web.base.BaseMapper;
import org.parallel.web.modules.bmc.jpa.domain.PbNodeFan;
import org.parallel.web.modules.bmc.jpa.service.dto.PbNodeFanDto;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PbNodeFanMapper  extends BaseMapper<PbNodeFanDto, PbNodeFan> {
}
