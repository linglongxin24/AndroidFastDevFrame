package cn.bluemobi.dylan.pay;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.alipay.sdk.app.PayTask;
import com.orhanobut.logger.Logger;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;

import cn.bluemobi.dylan.pay.alipay.OrderInfoUtil2_0;
import cn.bluemobi.dylan.pay.alipay.PayResult;

/**
 * Created by ydl on 2017/9/27.
 */

public class AliPay {
    /**
     * 当前支付的Activity
     */
    private Activity mActivity;
    /**
     * appid
     */
    private String app_id;
    /**
     * 私钥
     */
    private String RSA_PRIVATE;
    /**
     * 商品主题
     */
    private String subject;
    /**
     * 商品描述
     */
    private String body;
    /**
     * 交易订单号
     */
    private String out_trade_no;
    /**
     * 支付金额
     */
    private String pay_amount;
    /**
     * 回调地址
     */
    private String notify_url;
    /**
     * 是否使用rsa2加密
     */
    private boolean rsa2;
    /**
     * 支付监听
     */
    private PayListener payListener;

    public AliPay(Activity mActivity, String app_id, String RSA_PRIVATE, String subject, String body, String out_trade_no, String pay_amount, String notify_url, boolean rsa2) {
        this.mActivity = mActivity;
        this.app_id = app_id;
        this.RSA_PRIVATE = RSA_PRIVATE;
        this.subject = subject;
        this.body = body;
        this.out_trade_no = out_trade_no;
        this.pay_amount = pay_amount;
        this.notify_url = notify_url;
        this.rsa2 = rsa2;
    }

    private String orderInfo;

    /**
     * 构造函数，服务端返回的订单信息
     *
     * @param orderInfo 订单信息
     */
    public AliPay(Activity mActivity, String orderInfo) {
        this.mActivity = mActivity;
        this.orderInfo = orderInfo;
    }

    public void pay(PayListener payListener) {
        this.payListener = payListener;
        if (TextUtils.isEmpty(orderInfo)) {
            Map<String, String> params = OrderInfoUtil2_0.buildOrderParamMap(app_id, out_trade_no, notify_url, pay_amount, subject, body, rsa2);
            String orderParam = OrderInfoUtil2_0.buildOrderParam(params);
            String sign = OrderInfoUtil2_0.getSign(params, RSA_PRIVATE);
            orderInfo = orderParam + "&" + sign;
        }
        try {
            Logger.d("支付宝订单信息orderInfo=" + URLDecoder.decode(orderInfo, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(mActivity);
                Map<String, String> result = alipay.payV2(orderInfo, true);
                Message msg = new Message();
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            @SuppressWarnings("unchecked")
            PayResult payResult = new PayResult((Map<String, String>) msg.obj);
            Logger.d("payResult=" + payResult);
            /**
             对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
             同步返回需要验证的信息
             该笔订单真实的支付结果，需要依赖服务端的异步通知。
             */
            String resultInfo = payResult.getResult();
            String resultStatus = payResult.getResultStatus();
            // 判断resultStatus 为9000则代表支付成功
            if (TextUtils.equals(resultStatus, "9000")) {
                //成功
                if (payListener != null) {
                    payListener.paySuccess();
                }
            } else if (TextUtils.equals(resultStatus, "6001")) {
                //取消
                if (payListener != null) {
                    payListener.payCancel();
                }
            } else {
                //失败
                if (payListener != null) {
                    payListener.payFailed();
                }
            }
        }

    };

}
