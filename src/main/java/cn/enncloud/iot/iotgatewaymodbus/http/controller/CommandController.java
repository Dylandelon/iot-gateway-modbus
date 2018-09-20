package cn.enncloud.iot.iotgatewaymodbus.http.controller;

import cn.enncloud.iot.iotgatewaymodbus.entity.exception.ConnectionException;
import cn.enncloud.iot.iotgatewaymodbus.entity.exception.ErrorResponseException;
import cn.enncloud.iot.iotgatewaymodbus.entity.exception.NoResponseException;
import cn.enncloud.iot.iotgatewaymodbus.entity.func.WriteSingleCoil;
import cn.enncloud.iot.iotgatewaymodbus.entity.func.response.WriteMultipleRegistersResponse;
import cn.enncloud.iot.iotgatewaymodbus.example.ClientForTests;
import cn.enncloud.iot.iotgatewaymodbus.http.constants.CodeEnum;
import cn.enncloud.iot.iotgatewaymodbus.http.response.DataRespBody;
import cn.enncloud.iot.iotgatewaymodbus.http.service.GatewayApiService;
import cn.enncloud.iot.iotgatewaymodbus.http.service.dtos.ModbusPointInfo;
import cn.enncloud.iot.iotgatewaymodbus.http.service.dtos.PointDTO;
import cn.enncloud.iot.iotgatewaymodbus.http.tools.ValidatorTools;
import cn.enncloud.iot.iotgatewaymodbus.http.vo.dto.DmsGateWayDevicControlVo;
import cn.enncloud.iot.iotgatewaymodbus.server.ModbusClient;
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





        ModbusClient modbusClient = ClientForTests.getInstance().getModbusClient();
//        boolean state = true;
//        for (int i = 0; i < 20; i++) {
//            WriteSingleCoil writeCoil = null;
//            try {
//                writeCoil = modbusClient.writeSingleCoil(modbusPointInfo.getRegisterAddr(), state);
//            } catch (NoResponseException e) {
//                e.printStackTrace();
//            } catch (ErrorResponseException e) {
//                e.printStackTrace();
//            } catch (ConnectionException e) {
//                e.printStackTrace();
//            }
//
//
//            log.info(writeCoil);
//
//            state = state ? false : true;
//        }
        int quantityOfRegisters = 10;

        int[] registers = new int[quantityOfRegisters];
        registers[0] = 0xFFFF;
        registers[1] = 0xF0F0;
        registers[2] = 0x0F0F;

        WriteMultipleRegistersResponse writeMultipleRegisters = null;
        try {
            writeMultipleRegisters = modbusClient.writeMultipleRegisters(1, quantityOfRegisters, registers);
        } catch (NoResponseException e) {
            e.printStackTrace();
        } catch (ErrorResponseException e) {
            e.printStackTrace();
        } catch (ConnectionException e) {
            e.printStackTrace();
        }
        log.info(writeMultipleRegisters);



        return respBody;

    }
}
