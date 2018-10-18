package cn.enncloud.iot.iotgatewaymodbus.http.constants;

import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "enn.netty")
@Data
@ToString
public class NettyConfig {
    private int port;

}
