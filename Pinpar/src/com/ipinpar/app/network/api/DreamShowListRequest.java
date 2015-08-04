package com.ipinpar.app.network.api;

import org.json.JSONObject;

import android.R.integer;

import com.android.volley.Response.Listener;

public class DreamShowListRequest extends BaseJsonRequest {
	private static final String PROTOCOL ="30018";

	public DreamShowListRequest(
			Listener<JSONObject> listener) {
		super(Method.POST, String.format("api.pinpa?protocol=%s",
				PROTOCOL
				), null, listener);
		// TODO Auto-generated constructor stub
	}

}
