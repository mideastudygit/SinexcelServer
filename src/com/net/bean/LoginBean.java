package com.net.bean;

import com.base.http.HttpManager;
import com.base.web.AppConfig;
import com.net.util.LogUtils;

import io.netty.channel.Channel;

/**
 * 登录
 * (CMD=106)充电桩签到信息上报
 */
public class LoginBean extends BaseBean {

	private int[] pileType = { 1, 0 }; // 电桩类型  0：普通类型1：自动功率分配2.：轮巡
	private int[] pileVersion = { 4, 0 }; // 充电桩软件版本

	private int[] pileItemType = { 2, 0 }; // 充电桩项目类型
	private int[] startCount = { 4, 0 }; // 启动次数
	private int[] uploadMode = { 1, 0 }; // 数据上传模式  1：应答模式2：主动上报模式
	private int[] intervalTime = { 2, 0 }; // 签到间隔时间
	private int[] runVariable = { 1, 0 }; // 运行内部变量  0‐ 正常模式 1‐ IAP 模式

	private int[] gunCount = { 1, 0 }; // 充电枪个数
	private int[] uploadPeriod = { 1, 0 }; // 心跳上报周期
	private int[] heartTimeoutCount = { 1, 0 }; // 心跳包检测超时次数
	private int[] recordCount = { 4, 0 }; // 充电记录数量

	private String[] currSysTime = { "8", "" }; // 当前充电桩系统时间
	private String[] lastChargeTime = { "8", "" }; // 最近一次充电时间
	private String[] lastStartTime = { "8", "" }; // 最近一次启动时间
	private String[] password = { "8", "" }; // 签到密码

	public void init() {
		super.init();
		byteTo(userid);
		byteTo(cmdseq);
		byteToAscii(pileCode);
		byteTo(pileType);
		byteTo(pileVersion);

		byteTo(pileItemType);
		byteTo(startCount);
		byteTo(uploadMode);
		byteTo(intervalTime);
		byteTo(runVariable);

		byteTo(gunCount);
		byteTo(uploadPeriod);
		byteTo(heartTimeoutCount);
		byteTo(recordCount);

		byteToHexString(currSysTime);
		byteToHexString(lastChargeTime);
		byteToHexString(lastStartTime);
		byteToAscii(password);
	}

	public void handle(Channel channel) throws Exception {
		String uri = AppConfig.getStringPro("sp-url");
		uri += "/direct-module=net_service=Sinexcel_method=login";
		uri += "_gsn=" + getPileCode(); // 网关编号
		uri += "_csn=" + getPileCode(); // 充电桩编码
		uri += "_soft=" + getPileVersion(); // 充电桩软件版本
		uri += "_time=" + getIntervalTime(); // 签到间隔时间
		uri += "_runVar=" + getRunVariable(); // 运行内部变量
		uri += "_gunCount=" + getGunCount(); // 充电枪数量
		uri += "_uperiod=" + getUploadPeriod(); // 心跳上报周期
		uri += "_htoc=" + getHeartTimeoutCount(); // 心跳包检测超时次数
		uri += "_reCount=" + getRecordCount(); // 充电记录数量
		uri += "_ts=" + getCurrSysTime(); // 当前充电桩系统时间
		
		LogUtils.send.info("登录请求url：" + uri);
		
		try {
			String res = HttpManager.getData(uri, "");
			LogUtils.send.info("登录请求返回：" + res);
		} catch (Exception e) {
			LogUtils.send.info("登录请求异常，", e);
		}
		
		send(channel);
	}

	public void send(Channel channel) {
		tobyte(userid);
		tobyte(cmdseq);
		super.send(channel);
	}
	
	public int getPileVersion() {
		return pileVersion[1];
	}
	public void setPileVersion(int pileVersion) {
		this.pileVersion[1] = pileVersion;
	}
	
	public int getIntervalTime() {
		return intervalTime[1];
	}
	
	public void setIntervalTime(int intervalTime) {
		this.intervalTime[1] = intervalTime;
	}
	
	public int getRunVariable() {
		return runVariable[1];
	}
	
	public void setRunVariable(int runVariable) {
		this.runVariable[1] = runVariable;
	}
	
	public int getGunCount() {
		return gunCount[1];
	}
	public void setGunCount(int gunCount) {
		this.gunCount[1] = gunCount;
	}
	
	public int getUploadPeriod() {
		return uploadPeriod[1];
	}
	
	public void setUploadPeriod(int uploadPeriod) {
		this.uploadPeriod[1] = uploadPeriod;
	}
	
	public int getHeartTimeoutCount() {
		return heartTimeoutCount[1];
	}
	
	public void setHeartTimeoutCount(int heartTimeoutCount) {
		this.heartTimeoutCount[1] = heartTimeoutCount;
	}
	
	public int getRecordCount() {
		return recordCount[1];
	}
	
	public void setRecordCount(int recordCount) {
		this.recordCount[1] = recordCount;
	}
	
	public String getCurrSysTime() {
		return currSysTime[1];
	}
	public void setCurrSysTime(String currSysTime) {
		this.currSysTime[1] = currSysTime;
	}
	public String getPassword() {
		return password[1];
	}
	public void setPassword(String password) {
		this.password[1] = password;
	}
}
