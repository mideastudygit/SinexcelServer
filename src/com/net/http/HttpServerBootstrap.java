package com.net.http;

import com.net.util.LogUtils;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class HttpServerBootstrap extends Thread {
	private int port;

	public HttpServerBootstrap(int port) {
		this.port = port;
	}

	public void run() {
		// 接收客户端连接用
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		// 处理网络读写事件
		EventLoopGroup workerGroup = new NioEventLoopGroup();

		try {
			// 配置服务器启动类
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workerGroup);
			b.channel(NioServerSocketChannel.class);
			b.handler(new LoggingHandler(LogLevel.INFO));// 配置日志输出
			b.childHandler(new ChannelInitializer<SocketChannel>() {
				protected void initChannel(SocketChannel ch) throws Exception {
					ChannelPipeline cp = ch.pipeline();
					// server端发送的是httpResponse，所以要使用HttpResponseEncoder进行编码
					cp.addLast(new HttpResponseEncoder());
					// server端接收到的是httpRequest，所以要使用HttpRequestDecoder进行解码
					cp.addLast(new HttpRequestDecoder());
					cp.addLast(new HttpServerHandler());
				}
			});
			b.option(ChannelOption.SO_BACKLOG, 128);
			b.option(ChannelOption.TCP_NODELAY, true);
			b.childOption(ChannelOption.SO_KEEPALIVE, true);

			ChannelFuture f = b.bind(port).sync();

			LogUtils.all.info("HTTP服务器已经启动..." + port);

			f.channel().closeFuture().sync();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			workerGroup.shutdownGracefully();
			bossGroup.shutdownGracefully();
		}
	}

}
