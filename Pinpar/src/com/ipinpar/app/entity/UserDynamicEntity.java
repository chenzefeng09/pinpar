package com.ipinpar.app.entity;

public class UserDynamicEntity {
	private String description;
	private String imgsrc;
	private String title;
	private String uid;
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getImgsrc() {
		return imgsrc;
	}
	public void setImgsrc(String imgsrc) {
		this.imgsrc = imgsrc;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	@Override
	public String toString() {
		return "UserDynamicEntity [description=" + description + ", imgsrc="
				+ imgsrc + ", title=" + title + ", uid=" + uid + "]";
	}
	
	
	
}
