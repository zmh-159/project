package org.parallel.web.modules.bmc.jpa.repository;


import org.parallel.web.modules.bmc.jpa.domain.PbNodeFan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PbNodeFanRepository extends JpaRepository<PbNodeFan, Long>, JpaSpecificationExecutor<PbNodeFan> {

}
