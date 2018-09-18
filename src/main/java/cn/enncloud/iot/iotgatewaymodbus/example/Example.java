package cn.enncloud.iot.iotgatewaymodbus.example;



import cn.enncloud.iot.iotgatewaymodbus.entity.exception.ConnectionException;
import cn.enncloud.iot.iotgatewaymodbus.entity.exception.ErrorResponseException;
import cn.enncloud.iot.iotgatewaymodbus.entity.exception.NoResponseException;
import cn.enncloud.iot.iotgatewaymodbus.entity.func.response.ReadCoilsResponse;
import cn.enncloud.iot.iotgatewaymodbus.server.ModbusClient;
import cn.enncloud.iot.iotgatewaymodbus.server.ModbusServer;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ares
 */
public class Example {

    public static void main(String[] args) {
        ModbusServer modbusServer = ServerForTests.getInstance().getModbusServer();
        ModbusClient modbusClient = ClientForTests.getInstance().getModbusClient();

        ReadCoilsResponse readCoils = null;
        try {
            readCoils = modbusClient.readCoils(12321, 10);
        } catch (NoResponseException | ErrorResponseException | ConnectionException ex) {
            Logger.getLogger(Example.class.getName()).log(Level.SEVERE, ex.getLocalizedMessage());
        }

        System.out.println(readCoils);

        modbusClient.close();
        modbusServer.close();
    }
}
