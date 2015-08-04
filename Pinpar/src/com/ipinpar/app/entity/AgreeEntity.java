package com.ipinpar.app.entity;

public class AgreeEntity {
	
	public AgreeEntity() {
		// TODO Auto-generated constructor stub
	}
	
	public AgreeEntity(int fromid,String fromidtype) {
		this.fromid = fromid;
		this.fromidtype = fromidtype;
	}	
	
	private int fromid;
	private String fromidtype="";
	public int getFromid() {
		return fromid;
	}
	public void setFromid(int fromid) {
		this.fromid = fromid;
	}
	public String getFromidtype() {
		return fromidtype;
	}
	public void setFromidtype(String fromidtype) {
		this.fromidtype = fromidtype;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == null ||!(o instanceof AgreeEntity)) {
			return false;
		}
		else {
			AgreeEntity agreeEntity = (AgreeEntity) o;
			if (agreeEntity.getFromid() == fromid && agreeEntity.getFromidtype().equals(fromidtype)) {
				return true;
			}
		}
		return super.equals(o);
	}
	
	
	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		 return this.fromidtype.hashCode() + 29*fromid;
	}
	

}
