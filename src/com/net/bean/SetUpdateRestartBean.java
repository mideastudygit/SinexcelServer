package com.net.bean;

import com.base.http.HttpManager;
import com.base.web.AppConfig;
import com.net.util.LogUtils;
import com.net.util.NetUtils;

import io.netty.channel.Channel;

/**
 * 服务器向充电桩下发重启指令 (CMD=1011)
 * @author 石马
 */
public class SetUpdateRestartBean extends BaseBean {
	private int[] restart = { 4, 0 };
	
	public void init() {
		super.init();
		byteTo(restart);
	}

	public void handle(Channel channel) throws Exception {
		String uri = AppConfig.getStringPro("sp-url");
		uri += "/direct-module=net_service=Sinexcel_method=updateRestart";
		uri += "_csn=" + NetUtils.getDataByChannel(channel); // 充电桩编码
		
		LogUtils.rece.info("下发重启指令请求url：" + uri);
		String res = HttpManager.getData(uri, "");
		LogUtils.rece.info("下发重启指令请求返回：" + res);
	}

	public void send(Channel channel) {
		setCmd(CMD.SET_UPDATE_RESTART);
		tobyte(restart);
		channel = NetUtils.getChannelByKey(getPileCode());
		
		LogUtils.send.info("下发重启指令，" + channel + this);
		
		super.send(channel);
	}
	
	public int getRestart() {
		return restart[1];
	}
	
	public void setRestart(int restart) {
		this.restart[1] = restart;
	}
}
