package com.net.bean;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import com.base.http.HttpManager;
import com.base.utils.ParaMap;
import com.base.web.AppConfig;
import com.net.util.ByteUtils;
import com.net.util.LogUtils;
import com.net.util.NetUtils;

import io.netty.channel.Channel;

/**
 * 服务器向充电桩下发升级文件数据指令 (CMD=1007)
 * @author 石马
 */
public class SetUpdateDataBean extends BaseBean {
	private int[] reciveSN = { 1, 0 }; // 已正确接收到的SN
	// 升级文件数据信息
	private String filePath = "";
	private int filePartLength = 0;
	private int filePart = 0;
	
	public void init() {
		super.init();
		byteTo(reciveSN);
	}

	public void handle(Channel channel) throws Exception {
		String uri = AppConfig.getStringPro("sp-url");
		uri += "/direct-module=net_service=Sinexcel_method=updateData";
		uri += "_reSN=" + getReciveSN(); // 已正确接收到的SN
		uri += "_csn=" + NetUtils.getDataByChannel(channel); // 充电桩编码
		
		LogUtils.send.info("下发升级文件数据指令请求url：" + uri);
		String res = HttpManager.getData(uri, "");
		LogUtils.rece.info("下发升级文件数据指令请求返回：" + res);
	}

	public void send(Channel channel) {
		setCmd(CMD.SET_UPDATE_DATA);

		ParaMap inMap = new ParaMap();
		inMap.put("filePath", getFilePath());
		inMap.put("filePartLength", getFilePartLength());
		inMap.put("filePart", getFilePart());
		tobyte(this.partFileData(inMap));
		
		channel = NetUtils.getChannelByKey(getPileCode());
		
		LogUtils.send.info("下发升级文件数据指令，" + channel + this);
		
		super.send(channel);
	}
	
	public String getFilePath() {
		return filePath;
	}
	
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	
	public int getFilePartLength() {
		return filePartLength;
	}
	
	public void setFilePartLength(int filePartLength) {
		this.filePartLength = filePartLength;
	}
	
	public int getFilePart() {
		return filePart;
	}
	
	public void setFilePart(int filePart) {
		this.filePart = filePart;
	}
	
	public int getReciveSN() {
		return reciveSN[1];
	}
	
	public void setReciveSN(int reciveSN) {
		this.reciveSN[1] = reciveSN;
	}
	
	
	// 获取源文件根据指定部分文件的字节数组
	public byte[] partFileData(ParaMap inMap) {
		String filePath = inMap.getString("filePath");
		int filePartLength = inMap.getInt("filePartLength");
		int filePart = inMap.getInt("filePart");
		byte[] array = new byte[filePartLength];
		byte[] array2 = null;
		BufferedInputStream bis = null;
		try {
			bis = new BufferedInputStream(new FileInputStream(filePath));
			int len = bis.read(array);
			int i = 1;
			while (len != -1) {
				if(filePart == i){
					array2 = new byte[len];
					System.arraycopy(array,0,array2,0,len);
//					System.out.println("目标数组长度：" + array2.length);
				}
				len = bis.read(array);
				i++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (bis != null) {
				try {
					bis.close();
				} catch (IOException e1) {
				}
			}
		}
		return array2;
	}

}
