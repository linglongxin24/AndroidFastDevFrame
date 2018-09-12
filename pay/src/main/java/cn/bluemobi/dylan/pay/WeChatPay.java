package cn.bluemobi.dylan.pay;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Xml;
import android.widget.Toast;

import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.xmlpull.v1.XmlPullParser;

import java.io.StringReader;
import java.security.MessageDigest;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import cn.bluemobi.dylan.pay.wechatpay.URLConnectionUtils;

/**
 * 微信支付工具类
 *
 * @author lenovo
 * @date 2018/1/3
 */

public class WeChatPay {

    /**
     * 微信统一下单接口地址
     */
    public final String WECHAT_UNIFIED_ORDER = "https://api.mch.weixin.qq.com/pay/unifiedorder";
    /**
     * 订单支付监听
     */
    public static PayListener payListener;

    /**
     * 应用上下文
     */
    private Context mContext;


    /**
     * 构造函数
     *
     * @param mContext 应用上下文
     */
    public WeChatPay(Context mContext) {
        this.mContext = mContext;
    }

    /**
     * 设置监听
     *
     * @param payListener
     */
    public WeChatPay setPayLisitener(PayListener payListener) {
        WeChatPay.payListener = payListener;
        return this;
    }

    /**
     * 微信支付统一下单接口地址: https://api.mch.weixin.qq.com/pay/unifiedorder
     *
     * @param appId        在微信开发平台生成的AppId
     * @param mch_id       商户ID
     * @param partnerKey   在商户平台生成的密钥
     * @param bodyString   商品描述交易描述。例如：腾讯充值中心-QQ会员充值
     * @param out_trade_no 服务端返回的支付订单号
     * @param total_fee    订单金额
     * @param notifyUrl    微信回调服务器接口地址
     */
    public WeChatPay pay(final String appId, final String mch_id, final String partnerKey, final String bodyString, final String out_trade_no, final String total_fee, final String notifyUrl) {
        Toast.makeText(mContext, "获取订单中...", Toast.LENGTH_SHORT).show();
        new AsyncTask<Void, Void, Map<String, String>>() {
            @Override
            protected Map<String, String> doInBackground(Void... voids) {
                // 统一下单接口地址
                String url = String.format(WECHAT_UNIFIED_ORDER);
                String result = URLConnectionUtils.requestPost(url, getProductArgs(appId, mch_id, partnerKey, bodyString, out_trade_no, total_fee, notifyUrl));
                try {
                    return decodeXml(result);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Map<String, String> result) {
                if (result != null && result.containsKey("return_code") && result.containsKey("result_code")) {
                    if (!"SUCCESS".equals(result.get("result_code"))) {
                        Toast.makeText(mContext, "签名错误", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    //获取订单预支付id
                    String prepay_id = result.get("prepay_id");
                    // 扩展字段，暂填写固定值Sign=WXPay
                    String packageValue = "Sign=WXPay";
                    // 随机数字符串
                    String nonceStr = result.get("nonce_str");
                    // 时间戳
                    final String timeStamp = String.valueOf(getTimeStamp());

                    Map<String, Object> signParams = new TreeMap<String, Object>(new Comparator<String>() {
                        @Override
                        public int compare(String s, String t1) {
                            return s.compareTo(t1);
                        }
                    });
                    signParams.put("appid", appId);
                    signParams.put("partnerid", mch_id);
                    signParams.put("prepayid", prepay_id);
                    signParams.put("package", packageValue);
                    signParams.put("noncestr", nonceStr);
                    signParams.put("timestamp", timeStamp);
                    String sign = getAppSign(signParams, partnerKey);
                    pay(appId, mch_id, prepay_id, packageValue, nonceStr, timeStamp, sign, "App");
                } else {
                    Toast.makeText(mContext, "非法交易", Toast.LENGTH_SHORT).show();
                }
            }
        }.execute();
        return this;
    }

    /**
     * 根据微信官方支付文档设置需要的参数
     *
     * @param appId        在微信开发平台生成的AppId
     * @param partnerId    商户ID
     * @param partnerKey   在商户平台生成的密钥
     * @param bodyString   商品描述交易描述。例如：腾讯充值中心-QQ会员充值
     * @param out_trade_no 服务端返回的支付订单号
     * @param total_fee    订单金额，单位分
     * @param notifyUrl    微信回调服务器接口地址
     * @return xml参数
     */
    private String getProductArgs(String appId, String partnerId, String partnerKey, String bodyString, String out_trade_no, String total_fee, String notifyUrl) {
        try {
            // 获取随机字符串，方法在本贴下面给出
            String nonceString = getNonceString();
            Map<String, Object> packageParams = new TreeMap<>(new Comparator<String>() {
                @Override
                public int compare(String s, String t1) {
                    return s.compareTo(t1);
                }
            });
            // AppId
            packageParams.put("appid", appId);
            // 商品描述交易描述。例如：腾讯充值中心-QQ会员充值
            packageParams.put("body", bodyString);
            // 微信支付分配的商户号
            packageParams.put("mch_id", partnerId);
            // 随机字符串
            packageParams.put("nonce_str", nonceString);
            // 微信回调服务端地址
            packageParams.put("notify_url", notifyUrl);
            // 服务端返回的支付订单号
            packageParams.put("out_trade_no", out_trade_no);
            // 设备IP，当前设备IP，默认为“WEB”
            packageParams.put("spbill_create_ip", "172.0.0.1");
            // 订单总金额，单位为分。根据商户商品价格定义，此处需自行设置
            packageParams.put("total_fee", String.valueOf(Double.valueOf(Double.parseDouble(total_fee) * 100).intValue()));
            // 交易类型
            packageParams.put("trade_type", "APP");
            // 签名，方法在本贴下面
            packageParams.put("sign", getAppSign(packageParams, partnerKey));
            String xmls = toXml(packageParams);
            // 设置编码格式
            return new String(xmls.toString().getBytes());
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 发起请求
     *
     * @param appId        在微信开发平台生成的AppId
     * @param partnerId    商户ID
     * @param prepayId     支付订单Id
     * @param packageValue 扩展字段，暂填写固定值Sign=WXPay
     * @param nonceStr     随机数字符串
     * @param timeStamp    时间戳
     * @param sign         签名
     * @param extData      额外参数
     */
    public WeChatPay pay(String appId, String partnerId, String prepayId, String packageValue, String nonceStr, String timeStamp, String sign, String extData) {
        PayReq mPayReq = new PayReq();
        // AppId
        mPayReq.appId = appId;
        // 微信支付分配的商户号
        mPayReq.partnerId = partnerId;
        // 支付订单Id
        mPayReq.prepayId = prepayId;
        // 扩展字段，暂填写固定值Sign=WXPay
        mPayReq.packageValue = packageValue;
        // 随机数字符串
        mPayReq.nonceStr = nonceStr;
        // 时间戳
        mPayReq.timeStamp = timeStamp;
        //签名
        mPayReq.sign = sign;
        mPayReq.extData = extData;
        IWXAPI api = WXAPIFactory.createWXAPI(mContext, appId);
        api.registerApp(appId);
        if (!api.isWXAppInstalled()) {
            Toast.makeText(mContext, "您还未安装微信", Toast.LENGTH_SHORT).show();
            return this;
        }
        api.getWXAppSupportAPI();
//        if (!api.isWXAppSupportAPI()) {
//            Toast.makeText(mContext, "您的微信不支持微信支付或微信版本过低", Toast.LENGTH_SHORT).show();
//            return this;
//        }
        api.sendReq(mPayReq);
        return this;
    }

    /**
     * 获取随机字符串
     *
     * @return
     */
    private String getNonceString() {
        Random random = new Random();
        return getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes());
    }

    /**
     * md4加密
     *
     * @param buffer
     * @return
     */
    private final String getMessageDigest(byte[] buffer) {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            MessageDigest mdTemp = MessageDigest.getInstance("MD5");
            mdTemp.update(buffer);
            byte[] md = mdTemp.digest();
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 签名
     *
     * @param params
     * @param partnerKey
     * @return
     */
    private String getAppSign(Map<String, Object> params, String partnerKey) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Object> stringObjectEntry : params.entrySet()) {
            sb.append(stringObjectEntry.getKey());
            sb.append('=');
            sb.append(stringObjectEntry.getValue());
            sb.append('&');
        }
        sb.append("key=");
        // 商户平台生成的密钥
        sb.append(partnerKey);
        String appSign = getMessageDigest(sb.toString().getBytes()).toUpperCase();
        return appSign;
    }

    /**
     * 获取时间戳
     *
     * @return
     */
    private long getTimeStamp() {
        return System.currentTimeMillis() / 1000L;
    }

    /**
     * 转换成xml
     *
     * @param params
     * @return
     */
    private String toXml(Map<String, Object> params) {
        StringBuilder sb = new StringBuilder();
        sb.append("<xml>");
        for (Map.Entry<String, Object> stringObjectEntry : params.entrySet()) {
            sb.append("<" + stringObjectEntry.getKey() + ">");
            sb.append(stringObjectEntry.getValue());
            sb.append("</" + stringObjectEntry.getKey() + ">");
        }
        sb.append("</xml>");
        return sb.toString();
    }

    /**
     * 解析xml
     *
     * @param content
     * @return
     */
    private Map<String, String> decodeXml(String content) {
        try {
            Map<String, String> xml = new HashMap<String, String>();
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(new StringReader(content));
            int event = parser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {
                String nodeName = parser.getName();
                switch (event) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        if ("xml".equals(nodeName) == false) {
                            xml.put(nodeName, parser.nextText());
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        break;
                }
                event = parser.next();
            }
            return xml;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
