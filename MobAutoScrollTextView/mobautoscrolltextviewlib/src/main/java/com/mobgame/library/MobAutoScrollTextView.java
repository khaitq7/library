package com.mobgame.library;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.annotation.AnimRes;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by HATIBOY on 9/19/2017.
 */

public class MobAutoScrollTextView extends TextSwitcher {

    private static final String TAG = MobAutoScrollTextView.class.getName();


    /*@Declare Variable*/
    private ArrayList<String> stringArrayList;
    private Long duration;
    private boolean repeat;
    private boolean reverse = true;
    private int i = 0;

    OnTextViewScrollListener onTextViewScrollListener;


/*End @Declare Variable*/


    /*@Declare Constructor*/
    public MobAutoScrollTextView(Context context) {
        super(context);
    }

    public MobAutoScrollTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


/*End @Declare Constructor*/


    /*Getter And Setter*/
    public boolean isRepeat() {
        return repeat;
    }

    public void setRepeat(boolean repeat) {
        this.repeat = repeat;
    }


    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }


    public void setArrayListText(ArrayList<String> arrayListText) {
        this.stringArrayList = arrayListText;
    }

    public ArrayList<String> getArrayListText() {
        return stringArrayList;
    }

    public boolean isReverse() {
        return reverse;
    }

    public void setReverse(boolean reverse) {
        this.reverse = reverse;
    }
/*End Getter And Setter*/

    /*start Animation*/
    public void startAnimation(final ArrayList<String> listText, final long duration, final boolean repeat, final boolean reverse) {
        //handler TextView scroll
        long timeCount = listText.size() * duration;
        CountDownTimer countDownTimer = new CountDownTimer(timeCount, duration) {
            public void onTick(long millisUntilFinished) {
                if (onTextViewScrollListener != null)
                    onTextViewScrollListener.onTextViewScroll(listText.get(i), reverse);
                setText(listText.get(i));
                i++;
                if (i == listText.size()) i = 0;
            }

            public void onFinish() {
                if (repeat) startAnimation(listText, duration, repeat, reverse);
                else Log.d(TAG, "onFinish: " + "animation stop");
            }
        };
        countDownTimer.start();
    }

    public void startAnimation(ArrayList<String> listText, long duration, boolean repeat) {
        //reverse is up to top
        startAnimation(listText, duration, repeat, true);
    }

    public void startAnimation(ArrayList<String> listText, long duration) {
        //reverse is up to top, repeat is true
        startAnimation(listText, duration, true, true);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    public void setInAnimation(Context context, @AnimRes int resourceID) {
        super.setInAnimation(context, resourceID);
    }

    @Override
    public void setOutAnimation(Context context, @AnimRes int resourceID) {
        super.setOutAnimation(context, resourceID);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public void setFactory(ViewFactory factory) {
        super.setFactory(new ViewFactory() {
            public View makeView() {
                final TextView myText = new TextView(getContext());
                myText.setGravity(Gravity.CENTER);

                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                        LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
                        Gravity.CENTER);
                myText.setLayoutParams(params);
                myText.setTextSize(36);
                myText.setTextColor(Color.BLACK);
                return myText;
            }
        });
    }

    /*Listener*/
    interface OnTextViewScrollListener {
        void onTextViewScroll(String text, boolean reverse);
    }

    public void setOnTextViewScrollListener(OnTextViewScrollListener onTextViewScrollListener) {
        this.onTextViewScrollListener = onTextViewScrollListener;
    }
}
