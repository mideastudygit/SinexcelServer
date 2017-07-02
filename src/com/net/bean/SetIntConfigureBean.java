package com.net.bean;

import com.base.http.HttpManager;
import com.base.web.AppConfig;
import com.net.util.LogUtils;
import com.net.util.NetUtils;

import io.netty.channel.Channel;

/**
 * 服务器向充电桩设置/查询工作参数和命令 (CMD=1)
 * 
 * @author 石马
 */
public class SetIntConfigureBean extends BaseBean {
	private int[] type = { 1, 1 }; // 类型（0-查询 1-设置）
	private int[] configureStartAddress = { 4, 1 }; // 设置/查询参始地数起址
	private int[] count = { 1, 1 }; // 设置/查询个数
	private int[] configureBytes = { 2, 4 }; // 设置参数字节数
	
	private int[] intervalTime = { 4, 0 }; // 签到时间间隔（默认30分）
	private int[] chargeProjectType = { 4, 0 }; // 充电桩项目类型
	private int[] chargeGunNumber = { 4, 0 }; // 充电枪个数
	private int[] channelNumber = { 4, 0 }; // 通道号
	private int[] maxChargeU = { 4, 0 }; // 最高充电电压
	private int[] maxChargeI = { 4, 0 }; // 最大充电电流
	private int[] cardReaderType = { 4, 0 }; // 读卡器类型
	private int[] cardReaderBaudRate = { 4, 0 }; // 读卡器波特率
	private int[] chargeCardProtocolNumber = { 4, 0 }; // 充电卡片协议编号
	private int[] serverVerification = { 4, 0 }; // 后台验证（0-不验证，1-验证）
	private int[] automaticVerification = { 4, 0 }; // 车牌验证（0-不验证，1-验证）
	private int[] cardVIN = { 4, 0 }; // 车卡VIN绑定
	private int[] bmsProtectionU = { 4, 0 }; // BMS单体保护电压
	private int[] bmsProtectionT = { 4, 0 }; // BMS充电保护温度
	private String[] debugAddress1 = { "4", "" }; // 调试地址1（0保持不变）
	private String[] debugAddress2 = { "4", "" }; // 调试地址2（0保持不变）
	private String[] debugAddress3 = { "4", "" }; // 调试地址3（0保持不变）
	private String[] debugAddress4 = { "4", "" }; // 调试地址4（0保持不变）
	private String[] debugAddress5 = { "4", "" }; // 调试地址5（0保持不变）
	private int[] timingReportInterval = { 4, 0 }; // 定时上报间隔（单位：15，缺省=15S）
	private int[] uploadPeriod = { 4, 0 }; // 心跳上报周期（0：保持不变，缺省=3S）
	private int[] heartTimeoutCount = { 4, 0 }; // 心跳包检测超时次数（0：保持不变，缺省=3S）
	private int[] pileStatusPeriod = { 4, 0 }; // 充电桩状态信息上报周期（0：保持不变，缺省=3S）
	private int[] communicationMode = { 4, 0 }; // 通信模式（1：应答模式，2：主动上报模式）
	private String[] serviceIP = { "4", "" }; // 中心服务器地址
	private int[] servicePort = { 4, 9830 }; // 中心服务器端口
	private int[] servicePrice = { 4, 0 }; // 服务费价格
	private String[] priceRate = { "4", "" }; // 全时段电费费率
	private int[] lightStartHour = { 4, 0 }; // 广告灯开启起始小时
	private int[] lightStartMinute = { 4, 0 }; // 广告灯开启起始分钟
	private int[] lightEndHour = { 4, 0 }; // 广告灯关闭起始小时
	private int[] lightEndMinute = { 4, 0 }; // 广告灯关闭起始分钟
	private String[] debugAdress = { "4", "" }; // 调试控制地址
	private String[] debugData = { "4", "" }; // 调试控制数据

	private int[] result = { 1, 0 }; // 设置/查询结果(0表示成功，其它失败)
	
	private int paramType = 1; // 设置参数类型：1-签到时间;2-心跳上报
	private int period = 120; // 整型参数值：paramType=1-签到时间间隔；paramType=2-心跳上报周期
	private String periodStr = ""; // 字符型参数值
	
