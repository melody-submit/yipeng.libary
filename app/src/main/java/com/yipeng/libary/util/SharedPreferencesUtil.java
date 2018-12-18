package com.yipeng.libary.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.yipeng.libary.WsApplication;

public class SharedPreferencesUtil {

	public final static String SHARE_KEY = "wsapp";

	public static void clearSharedPreferences(Context ctx) {
		SharedPreferences.Editor localEditor = ctx.getSharedPreferences(SHARE_KEY, 0).edit();
		localEditor.clear();
		localEditor.commit();
	}
//	
//	public static String getToken() {
//		return getString(ConstantKey.token, "");//abc123456789
//	}
//	
//	public static String getLocationAddr() {
//		return getString(ConstantKey.THE_LAST_LOCATION_ADDR, "");//abc123456789
//	}
	 
	
	public static String getString(String key, String defVal) {
		WsApplication content =WsApplication.getInstance();
		String val = defVal;
		if (content != null) {
			val = content.getSharedPreferences(SHARE_KEY, 0).getString(key, defVal);
		}
		return val;
	}

	public static void putString(String key, String val) {

		WsApplication content = WsApplication.getInstance();
		if (content != null) {
			SharedPreferences.Editor edit = content.getSharedPreferences(SHARE_KEY, 0).edit();
			edit.putString(key, val);
			edit.commit();
		}
	}
	
	public static void remove(String key){
		
	}
 

	public static void saveValueSharedPreferences(Context ctx, String key, String value) {
		SharedPreferences.Editor edit = ctx.getSharedPreferences(SHARE_KEY, 0).edit();
		edit.putString(key, value);
		edit.commit();
	}

	public static void saveValueSharedPreferences(Context ctx, String[] keys, String[] values) {
		SharedPreferences.Editor localEditor = null;
		if ((keys != null) && (values != null) && (keys.length != values.length))
			localEditor = ctx.getSharedPreferences(SHARE_KEY, 0).edit();
		for (int i = 0;; i++) {
			if (i >= keys.length)
				return;
			localEditor.putString(keys[i], values[i]);
			localEditor.commit();
		}
	}
 

}
