package com.ipinpar.app.network.api;

import org.json.JSONObject;

import com.android.volley.Response.Listener;
import com.ipinpar.app.util.MD5Util;

public class ExperienceDiaryRequest extends BaseJsonRequest {
	private static final String PROTOCOL ="30012";

	public ExperienceDiaryRequest(int sid, Listener<JSONObject> listener) {
		super(Method.POST, String.format("api.pinpa?protocol=%s&a=%s",
				PROTOCOL,
				sid
				), null, listener);
		// TODO Auto-generated constructor stub
	}
	public ExperienceDiaryRequest(int activityid,int uid, Listener<JSONObject> listener) {
		super(Method.POST, String.format("api.pinpa?protocol=%s&b=%s&c=%s",
				PROTOCOL,
				activityid,
				uid
				), null, listener);
		// TODO Auto-generated constructor stub
	}


}
