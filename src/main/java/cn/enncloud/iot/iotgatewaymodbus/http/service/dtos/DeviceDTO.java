package cn.enncloud.iot.iotgatewaymodbus.http.service.dtos;

import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * @author sunhongqiang
 * @date 2018/06/21
 * 设备信息
 */
@Data
@ToString
public class DeviceDTO {

    /**
     * 设备详细信息
     */
    private List<DeviceInfo> deviceList;

}
