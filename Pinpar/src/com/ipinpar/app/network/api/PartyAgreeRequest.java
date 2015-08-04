package com.ipinpar.app.network.api;

import org.json.JSONObject;

import com.android.volley.Response.Listener;
import com.ipinpar.app.util.MD5Util;

public class PartyAgreeRequest extends BaseJsonRequest {
	private static final String PROTOCOL ="40005";

	public PartyAgreeRequest(String uid, String fromidtype, String fromid,
			Listener<JSONObject> listener) {
		super(Method.POST, String.format("api.pinpa?protocol=%s&a=%s&b=%s&c=%s&e=%s",
				PROTOCOL,
				uid,
				fromidtype,
				fromid,
				MD5Util.MD5(PROTOCOL+uid+"pinpa")
				), null, listener);
		// TODO Auto-generated constructor stub
	}
	
	public PartyAgreeRequest(String uid, String fromidtype, String fromid,String weight,
			Listener<JSONObject> listener) {
		super(Method.POST, String.format("api.pinpa?protocol=%s&a=%s&b=%s&c=%s&d=%s&e=%s",
				PROTOCOL,
				uid,
				fromidtype,
				fromid,
				weight,
				MD5Util.MD5(PROTOCOL+uid+"pinpa")
				), null, listener);
		// TODO Auto-generated constructor stub
	}

}
