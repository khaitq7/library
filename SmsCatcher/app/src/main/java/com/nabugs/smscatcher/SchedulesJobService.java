package com.nabugs.smscatcher;


import android.util.Log;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

/**
 * Created by namanh on 6/1/2018.
 */

public class SchedulesJobService extends JobService {
    private static final String TAG = "DEV_JOB";

    @Override
    public boolean onStartJob(final JobParameters job) {
        Log.d(TAG, "onStartJob: ");
        new Thread(new Runnable() {
            @Override
            public void run() {
                codeYouWantToRun(job);
            }
        }).start();
        return true;
    }

    private void codeYouWantToRun(JobParameters job) {
        try {
            Log.d(TAG, "completeJob: " + "jobStarted");
            //This task takes 2 seconds to complete.
            Thread.sleep(2000);

            Log.d(TAG, "completeJob: " + "jobFinished");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            //Tell the framework that the job has completed and doesnot needs to be reschedule
            jobFinished(job, true);
        }
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        Log.d(TAG, "onStopJob: ");
        return false;
    }
}
