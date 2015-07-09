package com.ipinpar.app.network.api;

import org.json.JSONObject;

import com.android.volley.Response.Listener;

public class OngoingActivitiesRequest extends BaseJsonRequest{
	private static final String PROTOCOL ="30001";
	

	public OngoingActivitiesRequest(String status,String uid,String maxid,String pageNum,String pageCount,
			Listener<JSONObject> listener) {
		super(Method.GET,
				String.format("api.pinpa?protocol=%s&a=%s&b=%s&c=%s&pageNum=%s&pageCount=%s",
						PROTOCOL,
						status,
						uid,
						maxid,
						pageNum,
						pageCount
						),
				null, listener);
	}
	
	public OngoingActivitiesRequest(String uid,
			Listener<JSONObject> listener) {
		super(Method.GET,
				String.format("api.pinpa?protocol=%s&a=%s",
						PROTOCOL,
						uid
						),
				null, listener);
	}

}
