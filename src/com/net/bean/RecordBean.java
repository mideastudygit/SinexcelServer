package com.net.bean;

import com.base.http.HttpManager;
import com.base.utils.StrUtils;
import com.base.web.AppConfig;
import com.net.util.LogUtils;

import io.netty.channel.Channel;

/**
 * 充电记录信息上报
 * (CMD=202)充电桩上报最新一次充电信息
 */
public class RecordBean extends BaseBean {
	private int[] gunType = { 1, 0 }; // 充电枪类型  1‐直流 2‐交流
	private int[] chargeSlogan = { 1, 0 }; // 充电枪号
	private String[] cardNo = { "32", "" }; // 充电卡号
	private String[] startTime = { "8", "" }; // 充电开始时间
	private String[] endTime = { "8", "" }; // 充电结束时间
	
	private int[] chargeTime = { 4, 0 }; // 充电时间长度(秒)
	private int[] startSoc = { 1, 0 }; // 开始SOC(秒)
	private int[] endSoc = { 1, 0 }; // 结束SOC
	private int[] endReason = { 4, 0 }; // 充电结束原因
	private int[] power = { 4, 0 }; // 本次充电电量
	
	private int[] startMeterPower = { 4, 0 }; // 充电前电表读数
	private int[] currMeterPower = { 4, 0 }; // 充电后电表读数
	private int[] money = { 4, 0 }; // 本次充电金额
	private int[] variable = { 4, 0 }; // 预留
	private int[] startMoney = { 4, 0 }; // 充电前卡余额
	
	private int[] recordId = { 4, 0 }; // 当前充电记录索引（每条充电记录唯一编号）
	private int[] recordCount = { 4, 0 }; // 总充电记录条目
	private int[] variable2 = { 1, 0 }; // 预留
	private int[] chargeMode = { 1, 0 }; // 充电策略 0:充满为止 1:时间控制充电 2:金额控制充电 3:电量控制充电
	private int[] chargeModeParam = { 4, 0 }; // 充电策略参数  时间单位为1 秒 金额单位为0.01 元 电量时单位为0.01kw
	
	private int[] vin = { 17, 0 }; // 车辆 VIN  可选项，没有填’\0’
	private int[] carNo = { 8, 0 }; // 车牌号  可选项，没有填’\0’
	private int[] period1 = { 2, 0 }; // 时段1 电量
	private int[] period2 = { 2, 0 }; // 时段2 电量
	private int[] period3 = { 2, 0 }; // 时段3 电量
	
	private int[] period4 = { 2, 0 }; // 时段4 电量
	private int[] period5 = { 2, 0 }; // 时段5 电量
	private int[] period6 = { 2, 0 }; // 时段6 电量
	private int[] period7 = { 2, 0 }; // 时段7 电量
	private int[] period8 = { 2, 0 }; // 时段8 电量
	
	private int[] period9 = { 2, 0 }; // 时段9 电量
	private int[] period10 = { 2, 0 }; // 时段10电量
	private int[] period11 = { 2, 0 }; // 时段11电量
	private int[] period12 = { 2, 0 }; // 时段12 电量
	private int[] period13 = { 2, 0 }; // 时段13 电量
	
	private int[] period14 = { 2, 0 }; // 时段14 电量
	private int[] period15 = { 2, 0 }; // 时段15 电量
	private int[] period16 = { 2, 0 }; // 时段16 电量
	private int[] period17 = { 2, 0 }; // 时段17 电量
	private int[] period18 = { 2, 0 }; // 时段18 电量
	
	private int[] period19 = { 2, 0 }; // 时段19电量
	private int[] period20 = { 2, 0 }; // 时段20 电量
	private int[] period21 = { 2, 0 }; // 时段21 电量
	private int[] period22 = { 2, 0 }; // 时段22 电量
	private int[] period23 = { 2, 0 }; // 时段23 电量
	
	private int[] period24 = { 2, 0 }; // 时段24 电量
	private int[] period25 = { 2, 0 }; // 时段25 电量
	private int[] period26 = { 2, 0 }; // 时段26 电量
	private int[] period27 = { 2, 0 }; // 时段27 电量
	private int[] period28 = { 2, 0 }; // 时段28 电量
	
	private int[] period29 = { 2, 0 }; // 时段29 电量
	private int[] period30 = { 2, 0 }; // 时段30电量
	private int[] period31 = { 2, 0 }; // 时段31 电量
	private int[] period32 = { 2, 0 }; // 时段32 电量
	private int[] period33 = { 2, 0 }; // 时段33 电量
	
	private int[] period34 = { 2, 0 }; // 时段34 电量
	private int[] period35 = { 2, 0 }; // 时段35 电量
	private int[] period36 = { 2, 0 }; // 时段36 电量
	private int[] period37 = { 2, 0 }; // 时段37 电量
	private int[] period38 = { 2, 0 }; // 时段38 电量
	
