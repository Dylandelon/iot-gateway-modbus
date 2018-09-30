package cn.enncloud.iot.iotgatewaymodbus.http.constants;

/**
 * @author shq
 * @date 2018/09/06
 */
public interface Constants {

    /**
     * kafka数据结构版本
     */
    String KAFKA_DATA_VERSION = "1.3";

    /**
     * 注册包长度
     */
    int REGISTER_PACKAGE_LENGTH = 10;

    /**
     * 数据包最小长度
     */
    int DATA_PACKAGE_MIN_LENGTH = 18;

    /**
     * "03"功能码
     */
    String FUNCTION_CODE_03 = "03";

}
