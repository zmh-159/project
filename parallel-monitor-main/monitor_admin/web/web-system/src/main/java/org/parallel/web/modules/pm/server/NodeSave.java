package org.parallel.web.modules.pm.server;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.parallel.jpa.domain.*;
import org.parallel.jpa.repository.*;
import org.parallel.jpa.service.*;
import org.parallel.jpa.service.dto.*;
import org.parallel.jpa.service.mapstruct.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.*;


/**
 * @author yuyifade
 * @description
 * @date 2021/6/25 下午9:24
 */
@Service
@RequiredArgsConstructor
@Data
@Slf4j
public class NodeSave {
    private final OnlineNode nic;

    private final PmNodeService pmNodeService;
    private final PmNodeRepository pmNodeRepository;
    private final PmNodeMapper pmNodeMapper;
    private final JdbcTemplate jdbcTemplate;
    private final DruidDataSource dataSource;

    private final PmNodeCpuService pmNodeCpuService;
    private final PmNodeCpuMapper pmNodeCpuMapper;
    private final PmNodeCpuRepository pmNodeCpuRepository;

    private final PmNodeGpuService pmNodeGpuService;
    private final PmNodeGpuMapper pmNodeGpuMapper;
    private final PmNodeGpuRepository pmNodeGpuRepository;

    private final PmPhysicNetworkMapper pmPhysicNetworkMapper;
    private final PmPhysicNetworkService pmPhysicNetworkService;
    private final PmPhysicNetworkRepository pmPhysicNetworkRepository;

    private final PmNetworkMapper pmNetworkMapper;
    private final PmNetworkService pmNetworkService;
    private final PmNetworkRepository pmNetworkRepository;

    private final PmNodeMemoryMapper pmNodeMemoryMapper;
    private final PmNodeMemoryService pmNodeMemoryService;
    private final PmNodeMemoryRepository pmNodeMemoryRepository;

    private final PmNodeDiskMapper pmNodeDiskMapper;
    private final PmNodeDiskService pmNodeDiskService;
    private final PmNodeDiskRepository pmNodeDiskRepository;

    private final PmNodeStateMapper pmNodeStateMapper;
    private final PmNodeStateService pmNodeStateService;

    private final PmDiskStateMapper pmDiskStateMapper;
    private final PmDiskStateService pmDiskStateService;

    private final PmNetworkStateMapper pmNetworkStateMapper;
    private final PmNetworkStateService pmNetworkStateService;

    private final PmNodeCpuStateService pmNodeCpuStateService;
    private final PmNodeCpuStateMapper pmNodeCpuStateMapper;

    private final PmNodeGpuStateService pmNodeGpuStateService;
    private final PmNodeGpuStateMapper pmNodeGpuStateMapper;

//    private final PmCoreStateMapper pmCoreStateMapper;
//    private final PmCoreStateService pmCoreStateService;


