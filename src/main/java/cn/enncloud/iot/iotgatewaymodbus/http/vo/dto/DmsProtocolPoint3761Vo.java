package cn.enncloud.iot.iotgatewaymodbus.http.vo.dto;

/**
 * @description:    协议376.1vo
 * @author:         zdl
 * @createDate:     2018/9/13 17:32
 * @updateUser:     zdl
 * @updateDate:     2018/9/13 17:32
 * @updateRemark:   修改内容
 * @version:        1.0
 */
public class DmsProtocolPoint3761Vo {
    private int alignType;
    private String dataFormat;
    private int unitDt;
    private long dmsDeviceType;
    private long dmsProtocolTypeId;

    public int getAlignType() {
        return alignType;
    }

    public void setAlignType(int alignType) {
        this.alignType = alignType;
    }

    public String getDataFormat() {
        return dataFormat;
    }

    public void setDataFormat(String dataFormat) {
        this.dataFormat = dataFormat;
    }

    public int getUnitDt() {
        return unitDt;
    }

    public void setUnitDt(int unitDt) {
        this.unitDt = unitDt;
    }

    public long getDmsDeviceType() {
        return dmsDeviceType;
    }

    public void setDmsDeviceType(long dmsDeviceType) {
        this.dmsDeviceType = dmsDeviceType;
    }

    public long getDmsProtocolTypeId() {
        return dmsProtocolTypeId;
    }

    public void setDmsProtocolTypeId(long dmsProtocolTypeId) {
        this.dmsProtocolTypeId = dmsProtocolTypeId;
    }

    @Override
    public String toString() {
        return "DmsProtocolPoint3761Vo{" +
                "alignType=" + alignType +
                ", dataFormat='" + dataFormat + '\'' +
                ", unitDt=" + unitDt +
                ", dmsDeviceType=" + dmsDeviceType +
                ", dmsProtocolTypeId=" + dmsProtocolTypeId +
                '}';
    }
}
