package com.ipinpar.app.entity;

import java.io.Serializable;
import java.util.ArrayList;

public class ActivityEntity implements Serializable{
	private int acid;							//活动项目ID
	private String acname;						//活动项目名称
	private String activebegintime;				//活动开始时间
	private String activeendtime;				//活动结束时间
	private String address1;					//活动地址
	private String address2;					//活动地址
	private String address3;					//活动地址
	private String addressdetail;				//活动详细地址
	private int agreecount;						//活动支持人数（点赞人数）
	private String begintime;					//报名开始时间
	private int collectcount;					//活动收藏数（暂时不用）
	private String createtime;					//活动创建时间
	private String description;					//活动描述
	private String detail;						//活动详细
	private ArrayList<UserDynamicEntity> dynamic;
	private String endtime;						//报名截止时间
	private int experiencecount;				//活动体验人数
	private int flag;
	private ArrayList<AcImageEntity> imgs;	//活动图片列表
	private int incount;						//活动报名人数
	private String latitude;
	private String longitude;
	private String phone;
	private int readcount;						//活动浏览次数
	private String sname;
	private int status;							//活动的当前状态
	private int storeid;
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
	public String getAddress1() {
		return address1;
	}
	public void setAddress1(String address1) {
		this.address1 = address1;
	}
	public String getAddress2() {
		return address2;
	}
	public void setAddress2(String address2) {
		this.address2 = address2;
	}
	public String getAddress3() {
		return address3;
	}
	public void setAddress3(String address3) {
		this.address3 = address3;
	}
	public String getAddressdetail() {
		return addressdetail;
	}
	public void setAddressdetail(String addressdetail) {
		this.addressdetail = addressdetail;
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
	public String getCreatetime() {
		return createtime;
	}
	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getDetail() {
		return detail;
	}
	public void setDetail(String detail) {
		this.detail = detail;
	}
	public ArrayList<UserDynamicEntity> getDynamic() {
		return dynamic;
	}
	public void setDynamic(ArrayList<UserDynamicEntity> dynamic) {
		this.dynamic = dynamic;
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
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
	public ArrayList<AcImageEntity> getImgs() {
		return imgs;
	}
	public void setImgs(ArrayList<AcImageEntity> imgs) {
		this.imgs = imgs;
	}
	public int getIncount() {
		return incount;
	}
	public void setIncount(int incount) {
		this.incount = incount;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public int getReadcount() {
		return readcount;
	}
	public void setReadcount(int readcount) {
		this.readcount = readcount;
	}
	public String getSname() {
		return sname;
	}
	public void setSname(String sname) {
		this.sname = sname;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getStoreid() {
		return storeid;
	}
	public void setStoreid(int storeid) {
		this.storeid = storeid;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	@Override
	public String toString() {
		return "ActivityEntity [acid=" + acid + ", acname=" + acname
				+ ", activebegintime=" + activebegintime + ", activeendtime="
				+ activeendtime + ", address1=" + address1 + ", address2="
				+ address2 + ", address3=" + address3 + ", addressdetail="
				+ addressdetail + ", agreecount=" + agreecount + ", begintime="
				+ begintime + ", collectcount=" + collectcount
				+ ", createtime=" + createtime + ", description=" + description
				+ ", detail=" + detail + ", dynamic=" + dynamic + ", endtime="
				+ endtime + ", experiencecount=" + experiencecount + ", flag="
				+ flag + ", imgs=" + imgs + ", incount=" + incount
				+ ", latitude=" + latitude + ", longitude=" + longitude
				+ ", phone=" + phone + ", readcount=" + readcount + ", sname="
				+ sname + ", status=" + status + ", storeid=" + storeid
				+ ", title=" + title + "]";
	}
	
	
	
}
