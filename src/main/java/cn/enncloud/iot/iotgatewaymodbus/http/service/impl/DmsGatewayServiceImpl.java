package cn.enncloud.iot.iotgatewaymodbus.http.service.impl;


import cn.enncloud.iot.iotgatewaymodbus.http.response.DataRespBody;
import cn.enncloud.iot.iotgatewaymodbus.http.service.DmsGatewayService;
import cn.enncloud.iot.iotgatewaymodbus.http.service.dtos.DmsGatewayEntity;
import cn.enncloud.iot.iotgatewaymodbus.http.vo.dto.DmsGateWayUpdateVo;

import java.io.IOException;
import java.util.List;

//@Service
public class DmsGatewayServiceImpl implements DmsGatewayService {
    @Override
    public DataRespBody<List<DmsGatewayEntity>> dmsGatewayGet(Long id, String serialNum, String domain, String name) throws Exception {
        throw new IOException("响应超时");
    }

    @Override
    public DataRespBody dmsDeviceGet(Long id, Long dmsGatewayId, String serialNum) throws Exception {
        throw new IOException("响应超时");
    }

    @Override
    public DataRespBody dmsPointForGateway(Long dmsDeviceId) throws Exception {
        throw new IOException("响应超时");
    }

    @Override
    public DataRespBody dmsXinao3761ProtocolPoint(Long protocolTypeId) throws Exception {
        throw new IOException("响应超时");
    }
    @Override
    public DataRespBody dmsGateWayUpdatePost(DmsGateWayUpdateVo entity) throws Exception{
        throw new IOException("响应超时");
    }
}
