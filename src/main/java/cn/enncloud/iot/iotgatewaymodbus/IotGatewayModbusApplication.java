package cn.enncloud.iot.iotgatewaymodbus;

import cn.enncloud.iot.iotgatewaymodbus.http.configration.TCPServerNetty;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;

@EnableFeignClients
@SpringBootApplication
public class IotGatewayModbusApplication {

    public static void main(String[] args) {
        SpringApplication.run(IotGatewayModbusApplication.class, args);
//        ModbusServer modbusServer = new ModbusServer(30502); //ModbusConstants.MODBUS_DEFAULT_PORT);
//        try {
//            modbusServer.setup(new ModbusRequestHandlerExample());
//        } catch (ConnectionException ex) {
//            Logger.getLogger(ServerForTests.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        try {
//            TCPServerNetty tcpServerNetty = context.getBean(TCPServerNetty.class);
//            tcpServerNetty.start();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        try {
            new TCPServerNetty(80).start();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
