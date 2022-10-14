package org.parallel.client.controller.enity;

import lombok.Data;

/**
 * @author yuyifade
 * @description 控制协议实体
 * @date 2022/1/5 下午5:30
 */
@Data
public class ControlAgreement {
    String type;//指令类型：0：节点信息获取 1：shell命令
    String name;//指令名
    String returnType;//返回类型：0：直接返回，1：udp返回，2：定时返回
    String params;//键值对参数
}