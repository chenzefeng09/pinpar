package com.ipinpar.app.entity;


public class ReplyEntity {

	private int commentid;
	public int getCommentid() {
		return commentid;
	}
	public void setCommentid(int commentid) {
		this.commentid = commentid;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public long getCreatetime() {
		return createtime;
	}
	public void setCreatetime(long createtime) {
		this.createtime = createtime;
	}
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
	public String getFrom_img() {
		return from_img;
	}
	public void setFrom_img(String from_img) {
		this.from_img = from_img;
	}
	public String getFrom_username() {
		return from_username;
	}
	public void setFrom_username(String from_username) {
		this.from_username = from_username;
	}
	public int getFromid() {
		return fromid;
	}
	public void setFromid(int fromid) {
		this.fromid = fromid;
	}
	public int getReplyid() {
		return replyid;
	}
	public void setReplyid(int replyid) {
		this.replyid = replyid;
	}
	public String getTo_img() {
		return to_img;
	}
	public void setTo_img(String to_img) {
		this.to_img = to_img;
	}
	public String getTo_username() {
		return to_username;
	}
	public void setTo_username(String to_username) {
		this.to_username = to_username;
	}
	public int getToid() {
		return toid;
	}
	public void setToid(int toid) {
		this.toid = toid;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	private String content;
	private long createtime;
	private int flag;
	private String from_img;
	private String from_username;
	private int fromid;
	private int replyid;
	private String to_img;
	private String to_username;
	private int toid;
	private int type;
	
}
