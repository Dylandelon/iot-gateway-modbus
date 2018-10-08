package cn.enncloud.iot.iotgatewaymodbus.http.service.dtos;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * @description:    设备实体
 * @author:         zdl
 * @createDate:     2018/7/31 17:25
 * @updateUser:     zdl
 * @updateDate:     2018/7/31 17:25
 * @updateRemark:   修改内容
 * @version:        1.0
 */
@Data
@ToString
public class DmsDeviceEntity {
    private long id;
    private String creator;
    private Long dmsDeviceTypeId;
    private Long dmsGatewayId;
    private long dmsPointGroupId;
    private long dmsProtocolPointGroupId;
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime gmtCreate;
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime gmtModified;
    private String modifier;
    private String name;
    private String remark;
    private String serialNum;
    private Integer slaveAddress;
    private String standardNum;
    private String domain;
    private String stationId;
    private long sysOrgId;
    private String type;

}
