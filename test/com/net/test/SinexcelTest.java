package com.net.test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import junit.framework.TestCase;

import com.base.http.HttpManager;
import com.base.utils.ParaMap;

/*
 * 盛宏测试类
 * 发送请求
 */
public class SinexcelTest extends TestCase{
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
	
	public static void main(String[] args) throws Exception {
		SinexcelTest test = new SinexcelTest();
//		test.charge();
//		test.stopCharge();
//		test.appoint();
		test.insert();
//		test.record();
	}
	
	/**
	 *插枪
	 */
	public void insert() throws Exception{
		ParaMap inMap = new ParaMap();
		String uri = "http://192.168.0.108:8080/system";
		uri +="/direct-module=net_service=Sinexcel_method=pileStatus";
		uri += "_gsn=" + "0105510007020010"; // 网关编号
		uri += "_csn=" + "0105510007020010"; // 电桩序列号
		uri += "_wst=" + 1; // 工作状态()
		uri += "_cst=" + 2; // 车连接状态

		uri += "_et=" + 0; // 充电费用
		uri += "_dn=" + 0; // 充电时长(秒)
		uri += "_pr=" + 0; // 此次充电电量（0.01kwh）
		uri += "_smp=" + 0; // 充电前电表读数 0.01kw

		uri += "_cmp=" + 0; // 当前电表读数  0.01kw
		uri += "_sme=" + 1; // 充电启动方式(0:刷卡；1：后台；2：密码)
		uri += "_cme=" + 0; // 充电策略
		uri += "_chmp=" + 0; // 充电策略参数

		uri += "_afg=" + 0; // 预约标志
		uri += "_cno=" + "185884025360"; // 充电/预约卡号
		uri += "_aoe=" + 30; // 预约超时时间
		uri += "_ste=" + 0; // 预约/开始充电开始时间

		uri += "_smy=" + 123; // 充电前卡余额
		uri += "_ugm=" + 0; // 升级模式
		uri += "_chp=" + 600; // 充电功率
		
		String res = HttpManager.getData(uri, getJoinUrl(inMap));
		
		System.out.println("结果："+res);
	}
	
	/**
	 *电桩状态（充电中） 
	 */
	public void status() throws Exception{
		ParaMap inMap = new ParaMap();
		String uri = "http://192.168.0.108:8080/system";
		uri +="/direct-module=net_service=Sinexcel_method=pileStatus";
		uri += "_gsn=" + "0105510007020010"; // 网关编号
		uri += "_csn=" + "0105510007020010"; // 电桩序列号
		uri += "_wst=" + 0; // 工作状态(2:充电进行中)
		uri += "_cst=" + 0; // 车连接状态

		uri += "_et=" + 0; // 充电费用
		uri += "_dn=" + 60; // 充电时长(秒)
		uri += "_pr=" + 720; // 此次充电电量（0.01kwh）
		uri += "_smp=" + 0; // 充电前电表读数 0.01kw

		uri += "_cmp=" + 720; // 当前电表读数  0.01kw
		uri += "_sme=" + 1; // 充电启动方式(0:刷卡；1：后台；2：密码)
		uri += "_cme=" + 0; // 充电策略
		uri += "_chmp=" + 0; // 充电策略参数

		uri += "_afg=" + 0; // 预约标志
		uri += "_cno=" + "185884025360"; // 充电/预约卡号
		uri += "_aoe=" + 30; // 预约超时时间
		uri += "_ste=" + 0; // 预约/开始充电开始时间

		uri += "_smy=" + 123; // 充电前卡余额
		uri += "_ugm=" + 0; // 升级模式
		uri += "_chp=" + 600; // 充电功率
		
		String res = HttpManager.getData(uri, getJoinUrl(inMap));
		
		System.out.println("结果："+res);
	}
	
	/**
	 * 拔枪
	 */
	public void pullout() throws Exception{
		ParaMap inMap = new ParaMap();
		String uri = "http://192.168.0.108:8080/system";
		uri +="/direct-module=net_service=Sinexcel_method=pileStatus";
		uri += "_gsn=" + "0105510007020010"; // 网关编号
		uri += "_csn=" + "0105510007020010"; // 电桩序列号
		uri += "_wst=" + 2; // 工作状态(2:充电进行中)
		uri += "_cst=" + 2; // 车连接状态

		uri += "_et=" + 0; // 充电费用
		uri += "_dn=" + 60; // 充电时长(秒)
		uri += "_pr=" + 360; // 此次充电电量（0.01kwh）
		uri += "_smp=" + 0; // 充电前电表读数 0.01kw

		uri += "_cmp=" + 360; // 当前电表读数  0.01kw
		uri += "_sme=" + 1; // 充电启动方式(0:刷卡；1：后台；2：密码)
		uri += "_cme=" + 0; // 充电策略
		uri += "_chmp=" + 0; // 充电策略参数

		uri += "_afg=" + 0; // 预约标志
		uri += "_cno=" + "0012345678987"; // 充电/预约卡号
		uri += "_aoe=" + 30; // 预约超时时间
		uri += "_ste=" + 0; // 预约/开始充电开始时间

		uri += "_smy=" + 123; // 充电前卡余额
		uri += "_ugm=" + 0; // 升级模式
		uri += "_chp=" + 600; // 充电功率
		
		String res = HttpManager.getData(uri, getJoinUrl(inMap));
		
		System.out.println("结果："+res);
	}
	
