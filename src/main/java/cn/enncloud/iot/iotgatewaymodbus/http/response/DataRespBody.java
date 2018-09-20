package cn.enncloud.iot.iotgatewaymodbus.http.response;

import java.util.List;

/**
 * @description:    响应信息体
 * @author:         zdl
 * @createDate:     2018/7/31 17:23
 * @updateUser:     zdl
 * @updateDate:     2018/7/31 17:23
 * @updateRemark:   修改内容
 * @version:        1.0
 */
public class DataRespBody<T extends  List> extends ResponseBody {

    private T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
