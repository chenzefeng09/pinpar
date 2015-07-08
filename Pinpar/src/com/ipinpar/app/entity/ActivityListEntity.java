package com.ipinpar.app.entity;

import java.util.ArrayList;

public class ActivityListEntity {
	private String result;
	private ArrayList<ActivityEntity> actives;
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public ArrayList<ActivityEntity> getActives() {
		return actives;
	}
	public void setActives(ArrayList<ActivityEntity> actives) {
		this.actives = actives;
	}
	@Override
	public String toString() {
		return "ActivityListEntity [result=" + result + ", actives=" + actives
				+ "]";
	}
	
	
	
}
