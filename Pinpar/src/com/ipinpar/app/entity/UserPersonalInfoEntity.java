package com.ipinpar.app.entity;

import java.util.ArrayList;

public class UserPersonalInfoEntity {
	private String email;
	private ArrayList<HobbyEntity> hobbys;
	private String imgsrc;
	private String mobile;
	private String qq;
	private int result;
	private int sex;
	private String signature;
	private String username;
	private String weixin;
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public ArrayList<HobbyEntity> getHobbys() {
		return hobbys;
	}
	public void setHobbys(ArrayList<HobbyEntity> hobbys) {
		this.hobbys = hobbys;
	}
	public String getImgsrc() {
		return imgsrc;
	}
	public void setImgsrc(String imgsrc) {
		this.imgsrc = imgsrc;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getQq() {
		return qq;
	}
	public void setQq(String qq) {
		this.qq = qq;
	}
	public int getResult() {
		return result;
	}
	public void setResult(int result) {
		this.result = result;
	}
	public int getSex() {
		return sex;
	}
	public void setSex(int sex) {
		this.sex = sex;
	}
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getWeixin() {
		return weixin;
	}
	public void setWeixin(String weixin) {
		this.weixin = weixin;
	}
	

}
