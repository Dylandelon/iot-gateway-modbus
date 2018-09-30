package cn.enncloud.iot.iotgatewaymodbus.http.configration;


import cn.enncloud.iot.iotgatewaymodbus.http.service.dtos.*;
import cn.enncloud.iot.iotgatewaymodbus.http.tools.Tool;

import java.util.*;

/**
 * Created by Thinkpad on 2018/9/27.
 */
public class ModbusProto {
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
    public static MsgPack getDownProtocolCmdDTO(DmsDeviceEntity deviceInfo, List<DmsProtocolPointModbusEntity> pointDTO, CmdMsg cmdMsg) {

        //将点表信息
        MsgPack msgPack=new MsgPack();

        byte[] data=new byte[7];
        msgPack.devAddress=(byte)(int)deviceInfo.getSlaveAddress();
        int size = pointDTO.size();
        for(int i=0;i<size;i++)
        {
            DmsProtocolPointModbusEntity info=pointDTO.get(i);
            if(pointDTO.get(i).getDmsPointName().equals(cmdMsg.getPonintName()))
            {
                msgPack.startAddress=info.getRegisterAddress();
                msgPack.registerNum=info.getRegisterLen();
                //msgPack.value=cmdMsg.value
                msgPack.funCode=Byte.parseByte(info.getRemark().trim());
            }
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
        for(int i=dPCtr.getFunCodeIndex();i<5;i++)
        {
            if(pointDTO.size()>0)
            {
                funcode=i;
                dPCtr.setFunCodeIndex(i);
                dPCtr.setPonitIndex(0);
                dPCtr.setFunCodeIndex(dPCtr.getFunCodeIndex()+1);
                pointInfoList = pointInfoListSort(pointDTO);
                break;
            }
            if(i>=4)
            {
                dPCtr.setFunCodeIndex(0);
                dPCtr.setDevIndex(dPCtr.getDevIndex()+1);
                dPCtr.setPonitIndex(0);
            }
        }
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
                dPCtr.setPonitIndex(i);
                break;
            }
            registerNum=tmpnum;
            if(i==size-1){
                dPCtr.setDevIndex(dPCtr.getDevIndex()+1);
                dPCtr.setPonitIndex(0);
                break;
            }
        }
        msgPack.registerNum=registerNum;
        return msgPack;
    }
    /**
     * 点表按照寄存器地址升序排序
     *
     * @param pointInfoList
     */
//    public static List<ModbusPointInfo> pointInfoListFilter(List<ModbusPointInfo> pointInfoList,int funcode) {
//        List<ModbusPointInfo> funcodelist=new ArrayList<ModbusPointInfo>();
//        for(int i=0;i<pointInfoList.size();i++)
//        {
//            ModbusPointInfo info=pointInfoList.get(i);
//            int code=3;
//            try{
//                code=Integer.parseInt(info.getRemark().trim());
//            }catch (Exception ex)
//            {
//                //ex.printStackTrace();
//            }
//            if(code==0)
//                code=3;
//            if(code==funcode)
//            {
//                funcodelist.add(info);
//            }
//        }
//        return funcodelist;
//    }
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
    public static String convertModbusValue(byte[] data,int offset,int AlignType,String DataFormat,int registerLength)
    {
        String result=null;
        int value;
        if(DataFormat.toUpperCase().equals("FLOAT"))
        {
            if(registerLength==1)
            {
                if(AlignType==1)
                {
                    value=data[offset+0]*256+data[offset+1];
                    result=String.format("%d",value);
                }else
                {
                    value=data[offset+0]*256+data[offset+1];
                    result=String.format("%d",value);
                }
            }else if(registerLength==2)
            {
                if(AlignType==1)
                {
                    value=data[offset+0]*256*256*256+data[offset+1]*256*256+data[offset+2]*256+data[offset+3];
                    result=String.format("%d",value);
                }else {
                    value=data[offset+0]*256*256*256+data[offset+1]*256*256+data[offset+2]*256+data[offset+3];
                    result=String.format("%d",value);
                }
            }else
            {
                value=data[offset+0]*256+data[offset+1];
                result=String.format("%d",value);
            }
        }else
        {
            if(registerLength==1)
            {
                if(AlignType==1)
                {
                    value=data[offset+0]*256+data[offset+1];
                    result=String.format("%d",value);
                }else
                {
                    value=data[offset+0]*256+data[offset+1];
                    result=String.format("%d",value);
                }
            }else if(registerLength==2)
            {
                if(AlignType==1)
                {
                    value=data[offset+0]*256*256*256+data[offset+1]*256*256+data[offset+2]*256+data[offset+3];
                    result=String.format("%d",value);
                }else {
                    value=data[offset+0]*256*256*256+data[offset+1]*256*256+data[offset+2]*256+data[offset+3];
                    result=String.format("%d",value);
                }
            }else
            {
                value=data[offset+0]*256+data[offset+1];
                result=String.format("%d",value);
            }
        }
        return result;
    }
    public static float convertModbusValue_RetFloat(byte[] data,int offset,int AlignType,String DataFormat,int registerLength)
    {
        float result=0;
        int value;
        if(DataFormat.toUpperCase().equals("FLOAT"))
        {
            if(registerLength==1)
            {
                if(AlignType==1)
                {
                    value=data[offset+0]*256+data[offset+1];
                    result=(float)value;
                }else
                {
                    value=data[offset+0]*256+data[offset+1];
                    result=(float)value;
                }
            }else if(registerLength==2)
            {
                if(AlignType==1)
                {
                    value=data[offset+0]*256*256*256+data[offset+1]*256*256+data[offset+2]*256+data[offset+3];
                    result=(float)value;
                }else {
                    value=data[offset+0]*256*256*256+data[offset+1]*256*256+data[offset+2]*256+data[offset+3];
                    result=(float)value;
                }
            }else
            {
                value=data[offset+0]*256+data[offset+1];
                result=(float)value;
            }
        }else
        {
            if(registerLength==1)
            {
                if(AlignType==1)
                {
                    value=data[offset+0]*256+data[offset+1];
                    result=(float)value;
                }else
                {
                    value=data[offset+0]*256+data[offset+1];
                    result=(float)value;
                }
            }else if(registerLength==2)
            {
                if(AlignType==1)
                {
                    value=data[offset+0]*256*256*256+data[offset+1]*256*256+data[offset+2]*256+data[offset+3];
                    result=(float)value;
                }else {
                    value=data[offset+0]*256*256*256+data[offset+1]*256*256+data[offset+2]*256+data[offset+3];
                    result=(float)value;
                }
            }else
            {
                value=data[offset+0]*256+data[offset+1];
                result=(float)value;
            }
        }
        return result;
    }
    public static ReadUpInfo getUpProtocolDTO(MsgPack msgPack, byte[] revdata, int len, List<DmsProtocolPointModbusEntity>  pointDTO, String secretKey) {
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
            if(msgPack.funCode==0x01||msgPack.funCode==0x02
                    ||msgPack.funCode==0x03||msgPack.funCode==0x04){
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
                        funcode=Integer.parseInt(info.getRemark());
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
                        float value=convertModbusValue_RetFloat(revdata,bufoffset,AlignType,DataFormat,registerLength);
                       // if(value!=null)
                        //{
                            data.put(info.getDmsPointName(),value);
                       // }
                    }
                }
            }else {
                int sum=revdata[2]&0xff; //无符号赋值有符号
                readUpInfo.setByteNum(sum);
            }
        }
        readUpInfo.setData(data);
        return readUpInfo;
    }

}
