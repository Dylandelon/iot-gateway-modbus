package cn.enncloud.iot.iotgatewaymodbus.http.configration;


import cn.enncloud.iot.iotgatewaymodbus.http.service.dtos.DmsDeviceEntity;
import cn.enncloud.iot.iotgatewaymodbus.http.service.dtos.DmsProtocolPointModbusEntity;
import cn.enncloud.iot.iotgatewaymodbus.http.service.dtos.ModbusCMDGroupPackages;
import lombok.extern.log4j.Log4j;
import org.springframework.util.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Thinkpad on 2018/9/27.
 */
@Log4j
public class ModbusProto {
    static void print_byte(String name,byte[] data,int len)
    {
        String logmsg=name;
        for(int i=0;i<len;i++)
        {
            logmsg=logmsg+String.format("%02x ",data[i]);
        }
        log.info(logmsg);
    }
    static void print_byte(String name,byte[] data)
    {
        String logmsg=name;
        for(int i=0;i<data.length;i++)
        {
            logmsg=logmsg+String.format("%02x ",data[i]);
        }
        log.info(logmsg);
    }
    public static byte[] getBytesBuf(MsgPack msgPack) {
        byte[] datbytes=null;
        if(msgPack.funCode<5)
        {
            datbytes=new byte[6];
            datbytes[0]=(byte)msgPack.devAddress;
            datbytes[1]=msgPack.funCode;
            datbytes[2]=(byte)(msgPack.startAddress>>8);
            datbytes[3]=(byte)msgPack.startAddress;
            datbytes[4]=(byte)(msgPack.registerNum>>8);
            datbytes[5]=(byte)msgPack.registerNum;
            //datbytes=Tool.SC_Tea_Encryption(datbytes,secretKey.getBytes());
        }
        return datbytes;
    }
    /**
     * 封装下行协议
     */
    public static MsgPack getDownProtocolCmdDTO(DmsDeviceEntity deviceInfo, List<DmsProtocolPointModbusEntity> pointDTO) {

        //将点表信息
        MsgPack msgPack=new MsgPack();

        byte[] data=new byte[7];
        msgPack.devAddress=(byte)(int)deviceInfo.getSlaveAddress();
        int size = pointDTO.size();
        for(int i=0;i<size;i++)
        {
            DmsProtocolPointModbusEntity info=pointDTO.get(i);
//            if(pointDTO.get(i).getDmsPointName().equals(cmdMsg.getPonintName()))
//            {
//
//            }
            msgPack.startAddress=info.getRegisterAddress();
            msgPack.registerNum=info.getRegisterLen();
            //msgPack.value=cmdMsg.value
            msgPack.funCode=(byte)info.getRegType();

        }
        return msgPack;
    }
    public static MsgPack getDownProtocolDTO(DmsDeviceEntity deviceInfo, DevPonitCtr dPCtr,List<DmsProtocolPointModbusEntity> pointDTO) {

        //将点表信息
        MsgPack msgPack=new MsgPack();
        List<DmsProtocolPointModbusEntity> pointInfoList=null;
        int funcode=0x03;
        if(dPCtr.getFunCodeIndex()>4)
        {
            dPCtr.setFunCodeIndex(1);
            dPCtr.setPonitIndex(0);
        }
        List<DmsProtocolPointModbusEntity> pointInfoList2=null;
        for(int i=dPCtr.getFunCodeIndex();i<5;i++)
        {
            pointInfoList2 = pointInfoListFilter(pointDTO,i);
            if(pointInfoList2.size()>0)
            {
                funcode=i;
                if(dPCtr.getFunCodeIndex()==i)
                {
                    //当前点偏移超过当前时，更换功能码
                    if(dPCtr.getPonitIndex()>=pointInfoList2.size()) {
                        if(i==4) {
                            dPCtr.setPonitIndex(0);
                            dPCtr.setFunCodeIndex(0);
                            dPCtr.setDevIndex(dPCtr.getDevIndex()+1);
                            //该设备读取完成，
                            return null;
                            //切换设备
                        }else
                        {
                            dPCtr.setPonitIndex(0);
                            dPCtr.setFunCodeIndex(dPCtr.getFunCodeIndex()+1);
                        }
                    }else //还在当前功能码
                    {
                        break;
                    }
                } else
                {
                    dPCtr.setPonitIndex(0);
                    dPCtr.setFunCodeIndex(i);
                    //结束遍历，开始新功能码读取；
                    break;
                }
            } else
            {
                if(i==4)
                {
                    dPCtr.setPonitIndex(0);
                    dPCtr.setFunCodeIndex(0);
                    dPCtr.setDevIndex(dPCtr.getDevIndex()+1);
                    return null;
                }
            }

        }
        pointInfoList = pointInfoListSort(pointInfoList2);
        if(pointInfoList==null)
            return null;
        byte[] data=new byte[7];
        msgPack.devAddress=(byte)(int)deviceInfo.getSlaveAddress();
        if(dPCtr.getPonitIndex()>=pointInfoList.size())
        {
            dPCtr.setDevIndex(dPCtr.getDevIndex()+1);
            dPCtr.setPonitIndex(0);
            return null;
        }
        int index=dPCtr.getPonitIndex();
        DmsProtocolPointModbusEntity firstPointInfo = pointInfoList.get(index);
        msgPack.funCode=(byte) funcode;
        msgPack.startAddress=firstPointInfo.getRegisterAddress();
        int size = pointInfoList.size();
        int registerNum=1;
        for(int i=index;i<size;i++)
        {

            int tmpnum= pointInfoList.get(i).getRegisterAddress() -
                    firstPointInfo.getRegisterAddress() + pointInfoList.get(i).getRegisterLen();
            if(tmpnum>40)
            {
                dPCtr.setPonitIndex(dPCtr.getPonitIndex()+1);
                break;
            }
            dPCtr.setPonitIndex(i);
            dPCtr.setPonitIndex(dPCtr.getPonitIndex()+1);
            registerNum=tmpnum;
        }
        msgPack.registerNum=registerNum;
        return msgPack;
    }
    /**
     * 点表按照寄存器地址升序排序
     *
     * @param dmsProtocolPointModbusEntityList
     */
    public static List<DmsProtocolPointModbusEntity> pointInfoListFilter(List<DmsProtocolPointModbusEntity> dmsProtocolPointModbusEntityList,int funcode) {
        List<DmsProtocolPointModbusEntity> funcodelist=new ArrayList<DmsProtocolPointModbusEntity>();
        for(int i=0;i<dmsProtocolPointModbusEntityList.size();i++)
        {
            DmsProtocolPointModbusEntity info=dmsProtocolPointModbusEntityList.get(i);
            int code=3;
            try{
                code = info.getRegType();
//                code=Integer.parseInt(info.getRemark().trim());
            }catch (Exception ex)
            {
                //ex.printStackTrace();
            }
            if(code==0)
                code=3;
            if(code==funcode)
            {
                funcodelist.add(info);
            }
        }
        return funcodelist;
    }
    /**
     * 点表按照寄存器地址升序排序
     *
     * @param dmsProtocolPointModbusEntityList
     */
    public static List<DmsProtocolPointModbusEntity> pointInfoListSort(List<DmsProtocolPointModbusEntity> dmsProtocolPointModbusEntityList) {
        Collections.sort(dmsProtocolPointModbusEntityList, new Comparator<DmsProtocolPointModbusEntity>() {
            /*
             * int compare(ModbusPointInfo p1, ModbusPointInfo p2) 返回一个基本类型的整型，
             * 返回负数表示：p1 小于p2，
             * 返回0 表示：p1和p2相等，
             * 返回正数表示：p1大于p2
             */
            @Override
            public int compare(DmsProtocolPointModbusEntity p1, DmsProtocolPointModbusEntity p2) {
                //按照ModbusPointInfo的寄存器地址进行升序排列
                return Integer.compare(p1.getRegisterAddress(), p2.getRegisterAddress());
            }

        });
        return dmsProtocolPointModbusEntityList;
    }
    public static String convertModbusValue(byte[] data, int offset, int AlignType, String DataFormat, int registerLength) {
        String result = null;
        int value;
        if (DataFormat.toUpperCase().equals("FLOAT")) {
            if (registerLength == 1) {
                if (AlignType == 1) {
                    value = data[offset + 0] * 256 + data[offset + 1];
                    result = String.format("%d", value);
                } else {
                    value = data[offset + 0] * 256 + data[offset + 1];
                    result = String.format("%d", value);
                }
            } else if (registerLength == 2) {
                if (AlignType == 1) {
                    value = data[offset + 0] * 256 * 256 * 256 + data[offset + 1] * 256 * 256 + data[offset + 2] * 256 + data[offset + 3];
                    result = String.format("%d", value);
                } else {
                    value = data[offset + 0] * 256 * 256 * 256 + data[offset + 1] * 256 * 256 + data[offset + 2] * 256 + data[offset + 3];
                    result = String.format("%d", value);
                }
            } else {
                value = data[offset + 0] * 256 + data[offset + 1];
                result = String.format("%d", value);
            }
        } else {
            if (registerLength == 1) {
                if (AlignType == 1) {
                    value = data[offset + 0] * 256 + data[offset + 1];
                    result = String.format("%d", value);
                } else {
                    value = data[offset + 0] * 256 + data[offset + 1];
                    result = String.format("%d", value);
                }
            } else if (registerLength == 2) {
                if (AlignType == 1) {
                    value = data[offset + 0] * 256 * 256 * 256 + data[offset + 1] * 256 * 256 + data[offset + 2] * 256 + data[offset + 3];
                    result = String.format("%d", value);
                } else {
                    value = data[offset + 0] * 256 * 256 * 256 + data[offset + 1] * 256 * 256 + data[offset + 2] * 256 + data[offset + 3];
                    result = String.format("%d", value);
                }
            } else {
                value = data[offset + 0] * 256 + data[offset + 1];
                result = String.format("%d", value);
            }
        }
        return result;
    }

