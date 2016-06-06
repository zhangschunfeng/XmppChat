package zcf.demo.bean;

public class Friends {
	private String account;
	private String name;
	private String isReceiver;
    
	public Friends(String account, String name, String isReceiver) {
		super();
		this.account = account;
		this.name = name;
		this.isReceiver = isReceiver;
	}
	public Friends() {
		
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
	public String getIsReceiver() {
		return isReceiver;
	}
	public void setIsReceiver(String isReceiver) {
		this.isReceiver = isReceiver;
	}
  
}
