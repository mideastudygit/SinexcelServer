package com.net.server;

import com.net.util.LogUtils;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class NetServerBootstrap extends Thread {
	private int port;

	public NetServerBootstrap(int port) {
		this.port = port;
	}

	@Override
	public void run() {
		// 接收客户端连接用
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		// 处理网络读写事件
		EventLoopGroup workerGroup = new NioEventLoopGroup();

		try {
			// 配置服务器启动类
			ServerBootstrap bootstrap = new ServerBootstrap();
			bootstrap.group(bossGroup, workerGroup);

			bootstrap.channel(NioServerSocketChannel.class);
			bootstrap.handler(new LoggingHandler(LogLevel.INFO));// 配置日志输出
			bootstrap.childHandler(new NetServerInitializer());

			bootstrap.option(ChannelOption.SO_BACKLOG, 128);
			bootstrap.option(ChannelOption.TCP_NODELAY, true);
			bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);

			ChannelFuture future = bootstrap.bind(port).sync();

			LogUtils.all.info("TCP服务器已经启动..." + port);

			future.channel().closeFuture().sync();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			workerGroup.shutdownGracefully();
			bossGroup.shutdownGracefully();
		}
	}

}
