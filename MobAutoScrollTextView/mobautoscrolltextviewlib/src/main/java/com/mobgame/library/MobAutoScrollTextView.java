package com.mobgame.library;

import android.content.Context;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;

import com.mobgame.mobautoscrolltextview.R;

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
    public void startAutoScroll(final ArrayList<String> listText, final long duration, final boolean repeat, final boolean reverse) {
        //settings
        setFactory(new ViewFactory() {

            public View makeView() {
//                removeAllViews();
                final TextView myText = new TextView(getContext());
                myText.setGravity(Gravity.CENTER);
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                        LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
                        Gravity.CENTER);
                myText.setLayoutParams(params);
                myText.setTextSize(15);
                myText.setTextColor(Color.WHITE);
                return myText;
            }
        });
        setInAnimation(AnimationUtils.loadAnimation(getContext(),
                R.anim.push_up_in));
        setOutAnimation(getContext(), R.anim.push_up_out);

        //handler TextView scroll
        long timeCount = listText.size() * duration;
        CountDownTimer countDownTimer = new CountDownTimer(timeCount, duration) {
            public void onTick(long millisUntilFinished) {
                if (onTextViewScrollListener != null)
                    onTextViewScrollListener.onTextViewScroll(listText.get(i), reverse);
                setText(listText.get(i));
                i++;
            }

            public void onFinish() {
                onTextViewScrollListener.onTextViewScrollFinished();
            }
        };
        countDownTimer.start();
    }

    public void startAutoScroll(ArrayList<String> listText, long duration, boolean repeat) {
        //reverse is up to top
        startAutoScroll(listText, duration, repeat, true);
    }
// call this function for Mob Notification
    public void startAutoScroll(ArrayList<String> listText, long duration) {
        //reverse is up to top, repeat is true
        startAutoScroll(listText, duration, false, false);
    }


    /*Listener*/
    interface OnTextViewScrollListener {
        void onTextViewScroll(String text, boolean reverse);
        void onTextViewScrollFinished();
    }

    public void setOnTextViewScrollListener(OnTextViewScrollListener onTextViewScrollListener) {
        this.onTextViewScrollListener = onTextViewScrollListener;

    }
}
