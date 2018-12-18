package com.yipeng.libary.util;

import java.io.File;
import java.io.FileInputStream;
import java.util.UUID;

import android.content.Context;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;

public class Security {
 
	private static Context ctx;
	private static Security ins = null;
 
	private Security(Context ctx){
		Security.ctx = ctx;
	}

	public static Security getInstance(Context ctx) {
		if (ins == null)
			ins = new Security(ctx);
		return ins;
	}
    
 
 
	public static String getMacAddress() {
		String macAddress = null;
		try {
			if (new File("sys/class/net/wlan0/address").exists()) {
				FileInputStream fis = new FileInputStream("sys/class/net/wlan0/address");
				byte[] buf = new byte[8192];
				int i = fis.read(buf);
				if (i > 0) {
					macAddress = new String(buf, 0, i, "utf-8");
				}
				fis.close();
			}
			if (macAddress == null)
				;
		} catch (Exception e) {
			try {
				if (macAddress.length() == 0) {
					FileInputStream fis = new FileInputStream("sys/class/net/eth0/address");
					byte[] buf = new byte[8192];
					int j = fis.read(buf);
					if (j > 0){
						macAddress = new String(buf, 0, j, "utf-8");
					}
					fis.close();
				}
			} catch (Exception e1) {
			}
		}
		return macAddress;
	}

 

	public String getUUID() {
		
		TelephonyManager tm = (TelephonyManager) ctx.getSystemService("phone");
		String deviceId = tm.getDeviceId();
		if(StringUtils.isBlank(deviceId)){
			deviceId = "";
		}
		String serialNo = tm.getSimSerialNumber();
		if(StringUtils.isBlank(serialNo)){
			serialNo = "";
		}
		
		String sercurityId = Settings.Secure.getString(ctx.getContentResolver(), "android_id");
		if(StringUtils.isBlank(sercurityId)){
			sercurityId = getMacAddress();
			if(StringUtils.isBlank(sercurityId)){
				sercurityId = "";
			}
		}
		
		String uuid = new UUID(sercurityId.hashCode(), deviceId.hashCode() << 32
				| serialNo.hashCode()).toString();
		Log.d("debug", "uuid=" + uuid);
		return uuid;
	}
}