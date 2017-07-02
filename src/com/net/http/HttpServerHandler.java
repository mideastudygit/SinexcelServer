package com.net.http;

import com.alibaba.fastjson.JSONObject;
import com.base.utils.ParaMap;
import com.net.bean.BaseBean;
import com.net.bean.ChargeBean;
import com.net.bean.EndChargeBean;
import com.net.bean.SetIntConfigureBean;
import com.net.bean.SetStringConfigureBean;
import com.net.bean.SetUpdateClearSoftBean;
import com.net.bean.SetUpdateDataBean;
import com.net.bean.SetUpdateDataEndBean;
import com.net.bean.SetUpdateLengthBean;
import com.net.bean.SetUpdateNameBean;
import com.net.bean.SetUpdateRestartBean;
import com.net.util.LogUtils;
import com.net.util.NetUtils;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.CharsetUtil;

public class HttpServerHandler extends ChannelInboundHandlerAdapter {
	private HttpRequest request;
	private String uri;

	public void channelRead(ChannelHandlerContext ctx, Object msg) {
		try {
			if (msg instanceof HttpRequest) {
				request = (HttpRequest) msg;
				uri = request.getUri();
				LogUtils.send.info(ctx.channel() + "Uri:" + uri);
			}

			if (msg instanceof HttpContent) {
				HttpContent content = (HttpContent) msg;
				ByteBuf buf = content.content();
				String paramstr = buf.toString(CharsetUtil.UTF_8);
				buf.release();

				LogUtils.send.info("接收参数值：" + paramstr);

				if (uri.indexOf("/data") < 0) {
					return;
				}

				JSONObject map = null;
				if (paramstr.length() != 0) {
					int index = paramstr.indexOf("?");
					String s1 = paramstr.substring(index + 1);
					map = initMap(s1, "&");
				}
				
				LogUtils.send.info("接收参数对象：" + map);

				if (!map.containsKey("cmd")) {
					LogUtils.send.info("参数中未包含cmd，不进行处理");
					return;
				}

				int cmd = map.getIntValue("cmd");
				BaseBean bean = NetUtils.getBean(cmd + 1);
				bean.setPileCode(map.getString("pileCode"));
				

				if (bean instanceof ChargeBean) { // 开启充电
					ChargeBean chargeBean = (ChargeBean) bean;
					chargeBean.setChargeSlogan(map.getIntValue("chargeSlogan"));
					chargeBean.setChargeType(map.getIntValue("chargeType"));
					chargeBean.setChargeStrategy(map.getIntValue("chargeStrategy"));
					chargeBean.setChargeStrategyParam(map.getIntValue("chargeStrategyParam"));
					chargeBean.setStartTime(map.getString("startTime"));
					chargeBean.setBookOvertime(map.getIntValue("bookOvertime"));
					chargeBean.setCardnumber(map.getString("cardnumber"));
					LogUtils.send.info("生成开始充电对象，" + chargeBean);
				} else if (bean instanceof EndChargeBean) { // 结束充电
					EndChargeBean endChargeBean = (EndChargeBean) bean;
					endChargeBean.setChargeSlogan(map.getIntValue("chargeSlogan"));
					endChargeBean.setParamType(map.getIntValue("paramType"));
					endChargeBean.setPeriod(map.getIntValue("period"));
					String information = endChargeBean.getInformation(map.getIntValue("paramType"));
					LogUtils.send.info("生成" + information + "对象，" + endChargeBean);
				} else if (bean instanceof SetIntConfigureBean) { // 设置整型参数
					SetIntConfigureBean setIntConfigureBean = (SetIntConfigureBean) bean;
					setIntConfigureBean.setType(map.getIntValue("type"));
					setIntConfigureBean.setParamType(map.getIntValue("paramType"));
					if(map.getIntValue("type") == 1) {
						if(map.getIntValue("paramType") == 25){
							setIntConfigureBean.setCount(2);
							setIntConfigureBean.setConfigureBytes(8);
							setIntConfigureBean.setStringPeriod(map.getString("param"));
							setIntConfigureBean.setPeriod(map.getIntValue("period")); 
						} else {
							setIntConfigureBean.setPeriod(map.getIntValue("period"));
						}
					}
					LogUtils.send.info("生成下发整型参数对象，" + setIntConfigureBean);
				} else if (bean instanceof SetStringConfigureBean) { // 设置字符型参数
					SetStringConfigureBean setStringConfigureBean = (SetStringConfigureBean) bean;
					setStringConfigureBean.setType(map.getIntValue("type"));
					setStringConfigureBean.setParamType(map.getIntValue("paramType"));
					if(map.getIntValue("type") == 1) {
						setStringConfigureBean.setParam(map.getString("param"));
					}
					LogUtils.send.info("生成下发字符型参数对象，" + setStringConfigureBean);
				} else if (bean instanceof SetUpdateClearSoftBean) { // 下发擦除指令(CMD=1001)
					SetUpdateClearSoftBean setUpdateClearSoftBean = (SetUpdateClearSoftBean) bean;
					setUpdateClearSoftBean.setClearOrQuery(map.getIntValue("clearOrQuery"));
					LogUtils.send.info("生成服务器下发擦除指令对象，" + setUpdateClearSoftBean);
				} else if (bean instanceof SetUpdateLengthBean) { // 下发升级文件大小指令(CMD=1005)
					SetUpdateLengthBean setUpdateLengthBean = (SetUpdateLengthBean) bean;
					setUpdateLengthBean.setFileDataLength(map.getIntValue("fileDataLength"));
					LogUtils.send.info("生成服务器下发升级文件大小指令对象，" + setUpdateLengthBean);
				}  else if (bean instanceof SetUpdateNameBean) { // 下发升级文件名指令(CMD=1003)
					SetUpdateNameBean setUpdateNameBean = (SetUpdateNameBean) bean;
					setUpdateNameBean.setFileNameLength(map.getString("fileName"));
					setUpdateNameBean.setFileName(map.getString("fileName"));
					LogUtils.send.info("生成服务器下发升级文件名指令对象，" + setUpdateNameBean);
				} else if (bean instanceof SetUpdateDataBean) { // 下发升级文件数据指令(CMD=1007)
					SetUpdateDataBean setUpdateDataBean = (SetUpdateDataBean) bean;
					setUpdateDataBean.setSeq(map.getIntValue("seq"));
					setUpdateDataBean.setFilePath(map.getString("filePath"));
					setUpdateDataBean.setFilePartLength(map.getIntValue("filePartLength"));
					setUpdateDataBean.setFilePart(map.getIntValue("filePart"));
					LogUtils.send.info("生成服务器下发升级文件数据指令对象，" + setUpdateDataBean);
				} else if (bean instanceof SetUpdateDataEndBean) { // 下发升级文件数据结束指令(CMD=1009)
					SetUpdateDataEndBean setUpdateDataEndBean = (SetUpdateDataEndBean) bean;
					LogUtils.send.info("生成服务器下发升级文件数据结束指令对象，" + setUpdateDataEndBean);
				} else if (bean instanceof SetUpdateRestartBean) { // 下发重启指令(CMD=1011)
					SetUpdateRestartBean setUpdateRestartBean = (SetUpdateRestartBean) bean;
					LogUtils.send.info("生成服务器下发重启指令对象，" + setUpdateRestartBean);
				}
				
				Channel channel = NetUtils.getChannelByKey(bean.getPileCode());
				bean.send(channel);

				ParaMap resMap = new ParaMap();
				resMap.put("status", channel != null ? 0 : 1);
				String res = resMap.toString();
				FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK,
						Unpooled.wrappedBuffer(res.getBytes("UTF-8")));
				response.headers().set("Content-Type", "text/plain");
				response.headers().set("Content-Length", response.content().readableBytes());
				if (HttpHeaders.isKeepAlive(request)) {
					response.headers().set("Connection", "keep-alive");
				}
				ctx.write(response);
				ctx.flush();
			}
		} catch (Exception e) {
			LogUtils.all.info("Exception：", e);
		}
	}

	public static JSONObject initMap(String content, String delim) {
		JSONObject map = new JSONObject();
		String[] params = content.split(delim);
		for (String para : params) {
			String[] kv = para.split("=");
			if (kv.length == 2) {
				String key = kv[0].trim();
				String value = kv[1].trim();
				map.put(key, value);
			}
		}
		return map;
	}

	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
	}

	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		LogUtils.all.info("exceptionCaught：", cause);
		ctx.close();
	}
}
