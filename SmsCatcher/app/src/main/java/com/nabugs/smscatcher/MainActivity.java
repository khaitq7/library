package com.nabugs.smscatcher;

import android.Manifest;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.SmsManager;
import android.util.Log;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;
import com.nabugs.smscatcher.adapter.SmsMessageAdapter;
import com.nabugs.smscatcher.data.SmsMessageViewModel;
import com.nabugs.smscatcher.data.SmsRoomDatabase;
import com.nabugs.smscatcher.model.SmsMessage;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "DEV_JOB";
    private static final int SMS_PERMISSION_CODE = 1111;
    private SmsMessageViewModel smsViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        final SmsMessageAdapter adapter = new SmsMessageAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        smsViewModel = ViewModelProviders.of(this).get(SmsMessageViewModel.class);
        smsViewModel.getListSmsMessages().observe(this, (List<SmsMessage> listMess1) -> {
            adapter.setMess(listMess1);
        });

        findViewById(R.id.btn_start_jobdispatcher).setOnClickListener(view->scheduleJob(this));
        findViewById(R.id.btn_cancel_jobdispatcher).setOnClickListener(view->cancelJob(this));
        findViewById(R.id.btn_send_sms).setOnClickListener(view-> SmsManager.getDefault().sendTextMessage("0949495101", "900", "Test send message by code", null, null));
        findViewById(R.id.btn_add_mes).setOnClickListener(view-> smsViewModel.insert(new SmsMessage("Đây là một tin nhắn đến!!!", "113")));
        findViewById(R.id.btn_del_all_mes).setOnClickListener(view-> smsViewModel.deleteAll());

        if(!isSmsPermissionGranted()){
            showRequestPermissionsInfoAlertDialog();
        }
    }

    //================================== FIREBASE JOBDISPATCHER =======================================//
    private void scheduleJob(Context context) {
        Log.d(TAG, "scheduleJob: ");
        FirebaseJobDispatcher jobDispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(context));
        Job job = createJob(jobDispatcher);
        jobDispatcher.mustSchedule(job);
    }

    private Job createJob(FirebaseJobDispatcher jobDispatcher) {
        Log.d(TAG, "createJob: ");
        Job job = jobDispatcher.newJobBuilder()
                .setLifetime(Lifetime.FOREVER)
                .setService(SchedulesJobService.class)
                .setTag("TagOfFirebaseJob")
                .setReplaceCurrent(false)
                .setRecurring(true)
                .setTrigger(Trigger.executionWindow(30,60))
                .setRetryStrategy(RetryStrategy.DEFAULT_LINEAR)
                .setConstraints(Constraint.DEVICE_CHARGING, Constraint.ON_ANY_NETWORK)
                .build();
        return job;
    }

    public void cancelJob(Context context){
        Log.d(TAG, "cancelJob: ");
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(context));
        //Cancel all the jobs for this package
        dispatcher.cancelAll();
        // Cancel the job for this tag
        dispatcher.cancel("UniqueTagForYourJob");
    }
    //==================================================================================================//

    public boolean isSmsPermissionGranted() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestReadAndSendSmsPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_SMS)) {
            // You may display a non-blocking explanation here, read more in the documentation:
            // https://developer.android.com/training/permissions/requesting.html
        }
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_SMS}, SMS_PERMISSION_CODE);
    }

    public void showRequestPermissionsInfoAlertDialog() {
        showRequestPermissionsInfoAlertDialog(true);
    }

    public void showRequestPermissionsInfoAlertDialog(final boolean makeSystemRequest) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.permission_alert_dialog_title); // Your own title
        builder.setMessage(R.string.permission_dialog_message); // Your own message

        builder.setPositiveButton(R.string.action_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                // Display system runtime permission request?
                if (makeSystemRequest) {
                    requestReadAndSendSmsPermission();
                }
            }
        });

        builder.setCancelable(false);
        builder.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case SMS_PERMISSION_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // SMS related task you need to do.

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private Context getContext(){return this;}
}
