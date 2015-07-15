package com.ipinpar.app.network.api;

import org.json.JSONObject;

import com.android.volley.Response.Listener;

public class AnserInviteLetterRequest extends BaseJsonRequest {
	private static final String PROTOCOL ="30016";


	public AnserInviteLetterRequest(int uid,int acid, int answer, Listener<JSONObject> listener) {
		super(Method.POST, String.format("api.pinpa?protocol=%s&a=%s&b=%s&c=%s&d=%s",
				PROTOCOL,
				uid,
				acid,
				answer
				), null, listener);
		// TODO Auto-generated constructor stub
	}

}
