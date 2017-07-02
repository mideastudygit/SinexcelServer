package com.net.bean;

import java.util.HashMap;
import java.util.Map;

/**
 * 命令常量
 */
public class CMD {
	
	// 签到
	public static final int LOGIN = 106;

	// 状态信息
	public static final int PILE_STATUS = 104;

	// 心跳
	public static final int HEARTBEAT = 102;
	
	// 充电桩告警信息
	public static final int ALARM_REPOT = 108;

	// 充电记录数据
	public static final int RECORD = 202;

	// 开始充电
	public static final int CHARGE = 8;

	// 结束充电
	public static final int END_CHARGE = 6;
	
	// 服务器下发充电桩整形工作参数
	public static final int SET_INT_CONFIGURE = 2;

	// 服务器下发充电桩字符形工作参数
	public static final int SET_STRING_CONFIGURE = 4;
	
	// 服务器下发擦除/查询软件命令 
	public static final int CLEAR_SOFT = 0x03EA;
	
	// 服务器下发升级文件名指令 
	public static final int SET_UPDATE_NAME = 0x03EC;
	
	// 服务器下发升级文件大小指令 
	public static final int SET_UPDATE_LENGTH = 0x03EE;

	// 服务器下发升级文件大小指令 
	public static final int SET_UPDATE_DATA = 0x03F0;

	// 服务器下发升级文件数据结束指令 
	public static final int SET_UPDATE_DATA_END = 0x03F2;

	// 服务器下发重启指令 
	public static final int SET_UPDATE_RESTART = 0x03F4;
	
	
	public static final Map<Integer, String> MAP = new HashMap<Integer, String>();

	static {
		MAP.put(LOGIN, "登录");
		MAP.put(PILE_STATUS, "电桩状态");
		MAP.put(HEARTBEAT, "心跳");
		MAP.put(ALARM_REPOT, "充电桩告警信息");
		MAP.put(RECORD, "充电记录数据");
		MAP.put(CHARGE, "充电电桩响应");
		MAP.put(END_CHARGE, "结束充电电桩响应");
		MAP.put(SET_INT_CONFIGURE, "下发整型工作参数响应");
		MAP.put(SET_STRING_CONFIGURE, "下发字符型工作参数响应");
		MAP.put(CLEAR_SOFT, "下发擦除/查询软件命令响应");
		MAP.put(SET_UPDATE_NAME, "下发升级文件名指令响应");
		MAP.put(SET_UPDATE_LENGTH, "下发升级文件大小指令响应");
		MAP.put(SET_UPDATE_DATA, "下发升级文件数据指令响应");
		MAP.put(SET_UPDATE_DATA_END, "下发升级文件数据指令响应");
		MAP.put(SET_UPDATE_RESTART, "下发重启指令响应");
		
		MAP.put(105, "登录响应");
		MAP.put(103, "电桩状态响应");
		MAP.put(101, "心跳响应");
		MAP.put(107, "充电桩告警响应");
		MAP.put(201, "充电记录响应");
		MAP.put(7, "发送开始充电");
		MAP.put(5, "发送结束充电");
		MAP.put(1, "下发整型工作参数");
		MAP.put(3, "下发字符型工作参数");
		MAP.put(0x03E9, "下发擦除/查询软件命令");
		MAP.put(0x03EB, "下发升级文件名指令");
		MAP.put(0x03ED, "下发升级文件大小指令");
		MAP.put(0x03EF, "下发升级文件数据指令");
		MAP.put(0x03F1, "下发升级文件数据结束指令");
		MAP.put(0x03F3, "下发重启指令");
	}
}
