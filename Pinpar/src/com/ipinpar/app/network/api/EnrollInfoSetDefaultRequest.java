package com.ipinpar.app.network.api;

import org.json.JSONObject;

import com.android.volley.Request.Method;
import com.android.volley.Response.Listener;
import com.ipinpar.app.util.MD5Util;

public class EnrollInfoSetDefaultRequest extends BaseJsonRequest {
	private static final String PROTOCOL ="30008";

	public EnrollInfoSetDefaultRequest(int uid, int infoid,
			Listener<JSONObject> listener) {
		super(Method.POST, String.format("api.pinpa?protocol=%s&a=%s&b=%s&c=%s",
				PROTOCOL,
				uid,
				infoid,
				MD5Util.MD5(PROTOCOL+uid+infoid+"pinpa")
				), null, listener);
		// TODO Auto-generated constructor stub
	}
}