	/**
	 * 充电记录上传
	 */
	public void record() throws Exception{
		ParaMap inMap = new ParaMap();
		String uri = "http://192.168.0.108:8080/system";
		uri +="/direct-module=net_service=Sinexcel_method=record";
		uri += "_gsn=" + "0105510007020010"; // 网关编号
		uri += "_csn=" + "0105510007020010"; // 电桩序列号
		uri += "_ct=" + 1; // 充电枪位置类型
		uri += "_cs=" + 1; // 充电枪号
		
		uri += "_cno=" + "185884025360"; // 充电卡号
		uri += "_ste=" + "20160716142357"; // 充电开始时间
		uri += "_ete=" + "20160716142423"; // 充电结束时间
		uri += "_time=" + 120; // 充电时间长度(秒)
		
		uri += "_ern=" + 1; // 充电结束原因
		uri += "_pw=" + 360; // 本次充电电量
		uri += "_smp=" + 0; // 充电前电表读数
		uri += "_cmpr=" + 360; // 充电后电表读数

		uri += "_money=" + 233; // 本次充电金额
		uri += "_smy=" + 123; // 充电前卡余额
		uri += "_rid=" + 10; // 当前充电记录索引（每条充电记录唯一编号）
		uri += "_rct=" + 20; // 总充电记录条目
		
		uri += "_cme=" + 0; // 充电策略
		uri += "_cmpm=" + 0; // 充电策略参数
		uri += "_stm=" + 1; // 启动方式
		
		
		String res = HttpManager.getData(uri, getJoinUrl(inMap));
		
		System.out.println("结果："+res);
	}
	
	/**
	 * 充电
	 */
	public void charge() throws Exception{
		ParaMap inMap = new ParaMap();
		inMap.put("cmd", 7); 
		inMap.put("pileCode", "0105510007020010");  //电桩编号
		inMap.put("chargeType", 0); //充电生效类型
		inMap.put("chargeStrategy", 0);  //充电策略
		inMap.put("chargeStrategyParam", 1);  //充电策略参数
		inMap.put("startTime", sdf.format(new Date()));  //启动时间
		inMap.put("cardnumber", "1234567812345678");  //用户卡号
		
		String URL = "http://192.168.0.108:5679/data";
		String res = HttpManager.getData(URL, getJoinUrl(inMap));
		
		System.out.println("结果："+res);
	}
	
	/**
	 * 预约
	 */
	public void appoint() throws Exception{
		ParaMap inMap = new ParaMap();
		inMap.put("cmd", 7); 
		inMap.put("pileCode", "0105510007020010");  //电桩编号
		inMap.put("chargeType", 2); //充电生效类型
		inMap.put("startTime", sdf.format(new Date()));  //启动时间
		inMap.put("bookOvertime", "30");  //超时时间
		inMap.put("cardnumber", "12345678123456781234567812345678");  //用户卡号
		
		String URL = "http://192.168.0.108:5679/data";
		String res = HttpManager.getData(URL, getJoinUrl(inMap));
		
		System.out.println("结果："+res);
	}
	
	/**
	 * 停止充电
	 */
	public void stopCharge() throws Exception{
		ParaMap inMap = new ParaMap();
		inMap.put("cmd", 5); 
		inMap.put("pileCode", "0105510007020010");  //电桩编号
		inMap.put("optype", 1); //操作类型 1 停止充电 2 取消预约
		
		String URL = "http://192.168.0.108:5679/data";
		String res = HttpManager.getData(URL, getJoinUrl(inMap));
		
		System.out.println("结果："+res);
	}
	
	/**
	 * 取消预约
	 */
	public void cancelAppoint() throws Exception{
		ParaMap inMap = new ParaMap();
		inMap.put("cmd", 5); 
		inMap.put("pileCode", "0105510007020010");  //电桩编号
		inMap.put("optype", 2); //操作类型 1 停止充电 2 取消预约
		
		String URL = "http://192.168.0.108:5679/data";
		String res = HttpManager.getData(URL, getJoinUrl(inMap));
		
		System.out.println("结果："+res);
	}
	
	private static String getJoinUrl(ParaMap inMap) {
		List<String> keys = new ArrayList<String>(inMap.keySet());
		Collections.sort(keys);
		StringBuffer buff = new StringBuffer();
		for (int i = 0; i < keys.size(); i++) {
			String key = keys.get(i);
			String value = inMap.getString(key);
			if (buff.toString().length() == 0) {
				buff.append(key + "=" + value);
			} else {
				buff.append("&" + key + "=" + value);
			}
		}
		return buff.toString();
	}
}