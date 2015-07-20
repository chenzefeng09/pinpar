package com.ipinpar.app.network.api;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.json.JSONObject;

import android.R.integer;

import com.android.volley.Response.Listener;
import com.ipinpar.app.util.MD5Util;

public class ReplyCommentRequest extends BaseJsonRequest {
	private static final String PROTOCOL ="20008";

	public ReplyCommentRequest(int commentid, String content, int fromid,int toid,
			Listener<JSONObject> listener) throws UnsupportedEncodingException {
		super(Method.POST, 
				String.format("api.pinpa?protocol=%s&a=%s&b=%s&c=%s&d=%s&e=%s",
						PROTOCOL,
						commentid,
						URLEncoder.encode(URLEncoder.encode(content, "utf-8"), "utf-8"),
						fromid,
						toid,
						MD5Util.MD5(PROTOCOL+commentid+fromid+toid+"pinpa")
						), null, listener);
		// TODO Auto-generated constructor stub
	}

}
