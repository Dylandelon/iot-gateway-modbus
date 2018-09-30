package cn.enncloud.iot.iotgatewaymodbus.http.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author sunhongqiang
 * @date 2018/08/06
 * 统一数据格式-头信息
 */
@Data
@ToString
@NoArgsConstructor
public class IotHeader {

    /**
     * version 版本（必须）
     */
    @JsonProperty("v")
    private String version;

    /**
     * gatewayId  网关标识（可选）
     */
    @JsonProperty("g")
    private String gatewayId;

    /**
     * timestamp  时间戳（可选，采样时间三选一，优先级高）
     */
    @JsonProperty("t")
    private Long timestamp;

}
