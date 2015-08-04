package com.ipinpar.app.network.api;

import org.json.JSONObject;

import com.android.volley.Response.Listener;

public class StatementCommentListRequest extends BaseJsonRequest {
	private static final String PROTOCOL ="20009";

	public StatementCommentListRequest(int fromid, String from_idtype
			,Listener<JSONObject> listener) {
		super(Method.POST, String.format("api.pinpa?protocol=%s&a=%s&b=%s",
				PROTOCOL,
				fromid,
				from_idtype
				), null, listener);
	}

}
