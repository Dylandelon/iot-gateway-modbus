package cn.enncloud.iot.iotgatewaymodbus.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class OutBoundHandler extends ChannelOutboundHandlerAdapter {
    private static Logger logger = LoggerFactory.getLogger(OutBoundHandler.class);

    @Override
    public void write(ChannelHandlerContext ctx, Object msg,
                      ChannelPromise promise) throws Exception {


        if (msg instanceof byte[]) {
            byte[] bytesWrite = (byte[])msg;

//            String myflag = TCPServerNetty.bytesToHexString(bytesWrite);
//            String[] myarray = myflag.split(" ");
            if(bytesWrite.length>1){
                ByteBuf buf = ctx.alloc().buffer(bytesWrite.length);


                logger.info("out向设备下发的信息为："+TCPServerNetty.bytesToHexString(bytesWrite));
                buf.writeBytes(bytesWrite);
                ctx.writeAndFlush(buf).addListener(new ChannelFutureListener(){
                    @Override
                    public void operationComplete(ChannelFuture future)
                            throws Exception {
                        logger.info("out下发成功！"+TCPServerNetty.bytesToHexString(bytesWrite));
                    }
                });
            }

//            if(myarray[1].equalsIgnoreCase("73")){
//                ByteBuf buf = ctx.alloc().buffer(bytesWrite.length);
//                logger.info("向设备下发的信息为："+TCPServerNetty.bytesToHexString(bytesWrite));
//                buf.writeBytes(bytesWrite);
//                ctx.writeAndFlush(buf).addListener(new ChannelFutureListener(){
//                    @Override
//                    public void operationComplete(ChannelFuture future)
//                            throws Exception {
//                        logger.info("下发成功！");
//                    }
//                });
//            }
//            else if(myarray[1].equalsIgnoreCase("03")&&myarray[7].equalsIgnoreCase("0A") ){
//                byte[] bytesWrite2 = TCPServerNetty.hexToByteArray("010300000001840A");
//                ByteBuf buf = ctx.alloc().buffer(bytesWrite2.length);
//
//
//                logger.info("向设备下发的信息为："+TCPServerNetty.bytesToHexString(bytesWrite2));
//                buf.writeBytes(bytesWrite2);
//                ctx.writeAndFlush(buf).addListener(new ChannelFutureListener(){
//                    @Override
//                    public void operationComplete(ChannelFuture future)
//                            throws Exception {
//                        logger.info("下发成功！");
//                    }
//                });
//            }
//            logger.info("向设备下发的信息为："+TCPServerNetty.bytesToHexString(bytesWrite));


        }
    }
}

