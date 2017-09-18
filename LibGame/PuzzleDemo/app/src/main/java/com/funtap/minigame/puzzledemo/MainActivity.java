package com.funtap.minigame.puzzledemo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    ImageView iv;
    EditText edt_rowNum, edt_columnNum, edt_time;
    Button bt_start;
    int imageResId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();
    }

    private void initUI() {
        iv = (ImageView)findViewById(R.id.imageView);
        edt_columnNum = (EditText)findViewById(R.id.edt_columnNum);
        edt_rowNum = (EditText)findViewById(R.id.edt_rowNum);
        edt_time = (EditText)findViewById(R.id.edt_maxTime);
        bt_start = (Button)findViewById(R.id.bt_start);
        bt_start.setOnClickListener(this);
        setImage(R.drawable.example1);
    }

    private void setImage(int example1) {
        imageResId = example1;
        iv.setImageResource(example1);
    }


    @Override
    public void onClick(View view) {
        if (edt_columnNum.getText().toString().equals("") || edt_rowNum.getText().toString().equals(""))
            Toast.makeText(this, "Please add columns and rows ", Toast.LENGTH_SHORT).show();
        else if (edt_time.getText().toString().equals(""))
            Toast.makeText(this, "Please add max time", Toast.LENGTH_SHORT).show();
        else {
            int columnNum = Integer.parseInt(edt_columnNum.getText().toString());
            int rowNum = Integer.parseInt(edt_rowNum.getText().toString());
            int time = Integer.parseInt(edt_time.getText().toString());
            if (columnNum < 2 || rowNum < 2 || time < 5) {
                Toast.makeText(this, "Please add columNum > 2, rowNum > 2, time > 5s", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent i = new Intent(MainActivity.this, PuzzleActivity.class);
            i.putExtra("columnNum",columnNum);
            i.putExtra("rowNum",rowNum);
            i.putExtra("picture",imageResId);
            i.putExtra("time",time*1000);
            startActivity(i);
//            dimension = getDimension(columnNum,rowNum);
        }
    }

    private Uri getImageUri (int ResId) {
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), ResId);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        String path = MediaStore.Images.Media.insertImage(getContentResolver(), bmp, "title", null);
        return Uri.parse(path);
    }

    private Bitmap convertToBitmap(int resId){
        return BitmapFactory.decodeResource(getResources(), resId);
    }

}
