package cn.enncloud.iot.iotgatewaymodbus.http.service;


import cn.enncloud.iot.iotgatewaymodbus.http.service.dtos.*;

import java.util.Map;

/**
 * @author sunhongqiang
 * @date 2018/07/13
 * 云网关api接口
 */
public interface GatewayApiService {

    /**
     * @param serialNum 网关序列号
     * @return 网关信息
     * 根据网关编码获取网关信息
     */
    GatewayDTO getDatewayDTOFromApi(String serialNum);


    /**
     * @param gatewayId 网关Id
     * @return 设备信息
     * 根据网关id获取设备信息
     */
    DeviceDTO getDeviceDTOFromApi(int gatewayId);

    /**
     * @param deviceId 设备id
     * @return 点表信息
     * 获取modbus协议点表信息
     */
    PointDTO<ModbusPointInfo> getModbusPointDTOFromApi(int deviceId);

    /**
     * @param deviceId 设备id
     * @return 点表信息
     * 获取新奥376.1协议点表信息
     */
    PointDTO<Xinao376PointInfo> getXinao376PointDTOFromApi(int deviceId);

    /**
     * @param protocolTypeId 协议类型id
     * @return 点表信息
     * 获取新奥376.1协议点表信息
     */
    Map<String, ProtocolPointInfo> getXinao376PointDTOMapFromApi(int protocolTypeId);
}
