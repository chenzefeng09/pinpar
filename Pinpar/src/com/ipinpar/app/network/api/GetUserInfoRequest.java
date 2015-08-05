package com.ipinpar.app.network.api;

import org.json.JSONObject;

import com.android.volley.Response.Listener;
import com.ipinpar.app.util.MD5Util;

public class GetUserInfoRequest extends BaseJsonRequest {
	private static final String PROTOCOL ="10006";


	public GetUserInfoRequest(String uid,
			Listener<JSONObject> listener) {
		super(Method.POST, String.format("api.pinpa?protocol=%s&a=%s&b=%s",
				PROTOCOL,
				uid,
				MD5Util.MD5(PROTOCOL+uid+"pinpa")
				), null, listener);
		// TODO Auto-generated constructor stub
	}

}
