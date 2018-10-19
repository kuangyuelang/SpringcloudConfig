/*package com.example.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.time.DateFormatUtils;
import org.junit.Test;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

*//**
 * 
 *类描述: websocket连接测试
 * 
 *@Author:况月浪 
 *@date:2018年8月7日 
 *@Version:1.1.0
 *//*
public class Snippet {
	//连接数
	public static int connectNum = 0;
	//连接成功数
	public static int successNum = 0;
	//连接失败数
	public static int errorNum = 0;
	
	*//**
	 * 测试websocket最大连接数
	 * @throws InterruptedException
	 *//*
	@Test
	public void testConnect() throws InterruptedException {
	    new Thread() {
	        @Override
	        public void run() {
	            while (true) {
	                try {
	                    Thread.sleep(3000);
	                } catch (InterruptedException e) {
	                    e.printStackTrace();
	                }
	                //每次3秒打印一次连接结果
	                System.out.println(DateFormatUtils.format(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss:sss") +
	                        "  连接数：" + connectNum
	                        + "  成功数：" + successNum
	                        + "  失败数：" + errorNum);
	            }
	        }
	    }.start();
	    List<WebSocketStompClient> list = new ArrayList<>();
	    System.out.println("开始时间："
	            + DateFormatUtils.format(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss:sss"));
	    while (true) {
	        //连接失败超过10次，停止测试
	        if(errorNum > 10){
	            break;
	        }
	        list.add(newConnect(++connectNum));
	        Thread.sleep(10);
	    }
	}
	
	*//**
	 * 创建websocket连接
	 * @param i
	 * @return
	 *//*
	private WebSocketStompClient newConnect(int i) {
	    List<Transport> transports = new ArrayList<>();
	    transports.add(new WebSocketTransport(new StandardWebSocketClient()));
	    WebSocketClient socketClient = new SockJsClient(transports);
	    WebSocketStompClient stompClient = new WebSocketStompClient(socketClient);
	
	    stompClient.setMessageConverter(new MappingJackson2MessageConverter());
	    ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
	    taskScheduler.afterPropertiesSet();
	    stompClient.setTaskScheduler(taskScheduler);
	
	    String url = "ws://localhost:8086/websocket?token=" + i;
	    stompClient.connect(url, new TestConnectHandler());
	    return stompClient;
	}
	
	private static synchronized void addSuccessNum() {
	    successNum++;
	}
	
	private static synchronized void addErrorNum() {
	    errorNum++;
	}
	
	private static class TestConnectHandler extends StompSessionHandlerAdapter {
	    @Override
	    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
	        addSuccessNum();
	    }
	
	    @Override
	    public void handleTransportError(StompSession session, Throwable exception) {
	        addErrorNum();
	    }
	}
}  
  
  
*/