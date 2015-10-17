package com.newppt.android.logical;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

  
public class NetWorkInfo{  
     
	String subnet;  
    String ip;
    Map<String, String> map = new HashMap<String, String>();
	String ipAddress;
	String subNetMask;
	boolean flag = false; 				//用来标记一对IP地址跟子网掩码 （位置相邻的即为一对）
	
    public NetWorkInfo(String ip) {
    	this.ip=ip;
    }
    
	 /**
	  * 得到主机上所有的IP地址跟子网掩码
	  * @return
	  */
	 public Map<String, String> getIPAddressAndMask() {  
		 String os = System.getProperty("os.name");  
		 if (os != null && os.startsWith("Windows")) {  
			 try {    
				 String command = "cmd.exe /c ipconfig /all"; 
				 Process p = Runtime.getRuntime().exec(command);   
				 BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream(), "gb2312"));   
				 String line;     
				 while ((line = br.readLine()) != null) {   
					 /*
					  * 获得机器IP地址 
					  * 在以“IPv4”开头的那一行获取ip地址，英文版是以“IP Address”开头
					  * 格式为：
					  * IPv4 地址 . . . . . . . . . . . . : 10.10.116.132(首选)
					  */
					 if (line.indexOf("IPv4 地址") > 0 || line.indexOf("IP Address") > 0) {  
						 int index = line.indexOf(":");  
						 index += 2;    
						 ipAddress = line.substring(index);
						 
						 //去掉汉字    “10.10.116.132(首选)”
						 int i;
						 char [] array = ipAddress.toCharArray();
						 for(i=0; i<array.length; i++){
							 if(array[i]!='.' && (array[i]<'0'||array[i]>'9'))
								 break;
						 }
						 ipAddress = ipAddress.substring(0, i);
						   
						 flag = true; 			 //存在IP地址，必存在其相应的子网掩码
					 }     
					      
					 /*	
					  * 获得机器子网掩码 
					  * 在以“子网掩码”开头的那一行获取子网掩码，英文版是以“Subnet Mask”开头
					  * 格式为：
					  * 子网掩码  . . . . . . . . . . . . : 255.255.255.192
					  */
					 if (flag==true && (line.indexOf("子网掩码 ") > 0 || line.indexOf("Subnet Mask") > 0)) {    
						 int index = line.indexOf(":");      
						 index += 2;      
						 subNetMask = line.substring(index);     
						 flag = false;  //消耗掉
						 
						 map.put(ipAddress, subNetMask);
					 }      
				 }   
				 br.close();   
			 } catch (IOException e) {  
				e.printStackTrace();	 
			 }   
		 }
		return map;    
	 }     
    
	 /**
	   * 获取相对应IP地址的子网掩码
	   */
	 public void setSubNetMask(){
		 subnet = getIPAddressAndMask().get(ip);
		 System.out.println(ip+"----");
		 System.out.println(subnet+"----");
	 }  
		 
    
     /**
      * 根据本机IP和子网掩码，计算子网广播地址 
     * @return String
     */
    public String getBroadcastAddress() {  
    	 setSubNetMask();
         String[] ips = ip.split("\\.");   
         String[] subnets = subnet.split("\\.");  
         StringBuffer sb = new StringBuffer();  
         for(int i = 0; i < ips.length; i++) {  
        	 /***
        	  * 用IP地址和子网掩码计算网络地址 ips[i] = String.valueOf(Integer.parseInt(subnets[i]) & Integer.parseInt(ips[i])); 
        	  */
        	 /****
        	  * 用IP地址和子网掩码计算广播地址  ips[i] = String.valueOf((~Integer.parseInt(subnets[i]))|(Integer.parseInt(ips[i])));
        	  */
        	
        	 ips[i] = String.valueOf((~Integer.parseInt(subnets[i]))|(Integer.parseInt(ips[i]))); 
             sb.append(turnToStr(Integer.parseInt(ips[i])));  
             if(i != (ips.length-1))  
                 sb.append(".");  
         }      
//         System.out.println(turnToIp(sb.toString()));
         return turnToIp(sb.toString());  
    }  
  
     
     /** 
      * 把带符号整形转换为二进制 
      * @param num 
      * @return 
      */  
    private String turnToStr(int num) {  
        String str = "";  
        str = Integer.toBinaryString(num);            
        int len = 8 - str.length();  
        // 如果二进制数据少于8位,在前面补零.  
        for (int i = 0; i < len; i++) {  
            str = "0" + str;  
        }  
        //如果num为负数，转为二进制的结果有32位，如1111 1111 1111 1111 1111 1111 1101 1110  
        //则只取最后的8位.  
        if (len < 0)  
            str = str.substring(24, 32);  
        return str;  
    }      
      
    /** 
     * 把二进制形式的ip，转换为十进制形式的ip 
     * @param str 
     * @return 
     */  
    private String turnToIp(String str){  
        String[] ips = str.split("\\.");  
        StringBuffer sb = new StringBuffer();  
        for (int i = 0; i < ips.length; i++) {  
            sb.append(turnToInt(ips[i]));  
            sb.append(".");  
        }            
        sb.deleteCharAt(sb.length() - 1);  
        return sb.toString();  
    }  
  
    /** 
     * 把二进制转换为十进制 
     * @param str 
     * @return 
     */  
    private int turnToInt(String str){  
        int total = 0;  
        int top = str.length();  
        for (int i = 0; i < str.length(); i++) {  
            String h = String.valueOf(str.charAt(i));  
            top--;  
            total += ((int) Math.pow(2, top)) * (Integer.parseInt(h));  
        }  
        return total;  
    }
    
    
    
} 