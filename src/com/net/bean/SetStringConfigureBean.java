package com.net.bean;

import com.base.http.HttpManager;
import com.base.web.AppConfig;
import com.net.util.LogUtils;
import com.net.util.NetUtils;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;

/**
 * 后台服务器向充电桩设置/查询字符型工作参数和命令 (CMD=3)
 * 
 * @author 石马
 */
public class SetStringConfigureBean extends BaseBean {
	private int[] type = { 1, 1 }; // 类型（0-查询 1-设置）
	private int[] configureStartAddress = { 4, 1 }; // 设置/查询参始地数起址：1-充电桩编码;2-标准时钟时间；3-管理员密码；4-操作员密码；5-MAC地址；7-二维码；8-客户服务热线1；9-客户服务热线2；10-用户支付二维码；
	private int[] configureBytes = { 2, 1 }; // 设置参数字节数————参数字符串类型，一次只能设置一个参数
	
	private String[] devicePileCode = { "32", "" }; // 充电桩编码
	private String[] currentTime = { "8", "" }; // 标准时钟时间
	private String[] adminPwd = { "8", "" }; // 管理员密码
	private String[] operatePwd = { "8", "" }; // 操作员密码
	private String[] macAddress = { "6", "" }; // MAC地址
	private String[] qrCode = { "256", "" }; // 二维码
	private String[] serviceHotline1 = { "16", "" }; // 客户服务热线1
	private String[] serviceHotline2 = { "16", "" }; // 客户服务热线2
	private String[] paymentQRCode = { "256", "" }; // 用户支付二维码

	private int[] result = { 1, 0 }; // 设置/查询结果(0表示成功，其它失败)
	
	private int paramType = 2; // 设置参数类型：2-标准时钟时间
	private String param = ""; // 电桩接收时钟二进制格式：20 16 11 16 03 00 35 FF
	
	
	public void init() {
		super.init();
		byteTo(userid);
		byteTo(cmdseq);
		byteToAscii(pileCode);
		byteTo(type);
		byteTo(configureStartAddress);
		byteTo(result);
		
		if(getType() == 0) {
			switch (getConfigureStartAddress()) {
				case 1: byteToAscii(devicePileCode); break; // 查询充电桩编码
				case 2: byteToHexString(currentTime); break; // 查询标准时钟时间
				case 3: byteToAscii(adminPwd); break; // 查询管理员密码
				case 4: byteToAscii(operatePwd); break; // 查询操作员密码
				case 5: byteToAscii(macAddress); break; // 查询MAC地址
				case 7: byteToAscii(qrCode); break; // 查询二维码
				case 8: byteToAscii(serviceHotline1); break; // 查询客户服务热线1
				case 9: byteToAscii(serviceHotline2); break; // 查询客户服务热线2
				case 10: byteToAscii(paymentQRCode); break; // 查询用户支付二维码
				default: break;
			}
		}
	}

	public void handle(Channel channel) throws Exception {
		String uri = AppConfig.getStringPro("sp-url");
		uri += "/direct-module=net_service=Sinexcel_method=stringConfigure";
		uri += "_gsn=" + getPileCode(); // 网关编号
		uri += "_csn=" + getPileCode(); // 充电桩编码
		uri += "_type=" + getType();
		uri += "_csadd=" + getConfigureStartAddress();
		uri += "_result=" + getResult(); // 设置/查询结果
		
		if(getType() == 0) {
			switch (getConfigureStartAddress()) {
				case 1: uri += "_dpc=" + getDevicePileCode(); break;
				case 2: uri += "_crt=" + getCurrentTime(); break;
				case 3: uri += "_apwd=" + getAdminPwd(); break;
				case 4: uri += "_opwd=" + getOperatePwd(); break;
				case 5: uri += "_macadd=" + getMacAddress(); break;
				case 7: uri += "_qrc=" + getQrCode(); break;
				case 8: uri += "_shl1=" + getServiceHotline1(); break;
				case 9: uri += "_shl2=" + getServiceHotline2(); break;
				case 10: uri += "_pqrc=" + getPaymentQRCode(); break;
			default: break;
			}
		}
		
		LogUtils.send.info("设置/查询字符型参数请求url：" + uri);
		
		try {
			String res = HttpManager.getData(uri, "");
			LogUtils.send.info("设置/查询字符型参数请求返回：" + res);
		} catch (Exception e) {
			LogUtils.send.info("设置/查询字符型参数请求异常，", e);
		}
	}

