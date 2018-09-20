package cn.enncloud.iot.iotgatewaymodbus.http.configration;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InBoundHandler extends SimpleChannelInboundHandler<byte[]> {
    private static Logger logger = LoggerFactory.getLogger(InBoundHandler.class);
    private static Map<String, Channel> map = new ConcurrentHashMap<String, Channel>();

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);

        logger.info("CLIENT" + getRemoteAddress(ctx) + " 接入连接");
        //往channel map中添加channel信息
        TCPServerNetty.getMap().put(getIPString(ctx), ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        //删除Channel Map中的失效Client
        TCPServerNetty.getMap().remove(getIPString(ctx));
        ctx.close();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, byte[] msg)
            throws Exception {
        logger.info("来自设备的信息：" + TCPServerNetty.bytesToHexString(msg));
        byte byteA3 = msg[1];
//        byte[] addressDomain = new byte[3];
//        byte[] addressDomain = new byte[5];
//        System.arraycopy(msg, 7, addressDomain, 0, 5);
////        System.arraycopy(msg, 0, addressDomain, 0, 3);
//        String str1 = getKeyFromArray(addressDomain); //生成key
//        logger.info("根据地址域生成的Key为：" + str1);

        if (byteA3 == 115) {
            byte[] addressDomain = new byte[5];
            System.arraycopy(msg, 7, addressDomain, 0, 5);
            String str1 = getKeyFromArray(addressDomain); //生成key
            logger.info("根据地址域生成的Key为：" + str1);

        } else {
            logger.info("上述消息是从设备采集到的消息！");
            TCPServerNetty.getMessageMap().put("1", msg);
        }
        ctx.channel().write(msg);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt)
            throws Exception {
        String socketString = ctx.channel().remoteAddress().toString();

        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.READER_IDLE) {
                logger.info("Client: " + socketString + " READER_IDLE 读超时");
                ctx.disconnect();
            } else if (event.state() == IdleState.WRITER_IDLE) {
                logger.info("Client: " + socketString + " WRITER_IDLE 写超时");
                ctx.disconnect();
            } else if (event.state() == IdleState.ALL_IDLE) {
                logger.info("Client: " + socketString + " ALL_IDLE 总超时");
                ctx.disconnect();
            }
        }
    }

    public static String getIPString(ChannelHandlerContext ctx) {
        String ipString = "";
        String socketString = ctx.channel().remoteAddress().toString();
        int colonAt = socketString.indexOf(":");
        ipString = socketString.substring(1, colonAt);
        return ipString;
    }


    public static String getRemoteAddress(ChannelHandlerContext ctx) {
        String socketString = "";
        socketString = ctx.channel().remoteAddress().toString();
        return socketString;
    }


    private String getKeyFromArray(byte[] addressDomain) {
        StringBuffer sBuffer = new StringBuffer();
        for (int i = 0; i < 5; i++) {
//        for (int i = 0; i < 3; i++) {
            sBuffer.append(addressDomain[i]);
        }
        return sBuffer.toString();
    }

    protected String to8BitString(String binaryString) {
        int len = binaryString.length();
        for (int i = 0; i < 8 - len; i++) {
            binaryString = "0" + binaryString;
        }
        return binaryString;
    }

    protected static byte[] combine2Byte(byte[] bt1, byte[] bt2) {
        byte[] byteResult = new byte[bt1.length + bt2.length];
        System.arraycopy(bt1, 0, byteResult, 0, bt1.length);
        System.arraycopy(bt2, 0, byteResult, bt1.length, bt2.length);
        return byteResult;
    }
}

