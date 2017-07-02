package com.net.bean;

import com.base.http.HttpManager;
import com.base.web.AppConfig;
import com.net.util.LogUtils;
import com.net.util.NetUtils;

import io.netty.channel.Channel;

/**
 * 服务器向充电桩下发升级文件数据结束指令 (CMD=1009)
 * @author 石马
 */
public class SetUpdateDataEndBean extends BaseBean {
	private int[] end = { 4, 0 };
	
	public void init() {
		super.init();
		byteTo(end);
	}

	public void handle(Channel channel) throws Exception {
		String uri = AppConfig.getStringPro("sp-url");
		uri += "/direct-module=net_service=Sinexcel_method=updateDataEnd";
		uri += "_csn=" + NetUtils.getDataByChannel(channel); // 充电桩编码
		
		LogUtils.rece.info("下发升级文件数据结束指令uri：" + uri);
		String res = HttpManager.getData(uri, "");
		LogUtils.rece.info("下发升级文件数据结束指令请求返回：" + res);
	}

	public void send(Channel channel) {
		setCmd(CMD.SET_UPDATE_DATA_END);
		tobyte(end);
		channel = NetUtils.getChannelByKey(getPileCode());
		
		LogUtils.send.info("下发升级文件数据结束指令，" + channel + this);
		
		super.send(channel);
	}
	
	public int getEnd() {
		return end[1];
	}
	
	public void setEnd(int end) {
		this.end[1] = end;
	}
}
