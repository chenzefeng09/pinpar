package com.ipinpar.app.network.api;

import org.json.JSONObject;

import com.android.volley.Response.Listener;
import com.ipinpar.app.util.MD5Util;

public class VerifyCodeRequest extends BaseJsonRequest {
	private static final String PROTOCOL ="100051";


	public VerifyCodeRequest(String phone,String identifier,String rand_code, 
			Listener<JSONObject> listener) {
		super(Method.GET, String.format("api.pinpa?protocol=%s&a=%s&b=%s&c=%s&d=%s",
				PROTOCOL,
				phone,
				identifier,
				rand_code,
				MD5Util.MD5(PROTOCOL+phone+identifier+rand_code+"pinpa")
				), null, listener);
		// TODO Auto-generated constructor stub
	}

}
