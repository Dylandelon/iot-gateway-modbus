package cn.enncloud.iot.iotgatewaymodbus.example;


import cn.enncloud.iot.iotgatewaymodbus.entity.exception.ConnectionException;
import cn.enncloud.iot.iotgatewaymodbus.server.ModbusServer;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ares
 */
public class ServerForTests {

    private final ModbusServer modbusServer;

    private ServerForTests() {
        modbusServer = new ModbusServer(30502); //ModbusConstants.MODBUS_DEFAULT_PORT);
        try {
            modbusServer.setup(new ModbusRequestHandlerExample());
        } catch (ConnectionException ex) {
            Logger.getLogger(ServerForTests.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public ModbusServer getModbusServer() {
        return modbusServer;
    }

    public static ServerForTests getInstance() {
        return ServerForTestsHolder.INSTANCE;
    }

    private static class ServerForTestsHolder {

        private static final ServerForTests INSTANCE = new ServerForTests();
    }
}
