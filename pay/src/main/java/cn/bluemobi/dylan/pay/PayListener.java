package cn.bluemobi.dylan.pay;

/**
 * 支付回调
 * Created by ydl on 2017/9/27.
 */

public interface PayListener {
    /**
     * 支付成功
     */
    void paySuccess();

    /**
     * 支付失败
     */
    void payFailed();

    /**
     * 支付取消
     */
    void payCancel();
}
