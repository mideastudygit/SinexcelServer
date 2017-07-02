package com.net.bean;

import com.base.http.HttpManager;
import com.base.web.AppConfig;
import com.net.util.LogUtils;

import io.netty.channel.Channel;

/**
 * 心跳 102
 */
public class HeartbeatBean extends BaseBean {
	private int[] heartseq = { 2, 0 }; // 心跳序号
	private int[] heartRes = { 2, 0 }; // 心跳应答

	public void init() {
		super.init();
		byteTo(userid);
		byteTo(cmdseq);
		byteToAscii(pileCode);
		byteTo(heartseq);
	}

	public void handle(Channel channel) throws Exception {
		String uri = AppConfig.getStringPro("sp-url");
		uri += "/direct-module=net_service=Sinexcel_method=heartbeat";
		uri += "_gsn=" + getPileCode(); // 网关编号
		uri += "_csn=" + getPileCode(); // 充电桩编码
		uri += "_hsq=" + getHeartseq(); // 心跳序号
		
		String res = HttpManager.getData(uri, "");

		LogUtils.send.info("心跳信息返回：" + res);

		send(channel);
	}

	public void send(Channel channel) {
		tobyte(userid);
		tobyte(cmdseq);
		tobyte(heartRes);
		super.send(channel);
	}
	
	public int getHeartseq() {
		return heartseq[1];
	}
	public void setHeartseq(int heartseq) {
		this.heartseq[1] = heartseq;
	}
}
