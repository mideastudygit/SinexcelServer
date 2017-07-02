package com.net.util;

import java.util.concurrent.ConcurrentHashMap;

import com.net.bean.AlarmReportBean;
import com.net.bean.BaseBean;
import com.net.bean.CMD;
import com.net.bean.ChargeBean;
import com.net.bean.RecordBean;
import com.net.bean.EndChargeBean;
import com.net.bean.HeartbeatBean;
import com.net.bean.LoginBean;
import com.net.bean.PileStatusBean;
import com.net.bean.SetIntConfigureBean;
import com.net.bean.SetStringConfigureBean;
import com.net.bean.SetUpdateClearSoftBean;
import com.net.bean.SetUpdateDataBean;
import com.net.bean.SetUpdateDataEndBean;
import com.net.bean.SetUpdateLengthBean;
import com.net.bean.SetUpdateNameBean;
import com.net.bean.SetUpdateRestartBean;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;

/**
 * 处理类
 * 
 * @author 黄修彬
 */
public class NetUtils {
	private static ConcurrentHashMap<String, Channel> keyChannelMap = new ConcurrentHashMap<String, Channel>();
	private static ConcurrentHashMap<Channel, String> channelKeyMap = new ConcurrentHashMap<Channel, String>();
	// 存放渠道的数据，用于不断获取完整帧
	private static ConcurrentHashMap<Channel, ByteBuf> bufMap = new ConcurrentHashMap<Channel, ByteBuf>();

	/**
	 * 初始化
	 * 
	 * @param ch
	 * @param buf
	 */
	public static void init(Channel channel, byte[] buf) throws Exception {
		ByteBuf pool = bufMap.get(channel);
		if (pool == null) {
			pool = Unpooled.buffer(0);
			bufMap.put(channel, pool);
		}
		pool.writeBytes(buf);

		BaseBean bean = new BaseBean();
		while (bean.load(pool)) {
			byte[] data = bean.getBytes();
			bean.init();
			int cmd = bean.getCmd();
			bean = getBean(cmd, data);

			StringBuffer log = new StringBuffer();
			log.append("【" + CMD.MAP.get(cmd) + "】");
			log.append("解密数据：" + ByteUtils.pretty(bean.getBytes()));
			log.append("对象数据：" + bean);
			log.append("\n");
			LogUtils.rece.info(log);

			if (bean instanceof LoginBean) {
				setChannel(bean, channel);
			}

			bean.handle(channel);
		}
	}

	public static BaseBean getBean(int cmd, byte[] data) {
		BaseBean bean = getBean(cmd);
		bean.setBytes(data);
		bean.init();
		bean.setCursor(0);
		return bean;
	}
	
	public static BaseBean getBean(int cmd) {
		BaseBean bean = new BaseBean();
		switch (cmd) {
		case CMD.LOGIN:
			bean = new LoginBean();
			break;
		case CMD.PILE_STATUS:
			bean = new PileStatusBean();
			break;
		case CMD.HEARTBEAT:
			bean = new HeartbeatBean();
			break;
		case CMD.ALARM_REPOT:
			bean = new AlarmReportBean();
			break;
		case CMD.RECORD:
			bean = new RecordBean();
			break;
		case CMD.CHARGE:
			bean = new ChargeBean();
			break;
		case CMD.END_CHARGE:
			bean = new EndChargeBean();
			break;
		case CMD.SET_INT_CONFIGURE:
			bean = new SetIntConfigureBean();
			break;
		case CMD.SET_STRING_CONFIGURE:
			bean = new SetStringConfigureBean();
			break;
		case CMD.CLEAR_SOFT:
			bean = new SetUpdateClearSoftBean();
			break;
		case CMD.SET_UPDATE_NAME:
			bean = new SetUpdateNameBean();
			break;
		case CMD.SET_UPDATE_LENGTH:
			bean = new SetUpdateLengthBean();
			break;
		case CMD.SET_UPDATE_DATA:
			bean = new SetUpdateDataBean();
			break;
		case CMD.SET_UPDATE_DATA_END:
			bean = new SetUpdateDataEndBean();
			break;
		case CMD.SET_UPDATE_RESTART:
			bean = new SetUpdateRestartBean();
			break;
		}
		return bean;
	}

	public static void setChannel(BaseBean bean, Channel channel) {
		LogUtils.rece.info(channel + "通道设置，" + bean);
		
		String pileCode = bean.getPileCode();
		channelKeyMap.put(channel, pileCode);
		keyChannelMap.put(pileCode, channel);
	}
	
	// 根据电桩通道号获取电桩编码
	public static String getDataByChannel(Channel channel) {
		String pileCode = channelKeyMap.get(channel);
		return pileCode;
	}

	public static void removeChannel(Channel channel) {
		if(channel.isActive()) {
			LogUtils.rece.info(channel + "通道连接中，不能清除");
			return;
		}
		
		LogUtils.rece.info(channel + "通道清除");
		
		ByteBuf pool = bufMap.get(channel);
		if (pool != null) {
			pool.release();
		}

		bufMap.remove(channel);
		channelKeyMap.remove(channel);
	}

	public static Channel getChannelByKey(String key) {
		return keyChannelMap.get(key);
	}

	/**
	 * 发送
	 */
	public static void send(BaseBean bean, Channel channel) {
		if (channel != null) {
			byte[] bytes = ByteUtils.toArray(bean.getSendBytes());

			StringBuffer log = new StringBuffer();
			log.append("【" + CMD.MAP.get(bean.getCmd()) + "】" + channel + ":");
			log.append("电桩SN：" + bean.getPileCode());
			log.append("发送数据：" + ByteUtils.pretty(bytes));
			log.append("\n");
			LogUtils.send.info(log);
			
			channel.writeAndFlush(Unpooled.copiedBuffer(bytes));
		} else {
			LogUtils.send.info("没有找到通道");
		}
	}
}
