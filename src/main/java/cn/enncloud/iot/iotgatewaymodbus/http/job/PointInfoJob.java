package cn.enncloud.iot.iotgatewaymodbus.http.job;

import cn.enncloud.iot.iotgatewaymodbus.http.configration.DevPonitCtr;
import cn.enncloud.iot.iotgatewaymodbus.http.configration.ModbusProto;
import cn.enncloud.iot.iotgatewaymodbus.http.configration.MsgPack;
import cn.enncloud.iot.iotgatewaymodbus.http.configration.ReadUpInfo;
import cn.enncloud.iot.iotgatewaymodbus.http.constants.Constant;
import cn.enncloud.iot.iotgatewaymodbus.http.constants.Constants;
import cn.enncloud.iot.iotgatewaymodbus.http.constants.NettyChannelMap;
import cn.enncloud.iot.iotgatewaymodbus.http.data.IotDevice;
import cn.enncloud.iot.iotgatewaymodbus.http.data.IotHeader;
import cn.enncloud.iot.iotgatewaymodbus.http.data.IotMessage;
import cn.enncloud.iot.iotgatewaymodbus.http.data.IotMetric;
import cn.enncloud.iot.iotgatewaymodbus.http.service.GatewayApiService;
import cn.enncloud.iot.iotgatewaymodbus.http.service.dtos.DmsDeviceEntity;
import cn.enncloud.iot.iotgatewaymodbus.http.service.dtos.DmsGatewayEntity;
import cn.enncloud.iot.iotgatewaymodbus.http.service.dtos.DmsProtocolPointModbusEntity;
import cn.enncloud.iot.iotgatewaymodbus.http.service.dtos.ModbusCMDGroupPackages;
import cn.enncloud.iot.iotgatewaymodbus.http.tools.CRC16;
import cn.enncloud.iot.iotgatewaymodbus.http.tools.Tool;
import cn.enncloud.iot.iotgatewaymodbus.http.vo.dto.DmsDeviceListVo;
import cn.enncloud.iot.iotgatewaymodbus.http.vo.dto.DmsGateWayListVo;
import cn.enncloud.iot.iotgatewaymodbus.http.vo.dto.DmsGateWayUpdateVo;
import cn.enncloud.iot.iotgatewaymodbus.netty.TCPServerNetty;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.util.AttributeKey;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.MessageChannel;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Log4j
@Component
@EnableBinding(Source.class)
public class PointInfoJob implements Runnable{

    @Autowired
    private GatewayApiService gatewayApiService;
    @Autowired
    private MessageSource messageSource;
    @Resource
    private MessageChannel output;
    @Autowired
    private Environment environment;



