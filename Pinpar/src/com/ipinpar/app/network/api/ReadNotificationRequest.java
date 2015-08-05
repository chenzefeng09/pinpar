package com.ipinpar.app.network.api;

import org.json.JSONObject;

import com.android.volley.Response.Listener;
import com.ipinpar.app.util.MD5Util;

public class ReadNotificationRequest extends BaseJsonRequest {
	private static final String PROTOCOL ="20006";

	public ReadNotificationRequest(int uid, String ids, Listener<JSONObject> listener) {
		super(Method.POST, String.format("api.pinpa?protocol=%s&a=%s&b=%s&c=%s",
				PROTOCOL,
				uid,
				ids,
				MD5Util.MD5(PROTOCOL+uid+ids+"pinpa")
				), null, listener);
		// TODO Auto-generated constructor stub
	}

}
