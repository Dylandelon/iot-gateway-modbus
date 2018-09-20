package cn.enncloud.iot.iotgatewaymodbus.http.service.dtos;

import lombok.Data;
import lombok.ToString;

/**
 * @author shq
 * @date 2018/09/12
 * 新奥376.1协议点信息
 */
@Data
@ToString
public class ProtocolPointInfo {

    /**
     * 数据格式
     */
    private String dataFormat;

    /**
     * 对齐方式（大小端）
     */
    private Integer alignType;

}
