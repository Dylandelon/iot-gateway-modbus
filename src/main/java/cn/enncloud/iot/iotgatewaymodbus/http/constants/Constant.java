package cn.enncloud.iot.iotgatewaymodbus.http.constants;

/**
 * @author lifeia
 * @date 2018/7/31.
 */
public class Constant {

    /**
     * 实时数据采集任务执行频率
     */
    public static final String REAL_TIME_SCHEDULED_CRON = "${job.real-time.cron}";

    /**
     * 补招数据采集任务执行频率
     */
    public static final String MENDING_SCHEDULED_FIXED_DELAY = "${job.mending.fixedDelay}";

    public static final String MENDING_EVENT_TIME_SLOT = "${enn.mending.event.timeSlot}";

    /**
     * 状态量采集作业
     */
    public static final String DIGITAL_CRON = "${job.digital.cron}";


    /**
     * 最新时间戳键值
     */
    public static final String REAL_TIME_DEFAULT = "real_time_default";

    /**
     * 1魔法数字
     */
    public static final int ONE = 1;

    /**
     * 采集周期
     */
    public static final int CYCLE = 60;
}
