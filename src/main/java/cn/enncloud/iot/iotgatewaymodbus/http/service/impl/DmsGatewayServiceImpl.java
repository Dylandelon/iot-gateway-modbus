package cn.enncloud.iot.iotgatewaymodbus.http.service.impl;


import cn.enncloud.iot.iotgatewaymodbus.http.response.DataRespBody;
import cn.enncloud.iot.iotgatewaymodbus.http.service.DmsGatewayService;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class DmsGatewayServiceImpl implements DmsGatewayService {
    @Override
    public DataRespBody dmsGatewayGet(Long id, String serialNum, String domain, String name) throws Exception {
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
}
