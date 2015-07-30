package com.ipinpar.app.entity;

import java.io.Serializable;

public class AcStatementEntity implements Serializable{
	private int acid;
	private int agreecount;
	private int commentcount;
	private String createtime;
	private String declaration;
	private int enrollid;
	private int flg;
	private int infoid;
	private String reason;
	private int status;
	private String uid;
	private String username;
	public int getAcid() {
		return acid;
	}
	public void setAcid(int acid) {
		this.acid = acid;
	}
	public int getAgreecount() {
		return agreecount;
	}
	public void setAgreecount(int agreecount) {
		this.agreecount = agreecount;
	}
	public int getCommentcount() {
		return commentcount;
	}
	public void setCommentcount(int commentcount) {
		this.commentcount = commentcount;
	}
	public String getCreatetime() {
		return createtime;
	}
	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}
	public String getDeclaration() {
		return declaration;
	}
	public void setDeclaration(String declaration) {
		this.declaration = declaration;
	}
	public int getEnrollid() {
		return enrollid;
	}
	public void setEnrollid(int enrollid) {
		this.enrollid = enrollid;
	}
	public int getFlg() {
		return flg;
	}
	public void setFlg(int flg) {
		this.flg = flg;
	}
	public int getInfoid() {
		return infoid;
	}
	public void setInfoid(int infoid) {
		this.infoid = infoid;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	@Override
	public String toString() {
		return "AcStatementEntity [acid=" + acid + ", agreecount=" + agreecount
				+ ", commentcount=" + commentcount + ", createtime="
				+ createtime + ", declaration=" + declaration + ", enrollid="
				+ enrollid + ", flg=" + flg + ", infoid=" + infoid
				+ ", reason=" + reason + ", status=" + status + ", uid=" + uid
				+ ", username=" + username + "]";
	}
	
	
	
}
