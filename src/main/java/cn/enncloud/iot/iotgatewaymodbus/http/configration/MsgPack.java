package cn.enncloud.iot.iotgatewaymodbus.http.configration;

/**
 * Created by Thinkpad on 2018/9/26.
 */
public class MsgPack {
    byte funCode;
    byte[] sendData;
    int devAddress;
    int startAddress;
    int registerNum;
    byte[] value;
}
