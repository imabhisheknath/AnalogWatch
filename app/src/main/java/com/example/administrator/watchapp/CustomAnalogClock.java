package com.example.administrator.watchapp;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;


public class CustomAnalogClock extends View {

    private final ArrayList<DialOverlay> mDialOverlay = new ArrayList<DialOverlay>();
    private Calendar mCalendar;
    private Drawable mFace;
    private int mDialWidth;
    private float sizeScale = 1f;
    private int radius;
    private int mDialHeight;
    private int mBottom;
    private int mTop;
    private int mLeft;
    private int mRight;
    private boolean mSizeChanged;
    private HandsOverlay mHandsOverlay;
    private boolean autoUpdate;


    public CustomAnalogClock(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        handleAttrs(context, attrs);
    }

    public CustomAnalogClock(Context context, AttributeSet attrs) {
        super(context, attrs);
        handleAttrs(context, attrs);
    }

    public CustomAnalogClock(Context context) {
        super(context);
        setWatchBody(context);
    }

    public CustomAnalogClock(Context context, boolean defaultWatchFace) {
        super(context);
        if (defaultWatchFace)
            setWatchBody(context);
    }

    private void handleAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.CustomAnalogClock, 0, 0);
        if (typedArray.hasValue(R.styleable.CustomAnalogClock_default_watchface)) {
            if (!typedArray.getBoolean(R.styleable.CustomAnalogClock_default_watchface, true)) {
                typedArray.recycle();
                return;
            }
        }
        setWatchBody(context);
        typedArray.recycle();
    }

    public void setWatchBody(Context context) {

        Drawable dial = context.getDrawable(R.drawable.hoyo_flower_blue_dial);
        Drawable hour = context.getDrawable(R.drawable.hoyo_blue_watch_hour);
        Drawable min = context.getDrawable(R.drawable.hoyo_blue_watch_minute);
        Drawable sec = context.getDrawable(R.drawable.hoyo_blue_watch_second);

        setWatchBody(context, dial, hour, min, sec);
    }


    public void setScale(float scale) {
        if (scale <= 0)
            throw new IllegalArgumentException("Scale must be bigger than 0");
        this.sizeScale = scale;
        mHandsOverlay.withScale(sizeScale);
        invalidate();
    }

    public void setFace(int drawableRes) {
        final Resources r = getResources();
        setFace(r.getDrawable(drawableRes));
    }

    public void setWatchBody(Context context, Drawable watchFace, Drawable hourHand, Drawable minuteHand, Drawable secHand) {

        setFace(watchFace);

        Drawable Hhand = hourHand;

        Drawable Mhand = minuteHand;

        Drawable SHand = secHand;

        mCalendar = Calendar.getInstance();
        setTime(Calendar.getInstance());

        mHandsOverlay = new HandsOverlay(Hhand, Mhand, SHand).withScale(sizeScale);
        mHandsOverlay.setShowSeconds(true);
    }

    public void setFace(Drawable face) {
        mFace = face;
        mSizeChanged = true;
        mDialHeight = mFace.getIntrinsicHeight();
        mDialWidth = mFace.getIntrinsicWidth();
        radius = Math.max(mDialHeight, mDialWidth);

        invalidate();
    }


    public void setTime(long time) {
        mCalendar.setTimeInMillis(time);
        invalidate();
    }


    public void setTime(Calendar calendar) {
        mCalendar = calendar;
        invalidate();
        if (autoUpdate) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    setTime(Calendar.getInstance());
                }
            }, 1000);
        }
    }

    public void setAutoUpdate(boolean autoUpdate) {
        this.autoUpdate = autoUpdate;
        setTime(Calendar.getInstance());
    }


    public void setTimezone(TimeZone timezone) {
        mCalendar = Calendar.getInstance(timezone);
    }

    public void setHandsOverlay(HandsOverlay handsOverlay) {
        mHandsOverlay = handsOverlay;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mSizeChanged = true;
    }

    // some parts from AnalogClock.java
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        final boolean sizeChanged = mSizeChanged;
        mSizeChanged = false;

        final int availW = mRight - mLeft;
        final int availH = mBottom - mTop;

        final int cX = availW / 2;
        final int cY = availH / 2;

        final int w = (int) (mDialWidth * sizeScale);
        final int h = (int) (mDialHeight * sizeScale);

        boolean scaled = false;

        if (availW < w || availH < h) {
            scaled = true;
            final float scale = Math.min((float) availW / (float) w,
                    (float) availH / (float) h);
            canvas.save();
            canvas.scale(scale, scale, cX, cY);
        }

        if (sizeChanged) {
            mFace.setBounds(cX - (w / 2), cY - (h / 2), cX + (w / 2), cY
                    + (h / 2));
        }

        mFace.draw(canvas);

        for (final DialOverlay overlay : mDialOverlay) {
            overlay.onDraw(canvas, cX, cY, w, h, mCalendar, sizeChanged);
        }

        mHandsOverlay.onDraw(canvas, cX, cY, w, h, mCalendar, sizeChanged);

        if (scaled) {
            canvas.restore();
        }
    }

    // from AnalogClock.java
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int finalRadius = (int) (radius * sizeScale);
        setMeasuredDimension(finalRadius, finalRadius);
    }

    @Override
    protected int getSuggestedMinimumHeight() {
        return (int) (mDialHeight * sizeScale);
    }

    @Override
    protected int getSuggestedMinimumWidth() {
        return (int) (mDialWidth * sizeScale);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right,
                            int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mRight = right;
        mLeft = left;
        mTop = top;
        mBottom = bottom;
    }


    public void addDialOverlay(DialOverlay dialOverlay) {
        mDialOverlay.add(dialOverlay);
    }

    public void removeDialOverlay(DialOverlay dialOverlay) {
        mDialOverlay.remove(dialOverlay);
    }

    public void clearDialOverlays() {
        mDialOverlay.clear();
    }
}
