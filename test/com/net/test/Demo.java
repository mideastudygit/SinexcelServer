package com.net.test;

public class Demo {
     //将127.0.0.1形式的IP地址转换成十六进制字符串
     public static String ipToString(String strIp){
         //先找到IP地址字符串中.的位置
         int position1 = strIp.indexOf(".");
         int position2 = strIp.indexOf(".", position1 + 1);
         int position3 = strIp.indexOf(".", position2 + 1);
         //将每个.之间的字符串转换成16进制（高位为空时补0）
         int a,b,c,d = 0;
         a = Integer.parseInt(strIp.substring(0, position1));
         String a1 = Integer.toHexString(a);
         a1 = (a<16) ? ("0" + a1) : a1;
         b = Integer.parseInt(strIp.substring(position1+1, position2));
         String b1 = Integer.toHexString(b);
         b1 = (b<16) ? ("0" + b1) : b1;
         c = Integer.parseInt(strIp.substring(position2+1, position3));
         String c1 = Integer.toHexString(c);
         c1 = (c<16) ? ("0" + c1) : c1;
         d = Integer.parseInt(strIp.substring(position3+1));
         String d1 = Integer.toHexString(d);
         d1 = (d<16) ? ("0" + d1) : d1;
         System.out.println(a1+b1+c1+d1);
         return (a1+b1+c1+d1);
     }
    
     //将十进制字符串形式转换成127.0.0.1形式的ip地址
     public static String stringToIP(String str){
         StringBuffer sb = new StringBuffer("");
         //直接右移24位
         sb.append(Integer.parseInt(str.substring(0,2), 16));
         sb.append(".");
         //将高8位置0，然后右移16位
         sb.append(Integer.parseInt(str.substring(2,4), 16));
         sb.append(".");
         //将高16位置0，然后右移8位
         sb.append(Integer.parseInt(str.substring(4,6), 16));
         sb.append(".");
         //将高24位置0
         sb.append(Integer.parseInt(str.substring(6), 16));
         return sb.toString();
     }
    
     public static void main(String[] args){
         String ipStr = "1.168.0.1";
         String str = Demo.ipToString(ipStr);
         System.out.println("16进制字符串形式为：" + str);
         String ip = Demo.stringToIP(str);
         System.out.println("10进制字符串形式为：" + ip);
//         System.out.println("整数" + longIp + "转化成字符串IP地址："
//                 + Demo.longToIP(longIp));
//         //ip地址转化成二进制形式输出
//         System.out.println("二进制形式为：" + Long.toBinaryString(longIp));
     }
 
}