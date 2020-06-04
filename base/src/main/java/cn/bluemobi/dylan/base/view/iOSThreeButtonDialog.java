package cn.bluemobi.dylan.base.view;

import android.app.Dialog;
import android.content.Context;
import androidx.annotation.NonNull;
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

public class iOSThreeButtonDialog extends Dialog {

    private final LinearLayout ll_content;
    private final TextView textView;
    private final TextView tv_title;
    private final Button btn_one;
    private final Button btn_two;
    private final Button btn_three;
    private final View line_title;
    private LeftButtonOnClick lefttButtonOnClick;
    private CenterButtonOnClick centerButtonOnClick;
    private RightButtonOnClick rightButtonOnClick;
    private boolean isClickAutoCancel = true;

    public iOSThreeButtonDialog(@NonNull Context context) {
        super(context, R.style.ios_dialog_theme);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.ios_dialog_three_bt);

        Window dialogWindow = getWindow();
        dialogWindow.setGravity(Gravity.CENTER);

        ll_content = (LinearLayout) findViewById(R.id.ll_content);
        textView = (TextView) findViewById(R.id.text_message);
        tv_title = (TextView) findViewById(R.id.tv_title);
        btn_one = (Button) findViewById(R.id.btn_one);
        btn_two = (Button) findViewById(R.id.btn_two);
        btn_three = findViewById(R.id.btn_three);
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
            public void onClick(View v) {
                if (isClickAutoCancel) {
                    dismiss();
                }
                if (centerButtonOnClick != null) {
                    centerButtonOnClick.buttonCenterOnClick();
                }
            }
        });
        btn_three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isClickAutoCancel) {
                    dismiss();
                }
                if (rightButtonOnClick != null) {
                    rightButtonOnClick.buttonRightOnClick();
                }
            }
        });
    }

    public iOSThreeButtonDialog setClickAutoCancel(boolean clickAutoCancel) {
        isClickAutoCancel = clickAutoCancel;
        return this;
    }

    public iOSThreeButtonDialog setMessageGrivity(int gravity) {
        textView.setGravity(gravity);
        return this;
    }

    public iOSThreeButtonDialog setLeftButtonText(CharSequence text) {
        btn_one.setText(text);
        return this;
    }
    public iOSThreeButtonDialog setCenterButtonText(CharSequence text) {
        btn_two.setText(text);
        return this;
    }

    public iOSThreeButtonDialog setRightButtonText(CharSequence text) {
        btn_three.setText(text);
        return this;
    }

    public iOSThreeButtonDialog setTitle(String title) {
        tv_title.setText(title);
        return this;
    }

    public iOSThreeButtonDialog setCenterCustomView(int layoutId) {
        LayoutInflater.from(getContext()).inflate(layoutId, ll_content);
        return this;
    }

    public iOSThreeButtonDialog setMessage(CharSequence message) {
        textView.setVisibility(TextUtils.isEmpty(message.toString()) ? View.GONE : View.VISIBLE);
        textView.setText(message);
        return this;
    }

    public iOSThreeButtonDialog setLeftButtonOnClickListener(LeftButtonOnClick lefttButtonOnClick) {
        this.lefttButtonOnClick = lefttButtonOnClick;
        return this;
    }

    public iOSThreeButtonDialog setRightButtonOnClickListener(RightButtonOnClick rightButtonOnClick) {
        this.rightButtonOnClick = rightButtonOnClick;
        return this;
    }

    public iOSThreeButtonDialog setCenterButtonOnClick(CenterButtonOnClick centerButtonOnClick) {
        this.centerButtonOnClick = centerButtonOnClick;
        return this;
    }

    public interface LeftButtonOnClick {
        void buttonLeftOnClick();
    }

    public interface RightButtonOnClick {
        void buttonRightOnClick();
    }

    public interface CenterButtonOnClick {
        void buttonCenterOnClick();
    }

    public iOSThreeButtonDialog setTitleLineVisiBility(int visibility) {
        line_title.setVisibility(visibility);
        return this;
    }
}
