package cn.enncloud.iot.iotgatewaymodbus.http.service.dtos;

import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * @author sunhongqiang
 * @date 2018/06/22
 */
@Data
@ToString
public class PointDTO<T> {

    private List<T> pointList;
}
