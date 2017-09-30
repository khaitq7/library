package hatiboy.com.mobautoscrolltextviewdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextSwitcher;
import android.widget.Toast;

import com.mobgame.library.MobAutoScrollTextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final MobAutoScrollTextView textView = (MobAutoScrollTextView) findViewById(R.id.text);
        final ArrayList<String> texts = new ArrayList<>();
        texts.add("đây");
        texts.add("là");
        texts.add("demo");
        texts.add("MobAutoScrollTextView");
        texts.add("done");

        findViewById(R.id.btnStart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    int duration = Integer.parseInt(((EditText) findViewById(R.id.duration)).getText().toString());
                    boolean repeat = ((Switch) findViewById(R.id.repeat)).isChecked();
                    boolean reverse = ((Switch) findViewById(R.id.reverse)).isChecked();
                    textView.startAutoScroll(texts, duration, repeat, reverse, new MobAutoScrollTextView.OnTextViewScrollListener() {
                        @Override
                        public void onTextViewScroll(String text, boolean reverse) {
                        }

                        @Override
                        public void onTextViewScrollFinished() {
                            Log.d(TAG, "onTextViewScrollFinished: ");
                            Toast.makeText(getApplicationContext(), "TextViewScrollFinished", Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}
