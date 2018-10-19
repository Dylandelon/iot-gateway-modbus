package cn.enncloud.iot.iotgatewaymodbus.http.controller;

import cn.enncloud.iot.iotgatewaymodbus.http.configration.ModbusProto;
import cn.enncloud.iot.iotgatewaymodbus.http.configration.MsgPack;
import cn.enncloud.iot.iotgatewaymodbus.http.constants.CodeEnum;
import cn.enncloud.iot.iotgatewaymodbus.http.constants.Constants;
import cn.enncloud.iot.iotgatewaymodbus.http.constants.NettyChannelMap;
import cn.enncloud.iot.iotgatewaymodbus.http.response.DataRespBody;
import cn.enncloud.iot.iotgatewaymodbus.http.service.GatewayApiService;
import cn.enncloud.iot.iotgatewaymodbus.http.service.dtos.DmsDeviceEntity;
import cn.enncloud.iot.iotgatewaymodbus.http.service.dtos.DmsGatewayEntity;
import cn.enncloud.iot.iotgatewaymodbus.http.service.dtos.DmsProtocolPointModbusEntity;
import cn.enncloud.iot.iotgatewaymodbus.http.service.dtos.ModbusCMDGroupPackages;
import cn.enncloud.iot.iotgatewaymodbus.http.tools.CRC16;
import cn.enncloud.iot.iotgatewaymodbus.http.tools.Tool;
import cn.enncloud.iot.iotgatewaymodbus.http.tools.ValidatorTools;
import cn.enncloud.iot.iotgatewaymodbus.http.vo.dto.DmsGateWayDevicControlVo;
import cn.enncloud.iot.iotgatewaymodbus.netty.TCPServerNetty;
import io.netty.channel.Channel;
import io.netty.util.AttributeKey;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.stream.Collectors;

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
        respBody.setCode(CodeEnum.IOT_FAIL.getCode());
        respBody.setMsg(CodeEnum.IOT_FAIL.getValue());
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
        List<DmsProtocolPointModbusEntity> modbusEntityList = dmsProtocolPointModbusEntityList.stream().filter(dmsProtocolPointModbusEntity -> entity.getPointName().equalsIgnoreCase(dmsProtocolPointModbusEntity.getDmsPointName())).collect(Collectors.toList());

        // gatewayApiService


        List<DmsGatewayEntity> dmsGatewayEntityList = gatewayApiService.getDatewayDTOFromApiByGatewayId(entity.getGatewayId());
        List<DmsDeviceEntity> dmsDeviceEntityList = gatewayApiService.getDeviceDTOFromApiByDeviceId(entity.getDeviceId());


        Channel channel = NettyChannelMap.get(dmsGatewayEntityList.get(0).getSerialNum());

//        StringBuilder sb = new StringBuilder();
//        sb.append("01060000").append(String.format("%04x", Integer.valueOf(entity.getValue())));
//
//        byte[] bytesWriteMid = TCPServerNetty.hexToByteArray(sb.toString());

        MsgPack msgPack = ModbusProto.generateDownProtocolCmd(dmsDeviceEntityList.get(0), modbusEntityList.get(0), entity);
        ModbusCMDGroupPackages modbusCMDGroupPackages = new ModbusCMDGroupPackages();
        modbusCMDGroupPackages.setMsgPack(msgPack);
        modbusCMDGroupPackages.setDmsProtocolPointModbusEntityList(modbusEntityList);
        AttributeKey<ModbusCMDGroupPackages> attributeKey2 = AttributeKey.valueOf("modbusCMDGroupPackagesDown");
        channel.attr(attributeKey2).set(modbusCMDGroupPackages);

        AttributeKey<Condition> attributeKey3 = AttributeKey.valueOf("condition");
        Condition condition = Constants.lock.newCondition();
        channel.attr(attributeKey3).set(condition);


        byte[] bytesWriteMid = ModbusProto.getCmdBytes(msgPack);

        byte[] bytesWrite = CRC16.addCRC(bytesWriteMid);
        log.info("主动向设备下发的信息未加密为："+TCPServerNetty.bytesToHexString(bytesWrite));
        String cipherText = Tool.SC_Tea_Encryption_Str(TCPServerNetty.bytesToHexStringCompact(bytesWrite),"2018091200000000");
        byte[] bytesWriteSec = TCPServerNetty.hexToByteArray(cipherText);
        log.info("主动向设备下发的信息加密为："+TCPServerNetty.bytesToHexString(bytesWriteSec));
        channel.writeAndFlush(bytesWriteSec);

        boolean flag = false;
        int timeCount = 500;
        while ((System.currentTimeMillis() - startTime) <10*1000){

            byte[] bytesRec = TCPServerNetty.getMessageMap().get((long)modbusCMDGroupPackages.getDmsProtocolPointModbusEntityList().get(0).getRegisterAddress());
            if(bytesRec != null && Arrays.equals(bytesWrite,bytesRec)){

                flag = true;
                break;
            }else{
                timeCount = timeCount+1000;
                try {
                    Thread.sleep(timeCount);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }
        if(flag){
            respBody.setCode(CodeEnum.IOT_SUCCESS.getCode());
            respBody.setMsg(CodeEnum.IOT_SUCCESS.getValue());
        }else{
            respBody.setCode(CodeEnum.IOT_FAIL.getCode());
            respBody.setMsg("等待结果超时");
        }
        log.info("主动向设备下发的信息结果："+(flag ==true?"成功":"失败"));

        channel.attr(attributeKey2).remove();
        TCPServerNetty.getMessageMap().remove((long)modbusCMDGroupPackages.getDmsProtocolPointModbusEntityList().get(0).getRegisterAddress());
        Constants.lock.lock();
        try {
            condition.signal();
        }finally {
            Constants.lock.unlock();
        }
        return respBody;

    }
}
