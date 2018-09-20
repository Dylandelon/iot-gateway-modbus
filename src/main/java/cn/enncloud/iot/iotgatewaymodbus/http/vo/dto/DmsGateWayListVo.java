package cn.enncloud.iot.iotgatewaymodbus.http.vo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.List;

public class DmsGateWayListVo {

    private Integer id;

    @JsonFormat(pattern = "yyyy-MM-ddTHH:mm:ss.fff")
    private LocalDateTime recentOnline;

    private List<DmsDeviceListVo> deviceList;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDateTime getRecentOnline() {
        return recentOnline;
    }

    public void setRecentOnline(LocalDateTime recentOnline) {
        this.recentOnline = recentOnline;
    }

    public List<DmsDeviceListVo> getDeviceList() {
        return deviceList;
    }

    public void setDeviceList(List<DmsDeviceListVo> deviceList) {
        this.deviceList = deviceList;
    }

    @Override
    public String toString() {
        return "DmsGateWayListVo{" +
                "id=" + id +
                ", recentOnline=" + recentOnline +
                ", deviceList=" + deviceList +
                '}';
    }
}
