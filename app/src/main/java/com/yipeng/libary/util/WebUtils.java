package com.yipeng.libary.util;

import com.alibaba.fastjson.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class WebUtils {

	public final static int TIMEOUTS = 45000;

	public static void filter(String content) {

	}

	/**
	 * 发送get请求
	 * 
	 * @param url
	 * @return
	 */
	public static String sendReqGet(String url) {

		return null;
	}

	public static JSONObject sendReqJson(String url, Map<String, String> params) {
		String content = sendReq(url, params);
		JSONObject obj = null;
		if (StringUtils.isNotBlank(content)) {
			try {
				obj = JSONObject.parseObject(content);
			} catch (Exception e) {
			}
		}
		return obj;
	}
	public static class ReqResult{
		String body;
		Map<String, String> sessionMap;
		
		JSONObject jsonObj;
		
		public ReqResult(){
			
		}
		
		public JSONObject getJsonObj() {
			return jsonObj;
		}
		public void setJsonObj(JSONObject jsonObj) {
			this.jsonObj = jsonObj;
		}
		public ReqResult(String body){
			this.body = body;
		}
		public ReqResult(String body, Map<String, String> sessionMap){
			this.body = body;
			this.sessionMap = sessionMap;
		}
		public String getBody() {
			return body;
		}
		public void setBody(String body) {
			this.body = body;
		}
		public Map<String, String> getSessionMap() {
			return sessionMap;
		}
		public void setSessionMap(Map<String, String> sessionMap) {
			this.sessionMap = sessionMap;
		}
		
	}
	/**
	 * 发送post请求
	 * 
	 * @param url
	 * @param params
	 * @return
	 */
	public static String sendReq(String url, Map<String, String> params) {

		return null;
	}

	public static String sendReq(String url, HashMap<String, String> params) {
		String[] keys = null, values = null;
		if (params != null) {
			Iterator<Entry<String, String>> it = params.entrySet().iterator();
			keys = new String[params.size()];
			values = new String[params.size()];
			for (int i = 0; it.hasNext(); i++) {
				Entry<String, String> entry = it.next();
				keys[i] = entry.getKey();
				values[i] = entry.getValue();
			}
		}

		String result = sendReq(url, keys, values);

		return result;
	}

	public static JSONObject sendReqJson(String url, String[] keys, String... values) {
		String content = sendReq(url, keys, values);
		JSONObject obj = null;
		if (StringUtils.isNotBlank(content)) {
			try {
				obj = JSONObject.parseObject(content);
			} catch (Exception e) {
			}
		}
		return obj;
	}

	public static String sendReq(String url, String[] keys, String... values) {

		return null;
	}

	/**
	 * 上件文件
	 * 
	 * @param url
	 * @param keys
	 * @param values
	 * @return
	 */
	public static String sendReqFile(String url, String[] keys, File[] files, String[] formkeys, String[] formvals) {

		return null;//
	}

	public static String sendReqBodyDepct(String url, final String body) {

		return null;
	}
}
