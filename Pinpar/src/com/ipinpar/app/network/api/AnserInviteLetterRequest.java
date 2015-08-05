package com.ipinpar.app.network.api;

import org.json.JSONObject;

import com.android.volley.Response.Listener;
import com.ipinpar.app.util.MD5Util;

public class AnserInviteLetterRequest extends BaseJsonRequest {
	private static final String PROTOCOL ="30016";


	public AnserInviteLetterRequest(String uid,String acid, String answer, Listener<JSONObject> listener) {
		super(Method.POST, String.format("api.pinpa?protocol=%s&a=%s&b=%s&c=%s&d=%s",
				PROTOCOL,
				uid,
				acid,
				answer,
				MD5Util.MD5(PROTOCOL+uid+acid+"pinpa")
				), null, listener);
		// TODO Auto-generated constructor stub
	}

}
