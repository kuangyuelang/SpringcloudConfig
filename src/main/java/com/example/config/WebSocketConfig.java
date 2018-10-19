package com.example.config;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;  
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

@Configuration  
@EnableWebSocketMessageBroker  //注解开启STOMP协议来传输基于代理的消息，此时控制器支持使用@MessageMapping
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
	//在线用户名保存
    private static List<Map<String, Object>> listUsers = new ArrayList<Map<String, Object>>();
  
    @Override  
    public void registerStompEndpoints(StompEndpointRegistry registry) {  
//        registry.addEndpoint("/webServer").withSockJS();
//        registry.addEndpoint("/queueServer").withSockJS();//注册两个STOMP的endpoint，分别用于广播和点对点  
        registry.addEndpoint("/api/websocket")//注册一个 /websocket 的 websocket 节点
        .addInterceptors(myHandshakeInterceptor())  //添加 websocket握手拦截器
        .setHandshakeHandler(myDefaultHandshakeHandler())   //添加 websocket握手处理器
        .setAllowedOrigins("*") //设置允许可跨域的域名
        .withSockJS();//指定使用SockJS协议
    }  
    
    /**
     * WebSocket 握手拦截器
     * 可做一些用户认证拦截处理
     */
    private HandshakeInterceptor myHandshakeInterceptor(){
        return new HandshakeInterceptor() {
            /**
             * websocket握手连接
             * @return 返回是否同意握手
             */
            @Override
            public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
                ServletServerHttpRequest req = (ServletServerHttpRequest) request;
                //通过url的query参数获取认证参数
                String token = req.getServletRequest().getParameter("token");
                //根据token认证用户，不通过返回拒绝握手
                Principal user = authenticate(token);
//                if(user == null){
//                    return false;
//                }
                //保存认证用户
                attributes.put("user", user);
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("username", token);//存为多个用户
                listUsers.add(map);
                return true;
            }

            @Override
            public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {
//            	System.out.println("这里是握手连接是不是同意");
            }
        };
    }
    
  //WebSocket 握手处理器
    private DefaultHandshakeHandler myDefaultHandshakeHandler(){
        return new DefaultHandshakeHandler(){
            @Override
            protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {
//                System.out.println(attributes.get("user")+":握手处理器");
            	//设置认证通过的用户到当前会话中
                return (Principal)attributes.get("user");
            }
        };
    }
    
    @Override  
    public void configureMessageBroker(MessageBrokerRegistry config) {  
    	//设置客户端接收消息地址的前缀（可不设置）
    	config.enableSimpleBroker("/topic","/queue");//topic消息前缀，queue点对点消息前缀
        //设置客户端接收点对点消息地址的前缀，默认为 /user
        config.setUserDestinationPrefix("/user");
        //设置客户端向服务器发送消息的地址前缀（可不设置）
        config.setApplicationDestinationPrefixes("/app");
        
    }
    
    
    /**
     * 根据token认证授权
     * @param token
     */
    private Principal authenticate(String token){
        //TODO 实现用户的认证并返回用户信息，如果认证失败返回 null
        //用户信息需继承 Principal 并实现 getName() 方法，返回全局唯一值
    	Principal user = new Principal() {
			@Override
			public String getName() {
				// TODO Auto-generated method stub
				return token;
			}
		};
        return user;
    }
    
    /**
	 * 获取在线用户名以逗号隔开
	 * @return
	 */
//	public static String getOnlineUsers(){
//		StringBuffer user = new StringBuffer();
//	    for (String key : users.keySet()) {//admin是服务端自己的连接，不能算在线人数
//	    	if (!"admin".equals(users.get(key)))
//			{
//				user.append(users.get(key)+",");
//			}
//		}
//	    return user.toString();
//	}
	
	public static List<Map<String, Object>> getListUsers() {
		return listUsers;
	}
}