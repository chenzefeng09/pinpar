package com.ipinpar.app.entity;

import java.util.ArrayList;

public class AvailableRolesListEntity {
	private String result;
	private ArrayList<Integer> roles;
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public ArrayList<Integer> getRoles() {
		return roles;
	}
	public void setRoles(ArrayList<Integer> roles) {
		this.roles = roles;
	}
	@Override
	public String toString() {
		return "AvailableRolesListEntity [result=" + result + ", roles="
				+ roles + "]";
	}

	
	
}
