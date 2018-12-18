package com.yipeng.libary.util;

import android.os.Handler;

import com.yipeng.libary.net.Net;

public abstract class ThreadBlockingTask<Params, Progress, Result> extends Thread {

	Handler handler = null;

	protected String type, msg;

	public String getMsg() {
		if (StringUtils.isNotBlank(msg)) {
			return msg;
		} else {
			return Net.ERROR_MSG;
		}
	}

	public boolean isOk() {
		return "1".equals(type);
	}

	public ThreadBlockingTask() {
		handler = new Handler();
	}

	public void run() {
		final Result result = doInBackground(params);
		handler.post(new Runnable() {

			@Override
			public void run() {
				onPostExecute(result);
			}
		});
	}

	Params[] params;

	public void execute(Params... params) {
		this.params = params;

		handler.post(new Runnable() {

			@Override
			public void run() {
				onPreExecute();
			}
		});
		this.start();
	}

	protected void onPreExecute() {
	}

	protected void onPostExecute(Result result) {
	}

	protected abstract Result doInBackground(Params... params);

	 
}