	public void init() {
		super.init();
		byteTo(userid);
		byteTo(cmdseq);
		byteToAscii(pileCode);
		byteTo(type);
		byteTo(configureStartAddress);
		byteTo(count);
		byteTo(result);
		
		if(getType() == 0) {
			switch (getConfigureStartAddress()) {
				case 1: byteTo(intervalTime); break; // 查询签到时间间隔
				case 5: byteTo(maxChargeU); break; // 查询最高充电电压
				case 6: byteTo(maxChargeI); break; // 查询最大充电电流
				case 9: byteTo(chargeCardProtocolNumber); break; // 查询充电卡片协议编号
				case 10: byteTo(serverVerification); break; // 查询后台验证（0-不验证，1-验证）
				case 13: byteTo(bmsProtectionU); break; // 查询BMS单体保护电压
				case 14: byteTo(bmsProtectionT); break; // 查询BMS充电保护温度
				case 20: byteTo(timingReportInterval); break; // 查询定时上报间隔（单位：15，缺省=15S）
				case 21: byteTo(uploadPeriod); break; // 查询心跳上报周期
				case 22: byteTo(heartTimeoutCount); break; // 查询心跳包检测超时次数（0：保持不变，缺省=3S）
				case 23: byteTo(pileStatusPeriod); break; // 查询充电桩状态信息上报周期（0：保持不变，缺省=3S）
				case 25: 
					byteToHexString(serviceIP);
					byteTo(servicePort);
					break; // 查询服务器IP、端口
				case 29: byteTo(lightStartHour); break; // 查询广告灯开启起始小时
				case 30: byteTo(lightStartMinute); break; // 查询广告灯开启起始分钟
				case 31: byteTo(lightEndHour); break; // 查询广告灯关闭起始小时
				case 32: byteTo(lightEndMinute); break; // 查询广告灯关闭起始分钟
				default: break;
			}
		}
	}

	public void handle(Channel channel) throws Exception {
		String uri = AppConfig.getStringPro("sp-url");
		uri += "/direct-module=net_service=Sinexcel_method=intConfigure";
		uri += "_gsn=" + getPileCode(); // 网关编号
		uri += "_csn=" + getPileCode(); // 充电桩编码
		uri += "_type=" + getType(); // 类型（0-查询 1-设置）
		
		uri += "_csadd=" + getConfigureStartAddress(); // 设置/查询参始地数起址
		uri += "_count=" + getCount(); // 设置/查询个数
		uri += "_result=" + getResult(); // 设置/查询结果
		
		if(getType() == 0) {
			switch (getConfigureStartAddress()) {
				case 1: uri += "_intime=" + getIntervalTime(); break;
				case 5: uri += "_maxu=" + getMaxChargeU(); break;
				case 6: uri += "_maxi=" + getMaxChargeI(); break;
				case 9: uri += "_ccpn=" + getChargeCardProtocolNumber(); break;
				case 10: uri += "_sver=" + getServerVerification(); break;
				case 13: uri += "_bmspu=" + getBmsProtectionU(); break;
				case 14: uri += "_bmspt=" + getBmsProtectionT(); break;
				case 20: uri += "_timri=" + getTimingReportInterval(); break;
				case 21: uri += "_uperiod=" + getUploadPeriod(); break;
				case 22: uri += "_htoc=" + getHeartTimeoutCount(); break;
				case 23: uri += "_pspd=" + getPileStatusPeriod(); break;
				case 25: 
					uri += "_serip=" + getServiceIP(); 
					uri += "_serport=" + getServicePort();
					break;
				case 29: uri += "_lshour=" + getLightStartHour(); break;
				case 30: uri += "_lsmin=" + getLightStartMinute(); break;
				case 31: uri += "_lehour=" + getLightEndHour(); break;
				case 32: uri += "_lemin=" + getLightEndMinute(); break;
			default: break;
			}
		}
		
		LogUtils.send.info("设置/查询整型参数请求url：" + uri);
		String res = HttpManager.getData(uri, "");
		LogUtils.send.info("设置/查询整型参数请求返回：" + res);
	}

