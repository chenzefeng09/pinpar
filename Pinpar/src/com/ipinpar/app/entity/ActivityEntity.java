package com.ipinpar.app.entity;

import java.util.ArrayList;

public class ActivityEntity {
	private int acid;							//活动项目ID
	private String acname;						//活动项目名称
	private String activebegintime;				//活动开始时间
	private String activeendtime;				//活动结束时间
	private String address;						//活动地址
	private int agreecount;						//活动支持人数（点赞人数）
	private String begintime;					//报名开始时间
	private int collectcount;					//活动收藏数（暂时不用）
	private String endtime;						//报名截止时间
	private int experiencecount;				//活动体验人数
	private ArrayList<AcImageEntity> imgsList;	//活动图片列表
	private int incount;						//活动报名人数
	private int readcount;						//活动浏览次数
	private int status;							//活动的当前状态
	private String title;						//活动标题
	public int getAcid() {
		return acid;
	}
	public void setAcid(int acid) {
		this.acid = acid;
	}
	public String getAcname() {
		return acname;
	}
	public void setAcname(String acname) {
		this.acname = acname;
	}
	public String getActivebegintime() {
		return activebegintime;
	}
	public void setActivebegintime(String activebegintime) {
		this.activebegintime = activebegintime;
	}
	public String getActiveendtime() {
		return activeendtime;
	}
	public void setActiveendtime(String activeendtime) {
		this.activeendtime = activeendtime;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public int getAgreecount() {
		return agreecount;
	}
	public void setAgreecount(int agreecount) {
		this.agreecount = agreecount;
	}
	public String getBegintime() {
		return begintime;
	}
	public void setBegintime(String begintime) {
		this.begintime = begintime;
	}
	public int getCollectcount() {
		return collectcount;
	}
	public void setCollectcount(int collectcount) {
		this.collectcount = collectcount;
	}
	public String getEndtime() {
		return endtime;
	}
	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}
	public int getExperiencecount() {
		return experiencecount;
	}
	public void setExperiencecount(int experiencecount) {
		this.experiencecount = experiencecount;
	}
	public ArrayList<AcImageEntity> getImgsList() {
		return imgsList;
	}
	public void setImgsList(ArrayList<AcImageEntity> imgsList) {
		this.imgsList = imgsList;
	}
	public int getIncount() {
		return incount;
	}
	public void setIncount(int incount) {
		this.incount = incount;
	}
	public int getReadcount() {
		return readcount;
	}
	public void setReadcount(int readcount) {
		this.readcount = readcount;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	
}
