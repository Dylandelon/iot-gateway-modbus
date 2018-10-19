package cn.enncloud.iot.iotgatewaymodbus.http.constants;

public enum DeviceCodeEnum {
    /**
     * 命令响应成功
     */
    IOT_DEVICE_SUCCESS("0","命令响应成功"),
    /**
     * 认证权限不通过
     */
    IOT_DEVICE_IDENTITY("101","认证权限不通过"),
    /**
     * 备和点不存在
     */
    IOT_DEVICE_POINT_UNEXIST("201","设备和点不存在"),
    /**
     * 设备不在线
     */
    IOT_DEVICE_offLINE("202","设备不在线"),
    /**
     * 云网关响应超时
     */
    IOT_DEVICE_TIMEOUT("203","云网关响应超时"),
    /**
     * 协议点不存在
     */
    IOT_DEVICE_PROTOCOL_UNEXIST("301","协议点不存在"),
    /**
     * 通道忙，等待超时
     */
    IOT_DEVICE_BUSY("302","通道忙，等待超时"),
    /**
     * 等待响应超时
     */
    IOT_DEVICE_WAIT_TIMEOUT("303","等待响应超时"),
    /**
     * 命令返回失败
     */
    IOT_DEVICE_COMMAND_TIMEOUT("304","命令返回失败"),
    /**
     * 命令响应未知
     */
    IOT_DEVICE_ERROR("305","命令响应未知");


    private  String code;
    private  String value;
    DeviceCodeEnum(String code, String value){
        this.code = code;
        this.value = value;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
