package com.smartmaster.highlight;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import zhy.com.highlight.util.ViewUtils;

/**
 * 高亮引导view
 * Created by ychen on 2019/3/26.
 */
public class HighlightGuideView extends FrameLayout {

    private Bitmap mMaskBitmap;
    private int maskColor = 0x7F000000;
    private Bitmap mLightBitmap;
    private Paint mPaint;

    protected float blurRadius=15;//模糊半径 默认15

    private ViewGroup mParent;
    private View mTargetView;
    private LayoutInflater mInflater;

    private RectF mRectF;

    private static final PorterDuffXfermode MODE_DST_OUT = new PorterDuffXfermode(PorterDuff.Mode.DST_OUT);

    public HighlightGuideView(@NonNull Context context,ViewGroup parent,View targetView) {
        super(context);
        mInflater = LayoutInflater.from(context);
        this.mTargetView = targetView;
        this.mParent = parent;
        setWillNotDraw(false);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setDither(true);//防抖动
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);

    }

    private void buildMask() {
        recycleBitmap(mMaskBitmap);
        mMaskBitmap = Bitmap.createBitmap(getMeasuredWidth(), getMeasuredHeight(), Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(mMaskBitmap);
        canvas.drawColor(maskColor);
        mPaint.setXfermode(MODE_DST_OUT);

        recycleBitmap(mLightBitmap);
        mLightBitmap = Bitmap.createBitmap(getMeasuredWidth(), getMeasuredHeight(), Bitmap.Config.ARGB_4444);
        mRectF = new RectF(ViewUtils.getLocationInView(mParent,mTargetView));
        drawCircleShape(mLightBitmap,mRectF);
        canvas.drawBitmap(mLightBitmap, 0, 0, mPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        measureChildren(MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));
        setMeasuredDimension(width, height);


    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (changed) {
            buildMask();
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {

        try {
            canvas.drawBitmap(mMaskBitmap, 0, 0, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDraw(canvas);

    }

    /**
     * 画一个圆形镂空区域
     * @param bitmap
     * @param rectF
     */
    private void drawCircleShape(Bitmap bitmap, RectF rectF) {
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setDither(true);
        paint.setAntiAlias(true);
        if (blurRadius > 0) {
            paint.setMaskFilter(new BlurMaskFilter(blurRadius, BlurMaskFilter.Blur.SOLID));
        }

        canvas.drawCircle(rectF.left+(rectF.width()/2),rectF.top+(rectF.height()/2),
                Math.max(rectF.width(),rectF.height())/2,paint);

    }

    /**
     * 主动回收之前创建的bitmap
     *
     * @param bitmap
     */
    private void recycleBitmap(Bitmap bitmap) {
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
            bitmap = null;
            System.gc();
        }
    }

    public void addTipView(int layoutId){
        View view = mInflater.inflate(layoutId, this, false);
        LayoutParams lp = new LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);


        lp.leftMargin = (int) 20;
        lp.topMargin = (int) (mRectF.top + mRectF.width());
        lp.rightMargin = 0;
        lp.bottomMargin = 0;
        addView(view,lp);

    }

    public void addBottomView(int layoutId,OnClickListener onClickListener){
        View view = mInflater.inflate(layoutId, this, false);
        LayoutParams lp = new LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        lp.leftMargin = 0;
        lp.topMargin = 0;
        lp.rightMargin = 0;
        lp.bottomMargin = 60;
        lp.gravity = Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL;
        view.setOnClickListener(onClickListener);
        addView(view,lp);
    }





}
