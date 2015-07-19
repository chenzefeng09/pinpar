package com.ipinpar.app.network.api;

import org.json.JSONObject;

import android.util.Log;

import com.android.volley.Response.Listener;
import com.ipinpar.app.util.MD5Util;

public class StatementListRequest extends BaseJsonRequest{
	private static final String PROTOCOL ="30010";
	
	public StatementListRequest(String uid,String acid,
			Listener<JSONObject> listener) {
		super(Method.GET,
				String.format("api.pinpa?protocol=%s&a=%s&b=%s&c=%s",
						PROTOCOL,
						uid,
						acid,
						MD5Util.MD5(PROTOCOL+uid+acid+"pinpa")
						),
				null, listener);
	}
	
	public StatementListRequest(String uid,String acid,
			String pageNum,String pageCount,
			Listener<JSONObject> listener) {
		super(Method.GET,
				String.format("api.pinpa?protocol=%s&a=%s",
						PROTOCOL,
						uid,
						acid,
						MD5Util.MD5(PROTOCOL+uid+acid+"pinpa"),
						pageNum,
						pageCount
						),
				null, listener);
	}
	
	//根据type值可以决定返回的是最强宣言或者趴友经历（1、最强宣言；2、趴友经历）
	public StatementListRequest(String uid,String acid,String type,
			Listener<JSONObject> listener) {
		super(Method.GET,
				String.format("api.pinpa?protocol=%s&a=%s&b=%s&c=%s&d=%s",
						PROTOCOL,
						uid,
						acid,
						MD5Util.MD5(PROTOCOL+acid+"pinpa"),
						type
						),
				null, listener);
	}
	
	public StatementListRequest(String uid,String acid,String type,
			String pageNum,String pageCount,
			Listener<JSONObject> listener) {
		super(Method.GET,
				String.format("api.pinpa?protocol=%s&a=%s",
						PROTOCOL,
						uid,
						acid,
						MD5Util.MD5(PROTOCOL+acid+type+"pinpa"),
						type,
						pageNum,
						pageCount
						),
				null, listener);
	}

}
