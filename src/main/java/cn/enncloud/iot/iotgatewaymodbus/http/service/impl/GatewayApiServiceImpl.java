package cn.enncloud.iot.iotgatewaymodbus.http.service.impl;


import cn.enncloud.iot.iotgatewaymodbus.http.response.DataRespBody;
import cn.enncloud.iot.iotgatewaymodbus.http.service.DmsGatewayService;
import cn.enncloud.iot.iotgatewaymodbus.http.service.GatewayApiService;
import cn.enncloud.iot.iotgatewaymodbus.http.service.dtos.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author sunhongqiang
 * @date 2018/07/13
 */
@Service
@Slf4j
public class GatewayApiServiceImpl implements GatewayApiService {

    @Autowired
    private DmsGatewayService dmsGatewayService;

    @Autowired
    private MessageSource messageSource;

    /**
     * @param serialNum 网关序列号
     * @return 网关信息
     * 根据网关编码获取网关信息
     */
    @Override
    public GatewayDTO getDatewayDTOFromApi(String serialNum) {
        GatewayDTO gatewayDTO = null;
        DataRespBody dataRespBody = null;
        Long beginTime = System.currentTimeMillis();
        try {
            dataRespBody = dmsGatewayService.dmsGatewayGet(null, serialNum, null, null);
//            if (log.isDebugEnabled()) {
//                log.debug(messageSource.getMessage(
//                        LogKeyConst.LOG_META_DEBUG_211,
//                        new Object[]{System.currentTimeMillis() - beginTime},
//                        LogKeyConst.LOG_DEFAULT,
//                        Locale.CHINA));
//            }
            if (dataRespBody.getData().size() > 0) {
                HashMap map = (HashMap) dataRespBody.getData().get(0);
                gatewayDTO = new GatewayDTO();
                gatewayDTO.setGatewayId((Integer) map.get("id"));
                gatewayDTO.setSerialNum((String) map.get("serialNum"));
                gatewayDTO.setToken((Integer) map.get("token"));
            }
        } catch (Exception ex) {
//            log.error(messageSource.getMessage(
//                    LogKeyConst.LOG_META_ERROR_212,
//                    new Object[]{System.currentTimeMillis() - beginTime, serialNum, JsonUtils.writeValueAsString(dataRespBody)},
//                    LogKeyConst.LOG_DEFAULT,
//                    Locale.CHINA), ex);
        }
        return gatewayDTO;
    }

    /**
     * @param gatewayId 网关Id
     * @return 设备信息
     * 根据网关id获取设备信息
     */
    @Override
    public DeviceDTO getDeviceDTOFromApi(int gatewayId) {
        DeviceDTO deviceDTO = null;
        DataRespBody dataRespBody = null;
        Long beginTime = System.currentTimeMillis();
        try {
            dataRespBody = dmsGatewayService.dmsDeviceGet(null, (long) gatewayId, null);
//            if (log.isDebugEnabled()) {
//                log.debug(messageSource.getMessage(
//                        LogKeyConst.LOG_META_DEBUG_213,
//                        new Object[]{System.currentTimeMillis() - beginTime},
//                        LogKeyConst.LOG_DEFAULT,
//                        Locale.CHINA));
//            }
            if (dataRespBody.getData().size() > 0) {
                deviceDTO = new DeviceDTO();
                List<DeviceInfo> deviceInfoList = new ArrayList<>();
                for (int i = 0; i < dataRespBody.getData().size(); i++) {
                    HashMap map = (HashMap) dataRespBody.getData().get(i);
                    DeviceInfo deviceInfo = new DeviceInfo();
                    deviceInfo.setDeviceId((Integer) map.get("id"));
                    deviceInfo.setGatewayId((Integer) map.get("dmsGatewayId"));
                    deviceInfo.setAddress((Integer) map.get("slaveAddress"));
                    deviceInfo.setProtocolPointGroupId((Integer) map.get("dmsProtocolPointGroupId"));
                    deviceInfo.setSerialNum((String) map.get("serialNum"));
                    deviceInfo.setDomain((String) map.get("domain"));
                    deviceInfoList.add(deviceInfo);
                }
                deviceDTO.setDeviceList(deviceInfoList);
            }
        } catch (Exception ex) {
//            log.error(messageSource.getMessage(
//                    LogKeyConst.LOG_META_ERROR_214,
//                    new Object[]{System.currentTimeMillis() - beginTime, gatewayId, JsonUtils.writeValueAsString(dataRespBody)},
//                    LogKeyConst.LOG_DEFAULT,
//                    Locale.CHINA), ex);
        }
        return deviceDTO;
    }

