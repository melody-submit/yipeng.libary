package com.yipeng.libary.pay.alipay;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.yipeng.libary.pay.OnPayCallBack;
import com.yipeng.libary.util.TLog;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class AliPay extends FragmentActivity {

//
//    // 商户PID
//    public static final String PARTNER = "2088131559108760";
//    // 商户收款账号
//    public static final String SELLER = "375142768@qq.com";
//    // 商户私钥，pkcs8格式
//
//    public static final String RSA_PRIVATE = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAMCZJOsV8fMzxNPy" +
//            "xRaZA6OdvuQAN2HDmRmT4CnZy0r2w9ugEGnhdBlAAFACs+GeXVcLX3jHudKD79sY" +
//            "nWGxpIL25lFN22EnMfmP9yo9Y3KAngXrU9bAtMnWowybBKOlzYTvX+WUaEpczPrE" +
//            "tiuSgEEj3LEmm7FKcJFOht1/NzNjAgMBAAECgYAmChlQlTUEY+UsN3bVz8L4Pd/G" +
//            "CITnukoy0xIjMfEFDVgBuMuFHhJgKlPleaMtnKmy13wzzr7U5VoqW+I5uWxUWRqO" +
//            "CotvYoR5PpjGXJS+5h0qgbrVZu1qWamOqu+6oaBlXUOndwmrPTuY7YDNXlClgZLt" +
//            "+ujx9/WH8Y6Uuk9ygQJBAPzoXHhJpdu63aTg7dImCKLJAyJUhjXH9TQ1+oz39V+P" +
//            "bDGWBz61xbkM2BvQeL0uZiP/dNu40ayLz8GazTeZhY8CQQDC9AFkGKe3S4YuctrZ" +
//            "dTJGVcoqCe66Vd0J0x2sniIQi815k1jm67+tbMoxaCxdHJ4y9e05qVfYmmUCgj+R" +
//            "5ZLtAkAQYaijrfmNSwRSSrN82jPS/3wxfBIZZ7NNR3XMLb96Chsy9wnPGQ0OmHNj" +
//            "EdTQRDIfgMt78/O43nF9k95ZDJunAkEAqRaNpk2kGdyNcvf3TSjTBCIgiDm5LzCN" +
//            "2JmdYTx+ey9mdg4Qy6ipxTdYX2XlJJVPuQ+IQ7zXDm1LMtPZYydlOQJAXNXn4Z72" +
//            "QvBP6DZVobrYef+mVpv3QqETX1LrslQXBEgCwUYEkL33zg6aliBQ2ng4/Wb1r4p/" +
//            "MjOdLEzJKR2ynw==";



    private static final int SDK_PAY_FLAG = 1;

    private static final int SDK_CHECK_FLAG = 2;

    public static final int BUY = 3;
    public static final int RECHARGE = 4;

    private Activity activity;
    private String subject;
    private String body;
    private String price;
    private String orderNumber;
    private OnPayCallBack callback;
    private String callUrl;  //支付宝回调URL

    /**
     * @param activity 上下文
     * @param subject  商品民称
     * @param body     商品描述
     * @param price    价格
     */
    public AliPay(Activity activity, String subject, String body, String price, String orderNumber,
    		String notifyurl,  OnPayCallBack callback) {
        this.activity = activity;
        this.body = body;
        this.subject = subject;
        this.price = price;
        this.orderNumber = orderNumber;
        
        callUrl = notifyurl;
        this.callback = callback;
        pay();
    }
    
    /**
     * @param activity 上下文
     * @param subject  商品民称
     * @param body     商品描述
     * @param price    价格
     */
//    public AliPay(Activity activity, String subject, String body, String price, String orderNumber,
//    			String notifyurl, OnPayCallBack callback) {
//        this.activity = activity;
//        this.body = body;
//        this.subject = subject;
//        this.price = price;
//        this.orderNumber = orderNumber;
//        this.callUrl = notifyurl;
//        this.callback = callback;
//        pay();
//    }

    /**
     * call alipay sdk pay. 调用SDK支付
     */
    public void pay() {
        if (TextUtils.isEmpty(PARTNER) || TextUtils.isEmpty(RSA_PRIVATE)
                || TextUtils.isEmpty(SELLER)) {
            new AlertDialog.Builder(this)
                    .setTitle("警告")
                    .setMessage("需要配置PARTNER | RSA_PRIVATE| SELLER")
                    .setPositiveButton("确定",
                            new DialogInterface.OnClickListener() {
                                public void onClick(
                                        DialogInterface dialoginterface, int i) {
                                    //
                                    finish();
                                }
                            }).show();
            return;
        }
        // 订单
        String orderInfo = getOrderInfo(subject, body, price);

        // 对订单做RSA 签名
        String sign = sign(orderInfo);
        try {
            // 仅需对sign 做URL编码
            sign = URLEncoder.encode(sign, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        // 完整的符合支付宝参数规范的订单信息
        final String payInfo = orderInfo + "&sign=\"" + sign + "\"&"
                + getSignType();

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造PayTask 对象
//                PayTask alipay = new PayTask(activity);
//                // 调用支付接口，获取支付结果
//                String result = alipay.pay(payInfo);
//                
//                TLog.i("alipay result::"+result);
//
//                Message msg = new Message();
//                msg.what = SDK_PAY_FLAG;
//                msg.obj = result;
//                mHandler.sendMessage(msg);
                
             // 构造PayTask 对象
				PayTask alipay = new PayTask(activity);
				// 调用支付接口，获取支付结果
				String result = alipay.pay(payInfo, true);

				Message msg = new Message();
				msg.what = SDK_PAY_FLAG;
				msg.obj = result;
				mHandler.sendMessage(msg);
				TLog.i("alipay result::"+result);
                
//                04-08 14:28:27.051: I/TLog(22405): alipay result::resultStatus={6001};memo={操作已经取消。};result={}

            }
        };

        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    /**
     * check whether the device has authentication alipay account.
     * 查询终端设备是否存在支付宝认证账户
     */
//    public void check(View v) {
//        Runnable checkRunnable = new Runnable() {
//
//            @Override
//            public void run() {
//                // 构造PayTask 对象
//                PayTask payTask = new PayTask(activity);
//                // 调用查询接口，获取查询结果
//                boolean isExist = payTask.checkAccountIfExist();
//
//                Message msg = new Message();
//                msg.what = SDK_CHECK_FLAG;
//                msg.obj = isExist;
//                mHandler.sendMessage(msg);
//            }
//        };
//
//        Thread checkThread = new Thread(checkRunnable);
//        checkThread.start();
//
//    }

    /**
     * create the order info. 创建订单信息
     */
    public String getOrderInfo(String subject, String body, String price) {

        // 签约合作者身份ID
        String orderInfo = "partner=" + "\"" + PARTNER + "\"";

        // 签约卖家支付宝账号
        orderInfo += "&seller_id=" + "\"" + SELLER + "\"";

        // 商户网站唯一订单号
        orderInfo += "&out_trade_no=" + "\"" + orderNumber + "\"";

        // 商品名称
        orderInfo += "&subject=" + "\"" + subject + "\"";

        // 商品详情
        orderInfo += "&body=" + "\"" + body + "\"";

        // 商品金额
        orderInfo += "&total_fee=" + "\"" + price + "\"";

        // 服务器异步通知页面路径
        orderInfo += "&notify_url=" + "\"" + callUrl + "\"";

        // 服务接口名称， 固定值
        orderInfo += "&service=\"mobile.securitypay.pay\"";

        // 支付类型， 固定值
        orderInfo += "&payment_type=\"1\"";

        // 参数编码， 固定值
        orderInfo += "&_input_charset=\"utf-8\"";

        // 设置未付款交易的超时时间
        // 默认30分钟，一旦超时，该笔交易就会自动被关闭。
        // 取值范围：1m～15d。
        // m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
        // 该参数数值不接受小数点，如1.5h，可转换为90m。
        orderInfo += "&it_b_pay=\"30m\"";

        // extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
        // orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

        // 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
//        orderInfo += "&return_url=\"m.alipay.com\"";

        // 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
        // orderInfo += "&paymethod=\"expressGateway\"";

        return orderInfo;
    }


    /**
     * sign the order info. 对订单信息进行签名
     *
     * @param content 待签名订单信息
     */
    public String sign(String content) {
        return SignUtils.sign(content, RSA_PRIVATE);
    }

    /**
     * get the sign type we use. 获取签名方式
     */
    public String getSignType() {
        return "sign_type=\"RSA\"";
    }


    /**
     * 支付返回结果
     */
    private Handler mHandler = new MyHandler(this);

	private static class MyHandler extends WeakReferenceHandler<AliPay> {

        public MyHandler(AliPay reference) {
            super(reference);
        }

        @Override
        protected void handleMessage(AliPay reference, Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((String) msg.obj);

                    // 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
                    String resultInfo = payResult.getResult();

                    String resultStatus = payResult.getResultStatus();

                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        reference.callback.success();
                        Toast.makeText(reference.activity, "支付成功",
                                Toast.LENGTH_SHORT).show();
                    }  else if (TextUtils.equals(resultStatus, "4000")) {
                        reference.callback.fail();
                        Toast.makeText(reference.activity, "未安装支付宝应用！",
                                Toast.LENGTH_SHORT).show();
                    }else {
                        // 判断resultStatus 为非“9000”则代表可能支付失败
                        // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(reference.activity, "支付结果确认中",
                                    Toast.LENGTH_SHORT).show();

                        } else {
                            reference.callback.fail();
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            Toast.makeText(reference.activity, "支付失败",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
                }
                case SDK_CHECK_FLAG: {
                    Toast.makeText(reference.activity, "检查结果为：" + msg.obj,
                            Toast.LENGTH_SHORT).show();
                    break;
                }
                default:
                    break;
            }
        }
    }
}
