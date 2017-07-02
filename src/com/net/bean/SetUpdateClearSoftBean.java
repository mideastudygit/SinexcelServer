package com.net.bean;

import com.base.http.HttpManager;
import com.base.web.AppConfig;
import com.net.util.LogUtils;
import com.net.util.NetUtils;

import io.netty.channel.Channel;

/**
 * 服务器向充电桩下发擦除/查询软件命令 (CMD=1001)
 * @author 石马
 */
public class SetUpdateClearSoftBean extends BaseBean {
	private int[] clearOrQuery = { 4, 0 }; // 擦除（0xaa55有效）、查询（0x0000有效）
	private int clearCompletedRate[] = { 1, 0 }; // 擦除完成百分比
	
	public void init() {
		super.init();
		byteTo(clearCompletedRate);
	}

	public void handle(Channel channel) throws Exception {
		String uri = AppConfig.getStringPro("sp-url");
		uri += "/direct-module=net_service=Sinexcel_method=clearSoft";
		uri += "_clearRate=" + getClearCompletedRate(); // 擦除完成百分比
		uri += "_csn=" + NetUtils.getDataByChannel(channel); // 充电桩编码
		
		LogUtils.send.info("擦除/查询指令请求url：" + uri);
		String res = HttpManager.getData(uri, "");
		LogUtils.rece.info("擦除/查询指令请求返回：" + res);
	}

	public void send(Channel channel) {
		setCmd(CMD.CLEAR_SOFT);
		tobyte(clearOrQuery);
		channel = NetUtils.getChannelByKey(getPileCode());
		
		LogUtils.send.info("下发擦除/查询指令，" + channel + this);

		super.send(channel);
	}
	
	public int getClearOrQuery() {
		return clearOrQuery[1];
	}
	
	public void setClearOrQuery(int clearOrQuery) {
		this.clearOrQuery[1] = clearOrQuery;
	}
	
	public int getClearCompletedRate() {
		return clearCompletedRate[1];
	}
	
	public void setClearCompletedRate(int clearCompletedRate) {
		this.clearCompletedRate[1] = clearCompletedRate;
	}
}
