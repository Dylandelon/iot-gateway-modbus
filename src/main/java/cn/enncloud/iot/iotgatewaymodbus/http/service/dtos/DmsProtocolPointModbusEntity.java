package cn.enncloud.iot.iotgatewaymodbus.http.service.dtos;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * @description:    协议modbus实体类
 * @author:         zdl
 * @createDate:     2018/7/31 17:35
 * @updateUser:     zdl
 * @updateDate:     2018/7/31 17:35
 * @updateRemark:   修改内容
 * @version:        1.0
 */
@Data
@ToString
public class DmsProtocolPointModbusEntity {
    private long id;
    private int alignType;
    private String creator;
    private String dataFormat;
    private long dmsPointId;
    private long dmsProtocolPointGroupId;
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime gmtCreate;
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime gmtModified;
    private String modifier;
    private String name;
    private int registerAddress;
    private int registerLen;
    private String remark;

    private String dmsPointName;


}
