package cn.bluemobi.dylan.fastdev.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;


import java.util.Timer;
import java.util.TimerTask;

import cn.bluemobi.dylan.fastdev.R;

public class MyGallery extends Gallery implements OnItemSelectedListener {
    /**
     * 存储上一个选择项的Index
     */
    private int preSelImgIndex = 0;


    /**
     * 这里的数值，限制了每次滚动的最大长度，图片宽度为480PX。这里设置600效果好一些。 这个值越大，滚动的长度就越大。
     * 也就是会出现一次滚动跨多个Image。这里限制长度后，每次滚动只能跨一个Image
     */
    private static final int timerAnimation = 1;
    private final Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case timerAnimation:
                    int position = getSelectedItemPosition();
                    if (position >= (getCount() - 1)) {
                        onKeyDown(KeyEvent.KEYCODE_DPAD_LEFT, null);
                    } else {
                        onKeyDown(KeyEvent.KEYCODE_DPAD_RIGHT, null);
                    }
                    break;

                default:
                    break;
            }
        }

    };

    private final Timer timer = new Timer();
    private final TimerTask task = new TimerTask() {
        public void run() {
            if (autoPlay) {
                mHandler.sendEmptyMessage(timerAnimation);
            } else {
                timer.cancel();
            }
        }
    };
    private boolean autoPlay = true;

    public void setAutoPlay(boolean autoPlay) {
        this.autoPlay = autoPlay;
    }

    public MyGallery(Context paramContext) {
        super(paramContext);
        timer.schedule(task, 3000, 3000);
    }

    public MyGallery(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
        timer.schedule(task, 3000, 3000);

    }

    public MyGallery(Context paramContext, AttributeSet paramAttributeSet,
                     int paramInt) {
        super(paramContext, paramAttributeSet, paramInt);
        timer.schedule(task, 3000, 3000);
        init();
    }

    private void init() {
    }

    private boolean isScrollingLeft(MotionEvent paramMotionEvent1,
                                    MotionEvent paramMotionEvent2) {
        float f2 = paramMotionEvent2.getX();
        float f1 = paramMotionEvent1.getX();
        return f2 > f1;
    }

    public boolean onFling(MotionEvent paramMotionEvent1,
                           MotionEvent paramMotionEvent2, float paramFloat1, float paramFloat2) {
        int keyCode;
        if (isScrollingLeft(paramMotionEvent1, paramMotionEvent2)) {
            keyCode = KeyEvent.KEYCODE_DPAD_LEFT;
        } else {
            keyCode = KeyEvent.KEYCODE_DPAD_RIGHT;
        }
        onKeyDown(keyCode, null);
        return true;
    }

    private int count = 0;
    private LinearLayout ll_focus_indicator_container;

    public void init(int count, LinearLayout ll_focus_indicator_container) {
        this.count = count;
        this.ll_focus_indicator_container = ll_focus_indicator_container;
        InitFocusIndicatorContainer();
        this.setOnItemSelectedListener(this);
    }

    private void InitFocusIndicatorContainer() {
        ll_focus_indicator_container.removeAllViews();
        if (count == 0) {
            destroy();
            return;
        }
        if(count == 1){
            destroy();
        }
        for (int i = 0; i < count; i++) {
            ImageView localImageView = new ImageView(getContext());
            localImageView.setId(i);
            ImageView.ScaleType localScaleType = ImageView.ScaleType.FIT_XY;
            localImageView.setScaleType(localScaleType);
            LinearLayout.LayoutParams localLayoutParams = new LinearLayout.LayoutParams(
                    155, 11);
            localLayoutParams.weight = 1;
            localImageView.setLayoutParams(localLayoutParams);
            localImageView.setPadding(0, 0, 0, 0);
            localImageView.setImageDrawable(new ColorDrawable(Color.TRANSPARENT));
            ll_focus_indicator_container.addView(localImageView);
        }
    }


    public void destroy() {
        timer.cancel();
        mHandler.removeCallbacks(task);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int selIndex, long l) {
        //修改上一次选中项的背景
        if (count == 0) {
            return;
        }
        selIndex = selIndex % count;

        ImageView preSelImg = (ImageView) ll_focus_indicator_container
                .findViewById(preSelImgIndex);
        if (preSelImg == null) {
            return;
        }
        preSelImg.setImageDrawable(new ColorDrawable(Color.TRANSPARENT));
        //修改当前选中项的背景
        ImageView curSelImg = (ImageView) ll_focus_indicator_container
                .findViewById(selIndex);
        curSelImg
                .setImageDrawable(getContext()
                        .getResources().getDrawable(
                                R.drawable.dots_focus));
        preSelImgIndex = selIndex;
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}