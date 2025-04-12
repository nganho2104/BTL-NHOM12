package vn.edu.tlu.cse.btnhom.btap_android;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class LoadingActivity extends AppCompatActivity {
    private ProgressBar progressBar;
    private TextView textView;
    private int progressStatus = 0;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        progressBar = findViewById(R.id.progress_bar);
        textView = findViewById(R.id.textView);

        new Thread(() -> {
            while (progressStatus < 100) {
                progressStatus++;
                handler.post(() -> {
                    progressBar.setProgress(progressStatus);
                    textView.setText("Đang tải... " + progressStatus + " %");
                });
                try {
                    Thread.sleep(50); // 50ms * 100 = 5000ms = 5s
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            handler.post(() -> {
                Intent intent = new Intent(LoadingActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            });
        }).start();
    }
}