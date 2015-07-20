package com.ipinpar.app.network.api;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.json.JSONObject;

import com.android.volley.Response.Listener;
import com.ipinpar.app.util.MD5Util;

public class PublishCommentRequest extends BaseJsonRequest {
	private static final String PROTOCOL ="20007";

	public PublishCommentRequest(int fromid, String fromidtype,int authorid,String content,
			Listener<JSONObject> listener) throws UnsupportedEncodingException {
		super(Method.POST, String.format("api.pinpa?protocol=%s&a=%s&b=%s&c=%s&d=%s&e=%s",
				PROTOCOL,
				fromid,
				fromidtype,
				authorid,
				URLEncoder.encode(URLEncoder.encode(content,"utf-8"),"utf-8"),
				MD5Util.MD5(PROTOCOL+fromid+fromidtype+authorid+"pinpa")
				), null, listener);
		// TODO Auto-generated constructor stub
	}

}