    /**
     * @param deviceId 设备id
     * @return 点表信息
     * 根据设备信息获取点表信息
     */
    @Override
    public PointDTO<ModbusPointInfo> getModbusPointDTOFromApi(int deviceId) {
        PointDTO<ModbusPointInfo> pointDTO = null;
        DataRespBody dataRespBody = null;
        Long beginTime = System.currentTimeMillis();
        try {
            dataRespBody = dmsGatewayService.dmsPointForGateway((long) deviceId);
//            if (log.isDebugEnabled()) {
//                log.debug(messageSource.getMessage(
//                        LogKeyConst.LOG_META_DEBUG_215,
//                        new Object[]{System.currentTimeMillis() - beginTime},
//                        LogKeyConst.LOG_DEFAULT,
//                        Locale.CHINA));
//            }
            if (dataRespBody.getData().size() > 0) {
                pointDTO = new PointDTO<>();
                List<ModbusPointInfo> pointInfoList = new ArrayList<>();
                for (int i = 0; i < dataRespBody.getData().size(); i++) {
                    HashMap map = (HashMap) dataRespBody.getData().get(i);
                    ModbusPointInfo pointInfo = new ModbusPointInfo();
                    pointInfo.setAlignType((Integer) map.get("alignType"));
                    pointInfo.setDataFormat((String) map.get("dataFormat"));
                    pointInfo.setMetric((String) map.get("dmsPointName"));
                    pointInfo.setRegisterAddr((Integer) map.get("registerAddress"));
                    pointInfo.setRegisterLength((Integer) map.get("registerLen"));
                    pointInfoList.add(pointInfo);
                }
                pointDTO.setPointList(pointInfoList);
            }
        } catch (Exception ex) {
//            log.error(messageSource.getMessage(
//                    LogKeyConst.LOG_META_ERROR_216,
//                    new Object[]{System.currentTimeMillis() - beginTime, deviceId, JsonUtils.writeValueAsString(dataRespBody)},
//                    LogKeyConst.LOG_DEFAULT,
//                    Locale.CHINA), ex);
        }
        return pointDTO;
    }

    /**
     * @param deviceId 设备id
     * @return 点表信息
     * 获取新奥376.1协议点表信息
     */
    @Override
    public PointDTO<Xinao376PointInfo> getXinao376PointDTOFromApi(int deviceId) {
        PointDTO<Xinao376PointInfo> pointDTO = null;
        DataRespBody dataRespBody = null;
        Long beginTime = System.currentTimeMillis();
        try {
            dataRespBody = dmsGatewayService.dmsPointForGateway((long) deviceId);
//            if (log.isDebugEnabled()) {
//                log.debug(messageSource.getMessage(
//                        LogKeyConst.LOG_META_DEBUG_215,
//                        new Object[]{System.currentTimeMillis() - beginTime},
//                        LogKeyConst.LOG_DEFAULT,
//                        Locale.CHINA));
//            }
            if (dataRespBody.getData().size() > 0) {
                pointDTO = new PointDTO<>();
                List<Xinao376PointInfo> pointInfoList = new ArrayList<>();
                for (int i = 0; i < dataRespBody.getData().size(); i++) {
                    HashMap map = (HashMap) dataRespBody.getData().get(i);
                    Xinao376PointInfo pointInfo = new Xinao376PointInfo();
                    pointInfo.setAlignType((Integer) map.get("alignType"));
                    pointInfo.setDataFormat((String) map.get("dataFormat"));
                    pointInfo.setDt((Integer) map.get("unitDt"));
                    pointInfo.setMetric((String) map.get("dmsPointName"));
                    pointInfoList.add(pointInfo);
                }
                pointDTO.setPointList(pointInfoList);
            }
        } catch (Exception ex) {
//            log.error(messageSource.getMessage(
//                    LogKeyConst.LOG_META_ERROR_216,
//                    new Object[]{System.currentTimeMillis() - beginTime, deviceId, JsonUtils.writeValueAsString(dataRespBody)},
//                    LogKeyConst.LOG_DEFAULT,
//                    Locale.CHINA), ex);
        }
        return pointDTO;
    }

