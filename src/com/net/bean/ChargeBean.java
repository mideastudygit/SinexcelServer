package com.net.bean;

import com.base.http.HttpManager;
import com.base.web.AppConfig;
import com.net.util.LogUtils;

import io.netty.channel.Channel;

/**
 * 充电/预约 (CMD=7)后台服务器下发充电桩开启充电控制命令
 * 
 * @author 黄修彬
 */
public class ChargeBean extends BaseBean {
	private int[] chargeSlogan = { 1, 0 }; // 充电口号
	private int[] chargeType = { 4, 0 }; // 充电生效类型
	private int[] reserve = { 4, 0 }; // 预留
	private int[] chargeStrategy = { 4, 0 }; // 充电策略
	private int[] chargeStrategyParam = { 4, 0 }; // 充电策略参数
	private String[] startTime = { "8", "" }; // 预约/定时启动时间
	private int[] bookOvertime = { 1, 0 }; // 预约超时时间
	private String[] cardnumber = { "32", "" }; // 用户卡号/用户识别号
	private int[] offChargeFlag = { 1, 0 }; // 断网充电标志
	private int[] offElectric = { 4, 0 }; // 离线可充电电量
	private int[] result = { 4, 0 }; // 命令执行结果

	public void init() {
		super.init();
		byteTo(userid);
		byteTo(cmdseq);
		byteToAscii(pileCode);
		byteTo(chargeSlogan);
		byteTo(result);
	}

	public void handle(Channel channel) throws Exception {
		String uri = AppConfig.getStringPro("sp-url");
		uri += "/direct-module=net_service=Sinexcel_method=charge";
		uri += "_gsn=" + getPileCode(); // 网关编号
		uri += "_csn=" + getPileCode(); // 充电桩编码
		uri += "_gunno=" + getChargeSlogan(); // 充电口号
		uri += "_rs=" + getResult(); // 命令执行结果
		
		LogUtils.send.info("充电响应请求url：" + uri);
		
		try {
			String res = HttpManager.getData(uri, "");
			LogUtils.send.info("充电响应指令返回：" + res);
		} catch (Exception e) {
			LogUtils.send.info("充电响应指令异常，", e);
		}
	}

	public void send(Channel channel) {
		setCmd(CMD.CHARGE);

		tobyte(userid);
		tobyte(cmdseq);
		tobyte(chargeSlogan);
		tobyte(chargeType);
		tobyte(reserve);
		tobyte(chargeStrategy);
		tobyte(chargeStrategyParam);
		hexStringToByte(startTime);
		tobyte(bookOvertime);
		asciiToByte(cardnumber);
		tobyte(offChargeFlag);
		tobyte(offElectric);

		LogUtils.send.info("开始下发开始充电，" + channel + this);
		
		super.send(channel);
	}
	
	public int getChargeSlogan() {
		return chargeSlogan[1];
	}
	public void setChargeSlogan(int chargeSlogan) {
		this.chargeSlogan[1] = chargeSlogan;
	}
	public int getChargeType() {
		return chargeType[1];
	}
	public void setChargeType(int chargeType) {
		this.chargeType[1] = chargeType;
	}

	public int getChargeStrategy() {
		return chargeStrategy[1];
	}
	public void setChargeStrategy(int chargeStrategy) {
		this.chargeStrategy[1] = chargeStrategy;
	}
	public int getChargeStrategyParam() {
		return chargeStrategyParam[1];
	}
	public void setChargeStrategyParam(int chargeStrategyParam) {
		this.chargeStrategyParam[1] = chargeStrategyParam;
	}
	public String getStartTime() {
		return startTime[1];
	}
	public void setStartTime(String startTime) {
		this.startTime[1] = startTime;
	}
	public int getBookOvertime() {
		return bookOvertime[1];
	}
	public void setBookOvertime(int bookOvertime) {
		this.bookOvertime[1] = bookOvertime;
	}
	public String getCardnumber() {
		return cardnumber[1];
	}
	public void setCardnumber(String cardnumber) {
		this.cardnumber[1] = cardnumber;
	}
	
	public int getOffChargeFlag() {
		return offChargeFlag[1];
	}
	public void setOffChargeFlag(int offChargeFlag) {
		this.offChargeFlag[1] = offChargeFlag;
	}
	public int getOffElectric() {
		return offElectric[1];
	}
	public void setOffElectric(int offElectric) {
		this.offElectric[1] = offElectric;
	}
	public int getResult() {
		return result[1];
	}
	public void setResult(int result) {
		this.result[1] = result;
	}
}
