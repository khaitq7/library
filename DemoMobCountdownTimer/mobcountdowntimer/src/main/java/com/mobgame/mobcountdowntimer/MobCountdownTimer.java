package com.mobgame.mobcountdowntimer;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewTreeObserver;

/**
 * Created by NamAnh on 8/31/2017.
 */

public class MobCountdownTimer extends AppCompatTextView {
    public static final String TAG = MobCountdownTimer.class.getSimpleName();
    public static final int MOB_ORIENTATION_HORIZONTAL = 0;
    public static final int MOB_ORIENTATION_HORIZONTAL_REVERT = 180;
    public static final int MOB_ORIENTATION_VERTICAL = 90;
    public static final int MOB_ORIENTATION_VERTICAL_REVERT = 270;

    public static final int FORMAT_AUTO = 0;
    public static final int FORMAT_HH_MM_SS = 1;
    public static final int FORMAT_HHh_MMm_SSs = 2;
    public static final int FORMAT_D_HH = 3;
    public static final int FORMAT_Dd_HHh = 4;
    public static final int FORMAT_HH_MM = 5;
    public static final int FORMAT_HHh_MMm = 6;
    public static final int FORMAT_MM_SS = 7;
    public static final int FORMAT_MMm_SSs = 8;
    public static final int FORMAT_Dd_HHh_MMm_SSs = 9;
    public static final int FORMAT_D_HH_MM_SS = 10;

    private long timeMillis = 0;
    private boolean isRunning = false;
    private CountDownTimer countDownTimer;
    private int timeFormat = FORMAT_AUTO;
    private int rotationDegree = 0;
    private String blinkColorStart = "#ee4242";
    private String blinkColorEnd = "#ad2020";
    private ValueAnimator animBlink;

    private OnFinishMobCountdownListener mobCountdownListener;
    private boolean enableBlink = false;

