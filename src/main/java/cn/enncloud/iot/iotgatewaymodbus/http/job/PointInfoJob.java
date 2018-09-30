package cn.enncloud.iot.iotgatewaymodbus.http.job;

import cn.enncloud.iot.iotgatewaymodbus.http.configration.*;
import cn.enncloud.iot.iotgatewaymodbus.http.constants.Constant;
import cn.enncloud.iot.iotgatewaymodbus.http.constants.Constants;
import cn.enncloud.iot.iotgatewaymodbus.http.data.IotDevice;
import cn.enncloud.iot.iotgatewaymodbus.http.data.IotHeader;
import cn.enncloud.iot.iotgatewaymodbus.http.data.IotMessage;
import cn.enncloud.iot.iotgatewaymodbus.http.data.IotMetric;
import cn.enncloud.iot.iotgatewaymodbus.http.service.GatewayApiService;
import cn.enncloud.iot.iotgatewaymodbus.http.service.dtos.DmsDeviceEntity;
import cn.enncloud.iot.iotgatewaymodbus.http.service.dtos.DmsGatewayEntity;
import cn.enncloud.iot.iotgatewaymodbus.http.service.dtos.DmsProtocolPointModbusEntity;
import cn.enncloud.iot.iotgatewaymodbus.http.tools.CRC16;
import cn.enncloud.iot.iotgatewaymodbus.http.tools.Tool;
import cn.enncloud.iot.iotgatewaymodbus.netty.TCPServerNetty;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Log4j
@Component
public class PointInfoJob implements Runnable{

    @Autowired
    private GatewayApiService gatewayApiService;



//    @Scheduled(cron = Constant.DIGITAL_CRON)
    @Override
    public void run() {
        // 获取网关
        List<DmsGatewayEntity> dmsGatewayEntityList =  gatewayApiService.getDatewayDTOFromApiByDomain("HPS");
        // 获取网关下设备
        List<DmsDeviceEntity> dmsDeviceEntityList = gatewayApiService.getDeviceDTOFromApiByGatewayId(dmsGatewayEntityList.get(0).getId());
        // 获取设备下点表
        List<DmsProtocolPointModbusEntity> dmsProtocolPointModbusEntityList = gatewayApiService.getModbusPointDTOFromApiByDeviceId(dmsDeviceEntityList.get(0).getId());

        // 根据网关获取channel
        ChannelHandlerContext channel = TCPServerNetty.getMap().get(dmsGatewayEntityList.get(0).getSerialNum());

        // 采集数据命令下发
        CmdMsg cmdMsg = new CmdMsg(1,"PAs","0");
        MsgPack msgPack = ModbusProto.getDownProtocolCmdDTO(dmsDeviceEntityList.get(0),dmsProtocolPointModbusEntityList,cmdMsg);
        byte[] onepa = ModbusProto.getBytesBuf(msgPack);
        log.info("设备的信息采集：" + TCPServerNetty.bytesToHexStringCompact(onepa));

        String cipherText=Tool.SC_Tea_Encryption_Str(TCPServerNetty.bytesToHexStringCompact(onepa),"2018091200000000");
        byte[] bytesWrite4 = TCPServerNetty.hexToByteArray(cipherText);

        log.info("向设备下发的信息为："+TCPServerNetty.bytesToHexString(CRC16.addCRC(bytesWrite4)));

        channel.writeAndFlush(CRC16.addCRC(bytesWrite4));
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
//        len = Read_Data(databytes);    接收返回信息
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
    }
}