    /**
     * @param protocolTypeId 协议类型id
     * @return 点表信息
     * 获取新奥376.1协议点表信息
     */
    @Override
    public Map<String, ProtocolPointInfo> getXinao376PointDTOMapFromApi(int protocolTypeId) {
        Map<String, ProtocolPointInfo> pointInfoMap = null;
        DataRespBody dataRespBody = null;
        Long beginTime = System.currentTimeMillis();
        try {
            dataRespBody = dmsGatewayService.dmsXinao3761ProtocolPoint((long) protocolTypeId);
//            if (log.isDebugEnabled()) {
//                log.debug(messageSource.getMessage(
//                        LogKeyConst.LOG_META_DEBUG_215,
//                        new Object[]{System.currentTimeMillis() - beginTime},
//                        LogKeyConst.LOG_DEFAULT,
//                        Locale.CHINA));
//            }
            if (dataRespBody.getData().size() > 0) {
                for (int i = 0; i < dataRespBody.getData().size(); i++) {
                    HashMap map = (HashMap) dataRespBody.getData().get(i);
                    ProtocolPointInfo pointInfo = new ProtocolPointInfo();
                    String da = map.get("alignType").toString();
                    String dt = map.get("unitDt").toString();
                    pointInfo.setAlignType((Integer) map.get("alignType"));
                    pointInfo.setDataFormat((String) map.get("dataFormat"));
                    pointInfoMap.put(da + "_" + dt, pointInfo);
                }
            }
        } catch (Exception ex) {
//            log.error(messageSource.getMessage(
//                    LogKeyConst.LOG_META_ERROR_216,
//                    new Object[]{System.currentTimeMillis() - beginTime, protocolTypeId, JsonUtils.writeValueAsString(dataRespBody)},
//                    LogKeyConst.LOG_DEFAULT,
//                    Locale.CHINA), ex);
        }
        return pointInfoMap;
    }
    @Override
    public List<DmsGatewayEntity> getDatewayDTOFromApiByDomain(String domain) {
        DataRespBody<List<DmsGatewayEntity>> dataRespBody = null;
        Long beginTime = System.currentTimeMillis();
        try {
            dataRespBody = dmsGatewayService.dmsGatewayGet(null, null, domain, null);
//            if (log.isDebugEnabled()) {
//                log.debug(messageSource.getMessage(
//                        LogKeyConst.LOG_META_DEBUG_211,
//                        new Object[]{System.currentTimeMillis() - beginTime},
//                        LogKeyConst.LOG_DEFAULT,
//                        Locale.CHINA));
//            }

        } catch (Exception ex) {
//            log.error(messageSource.getMessage(
//                    LogKeyConst.LOG_META_ERROR_212,
//                    new Object[]{System.currentTimeMillis() - beginTime, serialNum, JsonUtils.writeValueAsString(dataRespBody)},
//                    LogKeyConst.LOG_DEFAULT,
//                    Locale.CHINA), ex);
        }
        return dataRespBody.getData();
    }
    @Override
    public List<DmsDeviceEntity> getDeviceDTOFromApiByGatewayId(long gatewayId) {
        DeviceDTO deviceDTO = null;
        DataRespBody<List<DmsDeviceEntity>> dataRespBody = null;
        Long beginTime = System.currentTimeMillis();
        try {
            dataRespBody = dmsGatewayService.dmsDeviceGet(null, gatewayId, null);
//            if (log.isDebugEnabled()) {
//                log.debug(messageSource.getMessage(
//                        LogKeyConst.LOG_META_DEBUG_213,
//                        new Object[]{System.currentTimeMillis() - beginTime},
//                        LogKeyConst.LOG_DEFAULT,
//                        Locale.CHINA));
//            }

        } catch (Exception ex) {
//            log.error(messageSource.getMessage(
//                    LogKeyConst.LOG_META_ERROR_214,
//                    new Object[]{System.currentTimeMillis() - beginTime, gatewayId, JsonUtils.writeValueAsString(dataRespBody)},
//                    LogKeyConst.LOG_DEFAULT,
//                    Locale.CHINA), ex);
        }
        return dataRespBody.getData();
    }
    @Override
    public List<DmsProtocolPointModbusEntity> getModbusPointDTOFromApiByDeviceId(long deviceId) {
        DataRespBody<List<DmsProtocolPointModbusEntity>> dataRespBody = null;
        Long beginTime = System.currentTimeMillis();
        try {
            dataRespBody = dmsGatewayService.dmsPointForGateway(deviceId);
//            if (log.isDebugEnabled()) {
//                log.debug(messageSource.getMessage(
//                        LogKeyConst.LOG_META_DEBUG_215,
//                        new Object[]{System.currentTimeMillis() - beginTime},
//                        LogKeyConst.LOG_DEFAULT,
//                        Locale.CHINA));
//            }

        } catch (Exception ex) {
//            log.error(messageSource.getMessage(
//                    LogKeyConst.LOG_META_ERROR_216,
//                    new Object[]{System.currentTimeMillis() - beginTime, deviceId, JsonUtils.writeValueAsString(dataRespBody)},
//                    LogKeyConst.LOG_DEFAULT,
//                    Locale.CHINA), ex);
        }
        return dataRespBody.getData();
    }
    @Override
    public List<DmsGatewayEntity> getDatewayDTOFromApiByGatewayId(long gatewayId) {
        DataRespBody<List<DmsGatewayEntity>> dataRespBody = null;
        Long beginTime = System.currentTimeMillis();
        try {
            dataRespBody = dmsGatewayService.dmsGatewayGet(gatewayId, null, null, null);
//            if (log.isDebugEnabled()) {
//                log.debug(messageSource.getMessage(
//                        LogKeyConst.LOG_META_DEBUG_211,
//                        new Object[]{System.currentTimeMillis() - beginTime},
//                        LogKeyConst.LOG_DEFAULT,
//                        Locale.CHINA));
//            }

        } catch (Exception ex) {
//            log.error(messageSource.getMessage(
//                    LogKeyConst.LOG_META_ERROR_212,
//                    new Object[]{System.currentTimeMillis() - beginTime, serialNum, JsonUtils.writeValueAsString(dataRespBody)},
//                    LogKeyConst.LOG_DEFAULT,
//                    Locale.CHINA), ex);
        }
        return dataRespBody.getData();
    }
}
