package com.innsmap.domain.bean;

public class NeedBase<T> {

	private boolean succeed;
	private String errerMsg;
	private T data;

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

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

}
