package com.ipinpar.app.network.api;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.json.JSONObject;

import com.android.volley.Response.Listener;
import com.ipinpar.app.util.MD5Util;


public class EditUserInfoRequest extends BaseJsonRequest {
	private static final String PROTOCOL ="10007";


	public EditUserInfoRequest(long uid,String uname,String signiture,String email,String qq,String weiixn,String sex,String hobbys,
			Listener<JSONObject> listener) throws UnsupportedEncodingException {
		super(Method.POST, String.format("api.pinpa?protocol=%s&a=%s&b=%s&c=%s&d=%s&e=%s&f=%s&g=%s&h=%s&i=%s",
				PROTOCOL,
				uid,
				URLEncoder.encode(URLEncoder.encode(uname, "utf-8"), "utf-8"),
				URLEncoder.encode(URLEncoder.encode(signiture, "utf-8"), "utf-8"),
				email,
				qq,
				weiixn,
				sex,
				URLEncoder.encode(URLEncoder.encode(hobbys, "utf-8"), "utf-8"),
				MD5Util.MD5(PROTOCOL+uid+"pinpa")
				), null, listener);
		// TODO Auto-generated constructor stub
	}

}
