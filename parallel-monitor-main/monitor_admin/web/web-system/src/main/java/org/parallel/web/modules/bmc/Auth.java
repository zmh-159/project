package org.parallel.web.modules.bmc;

import lombok.Data;
import org.parallel.web.exception.BadRequestException;
import org.parallel.web.modules.bmc.jpa.service.PbNodeService;
import org.parallel.web.modules.bmc.jpa.service.dto.PbNodeDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.concurrent.ConcurrentHashMap;

@Service
public class Auth {
    @Autowired
    Kp kp;
    @Autowired
    Ft ft;
    @Autowired
    PbNodeService pbNodeService;
    /** bmcNodeId->BmcInfo **/
    private ConcurrentHashMap<Long, BmcInfo> bmcInfoMap = new ConcurrentHashMap<>();

    public String getToken(Long nodeId) {
        return getBmcInfo(nodeId).getToken();
    }

    public BmcInfo getBmcInfo(Long nodeId) {
        BmcInfo bmcInfo = bmcInfoMap.get(nodeId);
        /** token已存在且未过期 **/
        if (null != bmcInfo) {
            String token = bmcInfo.getToken();
            if (!StringUtils.isEmpty(token)) {
                return bmcInfo;
            }
        }
        PbNodeDto pbNodeDto = pbNodeService.findById(nodeId);
        if (null == pbNodeDto) {
            throw new BadRequestException(StringLib.NODE_NOT_FOUND);
        }
        String username = pbNodeDto.getUser();
        String password = pbNodeDto.getPasswd();
        String ip = pbNodeDto.getBmcIp();
        String nodeType = pbNodeDto.getNodeType();
        String token = null;
        switch (nodeType) {
            case "kp":
                token = kp.getToken(username, password, ip);
                break;
            case "ft":
                token = ft.getToken(username, password, ip);
        }
        if (null == token) {
            throw new BadRequestException(StringLib.BAD_NODE_TYPE);
        }
        bmcInfo = new BmcInfo(username, password, ip, token);
        bmcInfo.setUpdateTime(System.currentTimeMillis());
        bmcInfoMap.put(nodeId, bmcInfo);
        return bmcInfo;
    }
}

@Data
class BmcInfo {
    /** token过期时间 **/
    public static int exp = 10 * 60 * 1000;

    private String ip;
    private String username;
    private String password;
    private String token;
    private long updateTime;

    public BmcInfo(String username, String password, String ip, String token) {
        this.username = username;
        this.password = password;
        this.ip = ip;
        this.token = token;
    }

    public String getToken() {
        if (System.currentTimeMillis() - updateTime < exp) {
            return token;
        } else {
            return null;
        }
    }
}
