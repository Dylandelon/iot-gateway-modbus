package cn.enncloud.iot.iotgatewaymodbus.http.configration;

import lombok.Data;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Thinkpad on 2018/9/27.
 */
@Data
public class CmdMsg {
    long deviceId;
    String ponintName;
    String value;
    double ret;
    String ReponseMsg;
    ReentrantLock rtLock;
    public CmdMsg(long deviceid, String ponintname,String value1)
    {
        deviceId=deviceid;
        ponintName=ponintname;
        value=value1;
        rtLock=new ReentrantLock();
        rtLock.tryLock();
    }
    public float waitCmdReponse(int sec)
    {
        boolean flag=false;
        try{
            //处理任务
            flag=rtLock.tryLock(sec, TimeUnit.SECONDS);
        }catch(Exception ex){
             return 1;
        }finally{
            rtLock.unlock();   //释放锁
        }
        return 0;
    }
}
