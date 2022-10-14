package org.parallel.web.modules.bmc.jpa.service.impl;

import lombok.RequiredArgsConstructor;
import org.parallel.web.modules.bmc.jpa.domain.PbNodeFan;
import org.parallel.web.modules.bmc.jpa.repository.PbNodeFanRepository;
import org.parallel.web.modules.bmc.jpa.service.PbNodeFanService;
import org.parallel.web.modules.bmc.jpa.service.dto.PbNodeFanDto;
import org.parallel.web.modules.bmc.jpa.service.dto.PbNodeFanQueryCriteria;
import org.parallel.web.modules.bmc.jpa.service.mapstruct.PbNodeFanMapper;
import org.parallel.web.utils.PageUtil;
import org.parallel.web.utils.QueryHelp;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PbNodeFanServiceImpl  implements PbNodeFanService {
    private final PbNodeFanRepository pbNodeFanRepository;
    private final PbNodeFanMapper pbNodeFanMapper;

    @Override
    public Map<String,Object> queryAll(PbNodeFanQueryCriteria criteria, Pageable pageable){
        Page<PbNodeFan> page = pbNodeFanRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(pbNodeFanMapper::toDto));
    }

    @Override
    public List<PbNodeFanDto> queryAll(PbNodeFanQueryCriteria criteria) {
        return pbNodeFanMapper.toDto(pbNodeFanRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    public PbNodeFanDto findById(Long nodeId) {
        return null;
    }

    @Override
    public PbNodeFanDto create(PbNodeFan resources) {
        return null;
    }

    @Override
    public PbNodeFan update(PbNodeFan resources) {
        return null;
    }

    @Override
    public void deleteAll(Long[] ids) {

    }

    @Override
    public void download(List<PbNodeFanDto> all, HttpServletResponse response) throws IOException {

    }
}
