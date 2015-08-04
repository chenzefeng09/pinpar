package com.ipinpar.app.network.api;

import org.json.JSONObject;

import com.android.volley.Response.Listener;
import com.ipinpar.app.util.MD5Util;

public class PartyExperiencesListRequest extends BaseJsonRequest{
	private static final String PROTOCOL ="40003";
	
	public PartyExperiencesListRequest(String roleid,String pageNum,String pageCount,
			Listener<JSONObject> listener) {
		super(Method.GET,
				String.format("api.pinpa?protocol=%s&a=%s&b=%s&pageNum=%s&pageCount=%s",
						PROTOCOL,
						roleid,
						MD5Util.MD5(PROTOCOL+roleid+"pinpa"),
						pageNum,
						pageCount
						),
				null, listener);
	}

	public PartyExperiencesListRequest(String roleid,
			Listener<JSONObject> listener) {
		super(Method.GET,
				String.format("api.pinpa?protocol=%s&a=%s&b=%s",
						PROTOCOL,
						roleid,
						MD5Util.MD5(PROTOCOL+roleid+"pinpa")
						),
				null, listener);
	}

}
