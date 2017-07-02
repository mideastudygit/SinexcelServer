package com.net.bean;

import com.base.http.HttpManager;
import com.base.web.AppConfig;
import com.net.util.LogUtils;
import com.net.util.NetUtils;

import io.netty.channel.Channel;

/**
 * 停止充电/取消预约充电 (CMD=6)充电桩控制命令
 */
public class EndChargeBean extends BaseBean {
	private int[] chargeSlogan = { 1, 0 }; // 充电口号
	private int[] configureStartAddress = { 4, 2 }; // 参始地数起址
	private int[] count = { 1, 1 }; // 命令个数
	private int[] configureBytes = { 2, 4 }; // 命令参数字节数
	
	private int[] endCharge = { 4, 0 }; // 停止充电(0x55有效)
	private int[] chargeControlMode = { 4, 0 }; // 充电控制方式(0:BMS控制充电，1:盲充)
	private int[] chargeVoltage = { 4, 0 }; // 充电电压(盲充充电电压)
	private int[] chargeCurrent = { 4, 0 }; // 充电电流(盲充充电电流)
	private int[] chargeMode = { 4, 0 }; // 充电模式(0-恒流，1-恒压)
	private int[] endBookCharge = { 4, 0 }; // 取消预约充电(0x55有效)
	private int[] restart = { 4, 0 }; // 设备重启(0x55有效)
	private int[] enterUpdateMode = { 4, 0 }; // 进入升级模式(0x55有效)
	private int[] enterUseMode = { 4, 0 }; // 进入正常应用模式(0x55有效)
	private int[] reportLoginNow = { 4, 0 }; // 立即上报一次签到106报文
	private int[] reportPileStatusNow = { 4, 0 }; // 立即上报一次桩状态信息104报文
	private int[] paymentSuccess = { 4, 0 }; // 扫描支付成功(0x55有效)
	
	private int[] result = { 1, 0 }; // 命令执行结果
	private int paramType = 1; // 设置参数类型：2-停止充电;10-取消预约充电
	private int period = 0x55; // 参数值：停止充电、取消预约充电-0x55有效
	String information = "停止充电";
	
	public void init() {
		super.init();
		byteTo(userid);
		byteTo(cmdseq);
		byteToAscii(pileCode);
		byteTo(chargeSlogan);
		byteTo(configureStartAddress);
		byteTo(count);
		byteTo(result);
	}

	public void handle(Channel channel) throws Exception {
		String uri = AppConfig.getStringPro("sp-url");
		uri += "/direct-module=net_service=Sinexcel_method=endCharge";
		uri += "_gsn=" + getPileCode(); // 网关编号
		uri += "_csn=" + getPileCode(); // 充电桩编码
		uri += "_gunno=" + getChargeSlogan(); // 充电口号
		uri += "_csadd=" + getConfigureStartAddress(); // 命令起始标志
		uri += "_count=" + getCount(); // 命令个数
		uri += "_result=" + getResult(); // 命令执行结果
		
		information = getInformation(getConfigureStartAddress());
		LogUtils.send.info(information + "请求url：" + uri);
		try {
			String res = HttpManager.getData(uri, "");
			LogUtils.send.info(information + "请求返回：" + res);
		} catch (Exception e) {
			LogUtils.send.info(information + "请求异常，", e);
		}
	}

	public void send(Channel channel) {
		setCmd(CMD.END_CHARGE);

		tobyte(userid);
		tobyte(cmdseq);
		tobyte(chargeSlogan);
		setConfigureStartAddress(getParamType());
		tobyte(configureStartAddress);
		tobyte(count);
		tobyte(configureBytes);
		
		period = getPeriod();
		switch (getParamType()) {
			case 2: setEndCharge(period); tobyte(endCharge); break; // 停止充电
			case 4: setChargeControlMode(period); tobyte(chargeControlMode); break; // 充电控制方式
			case 7: setChargeVoltage(period); tobyte(chargeVoltage); break; // 充电电压
			case 8: setChargeCurrent(period); tobyte(chargeCurrent); break; // 充电电流
			case 9: setChargeMode(period); tobyte(chargeMode); break; // 充电模式
			case 10: setEndBookCharge(period); tobyte(endBookCharge); break; // 取消预约充电
			case 11: setRestart(period); tobyte(restart); break; // 设备重启
			case 12: setEnterUpdateMode(period); tobyte(enterUpdateMode); break; // 进入升级模式
			case 13: setEnterUseMode(period); tobyte(enterUseMode); break; // 进入正常应用模式
			case 14: setReportLoginNow(period); tobyte(reportLoginNow); break; // 立即上报一次签到106报文
			case 15: setReportPileStatusNow(period); tobyte(reportPileStatusNow); break; // 立即上报一次桩状态信息104报文
			case 16: setPaymentSuccess(period); tobyte(paymentSuccess); break; // 扫描支付成功
			default: break;
		}
		
		channel = NetUtils.getChannelByKey(getPileCode());
		information = getInformation(getParamType());
		LogUtils.send.info(information + "，" + channel + this);
		super.send(channel);
	}