	private int[] period39 = { 2, 0 }; // 时段39 电量
	private int[] period40 = { 2, 0 }; // 时段40电量
	private int[] period41 = { 2, 0 }; // 时段41 电量
	private int[] period42 = { 2, 0 }; // 时段42 电量
	private int[] period43 = { 2, 0 }; // 时段43 电量
	
	private int[] period44 = { 2, 0 }; // 时段44 电量
	private int[] period45 = { 2, 0 }; // 时段45 电量
	private int[] period46 = { 2, 0 }; // 时段46 电量
	private int[] period47 = { 2, 0 }; // 时段47 电量
	private int[] period48 = { 2, 0 }; // 时段48 电量
	
	private int[] startMode = { 1, 0 }; // 充电启动方式  0：本地刷卡启动1：后台启动2：本地管理员启动
	
	public void init() {
		super.init();
		byteTo(userid);
		byteTo(cmdseq);
		byteToAscii(pileCode);
		byteTo(gunType);
		byteTo(chargeSlogan);
		
		byteToAscii(cardNo);
		byteToHexString(startTime);
		byteToHexString(endTime);
		byteTo(chargeTime);
		byteTo(startSoc);
		
		byteTo(endSoc);
		byteTo(endReason);
		byteTo(power);
		byteTo(startMeterPower);
		byteTo(currMeterPower);
		
		byteTo(money);
		byteTo(variable);
		byteTo(startMoney);
		byteTo(recordId);
		byteTo(recordCount);

		byteTo(variable2);
		byteTo(chargeMode);
		byteTo(chargeModeParam);
		byteTo(vin);
		byteTo(carNo);
		
		byteTo(period1);
		byteTo(period2);
		byteTo(period3);
		byteTo(period4);
		byteTo(period5);
		
		byteTo(period6);
		byteTo(period7);
		byteTo(period8);
		byteTo(period9);
		byteTo(period10);
		
		byteTo(period11);
		byteTo(period12);
		byteTo(period13);
		byteTo(period14);
		byteTo(period15);
		
		byteTo(period16);
		byteTo(period17);
		byteTo(period18);
		byteTo(period19);
		byteTo(period20);

		byteTo(period21);
		byteTo(period22);
		byteTo(period23);
		byteTo(period24);
		byteTo(period25);

		byteTo(period26);
		byteTo(period27);
		byteTo(period28);
		byteTo(period29);
		byteTo(period30);

		byteTo(period31);
		byteTo(period32);
		byteTo(period33);
		byteTo(period34);
		byteTo(period35);

		byteTo(period36);
		byteTo(period37);
		byteTo(period38);
		byteTo(period39);
		byteTo(period40);

		byteTo(period41);
		byteTo(period42);
		byteTo(period43);
		byteTo(period44);
		byteTo(period45);
		
		byteTo(period46);
		byteTo(period47);
		byteTo(period48);
		byteTo(startMode);
	}
	public void handle(Channel channel) throws Exception {
		String uri = AppConfig.getStringPro("sp-url");
		uri += "/direct-module=net_service=Sinexcel_method=record";
		uri += "_gsn=" + getPileCode(); // 网关编号
		uri += "_csn=" + getPileCode(); // 充电桩编码
		uri += "_gunType=" + getGunType(); // 充电枪类型
		uri += "_gunno=" + getChargeSlogan(); // 充电枪号
		uri += "_cno=" + getCardNo(); // 充电卡号
		
		String startTime = getStartTime();
		if (StrUtils.isNotNull(startTime) && startTime.length() > 14) {
			startTime = startTime.substring(0, 14);
		}
		uri += "_ste=" + startTime; // 充电开始时间
		uri += "_ete=" + getEndTime(); // 充电结束时间
		uri += "_time=" + getChargeTime(); // 充电时间长度(秒)
		
		uri += "_ern=" + getEndReason(); // 充电结束原因
		uri += "_pw=" + getPower(); // 本次充电电量
		uri += "_smp=" + getStartMeterPower(); // 充电前电表读数
		uri += "_cmpr=" + getCurrMeterPower(); // 充电后电表读数

		uri += "_money=" + getMoney(); // 本次充电金额
		uri += "_smy=" + getStartMoney(); // 充电前卡余额
		uri += "_rid=" + getRecordId(); // 当前充电记录索引（每条充电记录唯一编号）
		uri += "_rct=" + getRecordCount(); // 总充电记录条目
		
		uri += "_cme=" + getChargeMode(); // 充电策略
		uri += "_cmpm=" + getChargeModeParam(); // 充电策略参数
		uri += "_stm=" + getStartMode(); // 启动方式
		uri += "_p1=" + period1[1]; // 时段1电量
		uri += "_p2=" + period2[1]; // 时段2电量
		uri += "_p3=" + period3[1]; // 时段3电量
		uri += "_p4=" + period4[1]; // 时段4电量
		uri += "_p5=" + period5[1]; // 时段5电量
		uri += "_p6=" + period6[1]; // 时段6电量
		uri += "_p7=" + period7[1]; // 时段7电量
		uri += "_p8=" + period8[1]; // 时段8电量
		uri += "_p9=" + period9[1]; // 时段9电量
		uri += "_p10=" + period10[1]; // 时段10电量
		uri += "_p11=" + period11[1]; // 时段11电量
		uri += "_p12=" + period12[1]; // 时段12电量
		uri += "_p13=" + period13[1]; // 时段13电量
		uri += "_p14=" + period14[1]; // 时段14电量
		uri += "_p15=" + period15[1]; // 时段15电量
		uri += "_p16=" + period16[1]; // 时段16电量
		uri += "_p17=" + period17[1]; // 时段17电量
		uri += "_p18=" + period18[1]; // 时段18电量
		uri += "_p19=" + period19[1]; // 时段19电量
		uri += "_p20=" + period20[1]; // 时段20电量
		uri += "_p21=" + period21[1]; // 时段21电量
		uri += "_p22=" + period22[1]; // 时段22电量
		uri += "_p23=" + period23[1]; // 时段23电量
		uri += "_p24=" + period24[1]; // 时段24电量
		uri += "_p25=" + period25[1]; // 时段25电量
		uri += "_p26=" + period26[1]; // 时段26电量
		uri += "_p27=" + period27[1]; // 时段27电量
		uri += "_p28=" + period28[1]; // 时段28电量
		uri += "_p29=" + period29[1]; // 时段29电量
		uri += "_p30=" + period30[1]; // 时段30电量
		uri += "_p31=" + period31[1]; // 时段31电量
		uri += "_p32=" + period32[1]; // 时段32电量
		uri += "_p33=" + period33[1]; // 时段33电量
		uri += "_p34=" + period34[1]; // 时段34电量
		uri += "_p35=" + period35[1]; // 时段35电量
		uri += "_p36=" + period36[1]; // 时段36电量
		uri += "_p37=" + period37[1]; // 时段37电量
		uri += "_p38=" + period38[1]; // 时段38电量
		uri += "_p39=" + period39[1]; // 时段39电量
		uri += "_p40=" + period40[1]; // 时段40电量
		uri += "_p41=" + period41[1]; // 时段41电量
		uri += "_p42=" + period42[1]; // 时段42电量
		uri += "_p43=" + period43[1]; // 时段43电量
		uri += "_p44=" + period44[1]; // 时段44电量
		uri += "_p45=" + period45[1]; // 时段45电量
		uri += "_p46=" + period46[1]; // 时段46电量
		uri += "_p47=" + period47[1]; // 时段47电量
		uri += "_p48=" + period48[1]; // 时段48电量
		
		LogUtils.send.info("充电记录上传指令请求url：" + uri);
		
		try {
			String res = HttpManager.getData(uri, "");
			LogUtils.send.info("充电记录上传指令返回：" + res);
		} catch (Exception e) {
			LogUtils.send.info("充电记录上传指令异常，", e);
		}
		
		send(channel);
	}

