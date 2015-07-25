package com.ipinpar.app.network.api;

import org.json.JSONObject;

import com.android.volley.Response.Listener;
import com.ipinpar.app.util.DeviceUtil;
import com.ipinpar.app.util.MD5Util;

public class GetVerifyCodeRequest extends BaseJsonRequest {
	private static final String PROTOCOL ="10005";
	
	private int result;
	private int res_code;
	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public int getRes_code() {
		return res_code;
	}

	public void setRes_code(int res_code) {
		this.res_code = res_code;
	}

	public String getUserphone() {
		return userphone;
	}

	public void setUserphone(String userphone) {
		this.userphone = userphone;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	private String userphone;
	private  String identifier;

	public GetVerifyCodeRequest(String phone,int isFP,JSONObject jsonRequest,
			Listener<JSONObject> listener) {
		super(Method.POST,String.format("api.pinpa?protocol=%s&a=%s&b=%s&c=%s",
				PROTOCOL,
				phone,
				isFP,
				MD5Util.MD5(PROTOCOL+phone+"pinpa")
				),null, listener);
		// TODO Auto-generated constructor stub
	}

}
