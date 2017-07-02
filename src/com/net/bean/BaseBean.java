package com.net.bean;

import java.lang.reflect.Field;

import com.base.web.AppConfig;
import com.net.util.ByteUtils;
import com.net.util.NetUtils;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufProcessor;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;

/**
 * 基本Bean对象，主要有包头等信息
 */
public class BaseBean {

	// 每个字节存放长度
	public static final int BYTE_LENGTH = 8;

	// 起始标记，下标0：代表存放字节长度，下标1：代表具体存在的值，其他字段类似
	protected int[] startMarker = { 1, 0xAA }; // 起始域，第一个字节

	protected int[] endMarkder = { 1, 0xF5 }; // 起始域，第二个字节

	// 数据长度
	protected int[] dataLength = { 2, 0 };

	// 协议版本
	protected int[] protocolVersion = { 1, 0x10 };

	// 命令序列
	protected int[] seq = { 1, 0 };

	// 命令指令
	protected int[] cmd = { 2, 0 };

	// 校验码
	protected int[] crc = { 1, 0 };

	// 当前游标
	private int cursor = 0;

	// 接收的二进制数据
	private byte[] bytes;

	// 发送数据
	private ByteBuf sendBytes = Unpooled.buffer(0);
	
	// 常用业务字段
	protected int[] userid = { 2, 8 }; // 用户id
	protected int[] cmdseq = { 2, 0 }; // 指令序号
	protected String[] pileCode = { "32", "" }; // 电桩编码

	public boolean load(ByteBuf buf) {
		// 获取起始帧
		int startIndex = buf.forEachByte(new ByteBufProcessor() {
			boolean tag = false;

			public boolean process(byte value) throws Exception {
				if (value == (byte) getStartMarker()) {
					tag = true;
				} else if (tag && value == (byte) getEndMarker()) {
					return false;
				}
				return true;
			}
		});

		if (startIndex < 0) {
			buf.clear();
			return false;
		}
		
		// 判断长度
		if (buf.readableBytes() < getHeadLen()) {
			buf.readerIndex(startIndex - 1);
			buf.discardReadBytes();
			return false;
		}

		// 获取数据区长度
		cursor = 0;
		bytes = buf.copy(startIndex + 1, dataLength[0]).array();
		byteTo(dataLength);

		// 判断可读帧是否完整
		if (getDataLength() > buf.readableBytes()) {
			buf.readerIndex(startIndex - 1);
			buf.discardReadBytes();
			return false;
		}

		// 获取crc
		cursor = 0;
		bytes = buf.copy(startIndex - 1 + getDataLength() - crc[0], crc[0]).array();
		byteTo(crc);

		// crc验证
		byte[] data = buf.copy(startIndex + 1 + dataLength[0] + protocolVersion[0] + seq[0],
				getDataLength() - getHeadLen() + cmd[0]).array();
		byte crcing = crcAccum(data);
		if ((byte)getCrc() != crcing) {
			buf.readerIndex(startIndex + 1);
			buf.discardReadBytes();
			// 如果一次上传多个完整的错误包，也需要循环调用
			load(buf);
			return false;
		}

		bytes = buf.copy(startIndex - 1, getDataLength()).array();
		buf.readerIndex(getDataLength());
		buf.discardReadBytes();
		return true;
	}

	/**
	 * 初始化
	 */
	public void init() {
		cursor = 0;
		byteTo(startMarker);
		byteTo(endMarkder);
		byteTo(dataLength);
		byteTo(protocolVersion);
		byteTo(seq);
		byteTo(cmd);
	}

	/**
	 * 业务处理
	 */
	public void handle(Channel channel) throws Exception {
	}

