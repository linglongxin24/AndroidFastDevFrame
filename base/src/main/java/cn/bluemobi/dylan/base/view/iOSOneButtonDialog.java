package cn.bluemobi.dylan.base.view;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.bluemobi.dylan.base.R;

/**
 * Created by YDL on 2017/6/16.
 */

public class iOSOneButtonDialog extends Dialog {

    private final LinearLayout ll_content;
    private final TextView textView;
    private final TextView tv_title;
    private final Button btn_one;
    private View.OnClickListener onClickListener;

    public iOSOneButtonDialog(@NonNull Context context) {
        super(context, R.style.ios_dialog_theme);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.ios_dialog_one_bt);

        Window dialogWindow = getWindow();
        dialogWindow.setGravity(Gravity.CENTER);

        ll_content = (LinearLayout) findViewById(R.id.ll_content);
        textView = (TextView) findViewById(R.id.text_message);
        tv_title = (TextView) findViewById(R.id.tv_title);
        btn_one = (Button) findViewById(R.id.btn_one);
        btn_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (onClickListener != null) {
                    onClickListener.onClick(v);
                }
            }
        });
    }

    public iOSOneButtonDialog setMessageGrivity(int gravity) {
        textView.setGravity(gravity);
        return this;
    }

    public iOSOneButtonDialog setTitle(String title) {
        tv_title.setText(title);
        return this;
    }

    public iOSOneButtonDialog setOneButtonText(String text) {
        btn_one.setText(text);
        return this;
    }

    public iOSOneButtonDialog setMessage(String message) {
        textView.setVisibility(TextUtils.isEmpty(message) ? View.GONE : View.VISIBLE);
        textView.setText(message);
        return this;
    }

    public iOSOneButtonDialog setCenterCustomView(int layoutId) {
        LayoutInflater.from(getContext()).inflate(layoutId, ll_content);
        return this;
    }

    public iOSOneButtonDialog setButtonOnClickListener(final View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
        return this;
    }
}
