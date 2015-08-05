package com.ipinpar.app.network.api;

import org.json.JSONObject;
import com.android.volley.Response.Listener;
import com.ipinpar.app.util.MD5Util;

public class FriendsListRequest extends BaseJsonRequest {
	private static final String PROTOCOL ="20001";

	public FriendsListRequest(String uid,
			Listener<JSONObject> listener) {
		super(Method.POST, String.format("api.pinpa?protocol=%s&a=%s",
				PROTOCOL,
				uid,
				MD5Util.MD5(PROTOCOL+uid+"pinpa")
				), null, listener);
		// TODO Auto-generated constructor stub
	}

}
