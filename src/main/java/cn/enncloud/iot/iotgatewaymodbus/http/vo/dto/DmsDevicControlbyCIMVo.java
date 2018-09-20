package cn.enncloud.iot.iotgatewaymodbus.http.vo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public class DmsDevicControlbyCIMVo {

    private Integer id;
    private String cimDomain;
    private String cimStation;
    private String cimDeviceId;
    private String cimPoint;
    private String value;
    @JsonFormat(pattern = "yyyy-MM-ddTHH:mm:ss.fff")
    private LocalDateTime SendTime;
    private String user;
    private String token;
    private String remark;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCimDomain() {
        return cimDomain;
    }

    public void setCimDomain(String cimDomain) {
        this.cimDomain = cimDomain;
    }

    public String getCimStation() {
        return cimStation;
    }

    public void setCimStation(String cimStation) {
        this.cimStation = cimStation;
    }

    public String getCimDeviceId() {
        return cimDeviceId;
    }

    public void setCimDeviceId(String cimDeviceId) {
        this.cimDeviceId = cimDeviceId;
    }

    public String getCimPoint() {
        return cimPoint;
    }

    public void setCimPoint(String cimPoint) {
        this.cimPoint = cimPoint;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public LocalDateTime getSendTime() {
        return SendTime;
    }

    public void setSendTime(LocalDateTime sendTime) {
        SendTime = sendTime;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "DmsDevicControlbyCIMVo{" +
                "id=" + id +
                ", cimDomain='" + cimDomain + '\'' +
                ", cimStation='" + cimStation + '\'' +
                ", cimDeviceId='" + cimDeviceId + '\'' +
                ", cimPoint='" + cimPoint + '\'' +
                ", value='" + value + '\'' +
                ", SendTime=" + SendTime +
                ", user='" + user + '\'' +
                ", token='" + token + '\'' +
                ", remark='" + remark + '\'' +
                '}';
    }
}
