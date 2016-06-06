package zcf.demo.bean;

import java.io.Serializable;
import java.util.Date;

public class ImMessage implements Serializable {

	private int type;
	private String content;
	private Date time;
	public ImMessage()
	{
		
	}
	
	public ImMessage(int type, String content, Date time) {
		super();
		this.type = type;
		this.content = content;
		this.time = time;
	}

	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	
}
