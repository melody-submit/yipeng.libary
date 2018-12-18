package com.yipeng.libary.pay;

import android.app.Activity;

import com.yipeng.libary.pay.alipay.AliPay;
import com.yipeng.libary.ui.web.IWebCallback;

public class PayApiUtils {

 
	/**
	 * 支付宝支付
	 */
	public static void payByAlipay(Activity acitivty, String orderNo, String fee, String title, String desc, String notifyurl,  final IWebCallback callback) {
		// showProgressDialog();"付款"AliPay.BUY, 
		new AliPay(acitivty, title, desc, fee, orderNo, notifyurl, new OnPayCallBack() {
			@Override
			public void success() { 
				if(callback!=null){
					callback.callback("1");
				}
			}

			@Override
			public void fail() { 
				if(callback!=null){
					callback.callback("0");
				}
			}
		});
	}
	
	/**
	 * 支付宝支付
	 */
	public static void payByAlipayV2(Activity acitivty, String orderNo, String fee, String title, String desc, String notifyurl, final String callfn, final IWebCallback callback) {
		// showProgressDialog();"付款"
		new AliPay(acitivty, title, desc, fee, orderNo, notifyurl,  new OnPayCallBack() {
			@Override
			public void success() {
				// paying();
				if(callback!=null){
					callback.callback("1");
				} 
//				webview.loadUrl("javascript:"+callfn+"('1')");
			}

			@Override
			public void fail() {
				// hideProgressDialog();
				if(callback!=null){
					callback.callback("0");
				}
			}
		});
	}
}
