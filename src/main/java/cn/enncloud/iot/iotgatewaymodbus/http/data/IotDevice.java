package cn.enncloud.iot.iotgatewaymodbus.http.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

/**
 * @author sunhongqiang
 * @date 2018/08/06
 * 统一数据格式-设备信息
 */
@Data
@ToString
@NoArgsConstructor
public class IotDevice {

    /**
     * deviceId  设备标识(可选)
     */
    @JsonProperty("d")
    private String deviceId;

    /**
     * timestamp  时间戳（可选，采样时间三选一，优先级中）
     */
    @JsonProperty("t")
    private Long timestamp;

    /**
     * metrics 采集点(必须)
     */
    @JsonProperty("m")
    private List<IotMetric> metrics;

}
