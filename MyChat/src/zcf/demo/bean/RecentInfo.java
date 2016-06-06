package zcf.demo.bean;

public class RecentInfo {

	private String name;
	private String account;
	private Boolean MessCom;
	private String time;
	private String content;
	private String msgNum;
	public RecentInfo() {
	
	}
	
	public RecentInfo(String name, String time, String content) {
		super();
		this.name = name;
		this.time = time;
		this.content = content;
		MessCom=false;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getMsgNum() {
		return msgNum;
	}
	public void setMsgNum(String msgNum) {
		this.msgNum = msgNum;
	}
	
}