	public void send(Channel channel) {
		tobyte(userid);
		tobyte(cmdseq);
		tobyte(chargeSlogan);
		asciiToByte(cardNo);
		super.send(channel);
	}
	
	public int getGunType() {
		return gunType[1];
	}
	public void setGunType(int gunType) {
		this.gunType[1] = gunType;
	}
	public int getChargeSlogan() {
		return chargeSlogan[1];
	}
	public void setChargeSlogan(int chargeSlogan) {
		this.chargeSlogan[1] = chargeSlogan;
	}
	public String getCardNo() {
		return cardNo[1];
	}
	public void setCardNo(String cardNo) {
		this.cardNo[1] = cardNo;
	}
	public String getStartTime() {
		return startTime[1];
	}
	public void setStartTime(String startTime) {
		this.startTime[1] = startTime;
	}
	public String getEndTime() {
		return endTime[1];
	}
	public void setEndTime(String endTime) {
		this.endTime[1] = endTime;
	}
	public int getChargeTime() {
		return chargeTime[1];
	}
	public void setChargeTime(int chargeTime) {
		this.chargeTime[1] = chargeTime;
	}
	public int getEndReason() {
		return endReason[1];
	}
	public void setEndReason(int endReason) {
		this.endReason[1] = endReason;
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
	public int getMoney() {
		return money[1];
	}
	public void setMoney(int money) {
		this.money[1] = money;
	}
	public int getStartMoney() {
		return startMoney[1];
	}
	public void setStartMoney(int startMoney) {
		this.startMoney[1] = startMoney;
	}
	public int getRecordId() {
		return recordId[1];
	}
	public void setRecordId(int recordId) {
		this.recordId[1] = recordId;
	}
	public int getRecordCount() {
		return recordCount[1];
	}
	public void setRecordCount(int recordCount) {
		this.recordCount[1] = recordCount;
	}
	public int getVariable2() {
		return variable2[1];
	}
	public void setVariable2(int variable2) {
		this.variable2[1] = variable2;
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
	public int getStartMode() {
		return startMode[1];
	}
	public void setStartMode(int startMode) {
		this.startMode[1] = startMode;
	}
}
