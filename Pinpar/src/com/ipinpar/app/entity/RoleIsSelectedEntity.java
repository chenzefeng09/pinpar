package com.ipinpar.app.entity;


public class RoleIsSelectedEntity {
	private String result;
	private String rolecount;
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getRolecount() {
		return rolecount;
	}
	public void setRolecount(String rolecount) {
		this.rolecount = rolecount;
	}
	@Override
	public String toString() {
		return "RoleIsSelectedEntity [result=" + result + ", rolecount="
				+ rolecount + "]";
	}
	
	
}
