package com.net.bean;

import com.base.http.HttpManager;
import com.base.web.AppConfig;
import com.net.util.LogUtils;
import com.net.util.NetUtils;

import io.netty.channel.Channel;

/**
 * 服务器向充电桩下发升级文件名指令 (CMD=1003)
 * @author 石马
 */
public class SetUpdateNameBean extends BaseBean {
	private String[] fileName = { "127", "" }; // 文件名
	private int[] updatePartLength = { 4, 0 }; // 允许服务发送升级数据报文数据长度
	
	public void init() {
		super.init();
		byteTo(updatePartLength);
	}

	public void handle(Channel channel) throws Exception {
		String uri = AppConfig.getStringPro("sp-url");
		uri += "/direct-module=net_service=Sinexcel_method=updateName";
		uri += "_length=" + getUpdatePartLength(); // 升级数据长度
		uri += "_csn=" + NetUtils.getDataByChannel(channel); // 充电桩编码
		
		LogUtils.rece.info("下发升级文件名指令请求url：" + uri);
		String res = HttpManager.getData(uri, "");
		LogUtils.rece.info("下发升级文件名指令请求返回：" + res);
	}

	public void send(Channel channel) {
		setCmd(CMD.SET_UPDATE_NAME);
		asciiToByte(fileName);
		channel = NetUtils.getChannelByKey(getPileCode());
		
		LogUtils.send.info("下发升级文件名指令，" + channel + this);

		super.send(channel);
	}
	
	public int getUpdatePartLength() {
		return updatePartLength[1];
	}
	
	public void setUpdatePartLength(int updatePartLength) {
		this.updatePartLength[1] = updatePartLength;
	}
	
	public String getFileName() {
		return fileName[1];
	}
	public void setFileName(String fileName) {
		this.fileName[1] = fileName;
	}
	public void setFileNameLength(String fileName) {
		this.fileName[0] = fileName.length() + "";
	}
}
