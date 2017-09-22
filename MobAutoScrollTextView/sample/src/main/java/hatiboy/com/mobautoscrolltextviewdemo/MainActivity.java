package hatiboy.com.mobautoscrolltextviewdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextSwitcher;
import android.widget.Toast;

import com.mobgame.library.MobAutoScrollTextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    TextSwitcher mSwitcher;
    int currentIndex = 0;
    int messageCount = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final MobAutoScrollTextView textView = (MobAutoScrollTextView) findViewById(R.id.text);
        final ArrayList<String> texts = new ArrayList<>();
        texts.add("this is demo");
        texts.add("this is demo asdasdas");
        texts.add("dasd is dsadasdasdasemo");
        texts.add("this iasdags demo");
        texts.add("thisasdasd is demo");


        findViewById(R.id.btnStart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    int duration = Integer.parseInt(((EditText) findViewById(R.id.duration)).getText().toString());
                    boolean repeat = ((Switch) findViewById(R.id.repeat)).isChecked();
                    boolean reverse = ((Switch) findViewById(R.id.reverse)).isChecked();
                    textView.startAutoScroll(texts, duration, repeat, reverse);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
