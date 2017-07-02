package com.net.bean;

import com.base.http.HttpManager;
import com.base.web.AppConfig;
import com.net.util.LogUtils;

import io.netty.channel.Channel;

/**
 * 充电桩告警信息上报 108
 */
public class AlarmReportBean extends BaseBean {
	private String[] alarmInformation = { "32", "" }; // 告警位信息

	public void init() {
		super.init();
		byteTo(userid);
		byteTo(cmdseq);
		byteToAscii(pileCode);
		byteToAscii(alarmInformation);
	}

	public void handle(Channel channel) throws Exception {
		String uri = AppConfig.getStringPro("sp-url");
		uri += "/direct-module=net_service=Sinexcel_method=alarmReport";
		uri += "_gsn=" + getPileCode(); // 网关编号
		uri += "_csn=" + getPileCode(); // 充电桩编码
		uri += "_alarmInfo=" + getAlarmInformation(); // 告警位信息
		
		LogUtils.send.info("充电桩告警信息请求url：" + uri);
		
		String res = HttpManager.getData(uri, "");

		LogUtils.send.info("充电桩告警信息返回：" + res);
	}

	public void send(Channel channel) {
		super.send(channel);
	}
	
	public String getAlarmInformation() {
		return alarmInformation[1];
	}
	public void setAlarmInformation(String alarmInformation) {
		this.alarmInformation[1] = alarmInformation;
	}
}
