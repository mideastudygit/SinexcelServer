package com.net.bean;

import io.netty.channel.Channel;

/**
 * 设备初始化
 * (CMD=110)充电桩状态信息包上报（省流量模式）
 */
public class DeviceLessInitBean extends BaseBean {
	private int[] chargeSlogan = { 1, 0 }; // 充电口号
	private int[] workStatus = { 1, 0 }; // 工作状态  0‐空闲中1‐正准备开始充电2‐充电进行中3‐充电结束4‐启动失败5‐预约状态6‐系统故障(不能给汽车充电)
	private int[] alarmStatus = { 4, 0 }; // 告警状态  0‐无告警

	public void init() {
		super.init();
		byteTo(chargeSlogan);
		byteTo(workStatus);
		byteTo(alarmStatus);
	}

	public void handle(Channel channel) throws Exception {
		// String uri = getURI();
		// uri += "__cmdid_" + getCmdId();
		// uri += "__gsn_" + getGatewayAddress();
		// uri += "__csn_" + getChargerAddress();
		// uri += "__ts_" + getTimestamp();
		// uri += "__user_" + getUser();
		// uri += "__pwd_" + getPassword();
		// String res = HttpManager.getData(uri, "");

		send(channel);
	}

	public void send(Channel channel) {
		byteTo(chargeSlogan);
		super.send(channel);
	}

}
