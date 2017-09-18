package com.funtap.minigame.puzzledemo;

import android.animation.ObjectAnimator;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.Uri;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class PuzzleActivity extends AppCompatActivity implements MyTimer.OnMyTimerListener {
    public static int MAX_TIME = 60000;
    public static final int PER_COUNT_SECOND = 1000;
    Dimension dimension;
    GameBoard board;
    Bitmap sourceImage;
    public static int totalStep = 0;
    RelativeLayout layout;
    TextView tv_count;
    int colummn,row,image,time;
    ProgressBar progressBar;
    MyTimer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puzzle);
        colummn = getIntent().getExtras().getInt("columnNum");
        row = getIntent().getExtras().getInt("rowNum");
        image = getIntent().getExtras().getInt("picture");
        time = getIntent().getExtras().getInt("time");
        MAX_TIME = time;
        init();
//        showDialogTotal();
//        Display display = getWindowManager().getDefaultDisplay();
//        Point size = new Point();
//        display.getSize(size);
//        int width = size.x;
//        int height = size.y;
    }

    private void init() {
        dimension = getDimension(row,colummn);
        tv_count = (TextView)findViewById(R.id.tv_count);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        Log.e("Max time", MAX_TIME+"");
        setProgressMax(progressBar,MAX_TIME);
        progressBar.setInterpolator(new DecelerateInterpolator());
        timer = new MyTimer(MAX_TIME,PER_COUNT_SECOND,progressBar);
        timer.setOnMyTimerListener(this);
        layout = (RelativeLayout) findViewById(R.id.centerLayout);
        layout.post(new Runnable() {
            @Override
            public void run() {
                int width = layout.getWidth();
                int height = layout.getHeight();
                Log.e("here",width+ "---" + height);
                sourceImage = resize(BitmapFactory.decodeResource(getResources(), image),width,height);
                board = new GameBoard(dimension,
                        (RelativeLayout) findViewById(R.id.centerLayout),
                        GameBoard.ORIENTATION_PORTRAIT, PuzzleActivity.this, getDimension(width,height),timer);
                PuzzleCreator creator = new PuzzleCreator(sourceImage, board);
                board.loadTiles(creator.createPuzzle());
                board.drawBoard();
            }
        });
    }

    private void setProgressMax(ProgressBar pb, int max) {
        pb.setMax(max * 100);
        progressBar.setProgress(progressBar.getMax());
    }

    private void showDialogTotal() {
        progressBar.setProgress(100);
        MyDialog dialog = new MyDialog(this,timer);
        dialog.setCancelable(false);
        dialog.show(getSupportFragmentManager(),"");
    }

    private static Bitmap resize(Bitmap image, int maxWidth, int maxHeight) {
        if (maxHeight > 0 && maxWidth > 0) {
            int width = image.getWidth();
            int height = image.getHeight();
            float ratioBitmap = (float) width / (float) height;
            float ratioMax = (float) maxWidth / (float) maxHeight;

            int finalWidth = maxWidth;
            int finalHeight = maxHeight;
            if (ratioMax > 1) {
                finalWidth = (int) ((float)maxHeight * ratioBitmap);
            } else {
                finalHeight = (int) ((float)maxWidth / ratioBitmap);
            }
            image = Bitmap.createScaledBitmap(image, finalWidth, finalHeight, true);
            return image;
        } else {
            return image;
        }
    }

    private Dimension getDimension(int x, int y){
        return new Dimension(x,y);
    }

    private Bitmap loadBitmapFromIntent(){
        Bitmap selectedImage = null;
        Uri imgUri = getIntent().getParcelableExtra("picture");
        try{
            InputStream imageStream = getContentResolver().openInputStream(imgUri);
            selectedImage = BitmapFactory.decodeStream(imageStream);
        }catch(FileNotFoundException ex){
            Log.e("LOADING ERROR", "Cannot load picture from the URI given", ex);
        }
        return selectedImage;
    }

    @Override
    protected void onPause() {
        super.onPause();
        timer.cancel();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (timer.getPauseTime() != 0 && timer.getPauseTime() != MAX_TIME) {
            timer = new MyTimer(timer.getPauseTime(),PER_COUNT_SECOND,progressBar);
            timer.setOnMyTimerListener(this);
        }
        if (timer!=null) timer.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timer.cancel();
    }

    @Override
    public void onListener(int result) {
        if (result == 1) {
            Log.e("Time is up", "HERE");
            showDialogTotal();
        }
    }
}
