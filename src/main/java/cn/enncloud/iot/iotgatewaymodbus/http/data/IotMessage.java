package cn.enncloud.iot.iotgatewaymodbus.http.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

/**
 * @author sunhongqiang
 * @date 2018/08/16
 * 统一数据格式-完整数据包
 */
@Data
@ToString
@NoArgsConstructor
public class IotMessage {

    /**
     * header  头信息
     */
    @JsonProperty("h")
    private IotHeader header;

    /**
     * body   数据域
     */
    @JsonProperty("b")
    private List<IotDevice> body;

    /**
     * @param jsonData json String
     * @return IotMessage instance or null
     */
//    public static IotMessage fromJson(String jsonData) {
//        return JsonUtils.readObject(jsonData, IotMessage.class);
//    }
//
//    public String toJson() {
//        return JsonUtils.writeValueAsString(this);
//    }

}
