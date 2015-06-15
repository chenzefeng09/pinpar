package com.ipinpar.app.util;

import com.ipinpar.app.BuildConfig;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.Log;

public class DeviceUtil {
	public static TelephonyManager tm ;

	public static void init(Context context){
		tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
	}
	
	public static String getDeviceFullInfo(){
        StringBuilder sb = new StringBuilder();  
        sb.append("\nDeviceId(IMEI) = " + tm.getDeviceId());  
        sb.append("\nDeviceSoftwareVersion = " + tm.getDeviceSoftwareVersion());  
        sb.append("\nLine1Number = " + tm.getLine1Number());  
        sb.append("\nNetworkCountryIso = " + tm.getNetworkCountryIso());  
        sb.append("\nNetworkOperator = " + tm.getNetworkOperator());  
        sb.append("\nNetworkOperatorName = " + tm.getNetworkOperatorName());  
        sb.append("\nNetworkType = " + tm.getNetworkType());  
        sb.append("\nPhoneType = " + tm.getPhoneType());  
        sb.append("\nSimCountryIso = " + tm.getSimCountryIso());  
        sb.append("\nSimOperator = " + tm.getSimOperator());  
        sb.append("\nSimOperatorName = " + tm.getSimOperatorName());  
        sb.append("\nSimSerialNumber = " + tm.getSimSerialNumber());  
        sb.append("\nSimState = " + tm.getSimState());  
        sb.append("\nSubscriberId(IMSI) = " + tm.getSubscriberId());  
        sb.append("\nVoiceMailNumber = " + tm.getVoiceMailNumber());  
        if (BuildConfig.DEBUG) {
            Log.e("info", sb.toString());   
		}
        return sb.toString();
	}
	
	public static String getDeviceId(){
		return tm.getDeviceId();
	}

}
