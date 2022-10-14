package org.parallel.web.modules.bmc;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.parallel.jpa.domain.PmNodeMemory;
import org.parallel.jpa.service.dto.PmNodeMemoryDto;
import org.parallel.web.modules.bmc.jpa.domain.*;
import org.parallel.web.modules.bmc.jpa.repository.*;
import org.parallel.web.modules.bmc.jpa.service.*;
import org.parallel.web.modules.bmc.jpa.service.dto.*;
import org.parallel.web.modules.bmc.jpa.service.mapstruct.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.*;

@Service
@RequiredArgsConstructor
@Data
@Slf4j
public class PbNodeSave {
    private final PbNodeService pbNodeService;
    private final PbNodeRepository pbNodeRepository;
    private final PbNodeMapper pbNodeMapper;

    private final PbNodeCpuService pbNodeCpuService;
    private final PbNodeCpuRepository pbNodeCpuRepository;
    private final PbNodeCpuMapper pbNodeCpuMapper;

    private final PbNodeGpuService pbNodeGpuService;
    private final PbNodeGpuRepository pbNodeGpuRepository;
    private final PbNodeGpuMapper pbNodeGpuMapper;

    private final PbNodePhysicNetworkService pbNodePhysicNetworkService;
    private final PbNodePhysicNetworkRepository pbNodePhysicNetworkRepository;
    private final PbNodePhysicNetworkMapper pbNodePhysicNetworkMapper;

    private final PbNodeMemoryService pbNodeMemoryService;
    private final PbNodeMemoryRepository pbNodeMemoryRepository;
    private final PbNodeMemoryMapper pbNodeMemoryMapper;

