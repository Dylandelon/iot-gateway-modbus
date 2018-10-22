package cn.enncloud.iot.iotgatewaymodbus.http.constants;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

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

//    Object LOCK = new Object();

    Lock lock = new ReentrantLock();
//    Condition condition=lock.newCondition();
    ScriptEngine JSE = new ScriptEngineManager().getEngineByName("JavaScript");


}
