package com.ipinpar.app.network.api;

import org.json.JSONObject;

import com.android.volley.Response.Listener;
import com.ipinpar.app.entity.UserEntity;
import com.ipinpar.app.util.DeviceUtil;
import com.ipinpar.app.util.MD5Util;

public class LoginRequest extends BaseJsonRequest{
	private static final String PROTOCOL ="10001";
	

	public LoginRequest(String phone,String passWord,
			Listener<JSONObject> listener) {
		super(Method.GET,
				String.format("api.pinpa?protocol=%s&a=%s&b=%s&c=%s&d=%s&e=%s",
						PROTOCOL,
						phone,
						passWord,
						MD5Util.MD5(PROTOCOL+phone+passWord+"pinpa"),
						DeviceUtil.getDeviceId()
						),
				null, listener);
	}

}
