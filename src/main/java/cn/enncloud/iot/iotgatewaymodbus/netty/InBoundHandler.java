package cn.enncloud.iot.iotgatewaymodbus.netty;

import cn.enncloud.iot.iotgatewaymodbus.http.constants.NettyChannelMap;
import cn.enncloud.iot.iotgatewaymodbus.http.service.dtos.DmsDeviceEntity;
import cn.enncloud.iot.iotgatewaymodbus.http.service.dtos.DmsGatewayEntity;
import cn.enncloud.iot.iotgatewaymodbus.http.service.dtos.ModbusCMDGroupPackages;
import cn.enncloud.iot.iotgatewaymodbus.http.tools.Tool;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.AttributeKey;
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
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        //删除Channel Map中的失效Client
//        TCPServerNetty.getMap().remove(getIPString(ctx));
        NettyChannelMap.remove((SocketChannel)ctx.channel());
        ctx.close();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, byte[] msg)
            throws Exception {
        logger.info("来自设备的信息：" + TCPServerNetty.bytesToHexString(msg));

        if(msg.length==1){
            ByteBuf buf = ctx.alloc().buffer(msg.length);


            logger.info("向设备下发的信息为："+TCPServerNetty.bytesToHexString(msg));
            buf.writeBytes(msg);
            ctx.writeAndFlush(buf).addListener(new ChannelFutureListener(){
                @Override
                public void operationComplete(ChannelFuture future)
                        throws Exception {
                    logger.info("心跳下发成功！");
                }
            });
        }else{
            AttributeKey<DmsGatewayEntity> attributeKey = AttributeKey.valueOf("dmsGatewayEntity");
            DmsGatewayEntity dmsGatewayEntity = ctx.channel().attr(attributeKey).get();
            if(dmsGatewayEntity ==null){
                logger.info("根据注册信息解析手机号：" + Tool.getMobileNO(new String(msg,"utf-8")));
                NettyChannelMap.add((Tool.getMobileNO(new String(msg,"utf-8"))),(SocketChannel)ctx.channel());
            }else{
                logger.info("来自设备的信息2：" + TCPServerNetty.bytesToHexStringCompact(msg));

                String plainText=Tool.SC_Tea_Encryption_Str(TCPServerNetty.bytesToHexStringCompact(msg),"2018091200000000");
                logger.info("来自设备的信息解密后：{}" ,plainText);
                byte[] bytesTemp = TCPServerNetty.hexToByteArray(plainText);

                AttributeKey<ModbusCMDGroupPackages> attributeKeyCmd = AttributeKey.valueOf("modbusCMDGroupPackagesDown");
                ModbusCMDGroupPackages cmdGroupPackages= ctx.channel().attr(attributeKeyCmd).get();
                if(cmdGroupPackages!=null){
                    TCPServerNetty.getMessageMap().put((long)cmdGroupPackages.getDmsProtocolPointModbusEntityList().get(0).getRegisterAddress()+123, bytesTemp);
                }

                AttributeKey<ModbusCMDGroupPackages> attributeKey2 = AttributeKey.valueOf("modbusCMDGroupPackages");
                ModbusCMDGroupPackages modbusCMDGroupPackages = ctx.channel().attr(attributeKey2).get();
                if(modbusCMDGroupPackages == null){
                    logger.info("本轮接收的数据是命令下发返回的结果");
                }else{
                    TCPServerNetty.getMessageMap().put((long)modbusCMDGroupPackages.getDmsProtocolPointModbusEntityList().get(0).getRegisterAddress(), bytesTemp);
                }
                logger.info("上述消息是从设备采集到的消息！");

            }
        }


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

