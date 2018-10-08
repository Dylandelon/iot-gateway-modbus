package cn.enncloud.iot.iotgatewaymodbus.http.service.dtos;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * @description: 网关实体
 * @author: zdl
 * @createDate: 2018/7/31 17:27
 * @updateUser: zdl
 * @updateDate: 2018/7/31 17:27
 * @updateRemark: 修改内容
 * @version: 1.0
 */
@Data
@ToString
public class DmsGatewayEntity {
    private long id;
    private String address;
    private String creator;
    private String domain;
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
    private long sysOrgId;
    private Long token;
    private String type;


}
