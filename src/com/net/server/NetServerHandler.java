package com.net.server;

import java.util.List;

import com.net.util.ByteUtils;
import com.net.util.LogUtils;
import com.net.util.NetUtils;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public class NetServerHandler extends ByteToMessageDecoder {

	@Override
	protected void decode(ChannelHandlerContext channelHandler, ByteBuf byteBuf, List<Object> paramList)
			throws Exception {
		int len = byteBuf.readableBytes();
		byte[] buf = new byte[len];
		byteBuf.readBytes(buf);

		Channel channel = channelHandler.channel();
		LogUtils.rece.info(channel + "接收数据:" + ByteUtils.pretty(buf));

		NetUtils.init(channel, buf);
	}

	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
	}

	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		Channel channel = ctx.channel();
		LogUtils.rece.info("客户端建立连接" + channel);
	}

	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		Channel channel = ctx.channel();
		LogUtils.rece.info("客户端断开连接" + channel);
		NetUtils.removeChannel(channel);
		super.channelInactive(ctx);
	}

	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		Channel channel = ctx.channel();
		LogUtils.rece.info("连接异常" + channel, cause);
	}

}
