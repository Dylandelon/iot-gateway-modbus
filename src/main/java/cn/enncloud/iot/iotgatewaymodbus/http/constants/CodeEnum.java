package cn.enncloud.iot.iotgatewaymodbus.http.constants;

/**
 * @description:    请求影响编码
 * @author:         zdl
 * @createDate:     2018/7/31 17:22
 * @updateUser:     zdl
 * @updateDate:     2018/7/31 17:22
 * @updateRemark:   修改内容
 * @version:        1.0
 */
public enum CodeEnum {

    /**
     *   请求操作成功
     */
    IOT_SUCCESS("10000","操作成功"),
    /**
     * 请求操作失败
     */
    IOT_FAIL("10001","操作失败"),
    /**
     * 请求参数格式不正确
     */
    IOT_PARAM("10005","请求参数格式不正确"),
    /**
     *   请求数据已存在
     */
    IOT_EXIST("10006","数据已存在"),
    /**
     * 请求系统异常
     */
    IOT_EXCEPTION("99999","系统异常");


    private  String code;
    private  String value;
    CodeEnum(String code, String value){
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
