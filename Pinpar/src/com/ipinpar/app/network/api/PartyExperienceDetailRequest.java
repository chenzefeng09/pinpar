package com.ipinpar.app.network.api;

import org.json.JSONObject;

import android.R.integer;

import com.android.volley.Response.Listener;

public class PartyExperienceDetailRequest extends BaseJsonRequest {
	private static final String PROTOCOL ="400031";

	public PartyExperienceDetailRequest(int experiencingid, Listener<JSONObject> listener) {
		super(Method.POST, String.format("api.pinpa?protocol=%s&a=%s",
				PROTOCOL,
				experiencingid
				), null, listener);
	}

}
