package zcf.demo.bean;

import java.util.Date;

public class ChatMessage {
	private String name;
	private String message;
	
	public ChatMessage(String name, String message, Type type, Date date) {
		super();
		this.name = name;
		this.message = message;
		this.type = type;
		this.date = date;
	}
	public ChatMessage()
	{
		
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Type getType() {
		return type;
	}
	public void setType(Type type) {
		this.type = type;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	private Type type;
	private Date date;
	//定义枚举类型
	public enum Type
	{
		INCOME,OUTCOME
	}
}
