package com.ipinpar.app.network.api;

import org.json.JSONObject;

import com.android.volley.Response.Listener;
import com.ipinpar.app.util.MD5Util;

public class SetRoleRequest extends BaseJsonRequest{
	private static final String PROTOCOL ="40001";
	
	public SetRoleRequest(String uid,String roleid,
			Listener<JSONObject> listener) {
		super(Method.GET,
				String.format("api.pinpa?protocol=%s&a=%s&b=%s&c=%s",
						PROTOCOL,
						uid,
						roleid,
						MD5Util.MD5(PROTOCOL+uid+"pinpa")
						),
				null, listener);
	}

}