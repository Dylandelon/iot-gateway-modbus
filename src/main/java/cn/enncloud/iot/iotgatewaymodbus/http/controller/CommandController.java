package cn.enncloud.iot.iotgatewaymodbus.http.controller;

import cn.enncloud.iot.iotgatewaymodbus.entity.exception.ConnectionException;
import cn.enncloud.iot.iotgatewaymodbus.entity.exception.ErrorResponseException;
import cn.enncloud.iot.iotgatewaymodbus.entity.exception.NoResponseException;
import cn.enncloud.iot.iotgatewaymodbus.entity.func.WriteSingleCoil;
import cn.enncloud.iot.iotgatewaymodbus.entity.func.response.WriteMultipleRegistersResponse;
import cn.enncloud.iot.iotgatewaymodbus.example.ClientForTests;
import cn.enncloud.iot.iotgatewaymodbus.http.configration.CRC16;
import cn.enncloud.iot.iotgatewaymodbus.http.configration.Crc16ModbusUtil;
import cn.enncloud.iot.iotgatewaymodbus.http.configration.TCPServerNetty;
import cn.enncloud.iot.iotgatewaymodbus.http.constants.CodeEnum;
import cn.enncloud.iot.iotgatewaymodbus.http.response.DataRespBody;
import cn.enncloud.iot.iotgatewaymodbus.http.service.GatewayApiService;
import cn.enncloud.iot.iotgatewaymodbus.http.service.dtos.ModbusPointInfo;
import cn.enncloud.iot.iotgatewaymodbus.http.service.dtos.PointDTO;
import cn.enncloud.iot.iotgatewaymodbus.http.tools.Tool;
import cn.enncloud.iot.iotgatewaymodbus.http.tools.ValidatorTools;
import cn.enncloud.iot.iotgatewaymodbus.http.vo.dto.DmsGateWayDevicControlVo;
import cn.enncloud.iot.iotgatewaymodbus.server.ModbusClient;
import cn.enncloud.iot.iotgatewaymodbus.server.ModbusServer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
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
        PointDTO<ModbusPointInfo> modbusPointInfoPointDTO = gatewayApiService.getModbusPointDTOFromApi(entity.getDeviceId());
        ModbusPointInfo modbusPointInfo = new ModbusPointInfo();
        modbusPointInfoPointDTO.getPointList().forEach(modbusPointInfo1 -> {
            if(entity.getPointName().equalsIgnoreCase(modbusPointInfo1.getMetric())){
                BeanUtils.copyProperties(modbusPointInfo1,modbusPointInfo);
            }
        });






//        byte[] bytesWrite2 = TCPServerNetty.hexToByteArray("010300000001840A");
        TCPServerNetty.getMap().entrySet().forEach(ch->{

            StringBuilder sb = new StringBuilder();
            sb.append("01060000").append(String.format("%04x", Integer.valueOf(entity.getValue())));


            byte[] bytesWrite4 = TCPServerNetty.hexToByteArray(sb.toString());

            log.info("向设备下发的信息为："+TCPServerNetty.bytesToHexString(CRC16.addCRC(bytesWrite4)));

            ch.getValue().writeAndFlush(CRC16.addCRC(bytesWrite4));
        });
        ChannelHandlerContext  channel = TCPServerNetty.getMap().get("10.4.95.71");
//        ByteBuf buf = ChannelHandlerContext.alloc().buffer(bytesWrite2.length);


//        log.info("向设备下发的信息为："+TCPServerNetty.bytesToHexString(bytesWrite2));

//        buf.writeBytes(bytesWrite2);
//        TCPServerNetty.getMap().get("10.4.95.71").write(bytesWrite2).addListener(new ChannelFutureListener(){
//            @Override
//            public void operationComplete(ChannelFuture future)
//                    throws Exception {
//                log.info("下发成功！");
//            }
//        });


//        channel.writeAndFlush(bytesWrite2);
//                .addListener(new ChannelFutureListener(){
//                    @Override
//                    public void operationComplete(ChannelFuture future)
//                            throws Exception {
//                        log.info("下发成功！");
//                    }
//                });


//        byte[] bytesWrite3 = TCPServerNetty.hexToByteArray("01030037000135C4");
//
//        log.info("向设备下发的信息为："+TCPServerNetty.bytesToHexString(bytesWrite3));
//
//        channel.writeAndFlush(bytesWrite3);
//                .addListener(new ChannelFutureListener(){
//                    @Override
//                    public void operationComplete(ChannelFuture future)
//                            throws Exception {
//                        log.info("下发成功！");
//                    }
//                });
//        String ss = "01060000"+""+String.valueOf(Integer.parseInt(entity.getValue(),16));

        StringBuilder sb = new StringBuilder();
        sb.append("01060000").append(String.format("%04x", Integer.valueOf(entity.getValue())));


        String cipherText = Tool.SC_Tea_Encryption_Str(sb.toString(),"2018091200000000");
        byte[] bytesWrite4 = TCPServerNetty.hexToByteArray(cipherText);

        log.info("向设备下发的信息为："+TCPServerNetty.bytesToHexString(CRC16.addCRC(bytesWrite4)));

        channel.writeAndFlush(CRC16.addCRC(bytesWrite4));
//                .addListener(new ChannelFutureListener(){
//                    @Override
//                    public void operationComplete(ChannelFuture future)
//                            throws Exception {
//                        log.info("下发成功！");
//                    }
//                });

//        byte[] bytesWrite5 = TCPServerNetty.hexToByteArray("0106000000C8885C");
//
//        log.info("向设备下发的信息为："+TCPServerNetty.bytesToHexString(bytesWrite5));

//        channel.writeAndFlush(bytesWrite5);
//                .addListener(new ChannelFutureListener(){
//                    @Override
//                    public void operationComplete(ChannelFuture future)
//                            throws Exception {
//                        log.info("下发成功！");
//                    }
//                });
        return respBody;

    }
}
