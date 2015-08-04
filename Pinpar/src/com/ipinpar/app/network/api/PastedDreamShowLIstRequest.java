package com.ipinpar.app.network.api;

import org.json.JSONObject;

import com.android.volley.Response.Listener;

public class PastedDreamShowLIstRequest extends BaseJsonRequest {
	private static final String PROTOCOL ="30019";

	public PastedDreamShowLIstRequest(String pageNum, String pageCount,
			 Listener<JSONObject> listener) {
		super(Method.POST, String.format("api.pinpa?protocol=%s&pageNum=%s&pageCount=%s",
				PROTOCOL,
				pageNum,
				pageCount
				), null, listener);
		// TODO Auto-generated constructor stub
	}

}
