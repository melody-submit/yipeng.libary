package com.yipeng.libary.util;
 

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.telephony.TelephonyManager;

public class DeviceUtils {

	
	
	public static int getAppVersionCode(Context ctx) {
		int localVersionCode = 0;
		final PackageManager pm = ctx.getPackageManager();
		try {
			// 得到本地版本号
			localVersionCode = pm.getPackageInfo(ctx.getPackageName(), 0).versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return localVersionCode;
	}
//	public Map collectAppInfo() {
//		Map map = new HashMap();
//		try {
//			PackageManager pm = context.getPackageManager();// 获得包管理器
//			PackageInfo pi = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES);// 得到该应用的信息，即主Activity
//			if (pi != null) {
//				map.put("versionName", pi.versionName);
//				map.put("versionCode", pi.versionCode);
//			}
//		} catch (NameNotFoundException e) {
//			e.printStackTrace();
//		}
//		return map;
//	}
	public static String getAppVersionName(Context ctx) {
		String versionName = "";
		final PackageManager pm = ctx.getPackageManager();
		try {
			// 得到本地版本号
			versionName = pm.getPackageInfo(ctx.getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return versionName;
	}
	
	private static String deviceId;

	public static String getDeviceId(){
		if(StringUtils.isBlank(deviceId)){
			deviceId = "1212121212";//Security.getInstance(WsApplication.getInstance()).getUUID();
		}
		return deviceId;
	}
	

	public static String getPhoneNo(Context ctx) {
		String no = null;
		if (ctx != null) {
			TelephonyManager tm = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
			no = tm.getLine1Number();
			if (StringUtils.isNotBlank(no)) {
				
			}else{
				no = tm.getSimSerialNumber();
			}
		}
		if(no.startsWith("1")){
			return no;
		}else{
			return "";
		}
	}
	
}
