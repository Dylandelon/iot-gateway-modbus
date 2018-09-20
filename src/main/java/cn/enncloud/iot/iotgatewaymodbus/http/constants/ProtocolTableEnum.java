package cn.enncloud.iot.iotgatewaymodbus.http.constants;

/**
 * @description:    协议存储表标识
 * @author:         zdl
 * @createDate:     2018/7/31 17:38
 * @updateUser:     zdl
 * @updateDate:     2018/7/31 17:38
 * @updateRemark:   修改内容
 * @version:        1.0
 */
public enum ProtocolTableEnum {
    /**
     * modbus协议
     */
    MODBUS("MODBUS"),
    /***
     *  376.1协议
     */
    THREE_SEVEN_SIX_ONE("3761");

    private String value;

    ProtocolTableEnum(String value) {
        this.value=value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
