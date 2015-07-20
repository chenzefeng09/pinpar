package com.ipinpar.app.network.api;

import org.json.JSONObject;

import com.android.volley.Response.Listener;

public class StatementDetailRequest extends BaseJsonRequest {
	private static final String PROTOCOL ="300101";

	public StatementDetailRequest(int enrollid, Listener<JSONObject> listener) {
		super(Method.POST, String.format("api.pinpa?protocol=%s&a=%s",
				PROTOCOL,
				enrollid
				), null, listener);
		// TODO Auto-generated constructor stub
	}

}
