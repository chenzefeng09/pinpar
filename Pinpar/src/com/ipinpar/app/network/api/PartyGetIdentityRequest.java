package com.ipinpar.app.network.api;

import org.json.JSONObject;

import com.android.volley.Response.Listener;
import com.ipinpar.app.util.MD5Util;

public class PartyGetIdentityRequest extends BaseJsonRequest {
	private static final String PROTOCOL ="40010";


	public PartyGetIdentityRequest(String uid,String acid, String answer, Listener<JSONObject> listener) {
		super(Method.POST, String.format("api.pinpa?protocol=%s&a=%s",
				PROTOCOL,
				uid
				), null, listener);
		// TODO Auto-generated constructor stub
	}

}
