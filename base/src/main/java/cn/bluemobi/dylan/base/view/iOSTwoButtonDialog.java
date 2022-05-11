package cn.bluemobi.dylan.base.view;

import android.app.Activity;
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

public class iOSTwoButtonDialog extends Dialog {

    private final LinearLayout ll_content;
    private final TextView textView;
    private final TextView tv_title;
    private final Button btn_one;
    private final Button btn_two;
    private final View line_title;
    private LeftButtonOnClick lefttButtonOnClick;
    private RightButtonOnClick rightButtonOnClick;
    private boolean isClickAutoCancel = true;
    private final Context mContext;

    public iOSTwoButtonDialog(@NonNull Context context) {
        super(context, R.style.ios_dialog_theme);
        this.mContext=context;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.ios_dialog_two_bt);

        Window dialogWindow = getWindow();
        dialogWindow.setGravity(Gravity.CENTER);

        ll_content = (LinearLayout) findViewById(R.id.ll_content);
        textView = (TextView) findViewById(R.id.text_message);
        tv_title = (TextView) findViewById(R.id.tv_title);
        btn_one = (Button) findViewById(R.id.btn_one);
        btn_two = (Button) findViewById(R.id.btn_two);
        line_title = findViewById(R.id.line_title);
        btn_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isClickAutoCancel) {
                    dismiss();
                }
                if (lefttButtonOnClick != null) {
                    lefttButtonOnClick.buttonLeftOnClick();
                }
            }
        });
        btn_two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { if(isClickAutoCancel){
                dismiss();
            }
                if (rightButtonOnClick != null) {
                    rightButtonOnClick.buttonRightOnClick();
                }
            }
        });
    }

    public iOSTwoButtonDialog setClickAutoCancel(boolean clickAutoCancel) {
        isClickAutoCancel = clickAutoCancel;
        return this;
    }
    public iOSTwoButtonDialog setMessageGrivity(int gravity) {
        textView.setGravity(gravity);
        return this;
    }

    public iOSTwoButtonDialog setLeftButtonText(CharSequence text) {
        btn_one.setText(text);
        return this;
    }

    public iOSTwoButtonDialog setRightButtonText(CharSequence text) {
        btn_two.setText(text);
        return this;
    }

    public iOSTwoButtonDialog setTitle(String title) {
        tv_title.setText(title);
        return this;
    }

    public iOSTwoButtonDialog setCenterCustomView(int layoutId) {
        LayoutInflater.from(getContext()).inflate(layoutId, ll_content);
        return this;
    }

    public iOSTwoButtonDialog setMessage(CharSequence message) {
        textView.setVisibility(TextUtils.isEmpty(message.toString()) ? View.GONE : View.VISIBLE);
        textView.setText(message);
        return this;
    }

    public iOSTwoButtonDialog setLeftButtonOnClickListener(LeftButtonOnClick lefttButtonOnClick) {
        this.lefttButtonOnClick = lefttButtonOnClick;
        return this;
    }

    public iOSTwoButtonDialog setRightButtonOnClickListener(RightButtonOnClick rightButtonOnClick) {
        this.rightButtonOnClick = rightButtonOnClick;
        return this;
    }

    public interface LeftButtonOnClick {
        void buttonLeftOnClick();
    }

    public interface RightButtonOnClick {
        void buttonRightOnClick();
    }

    public iOSTwoButtonDialog setTitleLineVisiBility(int visibility) {
        line_title.setVisibility(visibility);
        return this;
    }
    @Override
    public void show() {
        if (mContext instanceof Activity){
            Activity activity = (Activity) this.mContext;
            if(!activity.isFinishing()&&!activity.isDestroyed()){
                super.show();
            }
        }
    }
}
