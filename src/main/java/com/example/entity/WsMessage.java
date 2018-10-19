package com.example.entity;
public class WsMessage {
	
    //消息接收人，对应认证用户Principal.name（全局唯一）
    private String toName;
    
    //消息发送人，对应认证用户Principal.name（全局唯一）
    private String fromName;
    
    //消息内容
    private Object content;
    
	public String getToName() {
		return toName;
	}
	public void setToName(String toName) {
		this.toName = toName;
	}
	public String getFromName() {
		return fromName;
	}
	public void setFromName(String fromName) {
		this.fromName = fromName;
	}
	public Object getContent() {
		return content;
	}
	public void setContent(Object content) {
		this.content = content;
	}
    
    
}