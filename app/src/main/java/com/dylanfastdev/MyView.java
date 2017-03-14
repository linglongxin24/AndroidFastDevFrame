package com.dylanfastdev;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by dylan on 2017-03-14.
 */

public class MyView extends View {
    public MyView(Context context) {
        super(context);
    }

    public MyView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint=new Paint();
        paint.setColor(Color.WHITE);
        Path path=new Path();
        path.moveTo(0,0);
        path.lineTo(100,100);
        path.lineTo(100,0);
        path.close();
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawPath(path,paint);
         paint=new Paint();
        paint.setColor(Color.BLUE);
        paint.setTextSize(30);
        canvas.drawTextOnPath("热门",path,30,-10,paint);


    }
}
