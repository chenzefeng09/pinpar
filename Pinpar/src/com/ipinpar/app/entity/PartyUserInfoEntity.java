package com.ipinpar.app.entity;

import java.util.ArrayList;

public class PartyUserInfoEntity {

	private int result;
	private int agreeweight;
	private int agreeweight1;
	private int agreeweight2;
	private int agreeweight3;
	private int agreeweight4;
	private int roleid;
	private ArrayList<PartyRoleIdEntity> roleids;
	private int score;
	private int teamid;
	private int uid;
	private String username;
	public int getResult() {
		return result;
	}
	public void setResult(int result) {
		this.result = result;
	}
	public int getAgreeweight() {
		return agreeweight;
	}
	public void setAgreeweight(int agreeweight) {
		this.agreeweight = agreeweight;
	}
	public int getAgreeweight1() {
		return agreeweight1;
	}
	public void setAgreeweight1(int agreeweight1) {
		this.agreeweight1 = agreeweight1;
	}
	public int getAgreeweight2() {
		return agreeweight2;
	}
	public void setAgreeweight2(int agreeweight2) {
		this.agreeweight2 = agreeweight2;
	}
	public int getAgreeweight3() {
		return agreeweight3;
	}
	public void setAgreeweight3(int agreeweight3) {
		this.agreeweight3 = agreeweight3;
	}
	public int getAgreeweight4() {
		return agreeweight4;
	}
	public void setAgreeweight4(int agreeweight4) {
		this.agreeweight4 = agreeweight4;
	}
	public int getRoleid() {
		return roleid;
	}
	public void setRoleid(int roleid) {
		this.roleid = roleid;
	}
	public ArrayList<PartyRoleIdEntity> getRoleids() {
		return roleids;
	}
	public void setRoleids(ArrayList<PartyRoleIdEntity> roleids) {
		this.roleids = roleids;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public int getTeamid() {
		return teamid;
	}
	public void setTeamid(int teamid) {
		this.teamid = teamid;
	}
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
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
		return "PartyUserInfoEntity [result=" + result + ", agreeweight="
				+ agreeweight + ", agreeweight1=" + agreeweight1
				+ ", agreeweight2=" + agreeweight2 + ", agreeweight3="
				+ agreeweight3 + ", agreeweight4=" + agreeweight4 + ", roleid="
				+ roleid + ", roleids=" + roleids + ", score=" + score
				+ ", teamid=" + teamid + ", uid=" + uid + ", username="
				+ username + "]";
	}

	
	
}
