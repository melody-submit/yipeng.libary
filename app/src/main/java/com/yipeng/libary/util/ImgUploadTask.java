package com.yipeng.libary.util;


import android.graphics.Bitmap;

import com.alibaba.fastjson.JSONObject;
import com.yipeng.libary.net.HttpPostClient;
import com.yipeng.libary.net.MyAsyncTask;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;


public abstract class ImgUploadTask extends MyAsyncTask<Void, Void, Void> {

	
	String reqUrl = "";
	String params = "";
	Bitmap bitmap;
	File file;
	byte[] data = null;
	String body = null;

	public ImgUploadTask(String reqUrl, File file, String params){
		this.reqUrl = reqUrl;
		this.file = file;
		this.params  =params ;
	}
	public ImgUploadTask(String reqUrl, byte[] data, String params){
		this.reqUrl = reqUrl;
		this.data = data;
		this.params  =params ;
	}
	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);
		if(StringUtils.isNotBlank(body)){
			try{
				JSONObject obj = JSONObject.parseObject(body);
				callback(obj.getString("data"));
			}catch (Exception e){
				e.printStackTrace();
			}
		}else{
			callback("-1");
		}
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();

	}

	@Override
	protected Void doInBackground(Void... args) {
		try {

			if(file!=null){
				body = HttpPostClient.sendReqFileV2(reqUrl, "file", file, new String[]{"params"}, new String[]{params});
			}else{
				 body = HttpPostClient.sendReqFileV2(reqUrl, "file", data, new String[]{"params"}, new String[]{params});
			}

		}  catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public abstract void callback(String imgUrl);
 
	public static InputStream getInputStream(Bitmap bitmap){
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		InputStream is = new ByteArrayInputStream(baos.toByteArray());
		return is;
	}
	
	
}