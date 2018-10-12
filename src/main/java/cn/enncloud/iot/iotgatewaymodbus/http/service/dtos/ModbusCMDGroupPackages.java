package cn.enncloud.iot.iotgatewaymodbus.http.service.dtos;

import cn.enncloud.iot.iotgatewaymodbus.http.configration.MsgPack;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class ModbusCMDGroupPackages {
    private List<DmsProtocolPointModbusEntity> dmsProtocolPointModbusEntityList;
    private MsgPack msgPack;
}
