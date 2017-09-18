package com.funtap.minigame.puzzledemo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Pc 02 on 9/15/2017.
 */

@SuppressLint("ValidFragment")
public class MyDialog extends DialogFragment {
    Context context;
    EditText edt;
    TextView tv,tv_1;
    int situation = 0;
    MyTimer timer;

    public MyDialog(Context context, TextView tv) {
        this.context = context;
        this.tv = tv;
    }

    public MyDialog(Context context, TextView tv, int situation) {
        this.context = context;
        this.tv = tv;
        this.situation = situation;
    }

    public MyDialog(Context context, MyTimer timer) {
        this.context = context;
        this.timer = timer;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        dialog.setContentView(R.layout.dialog_layout);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        tv_1 = dialog.findViewById(R.id.tv_title);
//        edt = dialog.findViewById(R.id.editText);
        if (situation == 1) {
            tv_1.setText("CONGRATULATION! \n You win the game (and received funcoin) ! \n" + "Your step: " + PuzzleActivity.totalStep );
            ((Button)dialog.findViewById(R.id.button)).setText("OK");
            dialog.findViewById(R.id.button).setVisibility(View.GONE);
        }

//        dialog.findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                int totalStep = Integer.parseInt(edt.getText().toString().trim());
//                if (totalStep == 0) Toast.makeText(context, "Larger than 0 please", Toast.LENGTH_SHORT).show();
//                else {
//                    PuzzleActivity.totalStep = totalStep;
//                    dismiss();
//                }
//            }
//        });
        tv_1.setText("CONTINUE?(Spend Funcoin)\n" + "Your step: " + PuzzleActivity.totalStep);
        dialog.findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timer.setTimeLast(PuzzleActivity.MAX_TIME);
                timer.setTimeTick(PuzzleActivity.PER_COUNT_SECOND);
                timer.start();
                dismiss();
            }
        });

        dialog.findViewById(R.id.button_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((Activity)context).finish();
            }
        });
        return dialog;
    }

//    @Override
//    public void onDismiss(DialogInterface dialog) {
//        super.onDismiss(dialog);
//        tv.setText(PuzzleActivity.totalStep+"");
//    }
}
