package cn.enncloud.iot.iotgatewaymodbus.http.configration;

import lombok.Data;
import lombok.ToString;

/**
 * Created by Thinkpad on 2018/9/27.
 */
@Data
@ToString
public class DevPonitCtr {
    private int devIndex;
    private int funCodeIndex;
    private int ponitIndex;
    public DevPonitCtr()
    {
        devIndex=0;
        funCodeIndex=0;
        ponitIndex=0;
    }
}
