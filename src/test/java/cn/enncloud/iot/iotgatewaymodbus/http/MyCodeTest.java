package cn.enncloud.iot.iotgatewaymodbus.http;

import cn.enncloud.iot.iotgatewaymodbus.http.configration.ModbusProto;
import cn.enncloud.iot.iotgatewaymodbus.http.configration.MsgPack;
import cn.enncloud.iot.iotgatewaymodbus.http.tools.CRC16;
import cn.enncloud.iot.iotgatewaymodbus.http.tools.Tool;
import cn.enncloud.iot.iotgatewaymodbus.netty.TCPServerNetty;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
    @Test
    public void codet5(){
        List<String> list = new ArrayList<>();
        list.add("2");
        list.add("8");
        list.add("23");
        list.add("32");
        list.add("24");
        list.add("33");
        list.add("34");
        list.add("342451");
        list.add("342452");

        List<String> list1 = list.stream().sorted().collect(Collectors.toList());

        list1.forEach(s -> System.out.println("lala:"+s));

    }
    @Test
    public void codet6(){
        List<Integer> list = new ArrayList<>();
        list.add(2);
        list.add(8);
        list.add(23);
        list.add(32);
        list.add(24);
        list.add(33);
        list.add(34);
        list.add(342451);
        list.add(342452);

        List<Integer> list1 = list.stream().sorted().collect(Collectors.toList());

        list1.forEach(s -> System.out.println("lala:"+s));

    }
    @Test
    public void codet7(){
        int hex = 0x03;
        int dig = 3;
        int hex2 = 0x0A;
        int dig2 = 10;
        System.out.println("hex"+(byte)hex);
        System.out.println("dig"+(byte)dig);
        System.out.println("hex2"+(byte)hex2);
        System.out.println("dig2"+(byte)dig2);

    }
    @Test
    public void codet8(){
        int number = 10;
        //原始数二进制
        printInfo(number);
        //左移一位 2的次幂
        number = number << 8;
        printInfo(number);
        //右移一位
        number = number >> 1;
        printInfo(number);
        char cc = '2';
        char[] chars = {'2'};
        byte[] ss = getBytes(chars);
        printInfo(cc);
        System.out.println("ss"+ss);

    }
//    @Test
//    public void codet9(){
//        MsgPack msgPack = new MsgPack();
//        msgPack.devAddress=2;
//        msgPack.funCode=3;
//        msgPack.startAddress=80;
//        msgPack.registerNum=80;
//        ModbusProto.getBytesBuf(msgPack);
//
//
//    }
    // char转byte

    private byte[] getBytes (char[] chars) {
        Charset cs = Charset.forName ("UTF-8");
        CharBuffer cb = CharBuffer.allocate (chars.length);
        cb.put (chars);
        cb.flip ();
        ByteBuffer bb = cs.encode (cb);

        return bb.array();

    }

// byte转char

    private char[] getChars (byte[] bytes) {
        Charset cs = Charset.forName ("UTF-8");
        ByteBuffer bb = ByteBuffer.allocate (bytes.length);
        bb.put (bytes);
        bb.flip ();
        CharBuffer cb = cs.decode (bb);

        return cb.array();
    }
    /**
     * 输出一个int的二进制数
     * @param num
     */
    private static void printInfo(int num){
        System.out.println("开始");
        System.out.println(num);
        System.out.println((Integer.toBinaryString((byte)num)));
        System.out.println(Integer.toBinaryString(num));
        System.out.println("结束");
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
    @Test
    public void codet10(){
        StringBuilder sb = new StringBuilder();
        sb.append("01060000");
        sb.append(String.format("%04x", Integer.valueOf("1")));
        byte[] bytesWriteMid = TCPServerNetty.hexToByteArray(sb.toString());
        byte[] bytesWrite = CRC16.addCRC(bytesWriteMid);
        System.out.println("1向设备下发的信息未加密为："+TCPServerNetty.bytesToHexString(bytesWrite));


        byte[] datbytes=null;
        datbytes=new byte[6];
        datbytes[0]=(byte)1;
        datbytes[1]=06;
        datbytes[2]=(byte)(00);
        datbytes[3]=(byte)00;
        datbytes[4]=(byte)(00);
        datbytes[5]=(byte)1;
        byte[] bytesWrite2 = CRC16.addCRC(datbytes);
        System.out.println("2向设备下发的信息未加密为："+TCPServerNetty.bytesToHexString(bytesWrite2));

        MsgPack msgPack = new MsgPack();
        msgPack.setFunCode((byte)6);
        msgPack.setDevAddress(1);
        msgPack.setStartAddress(0);
        msgPack.setValue(1);
        ;
        byte[] bytesWrite3 = CRC16.addCRC(ModbusProto.getCmdBytes(msgPack));
        System.out.println("3向设备下发的信息未加密为："+TCPServerNetty.bytesToHexString(bytesWrite3));



    }
    @Test
    public void codet11(){
        StringBuilder sb = new StringBuilder();
        sb.append("01060000");
        sb.append(String.format("%04x", Integer.valueOf("1")));
        byte[] bytesWriteMid = TCPServerNetty.hexToByteArray(sb.toString());
        byte[] bytesWrite = CRC16.addCRC(bytesWriteMid);
        System.out.println("1向设备下发的信息未加密为："+TCPServerNetty.bytesToHexString(bytesWrite));


        byte[] datbytes=null;
        datbytes=new byte[6];
        datbytes[0]=(byte)1;
        datbytes[1]=06;
        datbytes[2]=(byte)(00);
        datbytes[3]=(byte)00;
        datbytes[4]=(byte)(00);
        datbytes[5]=(byte)1;
        byte[] bytesWrite2 = CRC16.addCRC(datbytes);
        System.out.println("2向设备下发的信息未加密为："+TCPServerNetty.bytesToHexString(bytesWrite2));

        MsgPack msgPack = new MsgPack();
        msgPack.setFunCode((byte)6);
        msgPack.setDevAddress(1);
        msgPack.setStartAddress(0);
        msgPack.setValue(1);
        ;
        byte[] bytesWrite3 = CRC16.addCRC(ModbusProto.getCmdBytes(msgPack));
        System.out.println("3向设备下发的信息未加密为："+TCPServerNetty.bytesToHexString(bytesWrite3));

        System.out.println("比较是否相等："+ Arrays.equals(bytesWrite2,bytesWrite3));




    }
}
