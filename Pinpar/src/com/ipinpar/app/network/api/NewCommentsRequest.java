package com.ipinpar.app.network.api;

import org.json.JSONObject;

import com.android.volley.Response.Listener;

public class NewCommentsRequest extends BaseJsonRequest {
	private static final String PROTOCOL ="20009";

	public NewCommentsRequest(String from_id,
			Listener<JSONObject> listener) {
		super(Method.POST, String.format("api.pinpa?protocol=%s&a=%s&b=%s",
				PROTOCOL,
				from_id
				), null, listener);
		// TODO Auto-generated constructor stub
	}

}
