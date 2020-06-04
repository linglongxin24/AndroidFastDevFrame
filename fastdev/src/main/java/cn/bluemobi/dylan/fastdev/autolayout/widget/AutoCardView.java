package cn.bluemobi.dylan.fastdev.autolayout.widget;

import android.content.Context;
import androidx.cardview.widget.CardView;
import android.util.AttributeSet;


import cn.bluemobi.dylan.fastdev.autolayout.AutoFrameLayout;
import cn.bluemobi.dylan.fastdev.autolayout.utils.AutoLayoutHelper;

import static java.security.AccessController.getContext;

/**
 * Created by zhy on 15/12/8.
 */
public class AutoCardView extends CardView
{
    private final AutoLayoutHelper mHelper = new AutoLayoutHelper(this);

    public AutoCardView(Context context)
    {
        super(context);
    }

    public AutoCardView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public AutoCardView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public AutoFrameLayout.LayoutParams generateLayoutParams(AttributeSet attrs)
    {
        return new AutoFrameLayout.LayoutParams(getContext(), attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        if (!isInEditMode())
        {
            mHelper.adjustChildren();
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


}
