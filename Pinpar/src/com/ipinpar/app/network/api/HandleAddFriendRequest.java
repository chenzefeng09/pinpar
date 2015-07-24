package com.ipinpar.app.network.api;

import org.json.JSONObject;

import com.android.volley.Response.Listener;
import com.ipinpar.app.util.MD5Util;

public class HandleAddFriendRequest extends BaseJsonRequest {
	private static final String PROTOCOL ="20003";

	public HandleAddFriendRequest(int rid, int uid,String answer,String bkname,
			 Listener<JSONObject> listener) {
		super(Method.POST, String.format("api.pinpa?protocol=%s&a=%s&b=%s&c=%s&d=%s&e=%s",
				PROTOCOL,
				rid,
				uid,
				answer,
				bkname,
				MD5Util.MD5(PROTOCOL+rid+uid+answer+"pinpa")
				), null, listener);
	}
}
