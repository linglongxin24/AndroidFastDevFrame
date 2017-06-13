package cn.bluemobi.dylan.base;


import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import cn.bluemobi.dylan.base.utils.AppManager;


/**
 * 所有页面的基类
 * Created by YDL on 2017/4/18.
 */

public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener {
    /**
     * 标题栏
     */
    private Toolbar toolbar;
    /**
     * 中间的页面布局
     */
    private FrameLayout viewContent;
    /**
     * 标题
     */
    private TextView tvTitle;
    /**
     * 返回按钮点击接口
     */
    private OnClickListener onClickListenerTopLeft;
    /**
     * 右边按钮点击接口
     */
    private OnClickListener onClickListenerTopRight;
    /**
     * 右边按钮要显示的图标
     */
    private int menuResId;
    /**
     * 右边按钮要显示的文字
     */
    private String menuStr;
    /**
     * 上下文
     */
    protected Context mContext;


    public interface OnClickListener {
        void onClick();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_base_title_bar);
AppManager.getAppManager().addActivity(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        viewContent = (FrameLayout) findViewById(R.id.viewContent);
        tvTitle = (TextView) findViewById(R.id.tvTitle);

        //初始化设置 Toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
//        setTitleTextColor(Color.WHITE);
        setLeftButton();
        initTitleBar();
        //将继承 TopBarBaseActivity 的布局解析到 FrameLayout 里面
        LayoutInflater.from(this).inflate(getContentView(), viewContent);
        initViews(savedInstanceState);
        mContext = this;
        initData();
        addListener();
    }

    @Override
    protected void onDestroy() {

        AppManager.getAppManager().finishActivity(this);
        super.onDestroy();
    }

    /**
     * 设置标题栏文字
     *
     * @param title 要显示的文字
     */
    protected BaseActivity setTitle(String title) {
        if (!TextUtils.isEmpty(title)) {
            tvTitle.setText(title);
        }
        return this;
    }

    /**
     * 设置标题栏字体颜色
     *
     * @param titleTextColor 要显示的文字颜色
     */
    protected BaseActivity setTitleTextColor(int titleTextColor) {
        tvTitle.setTextColor(titleTextColor);
        return this;
    }

    /**
     * 设置标题栏字体大小
     *
     * @param titleTextSize 要显示的文字大小
     */
    protected BaseActivity setTitleTextSize(float titleTextSize) {
        tvTitle.setTextSize(titleTextSize);
        return this;
    }

    /**
     * 设置标题栏是否可见
     *
     * @param visible 是否可见
     */
    protected BaseActivity setTitleBarVisiable(boolean visible) {
        toolbar.setVisibility(visible ? View.VISIBLE : View.GONE);
        return this;
    }


    /**
     * 设置返回按钮，默认点击结束当前
     *
     * @return
     */
    private BaseActivity setLeftButton() {
        setLeftButtonIcon(R.drawable.ic_return_white_24dp)
                .setLeftButtonOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick() {
                        finish();
                    }
                });
        return this;
    }

    /**
     * 设置标题栏是否可见
     *
     * @param visible 是否可见
     */
    protected BaseActivity setLeftButtonVisible(boolean visible) {
        if (!visible) {
            toolbar.setNavigationIcon(null);
        }
        return this;
    }

    /**
     * 设置返回按钮图标
     *
     * @param iconResId
     * @return
     */
    protected BaseActivity setLeftButtonIcon(int iconResId) {
        toolbar.setNavigationIcon(iconResId);
        return this;
    }

    /**
     * 设置返回按钮监听
     *
     * @param onClickListener
     * @return
     */
    protected BaseActivity setLeftButtonOnClickListener(OnClickListener onClickListener) {
        this.onClickListenerTopLeft = onClickListener;
        return this;
    }

    /**
     * 设置最右面的按钮
     *
     * @param menuStr         要显示的文字
     * @param onClickListener 点击监听
     */
    protected BaseActivity setRightButton(String menuStr, OnClickListener onClickListener) {
        this.onClickListenerTopRight = onClickListener;
        this.menuStr = menuStr;
        return this;
    }

    /**
     * 设置最右面的按钮
     *
     * @param menuStr         要显示的文字
     * @param menuResId       要显示的图标
     * @param onClickListener 点击监听
     */
    protected BaseActivity setRightButton(String menuStr, int menuResId, OnClickListener onClickListener) {
        this.menuResId = menuResId;
        this.menuStr = menuStr;
        this.onClickListenerTopRight = onClickListener;
        return this;
    }

    /**
     * 设置最右面的按钮
     *
     * @param menuResId       要显示的图标
     * @param onClickListener 点击监听
     */
    protected BaseActivity setRightButton(int menuResId, OnClickListener onClickListener) {
        this.menuResId = menuResId;
        this.onClickListenerTopRight = onClickListener;
        return this;
    }

    /**
     * 加载标题栏右上角按钮
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (menuResId != 0 || !TextUtils.isEmpty(menuStr)) {
            getMenuInflater().inflate(R.menu.menu_base_title_bar, menu);
        }
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (menuResId != 0) {
            menu.findItem(R.id.menu_1).setIcon(menuResId);
        }
        if (!TextUtils.isEmpty(menuStr)) {
            menu.findItem(R.id.menu_1).setTitle(menuStr);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (onClickListenerTopLeft != null) {
                onClickListenerTopLeft.onClick();
            }
        } else if (item.getItemId() == R.id.menu_1) {
            if (onClickListenerTopRight != null) {
                onClickListenerTopRight.onClick();
            }
        }

        return true; // true 告诉系统我们自己处理了点击事件
    }

    private Toast toast;

    /**
     * 弹出Toast便捷方法
     *
     * @param charSequence
     */
    public void showToast(CharSequence charSequence) {
        if (null == toast) {
            toast = Toast.makeText(mContext, charSequence, Toast.LENGTH_SHORT);
        } else {
            toast.setText(charSequence);
        }
        toast.show();

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (null != toast) {
            toast.cancel();
        }

    }

    /**
     * 初始化设置标题栏
     */
    public abstract void initTitleBar();

    /**
     * 获取加载的布局
     *
     * @return
     */
    protected abstract int getContentView();

    /**
     * 初始化view控件
     */
    public abstract void initViews(Bundle savedInstanceState);

    /**
     * 初始化数据
     */
    public abstract void initData();

    /**
     * 给view添加事件监听
     */
    public abstract void addListener();

}