	public int getChargeSlogan() {
		return chargeSlogan[1];
	}
	public void setChargeSlogan(int chargeSlogan) {
		this.chargeSlogan[1] = chargeSlogan;
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
	
	public int getEndCharge() {
		return endCharge[1];
	}
	public void setEndCharge(int endCharge) {
		this.endCharge[1] = endCharge;
	}
	
	public int getChargeControlMode() {
		return chargeControlMode[1];
	}
	public void setChargeControlMode(int chargeControlMode) {
		this.chargeControlMode[1] = chargeControlMode;
	}
	
	public int getChargeVoltage() {
		return chargeVoltage[1];
	}
	public void setChargeVoltage(int chargeVoltage) {
		this.chargeVoltage[1] = chargeVoltage;
	}
	
	public int getChargeCurrent() {
		return chargeCurrent[1];
	}
	public void setChargeCurrent(int chargeCurrent) {
		this.chargeCurrent[1] = chargeCurrent;
	}
	
	public int getChargeMode() {
		return chargeMode[1];
	}
	public void setChargeMode(int chargeMode) {
		this.chargeMode[1] = chargeMode;
	}
	
	public int getEndBookCharge() {
		return endBookCharge[1];
	}
	public void setEndBookCharge(int endBookCharge) {
		this.endBookCharge[1] = endBookCharge;
	}
	
	public int getRestart() {
		return restart[1];
	}
	public void setRestart(int restart) {
		this.restart[1] = restart;
	}
	
	public int getEnterUpdateMode() {
		return enterUpdateMode[1];
	}
	public void setEnterUpdateMode(int enterUpdateMode) {
		this.enterUpdateMode[1] = enterUpdateMode;
	}
	
	public int getEnterUseMode() {
		return enterUseMode[1];
	}
	public void setEnterUseMode(int enterUseMode) {
		this.enterUseMode[1] = enterUseMode;
	}
	
	public int getReportLoginNow() {
		return reportLoginNow[1];
	}
	public void setReportLoginNow(int reportLoginNow) {
		this.reportLoginNow[1] = reportLoginNow;
	}
	
	public int getReportPileStatusNow() {
		return reportPileStatusNow[1];
	}
	public void setReportPileStatusNow(int reportPileStatusNow) {
		this.reportPileStatusNow[1] = reportPileStatusNow;
	}
	
	public int getPaymentSuccess() {
		return paymentSuccess[1];
	}
	public void setPaymentSuccess(int paymentSuccess) {
		this.paymentSuccess[1] = paymentSuccess;
	}
	
	public int getResult() {
		return result[1];
	}
	public void setResult(int result) {
		this.result[1] = result;
	}
	
	public int getParamType() {
		return paramType;
	}
	
	public void setParamType(int paramType) {
		this.paramType = paramType;
	}
	
	public int getPeriod() {
		return period;
	}
	
	public void setPeriod(int period) {
		this.period = period;
	}
	
	public String getInformation(int type) {
		switch (type) {
			case 2: information = "停止充电"; break;
			case 4: information = "设置充电控制方式"; break;
			case 7: information = "设置充电电压"; break;
			case 8: information = "设置充电电流"; break;
			case 9: information = "设置充电模式"; break;
			case 10: information = "取消预约充电"; break;
			case 11: information = "设备重启"; break;
			case 12: information = "进入升级模式"; break;
			case 13: information = "进入正常应用模式"; break;
			case 14: information = "立即上报106报文"; break;
			case 15: information = "立即上报104报文"; break;
			case 16: information = "设置扫描支付成功"; break;
		}
		return information;
	}
}
