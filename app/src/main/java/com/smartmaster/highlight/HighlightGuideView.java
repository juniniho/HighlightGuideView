package com.smartmaster.highlight;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import zhy.com.highlight.util.ViewTools;

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

    private View mAnchor;
    private View mTargetView;
    private LayoutInflater mInflater;

    private RectF mRectF;
    private float mRadius;

    private static final PorterDuffXfermode MODE_DST_OUT = new PorterDuffXfermode(PorterDuff.Mode.DST_OUT);

    public HighlightGuideView(@NonNull Context context, View anchor, View targetView) {
        super(context);
        mInflater = LayoutInflater.from(context);
        this.mTargetView = targetView;
        this.mAnchor = anchor;
        setWillNotDraw(false);
        init();
        //屏蔽遮罩下页面点击
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
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
        mRectF = new RectF(ViewTools.getLocationInView(mAnchor,mTargetView));
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

        mRadius = (float) (Math.sqrt(Math.pow(rectF.width(),2) + Math.pow(rectF.height(),2))/2) + dp2px(getContext(),1);
        canvas.drawCircle(rectF.left+(rectF.width()/2),rectF.top+(rectF.height()/2),
                mRadius,paint);

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

    /**
     * 添加提示框view
     * @param layoutId
     * @param leftMarginOffset 默认左对齐圆心，top离圆底部5dp
     */
    public void addTipView(int layoutId,int leftMarginOffset){
        View view = mInflater.inflate(layoutId, this, false);
        LayoutParams lp = new LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);


        lp.leftMargin = (int) (mRectF.left + mRectF.width()/2 + leftMarginOffset);
        lp.topMargin = (int) (mRectF.top + mRectF.height()/2 + mRadius + dp2px(getContext(),5));
        lp.rightMargin = 0;
        lp.bottomMargin = 0;
        addView(view,lp);

    }

    public void addBottomView(int layoutId, OnClickListener onClickListener){
        View view = mInflater.inflate(layoutId, this, false);
        LayoutParams lp = new LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        lp.leftMargin = 0;
        lp.topMargin = 0;
        lp.rightMargin = 0;
        lp.bottomMargin = dp2px(getContext(),88);
        lp.gravity = Gravity.BOTTOM| Gravity.CENTER_HORIZONTAL;
        view.setOnClickListener(onClickListener);
        addView(view,lp);
    }

    /**
     * dp转换成px
     */
    public static int dp2px(Context context, float dpValue){
        float scale=context.getResources().getDisplayMetrics().density;
        return (int)(dpValue*scale+0.5f);
    }






}