    @Transactional(rollbackFor = Exception.class)
    public JSONObject saveStaticInfo(JSONObject nodeInfo, String ip) {
        JSONObject res = new JSONObject();
        PmNode pmNode = pmNodeRepository.findByUuid(nodeInfo.getString("uuid"));
        boolean is_create = false;
        if (pmNode == null) {//判断该节点是否已存在
            is_create = true;
            pmNode = new PmNode();
        }
        System.out.println(nodeInfo);
        //存储pmNode数据
        pmNode.setIp(ip);
        pmNode.setUuid(nodeInfo.getString("uuid"));
        pmNode.setCpuNums(nodeInfo.getIntValue("cpu_nums"));
        pmNode.setGpuNums(nodeInfo.getIntValue("gpu_nums"));
        pmNode.setPerformance(nodeInfo.getString("performance"));
        pmNode.setMaxExtendMemory(nodeInfo.getString("max_extend_memory"));
        pmNode.setMemory(nodeInfo.getString("memory"));
        pmNode.setMemorySlot(nodeInfo.getIntValue("memory_slot"));
        pmNode.setOsName(nodeInfo.getString("os_name"));
        pmNode.setOsVersion(nodeInfo.getString("os_version"));
        pmNode.setOsBit(nodeInfo.getIntValue("os_bit"));
        pmNode.setHostName(nodeInfo.getString("host_name"));
        pmNode.setDiskTotal(nodeInfo.getString("disk_total"));
        pmNode.setStartTime(Timestamp.valueOf((String) nodeInfo.get("start_time")));
        if (is_create) {
            //存储序号
            Long maxSerialNumber = pmNodeRepository.getMaxSerialNumber();
            String sNumber = "";
            Calendar startTime = Calendar.getInstance();
            int year = startTime.get(Calendar.YEAR) % 100;
            sNumber += year;
            int month = startTime.get(Calendar.MONTH) + 1;
            if (month < 10) {
                sNumber += "0" + month;
            }
            if (maxSerialNumber == null || !sNumber.equals(maxSerialNumber / 1000000 + "")) {
                pmNode.setSerialNumber(Long.parseLong(sNumber + "000001"));
            } else {
                pmNode.setSerialNumber(maxSerialNumber + 1);
            }
            pmNode = pmNodeMapper.toEntity(pmNodeService.create(pmNode));
        } else {
            pmNode = pmNodeService.update(pmNode);
        }

        Long nodeId = pmNode.getNodeId();
        res.put("node_id", nodeId);
        //存储cpu信息
        JSONObject cpu = new JSONObject();
        JSONObject cifs = nodeInfo.getJSONObject("cpu");
        for (String cifsKey : cifs.keySet()) {
            PmNodeCpu pmNodeCpu = new PmNodeCpu();
            JSONObject cif = cifs.getJSONObject(cifsKey);
            pmNodeCpu.setNodeId(nodeId);
            pmNodeCpu.setCpuName(cif.getString("cpu_name"));
            pmNodeCpu.setCores(cif.getIntValue("cores"));
            pmNodeCpu.setArch(cif.getString("arch"));
            pmNodeCpu.setThreads(cif.getIntValue("threads"));
            pmNodeCpu.setL1ICache(cif.getString("l1_i_cache"));
            pmNodeCpu.setL1DCache(cif.getString("l1_d_cache"));
            pmNodeCpu.setL2Cache(cif.getString("l2_cache"));
            pmNodeCpu.setL3Cache(cif.getString("l3_cache"));
            pmNodeCpu.setMainFrequency(cif.getString("main_frequency"));
            pmNodeCpu.setBoostFrequency(cif.getString("boost_frequency"));
            Long cpuId = cif.getLong("cpu_id");

            PmNodeCpuDto p = null;
            if (cpuId != null) {
                if (pmNodeCpuRepository.existsById(cpuId)) {
                    p = pmNodeCpuService.findById(cpuId);
                }
            }

            if (p != null && p.getNodeId().equals(nodeId)) {
                pmNodeCpu.setCpuId(cpuId);
                pmNodeCpuService.update(pmNodeCpu);
            } else {
                pmNodeCpu = pmNodeCpuMapper.toEntity(pmNodeCpuService.create(pmNodeCpu));
            }
            cpu.put(cifsKey, pmNodeCpu.getCpuId());

        }
        res.put("cpu", cpu);

        //存储gpu信息
        JSONObject gpu = new JSONObject();
        JSONObject gifs = nodeInfo.getJSONObject("gpu");
        for (String gifsKey : gifs.keySet()) {
            JSONObject gif = gifs.getJSONObject(gifsKey);
            PmNodeGpu pmNodeGpu = new PmNodeGpu();
            pmNodeGpu.setNodeId(nodeId);
            pmNodeGpu.setName(gif.getString("name"));
            pmNodeGpu.setUuid(gif.getString("uuid"));
            pmNodeGpu.setManufacturer(gif.getString("manufacturer"));
            pmNodeGpu.setDriverVersion(gif.getString("driver_version"));
            pmNodeGpu.setAccountingModeBufferSize(gif.getLong("accounting_mode_buffer_size"));
            pmNodeGpu.setVbiosVersion(gif.getString("vbios_version"));
            pmNodeGpu.setMemory(gif.getString("memory"));
            pmNodeGpu.setGpuShutdownTemp(gif.getString("gpu_shutdown_temp"));
            pmNodeGpu.setGpuSlowdownTemp(gif.getString("gpu_slowdown_temp"));
            pmNodeGpu.setGpuMaxOperatingTemp(gif.getString("gpu_max_operating_temp"));
            pmNodeGpu.setMemoryMaxOperatingTemp(gif.getString("memory_max_operating_temp"));
            pmNodeGpu.setMaxPowerLimit(gif.getString("max_power_limit"));
            pmNodeGpu.setMinPowerLimit(gif.getString("min_power_limit"));
            pmNodeGpu.setMaxGraphicsFrequency(gif.getString("max_graphics_frequency"));
            pmNodeGpu.setMaxSmFrequency(gif.getString("max_sm_frequency"));
            pmNodeGpu.setMaxMemoryFrequency(gif.getString("max_memory_frequency"));
            pmNodeGpu.setMaxVideoFrequency(gif.getString("max_video_frequency"));

            Long gpuId = gif.getLong("gpu_id");

            PmNodeGpuDto p = null;
            if (gpuId != null) {
                if (pmNodeGpuRepository.existsById(gpuId)) {
                    p = pmNodeGpuService.findById(gpuId);
                }
            }
            if (p != null && p.getNodeId().equals(nodeId)) {
                pmNodeGpu.setGpuId(gpuId);
                pmNodeGpuService.update(pmNodeGpu);
            } else {
                pmNodeGpu = pmNodeGpuMapper.toEntity(pmNodeGpuService.create(pmNodeGpu));
            }
            gpu.put(gifsKey, pmNodeGpu.getGpuId());
        }
        res.put("gpu", gpu);


        //存储物理网卡信息
        JSONObject physic_network = new JSONObject();
        JSONObject npnifs = nodeInfo.getJSONObject("physic_network");
        for (String npnifKey : npnifs.keySet()) {
            JSONObject npnif = npnifs.getJSONObject(npnifKey);
            PmPhysicNetwork pmPhysicNetwork = new PmPhysicNetwork();
            pmPhysicNetwork.setNodeId(nodeId);
            pmPhysicNetwork.setName(npnif.getString("name"));

            Long physicNetworkId = npnif.getLong("physic_network_id");
            PmPhysicNetworkDto p = null;
            if (physicNetworkId != null) {
                if (pmPhysicNetworkRepository.existsById(physicNetworkId)) {
                    p = pmPhysicNetworkService.findById(physicNetworkId);
                }
            }

            if (p != null && p.getNodeId().equals(nodeId)) {
                pmPhysicNetwork.setPhysicNetworkId(physicNetworkId);
                pmPhysicNetworkService.update(pmPhysicNetwork);
            } else {
                pmPhysicNetwork = pmPhysicNetworkMapper.toEntity(pmPhysicNetworkService.create(pmPhysicNetwork));
            }
            physic_network.put(npnifKey, pmPhysicNetwork.getPhysicNetworkId());
        }
        res.put("physic_network", physic_network);

        //存储虚拟网卡信息
        JSONObject network = new JSONObject();
        JSONObject nnifs = nodeInfo.getJSONObject("network");
        for (String nnifKey : nnifs.keySet()) {
            JSONObject nnif = nnifs.getJSONObject(nnifKey);
            PmNetwork pmNetwork = new PmNetwork();
            pmNetwork.setNodeId(nodeId);
            pmNetwork.setIp(nnif.getString("ip"));
            pmNetwork.setMac(nnif.getString("mac"));
            pmNetwork.setName(nnif.getString("name"));
            Long networkId = nnif.getLong("network_id");

            PmNetworkDto p = null;
            if (networkId != null) {
                if (pmNetworkRepository.existsById(networkId)) {
                    p = pmNetworkService.findById(networkId);
                }
            }

            if (p != null && p.getNodeId().equals(nodeId)) {
                pmNetwork.setNetworkId(networkId);
                pmNetworkService.update(pmNetwork);
            } else {
                pmNetwork = pmNetworkMapper.toEntity(pmNetworkService.create(pmNetwork));
            }
            network.put(nnifKey, pmNetwork.getNetworkId());
        }
        res.put("network", network);

        //存储内存条信息
        JSONObject memory = new JSONObject();
        JSONObject memifs = nodeInfo.getJSONObject("memory_moudle");
        for (String memifKey : memifs.keySet()) {
            JSONObject memif = memifs.getJSONObject(memifKey);
            PmNodeMemory pmNodeMemory = new PmNodeMemory();
            pmNodeMemory.setNodeId(nodeId);
            pmNodeMemory.setManufacturer(memif.getString("manufacturer"));
            pmNodeMemory.setMemoryType(memif.getString("memory_type"));
            pmNodeMemory.setSpeed(memif.getString("speed"));
            pmNodeMemory.setCapacity(memif.getString("capacity"));

            Long memoryId = memif.getLong("memory_id");

            PmNodeMemoryDto p = null;
            if (memoryId != null) {
                if (pmNodeMemoryRepository.existsById(memoryId)) {
                    p = pmNodeMemoryService.findById(memoryId);
                }
            }
            if (p != null && p.getNodeId().equals(nodeId)) {
                pmNodeMemory.setMemoryId(memoryId);
                pmNodeMemoryService.update(pmNodeMemory);
            } else {
                pmNodeMemory = pmNodeMemoryMapper.toEntity(pmNodeMemoryService.create(pmNodeMemory));
            }

            memory.put(memifKey, pmNodeMemory.getMemoryId());
        }
        res.put("memory_moudle", memory);

        //存储磁盘信息
        JSONObject disk = new JSONObject();
        JSONObject diskifs = nodeInfo.getJSONObject("disk");
        for (String diskifKey : diskifs.keySet()) {
            JSONObject diskif = diskifs.getJSONObject(diskifKey);
            PmNodeDisk pmNodeDisk = new PmNodeDisk();
            pmNodeDisk.setNodeId(nodeId);
            pmNodeDisk.setName(diskif.getString("name"));
            pmNodeDisk.setCapacity(diskif.getString("capacity"));

            Long diskId = diskif.getLong("disk_id");

            PmNodeDiskDto p = null;
            if (diskId != null) {
                if (pmNodeDiskRepository.existsById(diskId)) {
                    p = pmNodeDiskService.findById(diskId);
                }
            }
            if (p != null && p.getNodeId().equals(nodeId)) {
                pmNodeDisk.setDiskId(diskId);
                pmNodeDiskService.update(pmNodeDisk);
            } else {
                pmNodeDisk = pmNodeDiskMapper.toEntity(pmNodeDiskService.create(pmNodeDisk));
            }

            disk.put(diskifKey, pmNodeDisk.getDiskId());
        }
        res.put("disk", disk);
        //todo 更新在线节点ip
//        nic.updateIds(nodeId);
        return res;
    }

