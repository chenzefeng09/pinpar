package com.ipinpar.app.network.api;

import org.json.JSONObject;

import com.android.volley.Response.Listener;

public class MyDreamListRequest extends BaseJsonRequest {
private static final String PROTOCOL ="30021";
	
	public MyDreamListRequest(int uid,int maxid,
			Listener<JSONObject> listener) {
		super(Method.POST,
				String.format("api.pinpa?protocol=%s&b=%s",
						PROTOCOL,
						uid,
						maxid
						),
				null, listener);
	}
}
