package cn.enncloud.iot.iotgatewaymodbus.http.constants;

/**
 * @description:    点表校验枚举
 * @author:         zdl
 * @createDate:     2018/7/31 17:21
 * @updateUser:     zdl
 * @updateDate:     2018/7/31 17:21
 * @updateRemark:   修改内容
 * @version:        1.0
 */
public enum CheckEnum {

    /**
     * 点表校验通过
     */
    IOT_CHECK_SUCCESS("1","校验通过"),
    /**
     * 点表校验不通过
     */
    IOT_CHECK_FIAL("0","校验不过"),
    /**
     * 2是 代表协议模板已应用不可以删除
     */
    IOT_CHECK_POINT("2","该点表设备注册时已经应用，不能删除"),
    /**
     * 3代表点表已经应用不可以删除
     */
    IOT_CHECK_PROTOCOL("3","该点表设备注册时已经应用，不能删除"),
    /**
     * 4代表点表网关不能删除
     */
    IOT_CHECK_GATEWAY("4","该网关设备注册时已经应用"),
    /**
     * 5代表该点表模板不存在
     */
    IOT_CHECK_DESPOINT("5","该点表模板不存在"),
    /**
     * 6代表该协议模板不存在
     */
    IOT_CHECK_PROTOCOLPOINT("6","该协议模板不存在");


    private  String code;
    private  String value;
    CheckEnum(String code, String value){
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