    @Scheduled(cron = Constant.DIGITAL_CRON)
    @Override
    public void run() {
        // 获取网关
        List<DmsGatewayEntity> dmsGatewayEntityList =  gatewayApiService.getDatewayDTOFromApiByDomain("HPS");

        dmsGatewayEntityList.forEach(dmsGatewayEntity -> {
            // 根据网关获取channel
            Channel channel = NettyChannelMap.get(dmsGatewayEntity.getSerialNum());
            if(channel == null){
                log.info("网关没有注册："+dmsGatewayEntity.getSerialNum());
            }else{
                log.info("网关准备采集："+dmsGatewayEntity.getSerialNum());
                DmsGateWayUpdateVo dmsGateWayUpdateVo = new DmsGateWayUpdateVo();
                // ip+port
                String ip = "";
                try {
                    ip = InetAddress.getLocalHost().getHostAddress();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
                dmsGateWayUpdateVo.setId((int)dmsGatewayEntity.getId());
                dmsGateWayUpdateVo.setType(dmsGatewayEntity.getType()==null?"HPS":dmsGatewayEntity.getType());
                dmsGateWayUpdateVo.setRecentOnline(LocalDateTime.now());
                dmsGateWayUpdateVo.setServerName(environment.getProperty("spring.application.name"));
                dmsGateWayUpdateVo.setGatewayIp(ip);
                dmsGateWayUpdateVo.setGatewayPort(Integer.valueOf(environment.getProperty("server.port")));
                List<DmsGateWayListVo> gatewayList = new ArrayList<>();
                DmsGateWayListVo dmsGateWayListVo = new DmsGateWayListVo();
                dmsGateWayListVo.setId((int)dmsGatewayEntity.getId());
                dmsGateWayListVo.setRecentOnline(LocalDateTime.now());
                dmsGateWayUpdateVo.setGatewayList(gatewayList);
                List<Boolean> onlineFlag = new ArrayList<>();
                // 获取网关下设备
                List<DmsDeviceEntity> dmsDeviceEntityList = gatewayApiService.getDeviceDTOFromApiByGatewayId(dmsGatewayEntity.getId());
                dmsDeviceEntityList.forEach(dmsDeviceEntity -> {
                    // 获取设备下点表
                    List<DmsProtocolPointModbusEntity> dmsProtocolPointModbusEntityList = gatewayApiService.getModbusPointDTOFromApiByDeviceId(dmsDeviceEntity.getId());

                    // 采集数据命令下发
//                CmdMsg cmdMsg = new CmdMsg(1,"PAs","0");
//                    MsgPack msgPack = ModbusProto.getDownProtocolCmdDTO(dmsDeviceEntity,dmsProtocolPointModbusEntityList);
                    DevPonitCtr dPCtr = new DevPonitCtr();
//                    MsgPack msgPack = ModbusProto.getDownProtocolDTO(dmsDeviceEntity,dPCtr,dmsProtocolPointModbusEntityList);
                    List<ModbusCMDGroupPackages> modbusCMDGroupPackagesList = ModbusProto.generateDownProtocol(dmsDeviceEntity,dmsProtocolPointModbusEntityList);
                    modbusCMDGroupPackagesList.forEach(modbusCMDGroupPackages -> {
                        byte[] onepa = ModbusProto.getBytesBuf(modbusCMDGroupPackages.getMsgPack());
                        byte[] bytesWrite = CRC16.addCRC(onepa);
                        log.info("向设备下发的信息未加密为："+TCPServerNetty.bytesToHexString(bytesWrite));
                        String cipherText = Tool.SC_Tea_Encryption_Str(TCPServerNetty.bytesToHexStringCompact(bytesWrite),"2018091200000000");
                        byte[] bytesWriteSec = TCPServerNetty.hexToByteArray(cipherText);
                        log.info("向设备下发的信息加密为："+TCPServerNetty.bytesToHexString(bytesWriteSec));
                        ;
                        AttributeKey<DmsDeviceEntity> attributeKey = AttributeKey.valueOf("dmsDeviceEntity");
                        channel.attr(attributeKey).set(dmsDeviceEntity);
                        AttributeKey<ModbusCMDGroupPackages> attributeKey2 = AttributeKey.valueOf("modbusCMDGroupPackages");
                        channel.attr(attributeKey2).set(modbusCMDGroupPackages);
                        channel.writeAndFlush(bytesWriteSec).addListener(new ChannelFutureListener(){
                            @Override
                            public void operationComplete(ChannelFuture future)
                                    throws Exception {
                                log.info("job下发成功！"+TCPServerNetty.bytesToHexString(bytesWrite));
                            }
                        });

                        long startTime = System.currentTimeMillis();
                        ReadUpInfo readUpInfo=null;
                        IotMessage kafkaData=null;

                        while (System.currentTimeMillis() - startTime <20*1000){
                            byte[] bytesRec = TCPServerNetty.getMessageMap().get((long)modbusCMDGroupPackages.getDmsProtocolPointModbusEntityList().get(0).getRegisterAddress());
//                        log.info("获取采集返回的信息："+bytesRec);
                            if(bytesRec != null ){
                                onlineFlag.add(true);
                                try {
//                                    readUpInfo= ModbusProto.getUpProtocolDTO(msgPack,bytesRec,bytesRec.length,dmsProtocolPointModbusEntityList,"");
                                    readUpInfo= ModbusProto.analysisUpProtocol(bytesRec,bytesRec.length,modbusCMDGroupPackages,"");
                                    Long timestamp = System.currentTimeMillis();
                                    kafkaData=convertData(dmsDeviceEntity,readUpInfo,timestamp);
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                                try {
                                    //发送数据到kafka
                                    sendDataToKafka(dmsDeviceEntity.getSerialNum(), kafkaData);
                                    break;
                                } catch (Exception ex) {
                                    ObjectMapper mapper = new ObjectMapper();
                                    log.error("uuid:,发送kafka错误!入参:{}");

//                log.error("uuid:,发送kafka错误!入参:{}", JsonUtils.writeValueAsString(kafkaData));
                                }
                            }
                        }
                    });

                    if(!onlineFlag.isEmpty()&&onlineFlag.get(0) ==true){
                        List<DmsDeviceListVo> deviceList = new ArrayList<>();
                        DmsDeviceListVo dmsDeviceListVo = new DmsDeviceListVo();
                        dmsDeviceListVo.setRecentOnline(LocalDateTime.now());
                        dmsDeviceListVo.setId((int)dmsDeviceEntity.getId());
                        deviceList.add(dmsDeviceListVo);
                        dmsGateWayListVo.setDeviceList(deviceList);
                        gatewayList.add(dmsGateWayListVo);
                    }





                });
                gatewayApiService.dmsGateWayUpdatePost(dmsGateWayUpdateVo);
            }


        });




        //接受返回的数据

        // 设备数据发送kafka

        // 设备状态同步






    }
    public void ReadData(DevPonitCtr dPCtr, byte[] databytes)
    {
        int len=0;
        MsgPack msgPack=null;
        List<DmsGatewayEntity> dmsGatewayEntityList =  gatewayApiService.getDatewayDTOFromApiByDomain("HPS");
        // 获取网关下设备
        List<DmsDeviceEntity> dmsDeviceEntityList = gatewayApiService.getDeviceDTOFromApiByGatewayId(dmsGatewayEntityList.get(0).getId());
        // 获取设备下点表
        List<DmsProtocolPointModbusEntity> dmsProtocolPointModbusEntityList = gatewayApiService.getModbusPointDTOFromApiByDeviceId(dmsDeviceEntityList.get(0).getId());
        if (dPCtr.getDevIndex() >= dmsDeviceEntityList.size())
            dPCtr.setDevIndex(0);
        DmsDeviceEntity dmsDeviceEntity = dmsDeviceEntityList.get(dPCtr.getDevIndex());
        if (dmsDeviceEntity != null) {

            msgPack = ModbusProto.getDownProtocolDTO(dmsDeviceEntity,dPCtr,dmsProtocolPointModbusEntityList);
        }
        byte[] sendbyte=null;
        if(msgPack!=null)
        {
            sendbyte= ModbusProto.getBytesBuf(msgPack);
            log.info("设备的信息采集：" + TCPServerNetty.bytesToHexStringCompact(sendbyte));

            String cipherText=Tool.SC_Tea_Encryption_Str(TCPServerNetty.bytesToHexStringCompact(sendbyte),"2018091200000000");
            byte[] bytesWrite4 = TCPServerNetty.hexToByteArray(cipherText);

            log.info("向设备下发的信息为："+TCPServerNetty.bytesToHexString(CRC16.addCRC(bytesWrite4)));

//            channel.writeAndFlush(CRC16.addCRC(bytesWrite4));
        }
        // 接收返回信息
//        len = Read_Data(databytes);
        if (len <= 0) {
            return;
        } else if(msgPack!=null){
            Date date = new Date();
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            ReadUpInfo readUpInfo=null;
            IotMessage kafkaData=null;
            try {
                readUpInfo= ModbusProto.getUpProtocolDTO(msgPack,databytes,len,dmsProtocolPointModbusEntityList,"");
                Long timestamp = System.currentTimeMillis();
                kafkaData=convertData(dmsDeviceEntity,readUpInfo,timestamp);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            try {
                //发送数据到kafka
                sendDataToKafka(dmsDeviceEntity.getSerialNum(), kafkaData);
            } catch (Exception ex) {
//                log.error("uuid:,发送kafka错误!入参:{}", JsonUtils.writeValueAsString(kafkaData));
            }
        }
    }
    /**
     * @param deviceInfo 设备信息
     * @param readUpInfo 发送数据到kafka
     * @param timestamp  时间戳
     */
    private IotMessage convertData(DmsDeviceEntity  deviceInfo, ReadUpInfo readUpInfo, Long timestamp) {

        IotHeader header = new IotHeader();
        header.setVersion(Constants.KAFKA_DATA_VERSION);
        header.setGatewayId(deviceInfo.getSerialNum());
        header.setTimestamp(timestamp / 1000);
        List<IotDevice> deviceDataList = new ArrayList<>();
        IotDevice deviceData = new IotDevice();
        deviceData.setDeviceId(deviceInfo.getSerialNum());
        List<IotMetric> metricDTOList = new ArrayList<>();
        Map<String, Object> data = readUpInfo.getData();
        for (String key : data.keySet()) {
            IotMetric iotMetric = new IotMetric();
            iotMetric.setName(key);
            iotMetric.setValue((Float) data.get(key));
            metricDTOList.add(iotMetric);
        }
        deviceData.setMetrics(metricDTOList);
        deviceDataList.add(deviceData);
        IotMessage kafkaData = new IotMessage();
        kafkaData.setHeader(header);
        kafkaData.setBody(deviceDataList);

        return kafkaData;
    }

    /**
     * 发送数据到kafka
     */
    private void sendDataToKafka(String key, IotMessage kafkaData) throws Exception {

//        kafkaUtils.sendSync(kafkaConfig.getTopic(), key, JsonUtils.writeValueAsString(kafkaData));
        this.output.send(MessageBuilder.withPayload(kafkaData).build());
    }
}
