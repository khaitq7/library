package com.mobgame.demomobcountdowntimer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.mobgame.mobcountdowntimer.MobCountdownTimer;

public class MainActivity extends AppCompatActivity {
    private MobCountdownTimer mobCountdownTimer;

    private Button btnStart, btnStop, btnSetTime;
    private EditText edtTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtTime = (EditText) findViewById(R.id.edtTime);
        mobCountdownTimer = (MobCountdownTimer) findViewById(R.id.mobCountdownTimer);
//        mobCountdownTimer.setBlinkColor("#00000000", "#00000000");
        mobCountdownTimer.setFormat(MobCountdownTimer.FORMAT_HHh_MMm_SSs);
        mobCountdownTimer.setMobCountdownListener(() -> Toast.makeText(MainActivity.this, "Countdown onFinish()", Toast.LENGTH_LONG).show());

        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mobCountdownTimer.setFormat(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mobCountdownTimer.setFormat(0);
            }
        });

        ToggleButton tgBlink = (ToggleButton) findViewById(R.id.tgBlink);
        tgBlink.setOnCheckedChangeListener((buttonView, isChecked) -> {
            mobCountdownTimer.setEnableBlink(isChecked);
        });

        findViewById(R.id.btnStart).setOnClickListener(view->mobCountdownTimer.start());
        findViewById(R.id.btnStop).setOnClickListener(view->mobCountdownTimer.stop());
        findViewById(R.id.btnSetTime).setOnClickListener(view->mobCountdownTimer.setTime(Long.parseLong(edtTime.getText().toString())));
        findViewById(R.id.btnRotate90).setOnClickListener(view -> mobCountdownTimer.rotate(MobCountdownTimer.MOB_ORIENTATION_VERTICAL));
        findViewById(R.id.btnRotate270).setOnClickListener(view -> mobCountdownTimer.rotate(MobCountdownTimer.MOB_ORIENTATION_VERTICAL_REVERT));
        findViewById(R.id.btnRotate0).setOnClickListener(view -> mobCountdownTimer.rotate(MobCountdownTimer.MOB_ORIENTATION_HORIZONTAL));
    }
}
