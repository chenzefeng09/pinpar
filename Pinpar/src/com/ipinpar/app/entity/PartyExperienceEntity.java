package com.ipinpar.app.entity;

import java.util.ArrayList;

public class PartyExperienceEntity {
	
	private int agreecount;
	private int authorid;
	private int commentcount;
	private String content;
	private String createtime;
	private int experiencingid;
	private String img;
	private ArrayList<AcImageEntity> imgs;
	private int roleid;
	private String username;
	public int getAgreecount() {
		return agreecount;
	}
	public void setAgreecount(int agreecount) {
		this.agreecount = agreecount;
	}
	public int getAuthorid() {
		return authorid;
	}
	public void setAuthorid(int authorid) {
		this.authorid = authorid;
	}
	public int getCommentcount() {
		return commentcount;
	}
	public void setCommentcount(int commentcount) {
		this.commentcount = commentcount;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getCreatetime() {
		return createtime;
	}
	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}
	public int getExperiencingid() {
		return experiencingid;
	}
	public void setExperiencingid(int experiencingid) {
		this.experiencingid = experiencingid;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	public ArrayList<AcImageEntity> getImgs() {
		return imgs;
	}
	public void setImgs(ArrayList<AcImageEntity> imgs) {
		this.imgs = imgs;
	}
	public int getRoleid() {
		return roleid;
	}
	public void setRoleid(int roleid) {
		this.roleid = roleid;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	@Override
	public String toString() {
		return "PartyExperienceEntity [agreecount=" + agreecount
				+ ", authorid=" + authorid + ", commentcount=" + commentcount
				+ ", content=" + content + ", createtime=" + createtime
				+ ", experiencingid=" + experiencingid + ", img=" + img
				+ ", imgs=" + imgs + ", roleid=" + roleid + ", username="
				+ username + "]";
	}
	
	

}
