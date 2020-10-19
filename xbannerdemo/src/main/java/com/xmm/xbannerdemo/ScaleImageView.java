package com.xmm.xbannerdemo;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

public class ScaleImageView extends androidx.appcompat.widget.AppCompatImageView {

    /**
     * 高 宽 比
     */
    private float scale;
    public ScaleImageView(Context context) {
        super(context);
    }

    public ScaleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ScaleImageView);
        scale = ta.getFloat(R.styleable.ScaleImageView_scale,0);
        ta.recycle();
    }

    public ScaleImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
//        LogUtil.e(width+"=="+ (int) (width*scale));
        setMeasuredDimension(width, (int) (width*scale));
    }
}
