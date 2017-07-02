package com.net.test;

import com.alibaba.fastjson.JSONObject;

public class TestBean {
	public static void main(String[] args) {
		
		int e = 89;
		JSONObject obj = new JSONObject();
		obj.put("endpower", e);
		
		double endpower = obj.getDoubleValue("endpower"); // 本次充电的结束电量
		
		System.out.println(endpower / 1000 * 1.22 + endpower / 1000 * 0.45);
		
		obj.put("test", endpower / 1000);
		
		System.out.println(obj.getDouble("test"));
		
//		for (double i = 0; i < 1000; i++) {
//			System.out.println(i / 1000);
//		}
		
		
//		String str = "0112";
//		
//		byte b = (byte)str.charAt(0);
//		System.out.println((char)32);
		
//		String value = "00010";
//		while (value.startsWith("0")) {
//			value = value.substring(1);
//			System.out.println(value);
//		}
		
		// System.out.println(Integer.valueOf("ab", 16));
		// System.out.println(Integer.toHexString(171));

//		byte[] data = { 0x10, 0x10 };
//
//		int len = 2;
//
//		int value = 0;
//		for (int i = 0; i < len; i++) {
//			int shift = (len - 1 - i) * 8;
//			value += (data[i] & 0x000000FF) << shift;
//		}
//		
//		System.out.println(value);

	}

	public static void test() {
//		ChargeBean bean = new ChargeBean();
//		bean.setCmdId(CMD.LOGIN);
//		bean.setGatewayAddress("test00010002");
//		bean.setChargerAddress("charge1000001");
//		bean.setTimestamp(DateUtils.nowTime());
//		bean.setToken("abc123456789");
//		String binary = bean.toBinaryString();
//
//		NetService netService = new NetService();
//		BaseBean fromBean = netService.genBean(binary);
//
//		System.out.println(fromBean.getStartMarker());
//		System.out.println(fromBean.getCmdId());
//		System.out.println(fromBean.getToken());
//		System.out.println(fromBean.getGatewayAddress());
//		System.out.println(fromBean.getChargerAddress());
//		System.out.println(fromBean.getTimestamp());
//		System.out.println(fromBean.getDataLength());

		// System.out.println(fromBean.toBinaryString());
	}
}
