package com.yipeng.libary.util;

import android.widget.Toast;

import com.yipeng.libary.WsApplication;

public class ToastUtil {

	public static void show(String msg){
		
		Toast.makeText(WsApplication.getInstance(), msg, Toast.LENGTH_SHORT).show();
	}

	public static void showMessage(String msg){

		Toast.makeText(WsApplication.getInstance(), msg, Toast.LENGTH_SHORT).show();
	}

	public static void showMessage(int msg){

		Toast.makeText(WsApplication.getInstance(), msg, Toast.LENGTH_SHORT).show();
	}
}
