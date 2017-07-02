package com.net.bean;

import com.base.http.HttpManager;
import com.base.web.AppConfig;
import com.net.util.LogUtils;
import com.net.util.NetUtils;

import io.netty.channel.Channel;
import io.netty.util.NetUtil;

/**
 * 服务器向充电桩下发升级文件大小指令 (CMD=1005)
 * @author 石马
 */
public class SetUpdateLengthBean extends BaseBean {
	private int[] responseFlag = { 4, 9 }; // 响应标志
	private int[] fileDataLength = { 4, 0 }; // 下发升级文件长度
	
	public void init() {
		super.init();
		byteTo(responseFlag);
	}

	public void handle(Channel channel) throws Exception {
		String uri = AppConfig.getStringPro("sp-url");
		uri += "/direct-module=net_service=Sinexcel_method=updateLength";
		uri += "_resFlag=" + getResponseFlag(); // 响应标志
		uri += "_csn=" + NetUtils.getDataByChannel(channel); // 电桩编码
		LogUtils.rece.info("下发升级文件大小指令请求url：" + uri);
		String res = HttpManager.getData(uri, "");
		LogUtils.rece.info("下发升级文件大小指令请求返回：" + res);
	}

	public void send(Channel channel) {
		setCmd(CMD.SET_UPDATE_LENGTH);
		tobyte(fileDataLength);
		channel = NetUtils.getChannelByKey(getPileCode());
		
		LogUtils.send.info("下发升级文件名指令，" + channel + this);
		
		super.send(channel);
	}
	
	public int getFileDataLength() {
		return fileDataLength[1];
	}
	
	public void setFileDataLength(int fileDataLength) {
		this.fileDataLength[1] = fileDataLength;
	}
	
	public int getResponseFlag() {
		return responseFlag[1];
	}
	
	public void setResponseFlag(int responseFlag) {
		this.responseFlag[1] = responseFlag;
	}
	
	public String getPileCode() {
		return pileCode[1];
	}

	public void setPileCode(String pileCode) {
		this.pileCode[1] = pileCode;
	}
}
