package cn.enncloud.iot.iotgatewaymodbus.http.vo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public class DmsGateWayDevicControlVo {

    private Integer id;

    private Integer gatewayId;
    private Integer deviceId;
    private String pointName;
    private String value;
//    @JsonFormat(pattern = "yyyy-MM-ddTHH:mm:ss.fff")
    private LocalDateTime sendTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getGatewayId() {
        return gatewayId;
    }

    public void setGatewayId(Integer gatewayId) {
        this.gatewayId = gatewayId;
    }

    public Integer getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Integer deviceId) {
        this.deviceId = deviceId;
    }

    public String getPointName() {
        return pointName;
    }

    public void setPointName(String pointName) {
        this.pointName = pointName;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public LocalDateTime getSendTime() {
        return sendTime;
    }

    public void setSendTime(LocalDateTime sendTime) {
        this.sendTime = sendTime;
    }

    @Override
    public String toString() {
        return "DmsGateWayDevicControlVo{" +
                "id=" + id +
                ", gatewayId=" + gatewayId +
                ", deviceId=" + deviceId +
                ", pointName='" + pointName + '\'' +
                ", value='" + value + '\'' +
                ", sendTime=" + sendTime +
                '}';
    }
}
