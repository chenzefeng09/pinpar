package com.ipinpar.app.network.api;

import org.json.JSONObject;

import android.R.integer;

import com.android.volley.Response.Listener;

public class DreamShowDetailRequest extends BaseJsonRequest {
	private static final String PROTOCOL ="30020";

	public DreamShowDetailRequest(int dreamid, Listener<JSONObject> listener) {
		super(Method.POST, String.format("api.pinpa?protocol=%s&a=%s",
				PROTOCOL,
				dreamid
				), null, listener);
	}

}
