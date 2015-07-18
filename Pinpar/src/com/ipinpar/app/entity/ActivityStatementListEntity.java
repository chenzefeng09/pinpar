package com.ipinpar.app.entity;

import java.util.ArrayList;

public class ActivityStatementListEntity {
	private String result;
	private ArrayList<AcStatementEntity> declarations;
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public ArrayList<AcStatementEntity> getDeclarations() {
		return declarations;
	}
	public void setDeclarations(ArrayList<AcStatementEntity> declarations) {
		this.declarations = declarations;
	}
	@Override
	public String toString() {
		return "ActivityStatementListEntity [result=" + result
				+ ", declarations=" + declarations + "]";
	}
	
	
	
}
