package cn.enncloud.iot.iotgatewaymodbus.http.service.dtos;

import lombok.Data;
import lombok.ToString;

/**
 * @author sunhongqiang
 * @date 2018/06/22
 * 设备详细信息
 */
@Data
@ToString
public class DeviceInfo {

    /**
     * 设备id
     */
    private Integer deviceId;

    /**
     * 网关id
     */
    private Integer gatewayId;

    /**
     * 设备类型id
     */
    private Integer deviceTypeId;

    /**
     * 从站地址
     */
    private Integer address;

    /**
     * 协议模型id
     */
    private Integer protocolPointGroupId;

    /**
     * 设备序列号
     */
    private String serialNum;

    /**
     * 业务域
     */
    private String domain;

}
