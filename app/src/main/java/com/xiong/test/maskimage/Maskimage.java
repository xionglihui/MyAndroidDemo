package com.xiong.test.maskimage;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xionglh
 * @version Maskimage Created by xionglh on 2018/2/23 10:41 v1.0.0
 */
public class Maskimage extends View {


    private List<Bitmap> mBitmaps = new ArrayList<>();


    private Bitmap mBitmapButtom;
    private Paint mPaint;
    private Paint mPainLine;
    private PorterDuffXfermode mXfermode;

    private Bitmap mBitmapMask;

    private int mWidth;
    private int mHeight;

    private Paint mPaintMask;

    private Path mPath;

    private Context mContext;

    private float mCurrentProgess = 0f;


    public Maskimage(Context context) {
        this(context, null);
        this.mContext = context;
    }

    public Maskimage(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;

        mPaint = new Paint();
        mPaintMask = new Paint();
        mPaintMask.setColor(Color.parseColor("#FF000000"));
        mPaintMask.setStyle(Paint.Style.FILL);


        mPainLine = new Paint();
        mPainLine.setStyle(Paint.Style.FILL);
        mPainLine.setStrokeWidth(dp2px(getContext(), 2));
        mPainLine.setColor(Color.parseColor("#ffffffff"));


        mXfermode = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);
        mPath = new Path();
    }

    public Maskimage(Context context, AttributeSet attrs, int defStyle) {
        this(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(mWidth, mHeight);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        if (mBitmapButtom == null) {
            return;
        }
        canvas.drawBitmap(mBitmapButtom, 0, 0, mPaint);
        if (mBitmapMask == null)
            return;
        int saveFlags = Canvas.MATRIX_SAVE_FLAG | Canvas.CLIP_SAVE_FLAG | Canvas.HAS_ALPHA_LAYER_SAVE_FLAG | Canvas.FULL_COLOR_LAYER_SAVE_FLAG | Canvas.CLIP_TO_LAYER_SAVE_FLAG;
        canvas.saveLayer(0, 0, mWidth, mHeight, null, saveFlags);
        float w = (mWidth + (float) (mWidth * 0.5)) * mCurrentProgess;
        float h = (mHeight + (float) (mWidth / 0.5)) * mCurrentProgess;
        mPath.reset();
        mPath.moveTo(0, 0);
        mPath.lineTo(w, 0);
        mPath.lineTo(0, h);
        mPath.lineTo(0, 0);
        mPath.close();
        canvas.drawPath(mPath, mPaintMask);
        mPaint.setXfermode(mXfermode);
        int left = mWidth / 2 - mBitmapMask.getWidth() / 2;
        int top = mHeight / 2 - mBitmapMask.getHeight() / 2;
        canvas.drawBitmap(mBitmapMask, left, top, mPaint);
        mPaint.setXfermode(null);
        canvas.drawLine(w, 0, 0, h, mPainLine);
        canvas.restore();

    }

    private int mMasxIndex = 1;
    private ValueAnimator mValueAnimator;

    public void setImageResoucre(Integer[] imgResoucre) {
        mBitmaps.clear();
        if (imgResoucre.length < 1) {
            return;
        }
        for (int resource : imgResoucre) {
            Bitmap bitmap = decodeBitmap(resource);
            mBitmaps.add(bitmap);
        }
        mBitmapButtom = mBitmaps.get(0);
        mWidth = mBitmapButtom.getWidth();
        mHeight = mBitmapButtom.getHeight();
        if (imgResoucre.length == 1) {
            invalidate();
            return;
        }
        mBitmapMask = mBitmaps.get(1);
        if (mValueAnimator == null || !mValueAnimator.isRunning()) {

            mValueAnimator = ObjectAnimator.ofFloat(0, 1);
            mValueAnimator.setDuration(2000);
            mValueAnimator.setRepeatCount(-1);

            mValueAnimator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {

                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                    mBitmapButtom = mBitmaps.get(mMasxIndex);
                    mMasxIndex = mMasxIndex == mBitmaps.size() - 1 ? 0 : mMasxIndex + 1;
                    mBitmapMask = mBitmaps.get(mMasxIndex);

                }
            });
            mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    mCurrentProgess = (float) animation.getAnimatedValue();
                    invalidate();
                }
            });
            mValueAnimator.start();
        }

    }

    private Bitmap decodeBitmap(int resId) {
        return BitmapFactory.decodeResource(mContext.getResources(), resId);
    }

    public static int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

}