    public JSONObject SaveStaticInfo(JSONObject nodeInfo, String ip){
        JSONObject res = new JSONObject();
        PbNode pbNode = pbNodeRepository.findByUuid(nodeInfo.getString("uuid"));
        boolean is_exist = false;
        if(pbNode == null){
            is_exist = true;
            pbNode = new PbNode();
        }
        System.out.println(nodeInfo);
        //存储pbNode数据
        pbNode.setBmcIp(nodeInfo.getString("bmc_ip"));
        pbNode.setUuid(nodeInfo.getString("uuid"));
        pbNode.setUser(nodeInfo.getString("user"));
        pbNode.setPasswd(nodeInfo.getString("passwd"));
        pbNode.setCpuNums(nodeInfo.getInteger("cpuNums"));
        pbNode.setGpuNums(nodeInfo.getInteger("gpu_nums"));
        pbNode.setMemory(nodeInfo.getString("memory"));
        pbNode.setMemorySlot(nodeInfo.getInteger("memory_slot"));
        pbNode.setMaxExtendMemory(nodeInfo.getString("max_extend_memory"));
        pbNode.setDiskTotal(nodeInfo.getString("disk_total"));
        pbNode.setNodeType(nodeInfo.getString("node_type"));
        pbNode.setAllInfo(nodeInfo.getString("all_info"));
        pbNode.setCreateTime(Timestamp.valueOf((String) nodeInfo.get("create_time")));
        if(is_exist){
            Long maxSerialNumber = pbNodeRepository.getMaxSerialNumber();
            String sNumber = "";
            Calendar startTime = Calendar.getInstance();
            int year = startTime.get(Calendar.YEAR) % 100;
            sNumber += year;
            int month = startTime.get(Calendar.MONTH) + 1;
            if (month < 10) {
                sNumber += "0" + month;
            }
            if (maxSerialNumber == null || !sNumber.equals(maxSerialNumber / 1000000 + "")) {
                pbNode.setSerialNumber(Long.parseLong(sNumber + "000001"));
            } else {
                pbNode.setSerialNumber(maxSerialNumber + 1);
            }
            pbNode = pbNodeMapper.toEntity(pbNodeService.create(pbNode));
        } else {
            pbNode = pbNodeService.update(pbNode);
        }
        Long nodeId = pbNode.getNodeId();
        res.put("node_id", nodeId);
        //cpu
        JSONObject cpu = new JSONObject();
        JSONObject cifs = nodeInfo.getJSONObject("cpu");
        for (String cifsKey : cifs.keySet()) {
            PbNodeCpu pbNodeCpu = new PbNodeCpu();
            JSONObject cif = cifs.getJSONObject(cifsKey);
            pbNodeCpu.setNodeId(nodeId);
            pbNodeCpu.setCpuName(cif.getString("cpu_name"));
            pbNodeCpu.setCores(cif.getIntValue("cores"));
            pbNodeCpu.setArch(cif.getString("arch"));
            pbNodeCpu.setThreads(cif.getIntValue("threads"));
            pbNodeCpu.setL1Cache(cif.getString("l1_cache"));
            pbNodeCpu.setL2Cache(cif.getString("l2_cache"));
            pbNodeCpu.setL3Cache(cif.getString("l3_cache"));
            pbNodeCpu.setMainFrequency(cif.getString("main_frequency"));
            pbNodeCpu.setBoostFrequency(cif.getString("boost_frequency"));
            Long cpuId = cif.getLong("cpu_id");

            PbNodeCpuDto p = null;
            if (cpuId != null) {
                if (pbNodeCpuRepository.existsById(cpuId)) {
                    p = pbNodeCpuService.findById(cpuId);
                }
            }

            if (p != null && p.getNodeId().equals(nodeId)) {
                pbNodeCpu.setCpuId(cpuId);
                pbNodeCpuService.update(pbNodeCpu);
            } else {
                pbNodeCpu = pbNodeCpuMapper.toEntity(pbNodeCpuService.create(pbNodeCpu));
            }
            cpu.put(cifsKey, pbNodeCpu.getCpuId());

        }
        res.put("cpu", cpu);

        //gpu
        JSONObject gpu = new JSONObject();
        JSONObject gifs = nodeInfo.getJSONObject("gpu");
        for (String gifsKey : gifs.keySet()) {
            JSONObject gif = gifs.getJSONObject(gifsKey);
            PbNodeGpu pbNodeGpu = new PbNodeGpu();
            pbNodeGpu.setNodeId(nodeId);
            pbNodeGpu.setName(gif.getString("name"));
            pbNodeGpu.setUuid(gif.getString("uuid"));
            pbNodeGpu.setManufacturer(gif.getString("manufacturer"));
            pbNodeGpu.setFirmwareVersion(gif.getString("firmware_version"));
            pbNodeGpu.setSlotNumber(gif.getInteger("slot_number"));


            Long gpuId = gif.getLong("gpu_id");

            PbNodeGpuDto p = null;
            if (gpuId != null) {
                if (pbNodeGpuRepository.existsById(gpuId)) {
                    p = pbNodeGpuService.findById(gpuId);
                }
            }
            if (p != null && p.getNodeId().equals(nodeId)) {
                pbNodeGpu.setGpuId(gpuId);
                pbNodeGpuService.update(pbNodeGpu);
            } else {
                pbNodeGpu = pbNodeGpuMapper.toEntity(pbNodeGpuService.create(pbNodeGpu));
            }
            gpu.put(gifsKey, pbNodeGpu.getGpuId());
        }
        res.put("gpu", gpu);

        //物理网卡
        JSONObject physic_network = new JSONObject();
        JSONObject npnifs = nodeInfo.getJSONObject("physic_network");
        for (String npnifKey : npnifs.keySet()) {
            JSONObject npnif = npnifs.getJSONObject(npnifKey);
            PbNodePhysicNetwork pbNodePhysicNetwork = new PbNodePhysicNetwork();
            pbNodePhysicNetwork.setNodeId(nodeId);
            pbNodePhysicNetwork.setName(npnif.getString("name"));

            Long physicNetworkId = npnif.getLong("physic_network_id");
            PbNodePhysicNetworkDto p = null;
            if (physicNetworkId != null) {
                if (pbNodePhysicNetworkRepository.existsById(physicNetworkId)) {
                    p = pbNodePhysicNetworkService.findById(physicNetworkId);
                }
            }

            if (p != null && p.getNodeId().equals(nodeId)) {
                pbNodePhysicNetwork.setPhysicNetworkId(physicNetworkId);
                pbNodePhysicNetworkService.update(pbNodePhysicNetwork);
            } else {
                pbNodePhysicNetwork = pbNodePhysicNetworkMapper.toEntity(pbNodePhysicNetworkService.create(pbNodePhysicNetwork));
            }
            physic_network.put(npnifKey, pbNodePhysicNetwork.getPhysicNetworkId());
        }
        res.put("physic_network", physic_network);

        //内存
        JSONObject memory = new JSONObject();
        JSONObject memifs = nodeInfo.getJSONObject("memory_moudle");
        for (String memifKey : memifs.keySet()) {
            JSONObject memif = memifs.getJSONObject(memifKey);
            PbNodeMemory pbNodeMemory = new PbNodeMemory();
            pbNodeMemory.setNodeId(nodeId);
            pbNodeMemory.setManufacturer(memif.getString("manufacturer"));
            pbNodeMemory.setSpeed(memif.getString("speed"));
            pbNodeMemory.setCapacity(memif.getString("capacity"));
            pbNodeMemory.setHealth(memif.getString("health"));
            pbNodeMemory.setSocket(memif.getString("socket"));
            pbNodeMemory.setSlot(memif.getString("slot"));
            pbNodeMemory.setRemainingServiceLife(memif.getBigDecimal("remaining_service_life"));
            pbNodeMemory.setControllerTemp(memif.getBigDecimal("controller_temp"));
            pbNodeMemory.setMediumTemp(memif.getBigDecimal("medium_temp"));

            Long memoryId = memif.getLong("memory_id");

            PbNodeMemoryDto p = null;
            if (memoryId != null) {
                if (pbNodeMemoryRepository.existsById(memoryId)) {
                    p = pbNodeMemoryService.findById(memoryId);
                }
            }
            if (p != null && p.getNodeId().equals(nodeId)) {
                pbNodeMemory.setMemoryId(memoryId);
                pbNodeMemoryService.update(pbNodeMemory);
            } else {
                pbNodeMemory = pbNodeMemoryMapper.toEntity(pbNodeMemoryService.create(pbNodeMemory));
            }

            memory.put(memifKey, pbNodeMemory.getMemoryId());
        }
        res.put("memory_moudle", memory);
        return res;
    }

}
