package cn.enncloud.iot.iotgatewaymodbus.http.service.dtos;

import lombok.Data;
import lombok.ToString;

/**
 * @author sunhongqiang
 * @date 2018/06/21
 * 网关信息
 */
@Data
@ToString
public class GatewayDTO {

    /**
     * 网关id
     */
    private Integer gatewayId;

    /**
     * 设备序列号
     */
    private String serialNum;

    /**
     * token
     */
    private Integer token;

}
