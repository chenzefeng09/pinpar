package com.ipinpar.app.entity;

import java.util.ArrayList;

public class PartyExperiencesListEntity {
	private String result;
	private ArrayList<PartyExperienceEntity> experiencing;
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public ArrayList<PartyExperienceEntity> getExperiencing() {
		return experiencing;
	}
	public void setExperiencing(ArrayList<PartyExperienceEntity> experiencing) {
		this.experiencing = experiencing;
	}
	@Override
	public String toString() {
		return "PartyExperiencesListEntity [result=" + result
				+ ", experiencing=" + experiencing + "]";
	}
	
	
	
}
