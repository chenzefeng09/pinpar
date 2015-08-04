package com.ipinpar.app.network.api;

import org.json.JSONObject;

import com.android.volley.Response.Listener;

public class RolesAvailableRequest extends BaseJsonRequest{
	private static final String PROTOCOL ="40010";
	
	public RolesAvailableRequest(String uid,
			Listener<JSONObject> listener) {
		super(Method.GET,
				String.format("api.pinpa?protocol=%s&a=%s",
						PROTOCOL,
						uid
						),
				null, listener);
	}

}
