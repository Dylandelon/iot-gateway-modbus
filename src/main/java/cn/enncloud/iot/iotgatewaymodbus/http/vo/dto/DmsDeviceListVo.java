package cn.enncloud.iot.iotgatewaymodbus.http.vo.dto;

import java.time.LocalDateTime;

public class DmsDeviceListVo {
    private Integer id;

    private LocalDateTime recentOnline;

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

    @Override
    public String toString() {
        return "DmsDeviceListVo{" +
                "id=" + id +
                ", recentOnline=" + recentOnline +
                '}';
    }
}
