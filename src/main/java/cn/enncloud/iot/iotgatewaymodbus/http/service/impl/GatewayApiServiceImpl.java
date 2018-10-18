package cn.enncloud.iot.iotgatewaymodbus.http.service.impl;


import cn.enncloud.iot.iotgatewaymodbus.http.constants.CodeEnum;
import cn.enncloud.iot.iotgatewaymodbus.http.constants.LogKeyConst;
import cn.enncloud.iot.iotgatewaymodbus.http.response.DataRespBody;
import cn.enncloud.iot.iotgatewaymodbus.http.service.DmsGatewayService;
import cn.enncloud.iot.iotgatewaymodbus.http.service.GatewayApiService;
import cn.enncloud.iot.iotgatewaymodbus.http.service.dtos.*;
import cn.enncloud.iot.iotgatewaymodbus.http.tools.JsonUtils;
import cn.enncloud.iot.iotgatewaymodbus.http.vo.dto.DmsGateWayUpdateVo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.*;

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
            if (log.isDebugEnabled()) {
                log.debug(messageSource.getMessage(
                        LogKeyConst.LOG_2_1_1,
                        new Object[]{System.currentTimeMillis() - beginTime},
                        LogKeyConst.LOG_DEFAULT,
                        Locale.CHINA));
            }
            if (dataRespBody.getData().size() > 0) {
                HashMap map = (HashMap) dataRespBody.getData().get(0);
                gatewayDTO = new GatewayDTO();
                gatewayDTO.setGatewayId((Integer) map.get("id"));
                gatewayDTO.setSerialNum((String) map.get("serialNum"));
                gatewayDTO.setToken((Integer) map.get("token"));
            }
        } catch (Exception ex) {
            if(log.isErrorEnabled()){
                Map map = new HashMap();
                map.put("serialNum",serialNum);
                log.error(messageSource.getMessage(
                        LogKeyConst.LOG_2_1_2,
                        new Object[]{System.currentTimeMillis() - beginTime, JsonUtils.writeValueAsString(map), JsonUtils.writeValueAsString(dataRespBody)},
                        LogKeyConst.LOG_DEFAULT,
                        Locale.CHINA), ex);
            }

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
            if (log.isDebugEnabled()) {
                log.debug(messageSource.getMessage(
                        LogKeyConst.LOG_2_2_1,
                        new Object[]{System.currentTimeMillis() - beginTime},
                        LogKeyConst.LOG_DEFAULT,
                        Locale.CHINA));
            }
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
            if(log.isErrorEnabled()){
                Map map = new HashMap();
                map.put("gatewayId",gatewayId);
                log.error(messageSource.getMessage(
                        LogKeyConst.LOG_2_2_2,
                        new Object[]{System.currentTimeMillis() - beginTime, JsonUtils.writeValueAsString(map), JsonUtils.writeValueAsString(dataRespBody)},
                        LogKeyConst.LOG_DEFAULT,
                        Locale.CHINA), ex);
            }

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
            if (log.isDebugEnabled()) {
                log.debug(messageSource.getMessage(
                        LogKeyConst.LOG_2_3_1,
                        new Object[]{System.currentTimeMillis() - beginTime},
                        LogKeyConst.LOG_DEFAULT,
                        Locale.CHINA));
            }
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
            if(log.isErrorEnabled()){
                Map map = new HashMap();
                map.put("deviceId",deviceId);
                log.error(messageSource.getMessage(
                        LogKeyConst.LOG_2_3_2,
                        new Object[]{System.currentTimeMillis() - beginTime, JsonUtils.writeValueAsString(map), JsonUtils.writeValueAsString(dataRespBody)},
                        LogKeyConst.LOG_DEFAULT,
                        Locale.CHINA), ex);
            }

        }
        return pointDTO;
    }


    @Override
    public List<DmsGatewayEntity> getDatewayDTOFromApiByDomain(String domain) {
        DataRespBody dataRespBody = null;
        List<DmsGatewayEntity> dmsGatewayEntityList = new ArrayList<>();
        Long beginTime = System.currentTimeMillis();
        try {
            dataRespBody = dmsGatewayService.dmsGatewayGet(null, null, domain, null);
            if (log.isDebugEnabled()) {
                log.debug(messageSource.getMessage(
                        LogKeyConst.LOG_2_1_1,
                        new Object[]{System.currentTimeMillis() - beginTime},
                        LogKeyConst.LOG_DEFAULT,
                        Locale.CHINA));
            }
            ObjectMapper mapper = new ObjectMapper();
            dmsGatewayEntityList = mapper.convertValue(dataRespBody.getData(),new TypeReference<List<DmsGatewayEntity>>() {});


        } catch (Exception ex) {
            if(log.isErrorEnabled()){
                Map map = new HashMap();
                map.put("domain",domain);
                log.error(messageSource.getMessage(
                        LogKeyConst.LOG_2_1_2,
                        new Object[]{System.currentTimeMillis() - beginTime, JsonUtils.writeValueAsString(map), JsonUtils.writeValueAsString(dataRespBody)},
                        LogKeyConst.LOG_DEFAULT,
                        Locale.CHINA), ex);
            }

        }
        return dmsGatewayEntityList;
    }
    @Override
    public List<DmsDeviceEntity> getDeviceDTOFromApiByGatewayId(long gatewayId) {
        DeviceDTO deviceDTO = null;
        DataRespBody<List<DmsDeviceEntity>> dataRespBody = null;
        List<DmsDeviceEntity> dmsDeviceEntityList = null;
        Long beginTime = System.currentTimeMillis();
        try {
            dataRespBody = dmsGatewayService.dmsDeviceGet(null, gatewayId, null);
            ObjectMapper mapper = new ObjectMapper();
            dmsDeviceEntityList = mapper.convertValue(dataRespBody.getData(),new TypeReference<List<DmsDeviceEntity>>() {});
            if (log.isDebugEnabled()) {
                log.debug(messageSource.getMessage(
                        LogKeyConst.LOG_2_2_1,
                        new Object[]{System.currentTimeMillis() - beginTime},
                        LogKeyConst.LOG_DEFAULT,
                        Locale.CHINA));
            }

        } catch (Exception ex) {
            if(log.isErrorEnabled()){
                Map map = new HashMap();
                map.put("gatewayId",gatewayId);
                log.error(messageSource.getMessage(
                        LogKeyConst.LOG_2_2_2,
                        new Object[]{System.currentTimeMillis() - beginTime, JsonUtils.writeValueAsString(map), JsonUtils.writeValueAsString(dataRespBody)},
                        LogKeyConst.LOG_DEFAULT,
                        Locale.CHINA), ex);
            }

        }
        return dmsDeviceEntityList;
    }
    @Override
    public List<DmsProtocolPointModbusEntity> getModbusPointDTOFromApiByDeviceId(long deviceId) {
        DataRespBody<List<DmsProtocolPointModbusEntity>> dataRespBody = null;
        List<DmsProtocolPointModbusEntity> dmsProtocolPointModbusEntityList = null;
        Long beginTime = System.currentTimeMillis();
        try {
            dataRespBody = dmsGatewayService.dmsPointForGateway(deviceId);
            ObjectMapper mapper = new ObjectMapper();
            dmsProtocolPointModbusEntityList = mapper.convertValue(dataRespBody.getData(),new TypeReference<List<DmsProtocolPointModbusEntity>>() {});
            if (log.isDebugEnabled()) {
                log.debug(messageSource.getMessage(
                        LogKeyConst.LOG_2_3_1,
                        new Object[]{System.currentTimeMillis() - beginTime},
                        LogKeyConst.LOG_DEFAULT,
                        Locale.CHINA));
            }

        } catch (Exception ex) {
            if(log.isErrorEnabled()){
                Map map = new HashMap();
                map.put("deviceId",deviceId);
                log.error(messageSource.getMessage(
                        LogKeyConst.LOG_2_3_2,
                        new Object[]{System.currentTimeMillis() - beginTime, JsonUtils.writeValueAsString(map), JsonUtils.writeValueAsString(dataRespBody)},
                        LogKeyConst.LOG_DEFAULT,
                        Locale.CHINA), ex);
            }

        }
        return dmsProtocolPointModbusEntityList;
    }
    @Override
    public List<DmsGatewayEntity> getDatewayDTOFromApiByGatewayId(long gatewayId) {
        DataRespBody<List<DmsGatewayEntity>> dataRespBody = null;
        List<DmsGatewayEntity> dmsGatewayEntityList = null;
        Long beginTime = System.currentTimeMillis();
        try {
            dataRespBody = dmsGatewayService.dmsGatewayGet(gatewayId, null, null, null);
            ObjectMapper mapper = new ObjectMapper();
            dmsGatewayEntityList = mapper.convertValue(dataRespBody.getData(),new TypeReference<List<DmsGatewayEntity>>() {});
            if (log.isDebugEnabled()) {
                log.debug(messageSource.getMessage(
                        LogKeyConst.LOG_2_1_1,
                        new Object[]{System.currentTimeMillis() - beginTime},
                        LogKeyConst.LOG_DEFAULT,
                        Locale.CHINA));
            }

        } catch (Exception ex) {
            if(log.isErrorEnabled()){
                Map map = new HashMap();
                map.put("gatewayId",gatewayId);
                log.error(messageSource.getMessage(
                        LogKeyConst.LOG_2_1_2,
                        new Object[]{System.currentTimeMillis() - beginTime, JsonUtils.writeValueAsString(map), JsonUtils.writeValueAsString(dataRespBody)},
                        LogKeyConst.LOG_DEFAULT,
                        Locale.CHINA), ex);
            }

        }
        return dmsGatewayEntityList;
    }

    @Override
    public boolean dmsGateWayUpdatePost(DmsGateWayUpdateVo entity) {
        DataRespBody<List<DmsGatewayEntity>> dataRespBody = null;
        boolean flag = false;
        Long beginTime = System.currentTimeMillis();
        try {
            dataRespBody = dmsGatewayService.dmsGateWayUpdatePost(entity);
            if (log.isDebugEnabled()) {
                log.debug(messageSource.getMessage(
                        LogKeyConst.LOG_2_4_1,
                        new Object[]{System.currentTimeMillis() - beginTime},
                        LogKeyConst.LOG_DEFAULT,
                        Locale.CHINA));
            }

        } catch (Exception ex) {
            if(log.isErrorEnabled()){
                log.error(messageSource.getMessage(
                        LogKeyConst.LOG_2_4_2,
                        new Object[]{System.currentTimeMillis() - beginTime, JsonUtils.writeValueAsString(entity), JsonUtils.writeValueAsString(dataRespBody)},
                        LogKeyConst.LOG_DEFAULT,
                        Locale.CHINA), ex);
            }

        }
        if(CodeEnum.IOT_SUCCESS.getCode().equalsIgnoreCase(dataRespBody.getCode())){
            flag = true;
        }
        return flag;
    }
    @Override
    public List<DmsDeviceEntity> getDeviceDTOFromApiByDeviceId(long deviceId) {
        DeviceDTO deviceDTO = null;
        DataRespBody<List<DmsDeviceEntity>> dataRespBody = null;
        List<DmsDeviceEntity> dmsDeviceEntityList = null;
        Long beginTime = System.currentTimeMillis();
        try {
            dataRespBody = dmsGatewayService.dmsDeviceGet(deviceId, null, null);
            ObjectMapper mapper = new ObjectMapper();
            dmsDeviceEntityList = mapper.convertValue(dataRespBody.getData(),new TypeReference<List<DmsDeviceEntity>>() {});
            if (log.isDebugEnabled()) {
                log.debug(messageSource.getMessage(
                        LogKeyConst.LOG_2_2_1,
                        new Object[]{System.currentTimeMillis() - beginTime},
                        LogKeyConst.LOG_DEFAULT,
                        Locale.CHINA));
            }

        } catch (Exception ex) {
            if(log.isErrorEnabled()){
                Map map = new HashMap();
                map.put("deviceId",deviceId);
                log.error(messageSource.getMessage(
                        LogKeyConst.LOG_2_2_2,
                        new Object[]{System.currentTimeMillis() - beginTime, JsonUtils.writeValueAsString(map), JsonUtils.writeValueAsString(dataRespBody)},
                        LogKeyConst.LOG_DEFAULT,
                        Locale.CHINA), ex);
            }

        }
        return dmsDeviceEntityList;
    }
}
