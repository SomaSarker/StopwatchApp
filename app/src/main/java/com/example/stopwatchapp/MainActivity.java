package com.example.stopwatchapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Locale;



public class MainActivity extends AppCompatActivity {
    TextView stopwatchText;
    Button startPauseButton;
    long startTime = 0;
    long elapsedTime = 0;
    boolean isRunning = false;
    Handler handler = new Handler();

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putLong("startTime", startTime);
        savedInstanceState.putLong("elapsedTime", elapsedTime);
        savedInstanceState.putBoolean("isRunning", isRunning);
        super.onSaveInstanceState(savedInstanceState);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        stopwatchText = findViewById(R.id.stopwatchText);
        startPauseButton = findViewById(R.id.startPauseButton);
        if(savedInstanceState != null)
        {
            startTime = savedInstanceState.getLong("startTime");
            elapsedTime = savedInstanceState.getLong("elapsedTime");
            isRunning = savedInstanceState.getBoolean("isRunning");
        }
        updateStopwatchText();

        showToast("onCreate");
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (isRunning) {
            // Resume the stopwatch if it was running before going to the background
            startTimer();
        }
        showToast("onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        showToast("onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        showToast("onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (isRunning) {
            // Stop the stopwatch if it's running and the app is going to the background
            pauseTimer();
            showToast("onStop");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        showToast("onDestroy");
    }

    public void onClickStartPause(View view) {
        if (isRunning) {
            pauseTimer();
        } else {
            startTimer();
        }
    }

    public void onClickReset(View view) {
        resetTimer();
    }

    private void updateStartButtonColor(boolean isRunning) {
        int buttonColor = isRunning ? getResources().getColor(R.color.colorRed) : getResources().getColor(R.color.colorGreen);
        startPauseButton.setBackgroundColor(buttonColor);
    }

    public void startTimer() {
        isRunning = true;
        startPauseButton.setText("Pause");
        updateStartButtonColor(true);
        startTime = System.currentTimeMillis() - elapsedTime;
        handler.postDelayed(updateTimer, 10);
    }

    public void pauseTimer() {
        isRunning = false;
        startPauseButton.setText("Start");
        updateStartButtonColor(false);
        handler.removeCallbacks(updateTimer);
    }

    public void resetTimer() {
        isRunning = false;
        startPauseButton.setText("Start");
        elapsedTime = 0;
        updateStopwatchText();
        handler.removeCallbacks(updateTimer);
    }

    private Runnable updateTimer = new Runnable() {
        @Override
        public void run() {
            elapsedTime = System.currentTimeMillis() - startTime;
            updateStopwatchText();
            handler.postDelayed(this, 10);
        }
    };

    public void updateStopwatchText() {
        int hours = (int) (elapsedTime / 3600000);
        int minutes = (int) ((elapsedTime % 3600000) / 60000);
        int seconds = (int) ((elapsedTime % 60000) / 1000);
        int millis = (int) (elapsedTime % 1000);

        String format = "%02d:%02d:%02d.%03d";
        String formatString = String.format(Locale.getDefault(), format, hours, minutes, seconds, millis);
        stopwatchText.setText(formatString);
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}