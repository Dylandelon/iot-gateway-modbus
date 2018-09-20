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
public class ModbusPointInfo {

    /**
     * 采集点(cim)
     */
    private String metric;

    /**
     * 数据格式
     */
    private String dataFormat;

    /**
     * 寄存器地址
     */
    private Integer registerAddr;

    /**
     * 寄存器数量
     */
    private Integer registerLength;

    /**
     * 对齐方式（大小端）
     */
    private Integer alignType;

}
