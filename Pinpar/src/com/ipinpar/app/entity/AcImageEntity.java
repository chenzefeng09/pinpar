package com.ipinpar.app.entity;

import java.io.Serializable;

public class AcImageEntity implements Serializable{
	private String img;

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	@Override
	public String toString() {
		return "AcImageEntity [img=" + img + "]";
	}
	
}
