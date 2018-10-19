package com.example.controller;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.config.WebSocketConfig;
import com.example.entity.WsMessage;

@RestController //注册一个Controller，WebSocket的消息处理需要放在Controller下
public class WsController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;    //Spring WebSocket消息发送模板
    
    @PostMapping("/hellos")
    public void sg(WsMessage msg) {
    	msg.setContent("你好啊 光头强");
    	msg.setFromName("123456");
//      msg.setContent(notice);
      //向客户端发送广播消息（方式二），客户端订阅消息地址为：/topic/notice
      messagingTemplate.convertAndSend("/topic/notice", msg);
    	
    }

    //发送广播通知
    @MessageMapping("/addNotice")   //接收客户端发来的消息，客户端发送消息地址为：/app/addNotice
    @SendTo("/topic/notice")        //向客户端发送广播消息（方式一），客户端订阅消息地址为：/topic/notice
    public void notice(WsMessage msg, Principal fromUser) {
        //TODO 业务处理
//        WsMessage msg = new WsMessage();
        msg.setFromName(fromUser.getName());
//        msg.setContent(notice);
        //向客户端发送广播消息（方式二），客户端订阅消息地址为：/topic/notice
        messagingTemplate.convertAndSend("/topic/notice", msg);
//        return msg;
    }

    //发送点对点消息
    @MessageMapping("/msg")         //接收客户端发来的消息，客户端发送消息地址为：/app/msg
    @SendToUser("/queue/msg/result") //向当前发消息客户端（就是自己）发送消息的发送结果，客户端订阅消息地址为：/user/queue/msg/result
    public WsMessage sendMsg(WsMessage message, Principal fromUser){
        //TODO 业务处理
        message.setFromName(fromUser.getName());

        //向指定客户端发送消息，第一个参数Principal.name为前面websocket握手认证通过的用户name（全局唯一的），客户端订阅消息地址为：/user/queue/msg/new
        messagingTemplate.convertAndSendToUser(message.getToName(), "/queue/msg/new", message);
        return message;
    }
    
    /**
     * 获取当前在线用户
     * @return
     */
    @RequestMapping("/getListUsers")
    public ResponseEntity<Object> getListUsers(){
       Map<String, Object> map = new HashMap<String, Object>();
       //当前用户个数
       int count = WebSocketConfig.getListUsers().size();
       //当前用户的username
       List<Map<String, Object>> users = WebSocketConfig.getListUsers();
       map.put("count", WebSocketConfig.getListUsers().size());
       map.put("users", WebSocketConfig.getListUsers());
       return ResponseEntity.ok(map);
    }
}

/*通信地址除了握手请求地址最好写完整的地址，其它地址均不用写域名或IP 
- 握手请求(connect)：http://域名或IP/websocket?token=认证token 
- 广播订阅地址(subscribe)：/topic/notice 
- 个人消息订阅地址(subscribe)：/user/queue/msg/new 
- 发送广播通知(send)：/app/addNotice 
- 发送点对点消息(send)：/app/msg 
- 获取消息发送结果(subscribe)：/user/queue/msg/result*/