package com.net.bean;

import com.base.http.HttpManager;
import com.base.utils.StrUtils;
import com.base.web.AppConfig;
import com.net.util.LogUtils;

import io.netty.channel.Channel;

/**
 * 电桩状态
 * (CMD=104)充电桩状态信息包上报
 */
public class PileStatusBean extends BaseBean {
	private int[] gunCount = { 1, 0 }; // 充电枪个数
	private int[] chargeSlogan = { 1, 0 }; // 充电口号

	private int[] gunType = { 1, 0 }; // 充电枪类型  1=直流； 2=交流；
	private int[] workStatus = { 1, 0 }; // 工作状态  0‐空闲中1‐正准备开始充电2‐充电进行中3‐充电结束4‐启动失败5‐预约状态6‐系统故障(不能给汽车充电)
	private int[] soc = { 1, 0 }; // 当前SOC %
	private int[] alarmStatus = { 4, 0 }; // 告警状态
	private int[] connStatus = { 1, 0 }; // 车连接状态  0‐ 断开 1‐半连接 2‐连接 
										//  直流目前只有0 和2 状态 交流目前有0、1、2 三种状 态 只有状态不为0 时，手机才 能下发开机指令

	private int[] eleamount = { 4, 0 }; // 充电费用
	private int[] variable2 = { 4, 0 }; // 内部变量2
	private int[] variable3 = { 4, 0 }; // 内部变量3
	private int[] directVoltage = { 2, 0 }; // 直流充电电压
	private int[] directCurrent = { 2, 0 }; // 直流充电电流

	private int[] bmsVoltage = { 2, 0 }; // BMS 需求电压
	private int[] bmsCurrent = { 2, 0 }; // BMS 需求电流
	private int[] bmsChargeMode = { 1, 0 }; // BMS 充电模式
	private int[] aChargeVoltage = { 2, 0 }; // 交流A 相充电电压
	private int[] bChargeVoltage = { 2, 0 }; // 交流B 相充电电压

	private int[] cChargeVoltage = { 2, 0 }; // 交流C 相充电电压
	private int[] aChargeCurrent = { 2, 0 }; // 交流A 相充电电流
	private int[] bChargeCurrent = { 2, 0 }; // 交流B 相充电电流
	private int[] cChargeCurrent = { 2, 0 }; // 交流C 相充电电流
	private int[] surplusTime = { 2, 0 }; // 剩余充电时间(min)

	private int[] duration = { 4, 0 }; // 充电时长(秒)
	private int[] power = { 4, 0 }; // 此次充电电量（0.01kwh） //00 00 00 11
	private int[] startMeterPower = { 4, 0 }; // 充电前电表读数  0.01kw
	private int[] currMeterPower = { 4, 0 }; // 当前电表读数  0.01kw
	private int[] startMode = { 1, 0 }; // 充电启动方式  0：本地刷卡启动1：后台启动2：本地管理员启动

	private int[] chargeMode = { 1, 0 }; // 充电策略  0 自动充满 1 按时间充满 2 定金额 3 按电量充满
	private int[] chargeModeParam = { 4, 0 }; // 充电策略参数 时间单位为1 秒 金额单位为0.01 元 电量时单位为0.01kw
	private int[] appointFlag = { 1, 0 }; // 预约标志  0‐无预约（无效）1‐预约有效
	private String[] cardNo = { "32", "" }; // 充电/预约卡号
	private int[] appointOvertime = { 1, 0 }; // 预约超时时间

	private String[] startTime = { "8", "" }; // 预约/开始充电开始时间
	private int[] startMoney = { 4, 0 }; // 充电前卡余额
	private int[] upgradeMode = { 4, 0 }; // 升级模式 1 0‐ 正常模式 1‐ IAP 模式
	private int[] chargePower = { 4, 0 }; // 充电功率
	private int[] sysVariable3 = { 4, 0 }; // 系统变量3

	private int[] sysVariable4 = { 4, 0 }; // 系统变量4
	private int[] sysVariable5 = { 4, 0 }; // 系统变量5
	
