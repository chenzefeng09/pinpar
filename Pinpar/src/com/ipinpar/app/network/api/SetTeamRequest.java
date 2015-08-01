package com.ipinpar.app.network.api;

import org.json.JSONObject;

import com.android.volley.Response.Listener;
import com.ipinpar.app.util.MD5Util;

public class SetTeamRequest extends BaseJsonRequest{
	private static final String PROTOCOL ="40011";
	
	public SetTeamRequest(String uid,String teamid,
			Listener<JSONObject> listener) {
		super(Method.GET,
				String.format("api.pinpa?protocol=%s&a=%s&b=%s&c=%s",
						PROTOCOL,
						uid,
						teamid,
						MD5Util.MD5(PROTOCOL+uid+"pinpa")
						),
				null, listener);
	}

}

