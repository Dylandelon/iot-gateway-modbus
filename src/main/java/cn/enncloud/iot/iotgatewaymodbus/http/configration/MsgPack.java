package cn.enncloud.iot.iotgatewaymodbus.http.configration;

import lombok.Data;
import lombok.ToString;

/**
 * Created by Thinkpad on 2018/9/26.
 */
@Data
@ToString
public class MsgPack {
    private byte funCode;
    private byte[] sendData;
    private int devAddress;
    private int startAddress;
    private int registerNum;
    private String value;
    private String dataFormat;
}
