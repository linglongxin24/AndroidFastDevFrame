package cn.bluemobi.dylan.pay.wechatpay;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import cn.bluemobi.dylan.pay.WeChatPay;

/**
 * @author lenovo
 */
public abstract class WeChatCallBackActivity extends Activity implements IWXAPIEventHandler {

    private static final String TAG = "WeChatCallBackActivity";

    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(this, getAppId());
        api.handleIntent(getIntent(), this);
    }

    /**
     * 获取app id
     *
     * @return appId
     */
    public abstract String getAppId();

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(BaseResp resp) {
        Log.d(TAG, "onPayFinish, errCode = " + resp.errCode);
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            if (BaseResp.ErrCode.ERR_OK == resp.errCode) {
                Toast.makeText(this, "支付成功", Toast.LENGTH_SHORT).show();
                if (WeChatPay.payListener != null) {
                    WeChatPay.payListener.paySuccess();
                }
            } else if (BaseResp.ErrCode.ERR_USER_CANCEL == resp.errCode) {
                Toast.makeText(this, "支付取消", Toast.LENGTH_SHORT).show();
                if (WeChatPay.payListener != null) {
                    WeChatPay.payListener.payCancel();
                }
            } else {
                Toast.makeText(this, "支付失败", Toast.LENGTH_SHORT).show();
                if (WeChatPay.payListener != null) {
                    WeChatPay.payListener.payFailed();
                }
            }
            WeChatCallBackActivity.this.finish();
        }
    }
}