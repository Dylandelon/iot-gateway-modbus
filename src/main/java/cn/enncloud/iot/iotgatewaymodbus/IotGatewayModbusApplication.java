package cn.enncloud.iot.iotgatewaymodbus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class IotGatewayModbusApplication {

    public static void main(String[] args) {
        SpringApplication.run(IotGatewayModbusApplication.class, args);
    }
}
