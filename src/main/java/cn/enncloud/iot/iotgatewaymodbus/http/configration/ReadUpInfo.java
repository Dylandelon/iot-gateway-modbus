package cn.enncloud.iot.iotgatewaymodbus.http.configration;


import lombok.Data;
import lombok.ToString;

import java.util.Map;

/**
 * @author sunhongqiang
 * @date 2018/6/15
 * 读寄存器（0x03）上行
 */
@Data
@ToString
public class ReadUpInfo extends AbstractUpProtocolInfo {

    /**
     * 从站地址（1个字节）
     */
    private Integer address;

    /**
     * 功能码（1个字节）
     */
    private String functionCode;

    /**
     * 字节数（1个字节，读）
     */
    private Integer byteNum;

    /**
     * CRC校验（2个字节）
     */
    private String crcStr;

    /**
     * 数据项
     */
    private Map<String, Object> data;

}
