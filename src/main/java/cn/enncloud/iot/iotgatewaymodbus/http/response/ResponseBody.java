package cn.enncloud.iot.iotgatewaymodbus.http.response;
/**
 * @description:    响应实体类
 * @author:         zdl
 * @createDate:     2018/7/31 17:38
 * @updateUser:     zdl
 * @updateDate:     2018/7/31 17:38
 * @updateRemark:   修改内容
 * @version:        1.0
 */
public class ResponseBody {

    private String code;

    private String msg;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
