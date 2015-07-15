package com.ipinpar.app.network.api;

import org.json.JSONObject;

import com.android.volley.Response.Listener;

public class ExperienceDiaryListRequest extends BaseJsonRequest {
	private static final String PROTOCOL ="30011";
	public ExperienceDiaryListRequest(int acid, Listener<JSONObject> listener) {
		super(Method.POST, String.format("api.pinpa?protocol=%s&a=%s&c=%s",
				PROTOCOL,
				"acid",
				acid
				), null, listener);
		// TODO Auto-generated constructor stub
	}

}
