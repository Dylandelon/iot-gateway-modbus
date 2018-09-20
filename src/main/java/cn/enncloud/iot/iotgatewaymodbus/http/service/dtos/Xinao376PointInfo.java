package cn.enncloud.iot.iotgatewaymodbus.http.service.dtos;

import lombok.Data;
import lombok.ToString;

/**
 * @author sunhongqiang
 * @date 2018/06/21
 * 采集点
 */
@Data
@ToString
public class Xinao376PointInfo {

    /**
     * 采集点(cim)
     */
    private String metric;

    /**
     * 数据格式
     */
    private String dataFormat;

    /**
     * DT数据单元标识
     */
    private Integer dt;

    /**
     * 对齐方式（大小端）
     */
    private Integer alignType;

}
