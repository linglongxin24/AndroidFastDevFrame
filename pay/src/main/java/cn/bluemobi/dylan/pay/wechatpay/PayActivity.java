package cn.bluemobi.dylan.pay.wechatpay;


import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.tencent.mm.opensdk.constants.Build;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import cn.bluemobi.dylan.pay.R;
import cn.bluemobi.dylan.pay.WeChatPay;

public class PayActivity extends Activity {

    private IWXAPI api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay);

        api = WXAPIFactory.createWXAPI(this, "wxb4ba3c02aa476ea1");

        Button appayBtn = (Button) findViewById(R.id.appay_btn);
        appayBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final String url = "http://wxpay.wxutil.com/pub_v2/app/app_pay.php";
                Button payBtn = (Button) findViewById(R.id.appay_btn);
                payBtn.setEnabled(false);
                Toast.makeText(PayActivity.this, "获取订单中...", Toast.LENGTH_SHORT).show();
                try {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            String result = URLConnectionUtils.requestgGet(url);
                            Message message = new Message();
                            message.obj = result;
                            handler.sendMessage(message);
                        }
                    }).start();

                } catch (Exception e) {
                    Log.e("PAY_GET", "异常：" + e.getMessage());
                    Toast.makeText(PayActivity.this, "异常：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                payBtn.setEnabled(true);
            }
        });
        Button checkPayBtn = (Button) findViewById(R.id.check_pay_btn);
        checkPayBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                boolean isPaySupported = api.getWXAppSupportAPI() >= Build.PAY_SUPPORTED_SDK_INT;
                Toast.makeText(PayActivity.this, String.valueOf(isPaySupported), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                String content = (String) msg.obj;
                if (!TextUtils.isEmpty(content)) {
                    Log.e("get server pay params:", content);
                    JSONObject json = new JSONObject(content);
                    if (null != json && !json.has("retcode")) {
                        PayReq req = new PayReq();
                        //req.appId = "wxf8b4f85f3a794e77";  // 测试用appId
                        req.appId = json.getString("appid");
                        req.partnerId = json.getString("partnerid");
                        req.prepayId = json.getString("prepayid");
                        req.nonceStr = json.getString("noncestr");
                        req.timeStamp = json.getString("timestamp");
                        req.packageValue = json.getString("package");
                        req.sign = json.getString("sign");
                        // optional
                        req.extData = "app data";
                        Toast.makeText(PayActivity.this, "正常调起支付", Toast.LENGTH_SHORT).show();
                        // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
                        api.sendReq(req);
                    } else {
                        Log.d("PAY_GET", "返回错误" + json.getString("retmsg"));
                        Toast.makeText(PayActivity.this, "返回错误" + json.getString("retmsg"), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.d("PAY_GET", "服务器请求错误");
                    Toast.makeText(PayActivity.this, "服务器请求错误", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

}