	public void init() {
		super.init();
		byteTo(userid);
		byteTo(cmdseq);
		byteToAscii(pileCode);
		byteTo(gunCount);
		byteTo(chargeSlogan);

		byteTo(gunType);
		byteTo(workStatus);
		byteTo(soc);
		byteTo(alarmStatus);
		byteTo(connStatus);

		byteTo(eleamount);
		byteTo(variable2);
		byteTo(variable3);
		byteTo(directVoltage);
		byteTo(directCurrent);

		byteTo(bmsVoltage);
		byteTo(bmsCurrent);
		byteTo(bmsChargeMode);
		byteTo(aChargeVoltage);
		byteTo(bChargeVoltage);

		byteTo(cChargeVoltage);
		byteTo(aChargeCurrent);
		byteTo(bChargeCurrent);
		byteTo(cChargeCurrent);
		byteTo(surplusTime);

		byteTo(duration);
		byteTo(power);
		byteTo(startMeterPower);
		byteTo(currMeterPower);
		byteTo(startMode);

		byteTo(chargeMode);
		byteTo(chargeModeParam);
		byteTo(appointFlag);
		byteToAscii(cardNo);
		byteTo(appointOvertime);

		byteToHexString(startTime);
		byteTo(startMoney);
		byteTo(upgradeMode);
		byteTo(chargePower);
		byteTo(sysVariable3);

		byteTo(sysVariable4);
		byteTo(sysVariable5);
	}

	public void handle(Channel channel) throws Exception {
		String uri = AppConfig.getStringPro("sp-url");
		uri += "/direct-module=net_service=Sinexcel_method=pileStatus";
		uri += "_gsn=" + getPileCode(); // 网关编号
		uri += "_csn=" + getPileCode(); // 电桩序列号
		uri += "_gc=" + gunCount[1];  // 充电枪个数
		uri += "_cs=" + chargeSlogan[1]; // 充电口号
		uri += "_gt=" + gunType[1]; // 充电枪类型  1=直流； 2=交流
		uri += "_wst=" + getWorkStatus(); // 工作状态
		uri += "_soc=" + getSoc(); // SOC
		uri += "_as=" + getAlarmStatus(); // 告警状态
		uri += "_cst=" + getConnStatus(); // 车连接状态
		
		uri += "_dv=" + directVoltage[1]; // 直流充电电压
		uri += "_dc=" + directCurrent[1]; // 直流充电电流
		uri += "_bv=" + bmsVoltage[1]; // BMS 需求电压
		uri += "_bc=" + bmsCurrent[1]; // BMS 需求电流
		uri += "_bcm=" + bmsChargeMode[1]; // BMS 充电模式
		uri += "_acv=" + aChargeVoltage[1]; // 交流A 相充电电压
		uri += "_bcv=" + bChargeVoltage[1]; // 交流B 相充电电压
		uri += "_ccv=" + cChargeVoltage[1]; // 交流C 相充电电压
		uri += "_acc=" + aChargeCurrent[1]; // 交流A 相充电电流
		uri += "_bcc=" + bChargeCurrent[1]; // 交流B 相充电电流
		uri += "_ccc=" + cChargeCurrent[1]; // 交流C 相充电电流

		uri += "_st=" + surplusTime[1]; // 剩余充电时间(min)
		uri += "_et=" + getEleamount(); // 充电费用
		uri += "_dn=" + getDuration(); // 充电时长(秒)
		uri += "_pr=" + getPower(); // 此次充电电量（0.01kwh）
		uri += "_smp=" + getStartMeterPower(); // 充电前电表读数 0.01kw
		uri += "_cmp=" + getCurrMeterPower(); // 当前电表读数  0.01kw
		uri += "_sme=" + getStartMode(); // 充电启动方式
		uri += "_cme=" + getChargeMode(); // 充电策略
		uri += "_chmp=" + getChargeModeParam(); // 充电策略参数

		uri += "_afg=" + getAppointFlag(); // 预约标志
		uri += "_cno=" + getCardNo(); // 充电/预约卡号
		uri += "_aoe=" + getAppointOvertime(); // 预约超时时间
		
		String startTime = getStartTime();
		if (StrUtils.isNotNull(startTime) && startTime.length() > 14) {
			startTime = startTime.substring(0, 14);
		}
		uri += "_ste=" + startTime; // 预约/开始充电开始时间

		uri += "_smy=" + getStartMoney(); // 充电前卡余额
		uri += "_ugm=" + getUpgradeMode(); // 升级模式
		uri += "_chp=" + getChargePower(); // 充电功率
		
		LogUtils.send.info("电桩状态指令请求url：" + uri);
		
		try {
			String res = HttpManager.getData(uri, "");
			LogUtils.send.info("电桩状态指令返回：" + res);
		} catch (Exception e) {
			LogUtils.send.info("电桩状态指令异常，", e);
		}
		
		send(channel);
	}

