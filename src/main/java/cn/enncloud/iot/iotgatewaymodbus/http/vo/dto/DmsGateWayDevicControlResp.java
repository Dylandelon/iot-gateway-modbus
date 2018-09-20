package cn.enncloud.iot.iotgatewaymodbus.http.vo.dto;

public class DmsGateWayDevicControlResp {

    private String resultCode;
    private String resultDesc;
    private String devResponse;

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultDesc() {
        return resultDesc;
    }

    public void setResultDesc(String resultDesc) {
        this.resultDesc = resultDesc;
    }

    public String getDevResponse() {
        return devResponse;
    }

    public void setDevResponse(String devResponse) {
        this.devResponse = devResponse;
    }

    @Override
    public String toString() {
        return "DmsGateWayDevicControlResp{" +
                "resultCode='" + resultCode + '\'' +
                ", resultDesc='" + resultDesc + '\'' +
                ", devResponse='" + devResponse + '\'' +
                '}';
    }
}
