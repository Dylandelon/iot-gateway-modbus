package cn.enncloud.iot.iotgatewaymodbus.http.tools;


/**
 * @author sunhongqiang
 * @date 2018/6/21.
 */
public class ModbusAttributyKeyUtil {

//    /**
//     * 将设备列表信息添加上下文
//     *
//     * @param ctx
//     * @param deviceInfoList
//     */
//    public static void setDeviceInfoList(ChannelHandlerContext ctx, List<DeviceInfo> deviceInfoList) {
//        AttributeKey<List<DeviceInfo>> attributeKey = AttributeKey.valueOf("deviceInfoList");
//        ctx.channel().attr(attributeKey).set(deviceInfoList);
//    }
//
//    /**
//     * 从上下文中获取设备列表信息
//     *
//     *
//     * @param ctx
//     */
//    public static List<DeviceInfo> getDeviceInfoList(ChannelHandlerContext ctx) {
//        AttributeKey<List<DeviceInfo>> attributeKey = AttributeKey.valueOf("deviceInfoList");
//        return ctx.channel().attr(AttributeKey.valueOf("deviceInfoList")).get();
//    }
//
//    /**
//     * 将设备信息添加上下文
//     *
//     * @param ctx
//     * @param deviceInfo
//     */
//    public static void setDeviceInfo(ChannelHandlerContext ctx, DeviceInfo deviceInfo) {
//        AttributeKey<DeviceInfo> attributeDeviceId = AttributeKey.valueOf("deviceInfo");
//        ctx.channel().attr(attributeDeviceId).set(deviceInfo);
//    }
//
//    /**
//     * 从上下文中获取设备信息
//     *
//     * @param ctx
//     */
//    public static DeviceInfo getDeviceInfo(ChannelHandlerContext ctx) {
//        AttributeKey<DeviceInfo> attributeKey = AttributeKey.valueOf("deviceInfo");
//        return ctx.channel().attr(attributeKey).get();
//    }
//
//    /**
//     * 将下发指令添加上下文
//     *
//     * @param ctx
//     * @param downProtocolDTO
//     */
//    public static void setDownProtocolDTO(ChannelHandlerContext ctx, DownProtocolDTO downProtocolDTO) {
//        AttributeKey<DownProtocolDTO> authenticationKey = AttributeKey.valueOf("downProtocolDTO");
//        ctx.channel().attr(authenticationKey).set(downProtocolDTO);
//    }
//
//    /**
//     * 上下文获取下发指令
//     *
//     * @param ctx
//     */
//    public static DownProtocolDTO getDownProtocolDTO(ChannelHandlerContext ctx) {
//        AttributeKey<DownProtocolDTO> authenticationKey = AttributeKey.valueOf("downProtocolDTO");
//        return ctx.channel().attr(authenticationKey).get();
//    }
//
//    /**
//     * 将下发指令时间添加上下文
//     *
//     * @param ctx
//     * @param time
//     */
//    public static void setSendTime(ChannelHandlerContext ctx, long time) {
//        AttributeKey<Long> sendTime = AttributeKey.valueOf("sendTime");
//        ctx.channel().attr(sendTime).set(time);
//    }
//
//    /**
//     * 上下文获取下发指令时间
//     *
//     * @param ctx
//     */
//    public static long getSendTime(ChannelHandlerContext ctx) {
//        AttributeKey<Long> sendTime = AttributeKey.valueOf("sendTime");
//        return ctx.channel().attr(sendTime).get() == null ? 0L : ctx.channel().attr(sendTime).get();
//    }

}
