package com.innsmap.domain.bean;

public class LoginBean {

	private boolean succeed;
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public int getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(int timestamp) {
		this.timestamp = timestamp;
	}

	private String errerMsg;
	private int userId;
	private String token;
	private int timestamp;

	public boolean isSucceed() {
		return succeed;
	}

	public void setSucceed(boolean succeed) {
		this.succeed = succeed;
	}

	public String getErrerMsg() {
		return errerMsg;
	}

	public void setErrerMsg(String errerMsg) {
		this.errerMsg = errerMsg;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

}
