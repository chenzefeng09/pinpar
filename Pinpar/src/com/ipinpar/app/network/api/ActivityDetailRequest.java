package com.ipinpar.app.network.api;

import org.json.JSONObject;

import com.android.volley.Response.Listener;

public class ActivityDetailRequest extends BaseJsonRequest{
	private static final String PROTOCOL ="30002";
	
	public ActivityDetailRequest(String acid,
			Listener<JSONObject> listener) {
		super(Method.GET,
				String.format("api.pinpa?protocol=%s&a=%s",
						PROTOCOL,
						acid
						),
				null, listener);
	}

}
