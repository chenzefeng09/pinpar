package com.ipinpar.app.network.api;

import org.json.JSONObject;

import com.android.volley.Response.Listener;
import com.ipinpar.app.util.MD5Util;

public class AgreeRequest extends BaseJsonRequest {
	private static final String PROTOCOL ="20011";

	public AgreeRequest(int uid, String fromidtype, int fromid,String type,
			Listener<JSONObject> listener) {
		super(Method.POST, String.format("api.pinpa?protocol=%s&a=%s&b=%s&c=%s&d=%s&e=%s",
				PROTOCOL,
				uid,
				fromidtype,
				fromid,
				type,
				MD5Util.MD5(PROTOCOL+uid+"pinpa")
				), null, listener);
		// TODO Auto-generated constructor stub
	}

}
