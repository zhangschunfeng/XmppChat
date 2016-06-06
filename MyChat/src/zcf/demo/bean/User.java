package zcf.demo.bean;

import java.io.Serializable;

public class User implements Serializable {
	private String count;
	private String nickName;
	private String sortLetter;
	public String getCount() {
		return count;
	}
	public void setCount(String count) {
		this.count = count;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public User(){}
	
	public User(String count, String nickName, String sortLetter) {
		super();
		this.count = count;
		this.nickName = nickName;
		this.sortLetter = sortLetter;
	}
	public String getSortLetter() {
		return sortLetter;
	}
	public void setSortLetter(String sortLetter) {
		this.sortLetter = sortLetter;
	}
	
}
