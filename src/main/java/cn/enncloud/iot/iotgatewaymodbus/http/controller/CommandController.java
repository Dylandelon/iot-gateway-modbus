package cn.enncloud.iot.iotgatewaymodbus.http.controller;

import cn.enncloud.iot.iotgatewaymodbus.http.configration.ModbusProto;
import cn.enncloud.iot.iotgatewaymodbus.http.configration.MsgPack;
import cn.enncloud.iot.iotgatewaymodbus.http.constants.CodeEnum;
import cn.enncloud.iot.iotgatewaymodbus.http.constants.NettyChannelMap;
import cn.enncloud.iot.iotgatewaymodbus.http.response.DataRespBody;
import cn.enncloud.iot.iotgatewaymodbus.http.service.GatewayApiService;
import cn.enncloud.iot.iotgatewaymodbus.http.service.dtos.DmsDeviceEntity;
import cn.enncloud.iot.iotgatewaymodbus.http.service.dtos.DmsGatewayEntity;
import cn.enncloud.iot.iotgatewaymodbus.http.service.dtos.DmsProtocolPointModbusEntity;
import cn.enncloud.iot.iotgatewaymodbus.http.tools.CRC16;
import cn.enncloud.iot.iotgatewaymodbus.http.tools.Tool;
import cn.enncloud.iot.iotgatewaymodbus.http.tools.ValidatorTools;
import cn.enncloud.iot.iotgatewaymodbus.http.vo.dto.DmsGateWayDevicControlVo;
import cn.enncloud.iot.iotgatewaymodbus.netty.TCPServerNetty;
import io.netty.channel.Channel;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Log4j
@RestController
@RequestMapping("gateway")
public class CommandController {
    @Autowired
    private GatewayApiService gatewayApiService;

//    @Autowired
//    private TCPServerNetty tcpServerNetty;

    @RequestMapping(value = "/dmsGateWayDevicControl",method = RequestMethod.POST)
    public DataRespBody dmsGateWayDevicControlPost(@RequestBody DmsGateWayDevicControlVo entity){

        long startTime = System.currentTimeMillis();
        DataRespBody respBody = new DataRespBody();

        if(entity==null){
            respBody.setCode(CodeEnum.IOT_PARAM.getCode());
            respBody.setMsg(CodeEnum.IOT_PARAM.getValue());

        }
        respBody.setCode(CodeEnum.IOT_SUCCESS.getCode());
        respBody.setMsg(CodeEnum.IOT_SUCCESS.getValue());
        List<String> emptyValue = null;
        try {
            emptyValue = ValidatorTools.allfieldIsNUll(entity);
        } catch (Exception e) {
            // todo
            respBody.setCode(CodeEnum.IOT_PARAM.getCode());
            respBody.setMsg(CodeEnum.IOT_PARAM.getValue());
            return respBody;
        }
        if(emptyValue != null&& !emptyValue.isEmpty()){
            respBody.setCode(CodeEnum.IOT_PARAM.getCode());
            respBody.setMsg(CodeEnum.IOT_PARAM.getValue()+":"+emptyValue.toString());
            return respBody;
        }
        List<DmsProtocolPointModbusEntity> dmsProtocolPointModbusEntityList = gatewayApiService.getModbusPointDTOFromApiByDeviceId(entity.getDeviceId());
        DmsProtocolPointModbusEntity modbusPointInfo = new DmsProtocolPointModbusEntity();
        dmsProtocolPointModbusEntityList.forEach(modbusPointInfo1 -> {
            if(entity.getPointName().equalsIgnoreCase(modbusPointInfo1.getDmsPointName())){
                BeanUtils.copyProperties(modbusPointInfo1,modbusPointInfo);
            }
        });
        // gatewayApiService


        List<DmsGatewayEntity> dmsGatewayEntityList = gatewayApiService.getDatewayDTOFromApiByGatewayId(entity.getGatewayId());
        List<DmsDeviceEntity> dmsDeviceEntityList = gatewayApiService.getDeviceDTOFromApiByDeviceId(entity.getGatewayId());


        Channel channel = NettyChannelMap.get(dmsGatewayEntityList.get(0).getSerialNum());

//        StringBuilder sb = new StringBuilder();
//        sb.append("01060000").append(String.format("%04x", Integer.valueOf(entity.getValue())));
//
//        byte[] bytesWriteMid = TCPServerNetty.hexToByteArray(sb.toString());

        MsgPack msgPack = ModbusProto.generateDownProtocolCmd(dmsDeviceEntityList.get(0), modbusPointInfo, entity);
        byte[] bytesWriteMid = ModbusProto.getCmdBytes(msgPack);

        byte[] bytesWrite = CRC16.addCRC(bytesWriteMid);
        log.info("向设备下发的信息未加密为："+TCPServerNetty.bytesToHexString(bytesWrite));
        String cipherText = Tool.SC_Tea_Encryption_Str(TCPServerNetty.bytesToHexStringCompact(bytesWrite),"2018091200000000");
        byte[] bytesWriteSec = TCPServerNetty.hexToByteArray(cipherText);
        log.info("向设备下发的信息加密为："+TCPServerNetty.bytesToHexString(bytesWriteSec));
        channel.writeAndFlush(bytesWriteSec);
        return respBody;

    }
}
