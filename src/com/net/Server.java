package com.net;

import com.base.web.AppConfig;
import com.net.http.HttpServerBootstrap;
import com.net.server.NetServerBootstrap;

public class Server {
	public static void main(String[] args) {
		int port_tcp = AppConfig.getIntPro("port-tcp");
		NetServerBootstrap server = new NetServerBootstrap(port_tcp);
		server.start();

		int port_http = AppConfig.getIntPro("port-http");
		HttpServerBootstrap httpServer = new HttpServerBootstrap(port_http);
		httpServer.start();
	}
}
