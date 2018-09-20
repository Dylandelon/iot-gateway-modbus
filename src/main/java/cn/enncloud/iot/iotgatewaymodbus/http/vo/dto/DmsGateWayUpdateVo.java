package cn.enncloud.iot.iotgatewaymodbus.http.vo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.List;

public class DmsGateWayUpdateVo {

    private Integer id;

    private String type;

    @JsonFormat(pattern = "yyyy-MM-ddTHH:mm:ss.fff")
    private LocalDateTime recentOnline;

    private String serverName;

    private String gatewayIp;

    private Integer gatewayPort;

    private List<DmsGateWayListVo> gatewayList;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDateTime getRecentOnline() {
        return recentOnline;
    }

    public void setRecentOnline(LocalDateTime recentOnline) {
        this.recentOnline = recentOnline;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getGatewayIp() {
        return gatewayIp;
    }

    public void setGatewayIp(String gatewayIp) {
        this.gatewayIp = gatewayIp;
    }

    public Integer getGatewayPort() {
        return gatewayPort;
    }

    public void setGatewayPort(Integer gatewayPort) {
        this.gatewayPort = gatewayPort;
    }

    public List<DmsGateWayListVo> getGatewayList() {
        return gatewayList;
    }

    public void setGatewayList(List<DmsGateWayListVo> gatewayList) {
        this.gatewayList = gatewayList;
    }

    @Override
    public String toString() {
        return "DmsGateWayUpdateVo{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", recentOnline=" + recentOnline +
                ", serverName='" + serverName + '\'' +
                ", gatewayIp='" + gatewayIp + '\'' +
                ", gatewayPort=" + gatewayPort +
                ", gatewayList=" + gatewayList +
                '}';
    }
}
