package com.funtap.minigame.puzzledemo;

import android.animation.ObjectAnimator;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.animation.DecelerateInterpolator;
import android.widget.ProgressBar;

/**
 * Created by Pc 02 on 9/18/2017.
 */

public class MyTimer extends CountDownTimer {
    private int timeLast, timeTick;
    private ProgressBar progressBar;
    private int pauseTime;
    OnMyTimerListener onMyTimerListener;

    public MyTimer(long millisInFuture, long countDownInterval, ProgressBar progressBar) {
        super(millisInFuture, countDownInterval);
        this.timeLast = (int) millisInFuture;
        this.pauseTime = timeLast;
        this.timeTick = (int) countDownInterval;
        this.progressBar = progressBar;
    }

    public void setTimeLast(int timeLast) {
        this.timeLast = timeLast;
    }

    public void setTimeTick(int timeTick) {
        this.timeTick = timeTick;
    }

    public int getPauseTime() {
        return pauseTime;
    }

    @Override
    public void onTick(long l) {
        setProgressAnimate(progressBar, (int) (l/1000*1000));
//            Log.e("running",(int) (l/1000*100)+"");
        pauseTime = (int) (l/1000*1000);
        Log.e("running",pauseTime+"");
    }

    @Override
    public void onFinish() {
        pauseTime = 0;
        setProgressMax(progressBar,timeLast);
        onMyTimerListener.onListener(1);
    }

    private void setProgressAnimate(ProgressBar pb, int progressTo)
    {
        ObjectAnimator animation = ObjectAnimator.ofInt(pb, "progress", pb.getProgress(), progressTo * 100);
        animation.setDuration(2000);
        animation.setInterpolator(new DecelerateInterpolator());
        animation.start();
    }

    private void setProgressMax(ProgressBar pb, int max) {
        pb.setMax(max * 100);
        progressBar.setProgress(progressBar.getMax());
    }

    public void setOnMyTimerListener(OnMyTimerListener onMyTimerListener) {
        this.onMyTimerListener = onMyTimerListener;
    }

    public interface OnMyTimerListener {
        void onListener(int result);
    }
}