	public void send(Channel channel) {
		tobyte(userid);
		tobyte(cmdseq);
		tobyte(chargeSlogan);
		super.send(channel);
	}
	
	public int getWorkStatus() {
		return workStatus[1];
	}
	public void setWorkStatus(int workStatus) {
		this.workStatus[1] = workStatus;
	}
	public int getSoc() {
		return soc[1];
	}
	public void setSoc(int soc) {
		this.soc[1] = soc;
	}
	public int getAlarmStatus() {
		return alarmStatus[1];
	}
	public void setAlarmStatus(int alarmStatus) {
		this.alarmStatus[1] = alarmStatus;
	}
	public int getConnStatus() {
		return connStatus[1];
	}
	public void setConnStatus(int connStatus) {
		this.connStatus[1] = connStatus;
	}
	
	
	public int getEleamount() {
		return eleamount[1];
	}
	public void setEleamount(int eleamount) {
		this.eleamount[1] = eleamount;
	}
	public int getDuration() {
		return duration[1];
	}
	public void setDuration(int duration) {
		this.duration[1] = duration;
	}
	public int getPower() {
		return power[1];
	}
	public void setPower(int power) {
		this.power[1] = power;
	}
	public int getStartMeterPower() {
		return startMeterPower[1];
	}
	public void setStartMeterPower(int startMeterPower) {
		this.startMeterPower[1] = startMeterPower;
	}
	
	
	public int getCurrMeterPower() {
		return currMeterPower[1];
	}
	public void setCurrMeterPower(int currMeterPower) {
		this.currMeterPower[1] = currMeterPower;
	}
	public int getStartMode() {
		return startMode[1];
	}
	public void setStartMode(int startMode) {
		this.startMode[1] = startMode;
	}
	public int getChargeMode() {
		return chargeMode[1];
	}
	public void setChargeMode(int chargeMode) {
		this.chargeMode[1] = chargeMode;
	}
	public int getChargeModeParam() {
		return chargeModeParam[1];
	}
	public void setChargeModeParam(int chargeModeParam) {
		this.chargeModeParam[1] = chargeModeParam;
	}
	
	
	public int getAppointFlag() {
		return appointFlag[1];
	}
	public void setAppointFlag(int appointFlag) {
		this.appointFlag[1] = appointFlag;
	}
	public String getCardNo() {
		return cardNo[1];
	}
	public void setCardNo(String cardNo) {
		this.cardNo[1] = cardNo;
	}
	public int getAppointOvertime() {
		return appointOvertime[1];
	}
	public void setAppointOvertime(int appointOvertime) {
		this.appointOvertime[1] = appointOvertime;
	}
	public String getStartTime() {
		return startTime[1];
	}
	public void setStartTime(String startTime) {
		this.startTime[1] = startTime;
	}
	
	
	public int getStartMoney() {
		return startMoney[1];
	}
	public void setStartMoney(int startMoney) {
		this.startMoney[1] = startMoney;
	}
	public int getUpgradeMode() {
		return upgradeMode[1];
	}
	public void setUpgradeMode(int upgradeMode) {
		this.upgradeMode[1] = upgradeMode;
	}
	public int getChargePower() {
		return chargePower[1];
	}
	public void setChargePower(int chargePower) {
		this.chargePower[1] = chargePower;
	}
	
	
	
	

}