	public void send(Channel channel) {
		setCmd(CMD.SET_STRING_CONFIGURE);

		tobyte(userid);
		tobyte(cmdseq);
		setType(getType());
		tobyte(type);
		
		setConfigureStartAddress(getParamType());
		switch (getParamType()) {
			case 1: setConfigureBytes(32); break; // 查询充电桩编码
			case 2: setConfigureBytes(8); break; // 查询标准时钟时间
			case 3: setConfigureBytes(8); break; // 查询管理员密码
			case 4: setConfigureBytes(8); break; // 查询操作员密码
			case 5: setConfigureBytes(6); break; // 查询MAC地址
			case 6: setConfigureBytes(16); break; // 查询预留参数
			case 7: setConfigureBytes(256); break; // 查询二维码
			case 8: setConfigureBytes(16); break; // 查询客户服务热线1
			case 9: setConfigureBytes(16); break; // 查询客户服务热线2
			case 10: setConfigureBytes(256); break; // 查询用户支付二维码
			default: break;
		}
		
		tobyte(configureStartAddress);
		tobyte(configureBytes);
		
		// 设置整型参数
		if (getType() == 1) {
			param = getParam();
			switch (getParamType()) {
				case 1: setDevicePileCode(param); asciiToByte(devicePileCode); break; // 查询充电桩编码
				case 2: setCurrentTime(param); hexStringToByte(currentTime); break; // 查询标准时钟时间
				case 3: setAdminPwd(param); asciiToByte(adminPwd); break; // 查询管理员密码
				case 4: setOperatePwd(param); asciiToByte(operatePwd); break; // 查询操作员密码
				case 5: setMacAddress(param); asciiToByte(macAddress); break; // 查询MAC地址
				case 7: setQrCode(param); asciiToByte(qrCode); break; // 查询二维码
				case 8: setServiceHotline1(param); asciiToByte(serviceHotline1); break; // 查询客户服务热线1
				case 9: setServiceHotline2(param); asciiToByte(serviceHotline2); break; // 查询客户服务热线2
				case 10: setPaymentQRCode(param); asciiToByte(paymentQRCode); break; // 查询用户支付二维码
				default: break;
			}
		}
		
		LogUtils.send.info("设置/查询字符型参数，" + channel + this);
		
		super.send(channel);
	}
	
	public int getType() {
		return type[1];
	}
	
	public void setType(int type) {
		this.type[1] = type;
	}
	
	public int getConfigureStartAddress() {
		return configureStartAddress[1];
	}
	
	public void setConfigureStartAddress(int configureStartAddress) {
		this.configureStartAddress[1] = configureStartAddress;
	}
	
	public int getResult() {
		return result[1];
	}
	
	public void setResult(int result) {
		this.result[1] = result;
	}
	
	public int getConfigureBytes() {
		return configureBytes[1];
	}
	
	public void setConfigureBytes(int configureBytes) {
		this.configureBytes[1] = configureBytes;
	}
	
	public String getDevicePileCode() {
		return devicePileCode[1];
	}
	
	public void setDevicePileCode(String devicePileCode) {
		this.devicePileCode[1] = devicePileCode;
	}
	
	public String getCurrentTime() {
		return currentTime[1];
	}
	
	public void setCurrentTime(String currentTime) {
		this.currentTime[1] = currentTime;
	}
	
	public String getAdminPwd() {
		return adminPwd[1];
	}
	
	public void setAdminPwd(String adminPwd) {
		this.adminPwd[1] = adminPwd;
	}
	
	public String getOperatePwd() {
		return operatePwd[1];
	}
	
	public void setOperatePwd(String operatePwd) {
		this.operatePwd[1] = operatePwd;
	}
	
	public String getMacAddress() {
		return macAddress[1];
	}
	
	public void setMacAddress(String macAddress) {
		this.macAddress[1] = macAddress;
	}
	
	public String getQrCode() {
		return qrCode[1];
	}
	
	public void setQrCode(String qrCode) {
		this.qrCode[1] = qrCode;
	}
	
	public String getServiceHotline1() {
		return serviceHotline1[1];
	}
	
	public void setServiceHotline1(String serviceHotline1) {
		this.serviceHotline1[1] = serviceHotline1;
	}
	
	public String getServiceHotline2() {
		return serviceHotline2[1];
	}
	
	public void setServiceHotline2(String serviceHotline2) {
		this.serviceHotline2[1] = serviceHotline2;
	}
	
	public String getPaymentQRCode() {
		return paymentQRCode[1];
	}
	
	public void setPaymentQRCode(String paymentQRCode) {
		this.paymentQRCode[1] = paymentQRCode;
	}
	
	public int getParamType() {
		return paramType;
	}
	
	public void setParamType(int paramType) {
		this.paramType = paramType;
	}
	
	public String getParam() {
		return param;
	}
	
	public void setParam(String param) {
		this.param = param;
	}
}