	/**
	 * 响应
	 */
	public void send(Channel channel) {
		byte[] dataBytes = ByteUtils.toArray(sendBytes);

		int dataLen = dataBytes.length;
		setDataLength(dataLen + getHeadLen());

		sendBytes.clear();
		tobyte(startMarker);
		tobyte(endMarkder);
		tobyte(dataLength);
		tobyte(protocolVersion);
		tobyte(seq);

		//
		setCmd(getCmd() - 1);
		tobyte(cmd);

		sendBytes.writeBytes(dataBytes);
		
		byte[] crcBytes = ByteUtils.toArray(sendBytes, 6);
		setCrc(crcAccum(crcBytes));
		
		tobyte(crc);

		NetUtils.send(this, channel);
		
		sendBytes.clear();
	}

	public static byte crcAccum(byte[] datas) {
		int crc = 0;
		for (int i = 0; i < datas.length; i++) {
			crc += datas[i];
		}
		return (byte)(crc >>> 0);
	}

	public void byteTo(int[] obj) {
		int len = obj[0];
		int value = 0;
		for (int i = 0; i < len; i++) {
			int shift = i * BYTE_LENGTH;
			value += (bytes[cursor + i] & 0x000000FF) << shift;
		}
		obj[1] = value;
		cursor += len;
	}

	public void byteTo(long[] obj) {
		int len = (int) obj[0];
		long value = 0;
		for (int i = 0; i < len; i++) {
			int shift = i * BYTE_LENGTH;
			value += (bytes[cursor + i] & 0x000000FF) << shift;
		}
		obj[1] = value;
		cursor += len;
	}

	public void byteToIP(String[] obj) {
		int len = Integer.parseInt(obj[0]);
		StringBuffer buff = new StringBuffer();
		for (int i = 0; i < len; i++) {
			if (i > 0) {
				buff.append(".");
			}
			byte b = bytes[cursor + i];
			String value = Integer.toString(b, 10);
			buff.append(value);
		}
		String value = buff.toString();
		obj[1] = value;
		cursor += len;
	}

	public void byteToHexString(String[] obj) {
		int len = Integer.parseInt(obj[0]);
		StringBuffer buff = new StringBuffer();
		for (int i = 0; i < len; i++) {
			byte b = bytes[cursor + i];
			if (b == (byte) 0xff) {
				continue;
			}

			String value = Integer.toHexString(b);
			if (value.length() == 1) {
				value = "0" + value;
			}
			buff.append(value);
		}
		String value = buff.toString();
		while (value.startsWith("0")) {
			value = value.substring(1);
		}
		obj[1] = value;
		cursor += len;
	}

	public void byteToAscii(String[] obj) {
		int len = Integer.parseInt(obj[0]);
		StringBuffer buff = new StringBuffer();
		for (int i = 0; i < len; i++) {
			byte b = bytes[cursor + i];
			if (b != 0) {
				char c = (char) b;
				buff.append(c);
			}
		}
		obj[1] = buff.toString();
		cursor += len;
	}

	public void tobyte(int[] obj) {
		int len = obj[0];
		int data = obj[1];

		byte[] buf = new byte[len];
		for (int i = 0; i < len; i++) {
			int shift = i * BYTE_LENGTH;
			buf[i] = (byte) (data >>> shift);
		}

		sendBytes.writeBytes(buf);
	}

	public void tobyte(byte[] obj) {
		sendBytes.writeBytes(obj);
	}
	
	public void toByte(long[] obj) {
		int len = (int) obj[0];
		long data = obj[1];

		byte[] buf = new byte[len];
		for (int i = 0; i < len; i++) {
			int shift = i * BYTE_LENGTH;
			buf[i] = (byte) (data >>> shift);
		}

		sendBytes.writeBytes(buf);
	}

	public void ipToByte(String[] obj) {
		int len = Integer.parseInt(obj[0]);
		String[] data = obj[1].split("\\.");

		byte[] buf = new byte[len];
		for (int i = 0; i < buf.length; i++) {
			buf[i] = (byte) Integer.parseInt(data[i]);
		}

		sendBytes.writeBytes(buf);
	}

	public void asciiToByte(String[] obj) {
		int len = Integer.parseInt(obj[0]);
		int datalen = obj[1].length();

		byte[] buf = new byte[len];
		int index = 0;
		
		for (int i = 0; i < datalen; i++) {
			char c = obj[1].charAt(i);
			buf[index] = (byte) c;
			index++;
		}
		
		for (int i = 0; i < len - datalen; i++) {
			buf[index] = (byte)'\0';
			index++;
		}
		
		sendBytes.writeBytes(buf);
	}