    public MobCountdownTimer(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if(Build.VERSION.SDK_INT < 16){
                    getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }else{
                    getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                if(getMeasuredWidth()<getMeasuredHeight()){
                    rotate(MOB_ORIENTATION_VERTICAL);
                }
            }
        });
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
        if(rotationDegree==0){
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            setMeasuredDimension(getMeasuredWidth(), getMeasuredHeight());
        }else{
            super.onMeasure(heightMeasureSpec, widthMeasureSpec);
            setMeasuredDimension(getMeasuredHeight(), getMeasuredWidth());
        }
    }

    @Override
    protected void onDraw(Canvas canvas){
        TextPaint textPaint = getPaint();
        textPaint.setColor(getCurrentTextColor());
        textPaint.drawableState = getDrawableState();

        canvas.save();

        switch (rotationDegree){
            case MOB_ORIENTATION_VERTICAL:{
                canvas.translate(getWidth(), 0);
                canvas.rotate(90);
                canvas.translate(getCompoundPaddingLeft(), getExtendedPaddingTop());
                getLayout().draw(canvas);
                break;
            }
            case MOB_ORIENTATION_VERTICAL_REVERT:{
                canvas.translate(0, getHeight());
                canvas.rotate(270);
                canvas.translate(getCompoundPaddingLeft(), getExtendedPaddingTop());
                getLayout().draw(canvas);
                break;
            }
            default:{
                super.onDraw(canvas);
            }
        }
        canvas.restore();
    }

    public void start(){
        stop();
        isRunning = true;
        countDownTimer = new CountDownTimer(timeMillis, 1000) {
            public void onTick(long millisUntilFinished) {
                setText(converTimeString(millisUntilFinished,timeFormat));
            }

            public void onFinish() {
                setText(converTimeString(0,timeFormat));
                isRunning = false;
                if(animBlink!=null){
                    animBlink.cancel();
                }
                if(mobCountdownListener!=null){
                    mobCountdownListener.onFinish();
                }
            }

        };
        countDownTimer.start();
        if(enableBlink){
            startBlink();
        }
    }
    
    public void stop(){
        if(countDownTimer!=null && isRunning){
            countDownTimer.cancel();
            isRunning = false;
            if(animBlink!=null && enableBlink){
                animBlink.cancel();
            }
        }
    }

    public void startBlink(){
        if(animBlink!=null) {
            animBlink.start();
        }else{
            animBlink = new ValueAnimator();
            animBlink.setIntValues(Color.parseColor(blinkColorStart), Color.parseColor(blinkColorEnd));
            animBlink.setEvaluator(new ArgbEvaluator());
            animBlink.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    setBackgroundColor((Integer)valueAnimator.getAnimatedValue());
                }
            });
            animBlink.setRepeatCount(ValueAnimator.INFINITE);
            animBlink.setDuration(2000);
            animBlink.start();
        }
    }


    public void setFormat(int timeFormat){
        this.timeFormat = timeFormat;
    }

    public void setTime(long timeMillis){
        this.timeMillis = timeMillis;
    }

    public void rotate(int rotationDegree) {
        this.rotationDegree = rotationDegree;
        requestLayout();
        invalidate();
    }

    public boolean isEnableBlink() {
        return enableBlink;
    }

    public void setEnableBlink(boolean enableBlink) {
        this.enableBlink = enableBlink;
        if(isRunning){
            if(enableBlink){
                startBlink();
            }else{
                if(animBlink!=null){
                    animBlink.cancel();
                }
            }
        }
    }

    public void setBackgroundColor(String stringColor) {
        setBackgroundColor(Color.parseColor(stringColor));
    }

    public void setBlinkColor(String colorStart, String colorEnd){
        this.blinkColorStart = colorStart;
        this.blinkColorEnd = colorEnd;
    }

    public OnFinishMobCountdownListener getMobCountdownListener() {
        return mobCountdownListener;
    }

    public void setMobCountdownListener(OnFinishMobCountdownListener mobCountdownListener) {
        this.mobCountdownListener = mobCountdownListener;
    }

    public static String converTimeString(long millis, int format){
        long s = (millis / 1000) % 60;
        long m = (millis / 60000) % 60;
        long h = (millis / (60000 * 60)) % 24;
        long d = (millis / (60000 * 60 *24)) % 30;
        switch (format){
            case FORMAT_AUTO:{
                if(millis>86400000){
                    return String.format("%dd:%02dh",d,h);
                }
                else if(millis>3600000){
                    return String.format("%02dh:%02dm",h,m);
                }else{
                    return String.format("%02dm:%02ds",m,s);
                }
            }
            case FORMAT_HHh_MMm_SSs:{
                return String.format("%02dh:%02dm:%02ds",(24*d+h),m,s);
            }
            case FORMAT_HH_MM_SS:{
                return String.format("%02d:%02d:%02d",(24*d+h),m,s);
            }
            case FORMAT_D_HH:{
                return String.format("%d:%02d",d,h);
            }
            case FORMAT_Dd_HHh:{
                return String.format("%dd:%02dh",d,h);
            }
            case FORMAT_HH_MM:{
                return String.format("%02d:%02d",(24*d+h),m);
            }
            case FORMAT_HHh_MMm:{
                return String.format("%02dh:%02dm",(24*d+h),m);
            }
            case FORMAT_MM_SS:{
                return String.format("%02d:%02d",((24*d+h)*60+m),s);
            }
            case FORMAT_MMm_SSs:{
                return String.format("%02dm:%02ds",(24*d+h)*60+m,s);
            }
            case FORMAT_Dd_HHh_MMm_SSs:{
                return String.format("%dd:%02dh:%02dm:%02ds",d,h,m,s);
            }
            case FORMAT_D_HH_MM_SS:{
                return String.format("%d:%02d:%02d:%02d",d,h,m,s);
            }
            default:
                return String.format("%dd:%02dh:%02dm:%02ds",d,h,m,s);
        }
    }

    public interface OnFinishMobCountdownListener{
        void onFinish();
    }
}
