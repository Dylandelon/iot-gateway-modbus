package cn.enncloud.iot.iotgatewaymodbus.http;

import cn.enncloud.iot.iotgatewaymodbus.netty.TCPServerNetty;
import cn.enncloud.iot.iotgatewaymodbus.http.tools.Tool;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MyCodeTest {
    @Test
    public void codeTran(){
        System.out.println(asciiToString("74 73 65 74 31 37 36 31 30 31 33 35 33 38 33 00 0A 57 14 B3 00"));

    }
//    747365743137363130313335333833000A5714B300
    @Test
    public void codet2(){
        System.out.println((char) Integer.parseInt("74"));

    }
    @Test
    public void codet3(){

        byte[] bytes = "rr17610135383".getBytes();
        System.out.println(bytes);
        System.out.println(TCPServerNetty.bytesToHexString(bytes));

    }
    @Test
    public void codet4(){

        System.out.println(Tool.getMobileNO("rr17610135383"));

    }
public static String asciiToString(String value)
{
    StringBuffer sbu = new StringBuffer();
    String[] chars = value.split(" ");
    for (int i = 0; i < chars.length; i++) {
        sbu.append((char) Integer.parseInt(chars[i]));
        System.out.println(sbu.toString());
    }
    return sbu.toString();
}
}
