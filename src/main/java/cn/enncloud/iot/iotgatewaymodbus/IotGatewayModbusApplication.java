package cn.enncloud.iot.iotgatewaymodbus;

import cn.enncloud.iot.iotgatewaymodbus.http.constants.Constant;
import cn.enncloud.iot.iotgatewaymodbus.http.constants.LogKeyConst;
import cn.enncloud.iot.iotgatewaymodbus.http.constants.NettyConfig;
import cn.enncloud.iot.iotgatewaymodbus.netty.TCPServerNetty;
import com.sun.javafx.binding.IntegerConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.MessageSource;

import java.util.Locale;

@EnableFeignClients
@SpringBootApplication
public class IotGatewayModbusApplication {
    private static final Logger logger = LoggerFactory.getLogger(IotGatewayModbusApplication.class);

    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(IotGatewayModbusApplication.class, args);
        try {
            new TCPServerNetty(ctx.getBean(NettyConfig.class).getPort()).start();
        } catch (Exception e) {
            System.exit(SpringApplication.exit(ctx));
        }
        logger.info(
                ctx.getBean(MessageSource.class).getMessage(
                        LogKeyConst.APPLICATION_START,
                        new Object[]{
                                (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1024 / 1024,
                                Runtime.getRuntime().totalMemory() / 1024 / 1024,
                                Runtime.getRuntime().maxMemory() / 1024 / 1024,
                        },
                        LogKeyConst.LOG_DEFAULT,
                        Locale.CHINA)
        );


    }
}