    private List<JSONObject> totalInfo = new Vector<>();
    private static long preSaveTime = 0L;//记录上次插入数据库时间
    private final Integer mutex1 = 0;
    private final Integer mutex2 = 0;
    private final Hashtable<Long, NodeTempBuffer> ntbList = new Hashtable<>();
    private final Hashtable<Long, Hashtable<String, Long>> alarmIdMap = new Hashtable<>();
    private final int addNums = 100;

    @Transactional(rollbackFor = Exception.class)
    @Async
    public void fastInsert(JSONObject info) {
        /** 检查节点是否接入系统 **/
        Long nodeId = info.getLong("node_id");
        //todo
//        if (nodeId == null || !nic.hasId(nodeId)) {
//            log.warn("节点" + nodeId + "未接入系统！");
//            return;
//        }
        //todo 定义缓冲区大小
        synchronized (mutex1) {
            NodeTempBuffer tmp = ntbList.containsKey(nodeId) ? ntbList.get(nodeId) : new NodeTempBuffer(1000);
            tmp.add(info);
            ntbList.put(nodeId, tmp);
        }
        /**检查是否存储**/
        if (info.getIntValue("save") == 0) {
            log.info("drop data!");
            return;
        }

        totalInfo.add(info);
        long now = System.currentTimeMillis();
        if (totalInfo.size() < addNums && now - preSaveTime < 5000) {
            return;
        }
        List<JSONObject> infos;
        synchronized (mutex2) {
            infos = totalInfo;
            totalInfo = new Vector<>();
            preSaveTime = now;
        }

        String sql = "INSERT INTO pm_node_state" +
                "(node_id,process_nums,cpu_idle,cpu_system,cpu_user,memory_total,memory_used,memory_rate,swap_total,swap_used,swap_rate,disk_total," +
                "disk_used,disk_rate,gpu_utilization,memory_utilization,alarm_state,create_time,update_time) VALUES " +
                "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        try (Connection con = dataSource.getConnection()) {
            con.setAutoCommit(false);
            PreparedStatement pstmt = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            for (JSONObject o : infos) {
                pstmt.setLong(1, o.getLong("node_id"));
                pstmt.setInt(2, o.getIntValue("process_nums"));

                pstmt.setBigDecimal(3, o.getBigDecimal("cpu_idle"));
                pstmt.setBigDecimal(4, o.getBigDecimal("cpu_system"));
                pstmt.setBigDecimal(5, o.getBigDecimal("cpu_user"));

                pstmt.setDouble(6, o.getDouble("memory_total"));
                pstmt.setDouble(7, o.getDouble("memory_used"));
                pstmt.setBigDecimal(8, o.getBigDecimal("memory_rate"));

                pstmt.setDouble(9, o.getDouble("swap_total"));
                pstmt.setDouble(10, o.getDouble("swap_used"));
                pstmt.setBigDecimal(11, o.getBigDecimal("swap_rate"));

                pstmt.setDouble(12, o.getDouble("disk_total"));
                pstmt.setDouble(13, o.getDouble("disk_used"));
                pstmt.setBigDecimal(14, o.getBigDecimal("disk_rate"));

                pstmt.setBigDecimal(15, o.getBigDecimal("gpu_utilization"));
                pstmt.setBigDecimal(16, o.getBigDecimal("memory_utilization"));
                pstmt.setString(17, o.getString("alarm_state"));

                pstmt.setTimestamp(18, o.getTimestamp("create_time"));
                pstmt.setTimestamp(19, o.getTimestamp("create_time"));
                pstmt.addBatch();
            }
            pstmt.executeBatch();
            con.commit();

            ResultSet rs = pstmt.getGeneratedKeys();
            long nodeStateIds[] = new long[infos.size()];
            int i = 0;
            while (rs.next()) {
                nodeStateIds[i++] = rs.getLong(1);
            }
            rs.close();
            pstmt.close();


            sql = "INSERT INTO pm_node_disk_state" +
                    "(disk_id,node_state_id,read_speed,write_speed,create_time,update_time) VALUES " +
                    "(?,?,?,?,?,?)";
            pstmt = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            i = 0;
            for (JSONObject o : infos) {
                JSONObject diskStates = o.getJSONObject("disk_state");
                for (String s : diskStates.keySet()) {
                    JSONObject diskState = diskStates.getJSONObject(s);
                    pstmt.setLong(1, diskState.getLong("disk_id"));
                    pstmt.setLong(2, nodeStateIds[i]);
                    pstmt.setDouble(3, diskState.getDoubleValue("read_speed"));
                    pstmt.setDouble(4, diskState.getDoubleValue("write_speed"));
                    pstmt.setTimestamp(5, o.getTimestamp("create_time"));
                    pstmt.setTimestamp(6, o.getTimestamp("create_time"));
                    pstmt.addBatch();
                }
                i++;
            }
            pstmt.executeBatch();
            con.commit();
            pstmt.close();

            sql = "INSERT INTO pm_node_network_state" +
                    "(network_id,node_state_id,up_package,down_package,up_speed,down_speed,create_time,update_time) VALUES " +
                    "(?,?,?,?,?,?,?,?)";
            pstmt = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            i = 0;
            for (JSONObject o : infos) {
                JSONObject networkStates = o.getJSONObject("network_state");
                for (String s : networkStates.keySet()) {
                    JSONObject networkState = networkStates.getJSONObject(s);

                    pstmt.setLong(1, networkState.getLong("network_id"));
                    pstmt.setLong(2, nodeStateIds[i]);
                    pstmt.setLong(3, networkState.getLong("up_package"));
                    pstmt.setLong(4, networkState.getLong("down_package"));

                    pstmt.setLong(5, networkState.getLong("up_speed"));
                    pstmt.setLong(6, networkState.getLong("down_speed"));

                    pstmt.setTimestamp(7, o.getTimestamp("create_time"));
                    pstmt.setTimestamp(8, o.getTimestamp("create_time"));
                    pstmt.addBatch();
                }
                i++;
            }
            pstmt.executeBatch();
            con.commit();
            pstmt.close();

            sql = "INSERT INTO pm_node_cpu_state" +
                    "(cpu_id,node_state_id,core_state,temperature,create_time,update_time) VALUES " +
                    "(?,?,?,?,?,?)";
            pstmt = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            i = 0;
            int k = 0;
            for (JSONObject o : infos) {
                JSONObject cpuStates = o.getJSONObject("cpu_state");
                for (String s : cpuStates.keySet()) {
                    if (s.equals("temperature")) {
                        continue;
                    }
                    k++;
                    JSONObject cpuState = cpuStates.getJSONObject(s);

                    pstmt.setLong(1, cpuState.getLong("cpu_id"));
                    pstmt.setLong(2, nodeStateIds[i]);
                    pstmt.setString(3, cpuState.getString("core_state"));
                    pstmt.setBigDecimal(4, cpuState.getBigDecimal("temperature"));
                    pstmt.setTimestamp(5, o.getTimestamp("create_time"));
                    pstmt.setTimestamp(6, o.getTimestamp("create_time"));
                    pstmt.addBatch();
                }
                i++;
            }
            pstmt.executeBatch();
            con.commit();
            pstmt.close();

            sql = "INSERT INTO pm_node_gpu_state" +
                    "(gpu_id,node_state_id,tx_throughput,rx_throughput,fan_speed,performance_state,memory_used,gpu_utilization,memory_utilization," +
                    "encoder_utilization,decoder_utilization,gpu_current_temp,memory_current_temp,gpu_power_draw,current_graphics_frequency," +
                    "current_sm_frequency,current_memory_frequency,current_video_frequency,create_time,update_time) VALUES " +
                    "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            pstmt = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            i = 0;
            for (JSONObject o : infos) {
                JSONObject gpuStates = o.getJSONObject("gpu_state");
                for (String s : gpuStates.keySet()) {
                    JSONObject gpuState = gpuStates.getJSONObject(s);
                    pstmt.setLong(1, gpuState.getLong("gpu_id"));
                    pstmt.setLong(2, nodeStateIds[i]);
                    pstmt.setString(3, gpuState.getString("tx_throughput"));
                    pstmt.setString(4, gpuState.getString("rx_throughput"));
                    pstmt.setString(5, gpuState.getString("fan_speed"));
                    pstmt.setString(6, gpuState.getString("performance_state"));
                    pstmt.setString(7, gpuState.getString("memory_used"));
                    pstmt.setBigDecimal(8, gpuState.getBigDecimal("gpu_utilization"));
                    pstmt.setBigDecimal(9, gpuState.getBigDecimal("memory_utilization"));
                    pstmt.setBigDecimal(10, gpuState.getBigDecimal("encoder_utilization"));
                    pstmt.setBigDecimal(11, gpuState.getBigDecimal("decoder_utilization"));
                    pstmt.setString(12, gpuState.getString("gpu_current_temp"));
                    pstmt.setString(13, gpuState.getString("memory_current_temp"));
                    pstmt.setString(14, gpuState.getString("gpu_power_draw"));
                    pstmt.setString(15, gpuState.getString("current_graphics_frequency"));
                    pstmt.setString(16, gpuState.getString("current_sm_frequency"));
                    pstmt.setString(17, gpuState.getString("current_memory_frequency"));
                    pstmt.setString(18, gpuState.getString("current_video_frequency"));
                    pstmt.setTimestamp(19, o.getTimestamp("create_time"));
                    pstmt.setTimestamp(20, o.getTimestamp("create_time"));
                    pstmt.addBatch();
                }
                i++;
            }
            pstmt.executeBatch();
            con.commit();
            pstmt.close();

            //报警记录
            sql = "INSERT INTO pm_alarm" +
                    "(node_id,node_state_id,alarm_option,alarm_type,threshold,status,remark,create_time,update_time) VALUES " +
                    "(?,?,?,?,?,?,?,?,?)";
            pstmt = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            i = 0;
            int newAlarm = 0;
            for (JSONObject o : infos) {
                if (o.containsKey("alarm_state") && o.getIntValue("alarm_state") == 1) {
                    JSONObject alarms = o.getJSONObject("alarm");
                    for (String option : alarms.keySet()) {
                        JSONObject alarm = alarms.getJSONObject(option);
                        //创建新报警记录
                        if (alarm.getByteValue("add_type") == 0) {
                            System.out.println(alarm);
                            newAlarm++;
                            pstmt.setLong(1, o.getLong("node_id"));
                            pstmt.setLong(2, nodeStateIds[i]);
                            pstmt.setString(3, option);
                            pstmt.setInt(4, alarm.getIntValue("type"));
                            pstmt.setString(5, alarm.getString("threshold"));
                            pstmt.setInt(6, 0);
                            pstmt.setString(7, "");
                            pstmt.setTimestamp(8, o.getTimestamp("create_time"));
                            pstmt.setTimestamp(9, o.getTimestamp("create_time"));
                            pstmt.addBatch();
                        }
                    }
                }
                i++;
            }
            pstmt.executeBatch();
            con.commit();
            //获取alarm_id
            rs = pstmt.getGeneratedKeys();
            long alarmId[] = new long[newAlarm];
            i = 0;
            while (rs.next()) {
                alarmId[i++] = rs.getLong(1);
            }
            rs.close();
            pstmt.close();
            //追加报警信息
            sql = "INSERT INTO pm_alarm_detail" +
                    "(alarm_id,outlier,create_time) VALUES " +
                    "(?,?,?)";
            pstmt = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

            int alarmIdIndex = 0;
            for (JSONObject o : infos) {
                if (o.containsKey("alarm_state") && o.getIntValue("alarm_state") == 1) {
                    JSONObject alarms = o.getJSONObject("alarm");
                    long tmpNodeId = o.getLongValue("node_id");
                    for (String option : alarms.keySet()) {
                        JSONObject alarm = alarms.getJSONObject(option);
                        long tmpAlarmId;
                        //新增报警消息
                        if (alarm.getByteValue("add_type") == 0) {
                            tmpAlarmId = alarmId[alarmIdIndex++];
                            if (alarmIdMap.containsKey(tmpNodeId)) {
                                alarmIdMap.get(tmpNodeId).put(option, tmpAlarmId);
                            } else {
                                Hashtable<String, Long> hsi = new Hashtable<>();
                                hsi.put(option, tmpAlarmId);
                                alarmIdMap.put(tmpNodeId, hsi);
                            }
                        } else {//追加报警消息
                            tmpAlarmId = alarmIdMap.containsKey(tmpNodeId) ? alarmIdMap.get(tmpNodeId).get(option) : 0;
                        }

                        pstmt.setLong(1,tmpAlarmId);
                        pstmt.setString(2,alarm.getString("outlier"));
                        pstmt.setTimestamp(3,o.getTimestamp("create_time"));
                        pstmt.addBatch();
                    }
                }
            }
            pstmt.executeBatch();
            con.commit();
            pstmt.close();


//            sql = "INSERT INTO pm_alarm" +
//                    "(alarm_scheme_id,node_id,node_state_id,detail,status,create_time,update_time) VALUES " +
//                    "(?,?,?,?,?,?,?)";
//            pstmt = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
//            i = 0;
//            for (JSONObject o : infos) {
//                if (o.containsKey("alarm_state") && o.getIntValue("alarm_state") == 1) {
//                    JSONObject alarm = o.getJSONObject("alarm");
//                    pstmt.setInt(1, alarm.getIntValue("alarm_scheme_id"));
//                    alarm.remove("alarm_scheme_id");
//                    pstmt.setLong(2, o.getLong("node_id"));
//                    pstmt.setLong(3, nodeStateIds[i]);
//                    pstmt.setString(4, alarm.toJSONString());
//                    pstmt.setString(5, "0");
//                    pstmt.setTimestamp(6, o.getTimestamp("create_time"));
//                    pstmt.setTimestamp(7, o.getTimestamp("create_time"));
//                    pstmt.addBatch();
//                }
//                i++;
//            }
//            pstmt.executeBatch();
//            con.commit();
//            pstmt.close();

            con.close();
            infos.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Long a = Long.valueOf(7);
        Long b = new Long(7);
        Long c = Long.parseLong("7");
        Hashtable<Long, Long> hi = new Hashtable<>();
        hi.put(a, 1L);
        System.out.println(a.equals(b));
        System.out.println(hi.containsKey(b));
    }
}
