package com.net.util;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class ByteUtils {

	public static String pretty(byte[] ba) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < ba.length; i++) {
			String b = Integer.toHexString(ba[i] & 0xFF);
			if (b.length() == 1)
				b = "0" + b;
			sb.append(b + " ");
		}
		return sb.toString().trim().toUpperCase();
	}

	public static byte[] toArray(ByteBuf buffers) {
		ByteBuf buf = Unpooled.copiedBuffer(buffers);
		return buf.readBytes(buf.readableBytes()).array();
	}
	
	public static byte[] toArray(ByteBuf buffers, int index) {
		ByteBuf buf = Unpooled.copiedBuffer(buffers);
		buf.readerIndex(index);
		buf.discardReadBytes();
		return buf.readBytes(buf.readableBytes()).array();
	}
}