    public static float convertModbusValue_RetFloat(String name,byte[] data, int offset, int AlignType, String DataFormat, int registerLength) {
        float result = 0;
        int value;
        if (DataFormat.toUpperCase().equals("FLOAT")) {
            if (registerLength == 1) {
                if (AlignType == 1) {
                    result=ModbusByteToFloat(name,data,offset,AlignType,registerLength*2);
                } else {
                    result=ModbusByteToFloat(name,data,offset,AlignType,registerLength*2);
                }
            } else if (registerLength == 2) {
                if (AlignType == 1) {
                    result=ModbusByteToFloat(name,data,offset,AlignType,registerLength*2);
                } else {
                    result=ModbusByteToFloat(name,data,offset,AlignType,registerLength*2);
                }
            } else {
                result=ModbusByteToFloat(name,data,offset,AlignType,registerLength*2);
            }
        } else if (DataFormat.toUpperCase().equals("DOUBLE")) {
            if (registerLength == 1) {
                if (AlignType == 1) {
                    result=ModbusByteToDouble(name,data,offset,AlignType,registerLength*2);
                } else {
                    result=ModbusByteToDouble(name,data,offset,AlignType,registerLength*2);
                }
            } else if (registerLength == 2) {
                if (AlignType == 1) {
                    result=ModbusByteToDouble(name,data,offset,AlignType,registerLength*2);
                } else {
                    result=ModbusByteToDouble(name,data,offset,AlignType,registerLength*2);
                }
            } else if (registerLength == 4) {
                if (AlignType == 1) {
                    result=ModbusByteToDouble(name,data,offset,AlignType,registerLength*2);
                } else {
                    result=ModbusByteToDouble(name,data,offset,AlignType,registerLength*2);
                }
            } else {
                result=ModbusByteToDouble(name,data,offset,AlignType,registerLength*2);
            }
        } else  {
            if (registerLength == 1) {
                if (AlignType == 1) {
                    value = get256_n(data[offset + 0],1)  + get256_n(data[offset + 1],0);
                    result = (float) value;
                } else {
                    value = get256_n(data[offset + 0],1)  + get256_n(data[offset + 1],0);
                    result = (float) value;
                }
            } else if (registerLength == 2) {
                if (AlignType == 1) {
                    value = get256_n(data[offset + 0],3)  + get256_n(data[offset + 1],2)  + get256_n(data[offset + 2],1)  + get256_n(data[offset + 3],0);
                    result = (float) value;
                } else {
                    value = get256_n(data[offset + 0],3)  + get256_n(data[offset + 1],2)  + get256_n(data[offset + 2],1)  + get256_n(data[offset + 3],0);
                    result = (float) value;
                }
            } else if (registerLength == 4) {
                if (AlignType == 1) {
                    value = get256_n(data[offset + 0],7)  + get256_n(data[offset + 1],6)  + get256_n(data[offset + 2],5)  + get256_n(data[offset + 3],4)+
                            get256_n(data[offset + 4],3)  + get256_n(data[offset + 5],2)  + get256_n(data[offset + 6],1)  + get256_n(data[offset + 7],0);
                    result = (float) value;
                } else {
                    value = get256_n(data[offset + 0],7)  + get256_n(data[offset + 1],6)  + get256_n(data[offset + 2],5)  + get256_n(data[offset + 3],4)+
                            get256_n(data[offset + 4],3)  + get256_n(data[offset + 5],2)  + get256_n(data[offset + 6],1)  + get256_n(data[offset + 7],0);
                    result = (float) value;
                }
            } else {
                value = getint(data[offset + 0]) * 256 + getint(data[offset + 1]);
                result = (float) value;
            }
        }
        return result;
    }
    static  int get256_n(byte data,int pow)
    {
        double i=java.lang.Math.pow(256, pow);
        double data1=getint(data)*i;
        int intdata=(int)data1;
        return intdata;
    }
    static  int getint(byte data)
    {
        int i = data;
        i = data&0xff;
        return i;
    }
    static  float ModbusByteToFloat(String Name,byte[] datas,int offset,int AlignType,int num) {
        if(num>4)
            num=4;
        float retdata=0;
        float retdata1=0;
        float retdata2=0;
        float retdata3=0;
        if(num<2)
            return retdata;
        byte[] bytes=new byte[4];
        byte[] bytes1=new byte[4];
        byte[] bytes2=new byte[4];
        byte[] bytes3=new byte[4];


        //if(AlignType==2) //大端2
        //{
        //bytes[num-1-i]=datas[offset+i];//小端
        bytes[0]=datas[offset+1];
        bytes[1]=datas[offset+0];
        bytes[2]=datas[offset+3];
        bytes[3]=datas[offset+2];
        //  }else {
        bytes1[0]=datas[offset+0];
        bytes1[1]=datas[offset+1];
        bytes1[2]=datas[offset+2];
        bytes1[3]=datas[offset+3];
        //}
        bytes2[0]=datas[offset+2];
        bytes2[1]=datas[offset+3];
        bytes2[2]=datas[offset+0];
        bytes2[3]=datas[offset+1];

        bytes3[0]=datas[offset+3];
        bytes3[1]=datas[offset+2];
        bytes3[2]=datas[offset+1];
        bytes3[3]=datas[offset+0];

        //再将bais 封装为DataInputStream类型
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
            DataInputStream dis = new DataInputStream(bais);
            retdata= dis.readFloat();
            dis.close();
        } catch (Exception ex)
        {

        }
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes1);
            DataInputStream dis = new DataInputStream(bais);
            retdata1= dis.readFloat();
            dis.close();
        } catch (Exception ex)
        {

        }
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes2);
            DataInputStream dis = new DataInputStream(bais);
            retdata2= dis.readFloat();
            dis.close();
        } catch (Exception ex)
        {

        }
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes3);
            DataInputStream dis = new DataInputStream(bais);
            retdata3= dis.readFloat();
            dis.close();
        } catch (Exception ex)
        {

        }
        log.info(String.format(Name+"  AlignType=%d:(1)%f (2)%f (3)%f (4)%f",AlignType,retdata,retdata1,retdata2,retdata3));
        if(AlignType==0)
            retdata=retdata2;
        else
            retdata=retdata1;
        return retdata;
    }
    static  float ModbusByteToDouble(String Name,byte[] datas,int offset,int AlignType,int num) {
        if(num>8)
            num=8;
        double retdata=0;
        double retdata1=0;
        double retdata2=0;
        double retdata3=0;
        float floatdata=0;
        if(num<4)
            return floatdata;
        byte[] bytes=new byte[8];
        byte[] bytes1=new byte[8];
        byte[] bytes2=new byte[8];
        byte[] bytes3=new byte[8];

        //if(AlignType==2) //大端2
        //{
        //bytes[num-1-i]=datas[offset+i];//小端
        bytes[0]=datas[offset+1];
        bytes[1]=datas[offset+0];
        bytes[2]=datas[offset+3];
        bytes[3]=datas[offset+2];
        bytes[4]=datas[offset+5];
        bytes[5]=datas[offset+4];
        bytes[6]=datas[offset+7];
        bytes[7]=datas[offset+6];
        //  }else {
        bytes1[0]=datas[offset+0];
        bytes1[1]=datas[offset+1];
        bytes1[2]=datas[offset+2];
        bytes1[3]=datas[offset+3];
        bytes1[4]=datas[offset+4];
        bytes1[5]=datas[offset+5];
        bytes1[6]=datas[offset+6];
        bytes1[7]=datas[offset+7];
        //}
        bytes2[4]=datas[offset+4];
        bytes2[5]=datas[offset+5];
        bytes2[6]=datas[offset+6];
        bytes2[7]=datas[offset+7];
        bytes2[0]=datas[offset+2];
        bytes2[1]=datas[offset+3];
        bytes2[2]=datas[offset+0];
        bytes2[3]=datas[offset+1];


        bytes3[0]=datas[offset+7];
        bytes3[1]=datas[offset+6];
        bytes3[2]=datas[offset+5];
        bytes3[3]=datas[offset+4];
        bytes3[4]=datas[offset+3];
        bytes3[5]=datas[offset+2];
        bytes3[6]=datas[offset+1];
        bytes3[7]=datas[offset+0];

        //再将bais 封装为DataInputStream类型
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
            DataInputStream dis = new DataInputStream(bais);
            retdata= dis.readDouble();
            dis.close();
        } catch (Exception ex)
        {

        }
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes1);
            DataInputStream dis = new DataInputStream(bais);
            retdata1= dis.readDouble();
            dis.close();
        } catch (Exception ex)
        {

        }
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes2);
            DataInputStream dis = new DataInputStream(bais);
            retdata2= dis.readDouble();
            dis.close();
        } catch (Exception ex)
        {

        }
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes3);
            DataInputStream dis = new DataInputStream(bais);
            retdata3= dis.readDouble();
            dis.close();
        } catch (Exception ex)
        {

        }
        log.info(String.format(Name+"  AlignType=%d:(1)%f (2)%f (3)%f (4)%f",AlignType,retdata,retdata1,retdata2,retdata3));
        if(AlignType==0)
            retdata=retdata1;
        else
            retdata=retdata2;
        floatdata=(float)retdata;
        return floatdata;
    }
    public static ReadUpInfo getUpProtocolDTO(MsgPack msgPack, byte[] revdata, int len, List<DmsProtocolPointModbusEntity>  pointDTO, String secretKey) {
        if(pointDTO==null)
        {
            return null;
        }
        if(len<5)
        {
            return null;
        }
        ReadUpInfo readUpInfo=new ReadUpInfo();
        Map<String, Object> data = new HashMap();
        if(revdata[0]==msgPack.devAddress&&
                revdata[1]==msgPack.funCode)
        {
            readUpInfo.setFunctionCode(String.format("%x",msgPack.funCode));
            readUpInfo.setAddress(msgPack.devAddress);
            List<DmsProtocolPointModbusEntity> pointInfoList = pointInfoListSort(pointDTO);
            if( msgPack.funCode==0x03||msgPack.funCode==0x04){
                int sum=revdata[2]&0xff; //无符号赋值有符号
                readUpInfo.setByteNum(sum);
                int regsum=sum/2;
                int endAddr=msgPack.startAddress+regsum-1;
                int bufoffset=3;
                int regindex=0;
                for(int i=0;i<pointInfoList.size();i++)
                {
                    DmsProtocolPointModbusEntity info=pointInfoList.get(i);
                    int funcode=0x3;
                    funcode = info.getRegType();
                    if(funcode!=msgPack.funCode)
                        continue;
                    int regaddr=info.getRegisterAddress();
                    if(regaddr>=msgPack.startAddress&&
                            regaddr<=endAddr)
                    {
                        bufoffset=(regaddr-msgPack.startAddress)*2+3;
                        int AlignType=info.getAlignType();
                        String DataFormat=info.getDataFormat();
                        int registerLength=info.getRegisterLen();
                        float value=convertModbusValue_RetFloat(info.getDmsPointName(),revdata,bufoffset,AlignType,DataFormat,registerLength);
                        data.put(info.getDmsPointName(),value);
                    }
                }
            }else if(msgPack.funCode==0x01||msgPack.funCode==0x02){
                //bit位处理
                int sum=revdata[2]&0xff; //无符号赋值有符号
                readUpInfo.setByteNum(sum);
                int regsum=sum*8;
                int endAddr=msgPack.startAddress+regsum-1;
                int bufoffset=3;
                int regindex=0;
                for(int i=0;i<pointInfoList.size();i++)
                {
                    DmsProtocolPointModbusEntity info=pointInfoList.get(i);
                    int funcode=0x3;
                    funcode=info.getRegType();
                    if(funcode!=msgPack.funCode)
                        continue;
                    int regaddr=info.getRegisterAddress();
                    int byteoffset=0;
                    if(regaddr>=msgPack.startAddress&&
                            regaddr<=endAddr)
                    {
                        bufoffset=regaddr/8+3;
                        byteoffset=(regaddr);

                        int AlignType=info.getAlignType();
                        String DataFormat=info.getDataFormat();
                        int registerLength=info.getRegisterLen();
                        float value=0;
                        if(((revdata[bufoffset]>>byteoffset)&0x01)>0)
                        {
                            value=1;
                        }else
                        {
                            value=0;
                        }
                        data.put(info.getDmsPointName(),value);

                    }
                }
            } else {
                int sum=revdata[2]&0xff; //无符号赋值有符号
                readUpInfo.setByteNum(sum);
            }
        }
        readUpInfo.setData(data);
        return readUpInfo;
    }


    public static List<ModbusCMDGroupPackages> generateDownProtocol(DmsDeviceEntity deviceInfo,List<DmsProtocolPointModbusEntity> pointDTO) {
        // List<DmsProtocolPointModbusEntity> 升序排列

        List<ModbusCMDGroupPackages> modbusCMDGroupPackagesList = new ArrayList<>();
//        pointDTO.forEach(dmsProtocolPointModbusEntity -> {
//            if(StringUtils.isEmpty(dmsProtocolPointModbusEntity.getRemark())){
//                dmsProtocolPointModbusEntity.setRemark("3");
//            }});

        // 按照功能码分组
        Map<Integer,List<DmsProtocolPointModbusEntity>> dmsProtocolPointModbusEntityMap =
                pointDTO.stream().sorted(Comparator.comparing(DmsProtocolPointModbusEntity::getRegisterAddress))
                .collect(Collectors.groupingBy(DmsProtocolPointModbusEntity::getRegType));


        // 每40地址区间生成一个命令
        dmsProtocolPointModbusEntityMap.forEach((k,v)->{
            MsgPack msgPack=new MsgPack();
            msgPack.devAddress=(byte)(int)deviceInfo.getSlaveAddress();
            msgPack.funCode=(byte)(int)Integer.valueOf(k);
            ;
//            int packgeSize = (int)Math.ceil((double)(v.get(v.size()).getRegisterAddress()-v.get(0).getRegisterAddress()+v.get(v.size()).getRegisterLen())/40);
            int packgeSize = (int)Math.ceil((double)(v.size()/40));
            List<List<DmsProtocolPointModbusEntity>> groupPackages = new ArrayList<>();
            for (int i = 0; i < (packgeSize+1); i++) {
                if(i==packgeSize){
                    groupPackages.add(v.subList(i * 40, v.size()));
                }else{
                    groupPackages.add(v.subList(i * 40,(i+1)*40));
                }

            }
            groupPackages.forEach(dmsProtocolPointModbusEntities -> {
                msgPack.startAddress=dmsProtocolPointModbusEntities.get(0).getRegisterAddress();
                msgPack.registerNum=dmsProtocolPointModbusEntities.get(dmsProtocolPointModbusEntities.size()-1).getRegisterAddress()-msgPack.startAddress+dmsProtocolPointModbusEntities.get(dmsProtocolPointModbusEntities.size()-1).getRegisterLen();
                ModbusCMDGroupPackages modbusCMDGroupPackages = new ModbusCMDGroupPackages();
                modbusCMDGroupPackages.setMsgPack(msgPack);
                modbusCMDGroupPackages.setDmsProtocolPointModbusEntityList(dmsProtocolPointModbusEntities);
                modbusCMDGroupPackagesList.add(modbusCMDGroupPackages);
            });

        });

        return modbusCMDGroupPackagesList;
    }
    public static ReadUpInfo analysisUpProtocol(byte[] revdata, int len, ModbusCMDGroupPackages modbusCMDGroupPackages, String secretKey) {
        if(len<5)
        {
            return null;
        }
        MsgPack msgPack = modbusCMDGroupPackages.getMsgPack();
        List<DmsProtocolPointModbusEntity> pointInfoList = modbusCMDGroupPackages.getDmsProtocolPointModbusEntityList();

        ReadUpInfo readUpInfo=new ReadUpInfo();
        Map<String, Object> data = new HashMap();
        if(revdata[0]==msgPack.devAddress&&
                revdata[1]==msgPack.funCode)
        {
            readUpInfo.setFunctionCode(String.format("%x",msgPack.funCode));
            readUpInfo.setAddress(msgPack.devAddress);
            if( msgPack.funCode==0x03||msgPack.funCode==0x04){
                int sum=revdata[2]&0xff; //无符号赋值有符号
                readUpInfo.setByteNum(sum);
                int regsum=sum/2;
                int endAddr=msgPack.startAddress+regsum-1;
                int bufoffset=3;
                int regindex=0;
                for(int i=0;i<pointInfoList.size();i++)
                {
                    DmsProtocolPointModbusEntity info=pointInfoList.get(i);
                    int funcode=0x3;
                    try {
                        funcode=info.getRegType();
                    }catch (Exception ex)
                    {

                    }
                    if(funcode!=msgPack.funCode)
                        continue;
                    int regaddr=info.getRegisterAddress();
                    if(regaddr>=msgPack.startAddress&&
                            regaddr<=endAddr)
                    {
                        bufoffset=(regaddr-msgPack.startAddress)*2+3;
                        int AlignType=info.getAlignType();
                        String DataFormat=info.getDataFormat();
                        int registerLength=info.getRegisterLen();
                        float value=convertModbusValue_RetFloat(info.getDmsPointName(),revdata,bufoffset,AlignType,DataFormat,registerLength);
                        data.put(info.getDmsPointName(),value);
                    }
                }
            }else if(msgPack.funCode==0x01||msgPack.funCode==0x02){
                //bit位处理
                int sum=revdata[2]&0xff; //无符号赋值有符号
                readUpInfo.setByteNum(sum);
                int regsum=sum*8;
                int endAddr=msgPack.startAddress+regsum-1;
                int bufoffset=3;
                int regindex=0;
                for(int i=0;i<pointInfoList.size();i++)
                {
                    DmsProtocolPointModbusEntity info=pointInfoList.get(i);
                    int funcode=0x3;
                    try {
                        funcode=info.getRegType();
                    }catch (Exception ex)
                    {

                    }
                    if(funcode!=msgPack.funCode)
                        continue;
                    int regaddr=info.getRegisterAddress();
                    int byteoffset=0;
                    if(regaddr>=msgPack.startAddress&&
                            regaddr<=endAddr)
                    {
                        bufoffset=regaddr/8+3;
                        byteoffset=(regaddr);

                        int AlignType=info.getAlignType();
                        String DataFormat=info.getDataFormat();
                        int registerLength=info.getRegisterLen();
                        float value=0;
                        if(((revdata[bufoffset]>>byteoffset)&0x01)>0)
                        {
                            value=1;
                        }else
                        {
                            value=0;
                        }
                        data.put(info.getDmsPointName(),value);

                    }
                }
            } else {
                int sum=revdata[2]&0xff; //无符号赋值有符号
                readUpInfo.setByteNum(sum);
            }
        }
        readUpInfo.setData(data);
        return readUpInfo;
    }

}
