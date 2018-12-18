package com.yipeng.libary.pay.wxpay;
public class WxPay {

	public String timestamp;//: "1445057292",
	public String noncestr;//: "60BD65B7451242DCA7398D8A006F19B6",
	public String partnerid;//: "1272726901",
	public String appId;//: "wxcf11be2c3f97ada6",
	public String prepayid;//: "wx201510171246529cba5e02360732312698",
//	public String package;//: "Sign=WXPay",
	public String paySign;//: "15E8B6E3DDF55A17FD3E994921E44877"
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public String getNoncestr() {
		return noncestr;
	}
	public void setNoncestr(String noncestr) {
		this.noncestr = noncestr;
	}
	public String getPartnerid() {
		return partnerid;
	}
	public void setPartnerid(String partnerid) {
		this.partnerid = partnerid;
	}
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public String getPrepayid() {
		return prepayid;
	}
	public void setPrepayid(String prepayid) {
		this.prepayid = prepayid;
	}
	public String getPaySign() {
		return paySign;
	}
	public void setPaySign(String paySign) {
		this.paySign = paySign;
	}

	
}