	public void hexStringToByte(String[] obj) {
		int len = Integer.parseInt(obj[0]);
		String value = obj[1];
		int dataLen = value.length() / 2;

		byte[] buf = new byte[len];
		for (int i = 0; i < dataLen; i++) {
			String v = value.substring(i * 2, i * 2 + 2);
			buf[i] = (byte) Integer.parseInt(v, 16);
		}

		for (int i = 0; i < len - dataLen; i++) {
			buf[dataLen + i] = (byte) 0xff;
		}

		sendBytes.writeBytes(buf);
	}

	public String getURI() {
		String uri = AppConfig.getStringPro("sp-url");
		return uri;
	}

	public int getStartMarker() {
		return startMarker[1];
	}

	public int getEndMarker() {
		return endMarkder[1];
	}

	public int getProtocolVersion() {
		return protocolVersion[1];
	}

	public void setProtocolVersion(int protocolVersion) {
		this.protocolVersion[1] = protocolVersion;
	}

	public int getDataLength() {
		return dataLength[1];
	}

	public void setDataLength(int dataLength) {
		this.dataLength[1] = dataLength;
	}

	public int getSeq() {
		return seq[1];
	}

	public void setSeq(int seq) {
		this.seq[1] = seq;
	}

	public int getCmd() {
		return cmd[1];
	}

	public void setCmd(int cmd) {
		this.cmd[1] = cmd;
	}

	public int getCrc() {
		return crc[1];
	}

	public void setCrc(int crc) {
		this.crc[1] = crc;
	}

	public int getCursor() {
		return cursor;
	}

	public void setCursor(int cursor) {
		this.cursor = cursor;
	}

	public byte[] getBytes() {
		return bytes;
	}

	public void setBytes(byte[] bytes) {
		this.bytes = bytes;
	}

	public ByteBuf getSendBytes() {
		return sendBytes;
	}

	public int getHeadLen() {
		return startMarker[0] + endMarkder[0] + dataLength[0] + protocolVersion[0] + seq[0] + cmd[0] + crc[0];
	}
	
	public int getUserid() {
		return userid[1];
	}

	public void setUserid(int userid) {
		this.userid[1] = userid;
	}
	
	public int getCmdseq() {
		return cmdseq[1];
	}

	public void setCmdseq(int cmdseq) {
		this.cmdseq[1] = cmdseq;
	}
	
	public String getPileCode() {
		return pileCode[1];
	}

	public void setPileCode(String pileCode) {
		this.pileCode[1] = pileCode;
	}

	@Override
	public String toString() {
		String name = this.getClass().getSimpleName();
		StringBuffer buffer = new StringBuffer("(" + name + ")" + "{");
		try {
			buffer.append(getFieldValue(this.getClass().getSuperclass()));
			buffer.append(",");
			buffer.append(getFieldValue(this.getClass()));
		} catch (Exception e) {
		}
		buffer.append("}");
		return buffer.toString();
	}

	private String getFieldValue(Class clazz) throws Exception {
		StringBuffer buffer = new StringBuffer();
		Field[] fields = clazz.getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			boolean accessFlag = fields[i].isAccessible();
			if (!accessFlag) {
				fields[i].setAccessible(true);
			}
			Object o = fields[i].get(this);
			fields[i].setAccessible(accessFlag);

			Object value = null;
			if (o instanceof int[]) {
				int[] io = (int[]) o;
				value = io[1];
			} else if (o instanceof String[]) {
				String[] so = (String[]) o;
				value = "\"" + so[1] + "\"";
			}

			if (buffer.length() > 0 && value != null) {
				buffer.append(",");
			}

			if (value != null) {
				buffer.append("\"" + fields[i].getName() + "\"" + ":" + value);
			}
		}
		return buffer.toString();
	}
}
