package com.yipeng.libary.util;

import android.content.Context;
import android.os.Handler;

import com.yipeng.libary.R;
import com.yipeng.libary.ui.web.JsCallback;
import com.yipeng.libary.ui.web.WsWebView;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.Platform.ShareParams;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.ShareContentCustomizeCallback;

public class ShareSdkUtil {
	Context ctx;

	public ShareSdkUtil(Context ctx) {
		this.ctx = ctx;
	}

	public Context getContext() {
		return ctx;
	}

//	public static String shareImageUrl = Net.HOST + "/images/logo.png";

	public static void showShare(Context ctx,  String title, String content, String shareUrl, final String imageUrl,
			JsCallback callback) {
		final OnekeyShare oks = new OnekeyShare();
		final Handler handler = new Handler(ctx.getMainLooper());
		final JsCallback jsCallback = callback;
		
		oks.setTitle(title);
		oks.setTitleUrl(shareUrl);
		oks.setText(content);
		oks.setUrl(shareUrl);
		oks.setComment(ctx.getString(R.string.app_name));
		oks.setSite(ctx.getString(R.string.app_name));
		oks.setSiteUrl(shareUrl);

//		BDLocation location = BCApplication.bdLocation;
//		if (location != null) {
//			oks.setLatitude((float) location.getLatitude());
//			oks.setLongitude((float) location.getLongitude());
//		}
		
		oks.setSilent(true);

		// 令编辑页面显示为Dialog模式
		oks.setDialogMode();

		// 在自动授权时可以禁用SSO方式
		oks.disableSSOWhenAuthorize();

		oks.setShareContentCustomizeCallback(new ShareContentCustomizeCallback() {

			@Override
			public void onShare(Platform platform, ShareParams sp) {

				platform.setPlatformActionListener(new PlatformActionListener() {

					// 操作失败的处理代码
					public void onError(Platform platform, int action, Throwable t) {
						
						ToastUtil.show(t.toString());
						if (jsCallback!=null) {
							TaskExecutor.runTaskOnUiThread(new Runnable() {
	
								@Override
								public void run() {
									try {
										jsCallback.apply(DeviceUtils.getDeviceId(), "-1", "error");
									} catch (JsCallback.JsCallbackException je) {
										je.printStackTrace();
									}
								}
							});
						} 

					}

					public void onComplete(Platform platform, int action, HashMap<String, Object> res) {
						if (jsCallback!=null) {
							TaskExecutor.runTaskOnUiThread(new Runnable() {
	
								@Override
								public void run() {
									try {
										jsCallback.apply(DeviceUtils.getDeviceId(), "1", "ok");
									} catch (JsCallback.JsCallbackException je) {
										je.printStackTrace();
									}
								}
							});
						} 
					}

					// 操作取消的处理代码
					public void onCancel(Platform platform, int action) {
						if (jsCallback!=null) {
							TaskExecutor.runTaskOnUiThread(new Runnable() {
	
								@Override
								public void run() {
									try {
										jsCallback.apply(DeviceUtils.getDeviceId(), "0", "cancel");
									} catch (JsCallback.JsCallbackException je) {
										je.printStackTrace();
									}
								}
							});
						} 

					}
				});

				if ("WechatMoments".equals(platform.getName()) || "Wechat".equals(platform.getName()) || "QQ".equals(platform.getName())) {
					sp.setShareType(Platform.SHARE_WEBPAGE);
					
					if (StringUtils.isNotBlank(imageUrl)) {
						sp.setImageUrl(imageUrl);
					}
				}
			}
		});
		oks.show(ctx);
	}


	public static void showShareV2(final WsWebView webView, Context ctx, String title, String content, String shareUrl, final String imageUrl,
                                   final String callback) {
		final OnekeyShare oks = new OnekeyShare();
		final Handler handler = new Handler(ctx.getMainLooper());

		oks.setTitle(title);
		oks.setTitleUrl(shareUrl);
		oks.setText(content);
		oks.setUrl(shareUrl);
		oks.setComment(ctx.getString(R.string.app_name));
		oks.setSite(ctx.getString(R.string.app_name));
		oks.setSiteUrl(shareUrl);


		oks.setSilent(true);

		// 令编辑页面显示为Dialog模式
		oks.setDialogMode();

		// 在自动授权时可以禁用SSO方式
		oks.disableSSOWhenAuthorize();

		oks.setShareContentCustomizeCallback(new ShareContentCustomizeCallback() {

			@Override
			public void onShare(Platform platform, ShareParams sp) {

				platform.setPlatformActionListener(new PlatformActionListener() {

					// 操作失败的处理代码
					public void onError(Platform platform, int action, Throwable t) {

						ToastUtil.show(t.toString());
						if (callback!=null) {
							TaskExecutor.runTaskOnUiThread(new Runnable() {

								@Override
								public void run() {
                                    webView.callFunction(callback, "-1");
								}
							});
						}

					}

					public void onComplete(Platform platform, int action, HashMap<String, Object> res) {
						if (callback!=null) {
							TaskExecutor.runTaskOnUiThread(new Runnable() {

								@Override
								public void run() {
                                    webView.callFunction(callback, "1");
								}
							});
						}
					}

					// 操作取消的处理代码
					public void onCancel(Platform platform, int action) {
						if (callback!=null) {
							TaskExecutor.runTaskOnUiThread(new Runnable() {

								@Override
								public void run() {
                                    webView.callFunction(callback, "0");
								}
							});
						}

					}
				});

				if ("WechatMoments".equals(platform.getName()) || "Wechat".equals(platform.getName()) || "QQ".equals(platform.getName())) {
					sp.setShareType(Platform.SHARE_WEBPAGE);

					if (StringUtils.isNotBlank(imageUrl)) {
						sp.setImageUrl(imageUrl);
					}
				}
			}
		});
		oks.show(ctx);
	}

}
