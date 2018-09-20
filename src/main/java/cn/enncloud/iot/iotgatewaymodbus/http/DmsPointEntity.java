package cn.enncloud.iot.iotgatewaymodbus.http;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.Objects;

public class DmsPointEntity {
    private long id;
    private String creator;
    private long dmsDeviceTypeId;
    private LocalDateTime gmtCreate;
    private LocalDateTime gmtModified;
    private String modifier;
    private String name;
    private String remark;
    private String standardMetric;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public long getDmsDeviceTypeId() {
        return dmsDeviceTypeId;
    }

    public void setDmsDeviceTypeId(long dmsDeviceTypeId) {
        this.dmsDeviceTypeId = dmsDeviceTypeId;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public LocalDateTime getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(LocalDateTime gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public LocalDateTime getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(LocalDateTime gmtModified) {
        this.gmtModified = gmtModified;
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getStandardMetric() {
        return standardMetric;
    }

    public void setStandardMetric(String standardMetric) {
        this.standardMetric = standardMetric;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DmsPointEntity that = (DmsPointEntity) o;
        return id == that.id &&
                dmsDeviceTypeId == that.dmsDeviceTypeId &&
                Objects.equals(creator, that.creator) &&
                Objects.equals(gmtCreate, that.gmtCreate) &&
                Objects.equals(gmtModified, that.gmtModified) &&
                Objects.equals(modifier, that.modifier) &&
                Objects.equals(name, that.name) &&
                Objects.equals(remark, that.remark) &&
                Objects.equals(standardMetric, that.standardMetric);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, creator, dmsDeviceTypeId, gmtCreate, gmtModified, modifier, name, remark, standardMetric);
    }
}
