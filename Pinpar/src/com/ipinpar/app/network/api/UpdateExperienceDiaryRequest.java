package com.ipinpar.app.network.api;

import org.json.JSONObject;

import com.android.volley.Response.Listener;
import com.ipinpar.app.util.MD5Util;

public class UpdateExperienceDiaryRequest extends BaseJsonRequest {
	private static final String PROTOCOL ="30013";

	public UpdateExperienceDiaryRequest(int uid,int acid,String title,String headimg,String contentjson, Listener<JSONObject> listener) {
		super(Method.POST,  String.format("api.pinpa?protocol=%s&a=%s&b=%s&c=%s&c=%s&d=%s&e=%s&f=%s",
				PROTOCOL,
				uid,
				acid,
				title,
				headimg,
				contentjson,
				MD5Util.MD5(PROTOCOL+uid+"pinpa")
				), null, listener);
		// TODO Auto-generated constructor stub
	}

}
