package cn.enncloud.iot.iotgatewaymodbus.example;


import cn.enncloud.iot.iotgatewaymodbus.entity.exception.ConnectionException;
import cn.enncloud.iot.iotgatewaymodbus.server.ModbusClient;
import lombok.extern.log4j.Log4j;

/**
 *
 * @author ares
 */
@Log4j
public class ClientForTests {


    private final ModbusClient modbusClient;

    private ClientForTests() {
        modbusClient = new ModbusClient("localhost" /*
                 * "192.168.1.55"
                 */, 30502); //ModbusConstants.MODBUS_DEFAULT_PORT);

        try {
            modbusClient.setup();
        } catch (ConnectionException ex) {
            log.info(ex);
        }
    }

    public ModbusClient getModbusClient() {
        return modbusClient;
    }

    public static ClientForTests getInstance() {
        return ClientAndServerHolder.INSTANCE;
    }

    private static class ClientAndServerHolder {

        private static final ClientForTests INSTANCE = new ClientForTests();
    }
}
