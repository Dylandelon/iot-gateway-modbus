package cn.enncloud.iot.iotgatewaymodbus.http.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author sunhongqiang
 * @date 2018/8/16.
 * 统一数据格式-采集点
 */
@Data
@ToString
@NoArgsConstructor
public class IotMetric {

    /**
     * name 指标名称（必须）
     */
    @JsonProperty("n")
    private String name;

    /**
     * value 指标值(必须)
     */
    @JsonProperty("v")
    private Float value;

    /**
     * timestamp  时间戳（可选，采样时间三选一，优先级低）
     */
    @JsonProperty("t")
    private Long timestamp;
}
