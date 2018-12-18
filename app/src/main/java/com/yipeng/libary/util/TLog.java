package com.yipeng.libary.util;

import android.util.Log;

public class TLog {

	public final static String tag = "TLog";
	
	public static boolean DEBUG = true;
	
	public static void i(String msg){
		if(DEBUG){
			Log.i(tag, msg);
		}
		
	}
	public static void e(String msg){
		if(DEBUG){
			Log.e(tag, msg);
		}
	}
	public static void w(String msg){
		if(DEBUG){
			Log.w(tag, msg);
		}
	}
	public static void d(String msg){
		if(DEBUG){
			Log.d(tag, msg);
		}
	}
	

}