	public void send(Channel channel) {
		setCmd(CMD.SET_INT_CONFIGURE);

		tobyte(userid);
		tobyte(cmdseq);
		setType(getType());
		tobyte(type);
		setConfigureStartAddress(getParamType());
		tobyte(configureStartAddress);
		tobyte(count);
		tobyte(configureBytes);
		
		// 设置整型参数
		if (getType() == 1) {
			period = getPeriod();
			switch (getParamType()) {
				case 1: setIntervalTime(period); tobyte(intervalTime); break; // 签到时间间隔
				case 5: setMaxChargeU(period); tobyte(maxChargeU); break; // 最高充电电压
				case 6: setMaxChargeI(period); tobyte(maxChargeI); break; // 最大充电电流
				case 9: setChargeCardProtocolNumber(period); tobyte(chargeCardProtocolNumber); break; // 充电卡片协议编号
				case 10: setServerVerification(period); tobyte(serverVerification); break; // 后台验证（0-不验证，1-验证）
				case 13: setBmsProtectionU(period); tobyte(bmsProtectionU); break; // BMS单体保护电压
				case 14: setBmsProtectionT(period); tobyte(bmsProtectionT); break; // BMS充电保护温度
				case 20: setTimingReportInterval(period); tobyte(timingReportInterval); break; // 定时上报间隔（单位：15，缺省=15S）
				case 21: setUploadPeriod(period); tobyte(uploadPeriod); break; // 心跳上报周期
				case 22: setHeartTimeoutCount(period); tobyte(heartTimeoutCount); break; // 心跳包检测超时次数（0：保持不变，缺省=3S）
				case 23: setPileStatusPeriod(period); tobyte(pileStatusPeriod); break; // 充电桩状态信息上报周期（0：保持不变，缺省=3S）
				// 服务器IP、端口必须同时设置才生效
				case 25:
					setServiceIP(getStringPeriod());
					hexStringToByte(serviceIP); // 骆驼快充服务器IP
					setServicePort(period);
					tobyte(servicePort); // 骆驼快充服务器Port
					break;
				case 29: setLightStartHour(period); tobyte(lightStartHour); break; // 广告灯开启起始小时
				case 30: setLightStartMinute(period); tobyte(lightStartMinute); break; // 广告灯开启起始分钟
				case 31: setLightEndHour(period); tobyte(lightEndHour); break; // 广告灯关闭起始小时
				case 32: setLightEndMinute(period); tobyte(lightEndMinute); break; // 广告灯关闭起始分钟
				default: break;
			}
		}
		
		channel = NetUtils.getChannelByKey(getPileCode());

		LogUtils.send.info("设置/查询整型参数，" + channel + this);

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
	
	public int getCount() {
		return count[1];
	}
	
	public void setCount(int count) {
		this.count[1] = count;
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
	
	public int getIntervalTime() {
		return intervalTime[1];
	}
	
	public void setIntervalTime(int intervalTime) {
		this.intervalTime[1] = intervalTime;
	}
	
	public int getMaxChargeU() {
		return maxChargeU[1];
	}
	
	public void setMaxChargeU(int maxChargeU) {
		this.maxChargeU[1] = maxChargeU;
	}
	
	public int getMaxChargeI() {
		return maxChargeI[1];
	}
	
	public void setMaxChargeI(int maxChargeI) {
		this.maxChargeI[1] = maxChargeI;
	}
	
	public int getChargeCardProtocolNumber() {
		return chargeCardProtocolNumber[1];
	}
	
	public void setChargeCardProtocolNumber(int chargeCardProtocolNumber) {
		this.chargeCardProtocolNumber[1] = chargeCardProtocolNumber;
	}
	
	public int getServerVerification() {
		return serverVerification[1];
	}
	
	public void setServerVerification(int serverVerification) {
		this.serverVerification[1] = serverVerification;
	}
	
	public int getBmsProtectionU() {
		return bmsProtectionU[1];
	}
	
	public void setBmsProtectionU(int bmsProtectionU) {
		this.bmsProtectionU[1] = bmsProtectionU;
	}
	
	public int getBmsProtectionT() {
		return bmsProtectionT[1];
	}
	
	public void setBmsProtectionT(int bmsProtectionT) {
		this.bmsProtectionT[1] = bmsProtectionT;
	}
	
	public int getTimingReportInterval() {
		return timingReportInterval[1];
	}
	
	public void setTimingReportInterval(int timingReportInterval) {
		this.timingReportInterval[1] = timingReportInterval;
	}
	
	public int getHeartTimeoutCount() {
		return heartTimeoutCount[1];
	}
	
	public void setHeartTimeoutCount(int heartTimeoutCount) {
		this.heartTimeoutCount[1] = heartTimeoutCount;
	}
	
	public int getPileStatusPeriod() {
		return pileStatusPeriod[1];
	}
	
	public void setPileStatusPeriod(int pileStatusPeriod) {
		this.pileStatusPeriod[1] = pileStatusPeriod;
	}
	
	public int getLightStartHour() {
		return lightStartHour[1];
	}
	
	public void setLightStartHour(int lightStartHour) {
		this.lightStartHour[1] = lightStartHour;
	}
	
	public int getLightStartMinute() {
		return lightStartMinute[1];
	}
	
	public void setLightStartMinute(int lightStartMinute) {
		this.lightStartMinute[1] = lightStartMinute;
	}
	
	public int getLightEndHour() {
		return lightEndHour[1];
	}
	
	public void setLightEndHour(int lightEndHour) {
		this.lightEndHour[1] = lightEndHour;
	}
	
	public int getLightEndMinute() {
		return lightEndMinute[1];
	}
	
	public void setLightEndMinute(int lightEndMinute) {
		this.lightEndMinute[1] = lightEndMinute;
	}
	
	public int getUploadPeriod() {
		return uploadPeriod[1];
	}
	
	public void setUploadPeriod(int uploadPeriod) {
		this.uploadPeriod[1] = uploadPeriod;
	}
	
	public int getParamType() {
		return paramType;
	}
	
	public void setParamType(int paramType) {
		this.paramType = paramType;
	}
	
	
	public String getServiceIP() {
		return serviceIP[1];
	}
	
	public void setServiceIP(String serviceIP) {
		this.serviceIP[1] = serviceIP;
	}
	
	public int getServicePort() {
		return servicePort[1];
	}
	
	public void setServicePort(int servicePort) {
		this.servicePort[1] = servicePort;
	}
	
	public int getPeriod() {
		return period;
	}
	
	public String getStringPeriod() {
		return periodStr;
	}
	
	public void setPeriod(int period) {
		this.period = period;
	}
	
	public void setStringPeriod(String periodStr) {
		this.periodStr = periodStr;
	}
}
