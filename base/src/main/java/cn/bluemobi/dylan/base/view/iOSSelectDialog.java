package cn.bluemobi.dylan.base.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import cn.bluemobi.dylan.base.R;
import cn.bluemobi.dylan.base.utils.Tools;

/**
 * Created by YDL on 2017/6/16.
 */

public class iOSSelectDialog extends Dialog {

    private final LinearLayout ll_content;
    private final TextView tv_title;
    private final Button btn_one;
    private View.OnClickListener onClickListener;

    public iOSSelectDialog(@NonNull Context context) {
        super(context, R.style.ios_dialog_theme);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.ios_dialog_select);

        Window dialogWindow = getWindow();
        dialogWindow.setGravity(Gravity.CENTER);

        ll_content = (LinearLayout) findViewById(R.id.ll_content);
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

    public iOSSelectDialog setTitle(String title) {
        tv_title.setText(title);
        return this;
    }

    public iOSSelectDialog setOneButtonText(String text) {
        btn_one.setText(text);
        return this;
    }

    public iOSSelectDialog setCenterCustomView(int layoutId) {
        LayoutInflater.from(getContext()).inflate(layoutId, ll_content);
        return this;
    }

    public iOSSelectDialog addMenuText(final String text) {
        TextView textView = new TextView(getContext());
        textView.setText(text);
        textView.setTextColor(Color.parseColor("#2C7AD7"));
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        textView.setGravity(Gravity.CENTER);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (menuClick != null) {
                    menuClick.onMenuClick(text);
                }
            }
        });
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Tools.DPtoPX(40, getContext()));
        textView.setLayoutParams(lp);
        ll_content.addView(textView, lp);
        return this;
    }

    public iOSSelectDialog addMenuTexts(List<String> texts) {
        if (texts != null) {
            for (String text : texts) {
                addMenuText(text);
            }
        }
        return this;
    }

    public iOSSelectDialog setButtonOnClickListener(final View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
        return this;
    }

    private MenuClick menuClick;

    public iOSSelectDialog setMenuClickLisentner(MenuClick menuClick) {
        this.menuClick = menuClick;
        return this;
    }

    public interface MenuClick {
        void onMenuClick(String menuText);
    }
}
