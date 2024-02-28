package com.example.yicard;
import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.view.inputmethod.InputMethodManager;
import java.util.Calendar;
import android.content.Context;


public class AlarmActivity2 extends Activity {
    private EditText editText;
    private TextView displayText;

    private TimePicker timePicker;
    private Button setAlarmButton;
    private Button stopAlarmButton;
    private Button goToMainActivityButton;
    private MediaPlayer mediaPlayer;
    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        View view = getCurrentFocus();

        if (view != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_m);

        timePicker = findViewById(R.id.time);
        setAlarmButton = findViewById(R.id.button);
        stopAlarmButton = findViewById(R.id.button2);
        goToMainActivityButton = findViewById(R.id.goToAnotherPageButton);
        editText = findViewById(R.id.editText);
        Button confirmButton = findViewById(R.id.confirmButton);
        displayText = findViewById(R.id.displayText);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 获取EditText内容并显示在TextView上
                displayText.setText(editText.getText().toString());

                // 清空EditText的内容
                editText.getText().clear();

                // 隐藏键盘
                hideKeyboard();
            }
        });

        setAlarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setAlarm();
            }
        });

        stopAlarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopAlarm();
            }
        });

        goToMainActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToMainActivity();
            }
        });
    }

    private void setAlarm() {
        int hour = timePicker.getCurrentHour();
        int minute = timePicker.getCurrentMinute();
        String userInput = editText.getText().toString();

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);

        long alarmTime = calendar.getTimeInMillis();

        // Using a Handler to post a delayed message to simulate the alarm triggering
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                playAlarmSound();
            }
        }, alarmTime - System.currentTimeMillis());
    }

    private void playAlarmSound() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mediaPlayer = MediaPlayer.create(AlarmActivity2.this, R.raw.alarm_sound);

                // Ensure that the mediaPlayer is not null before starting
                if (mediaPlayer != null) {
                    mediaPlayer.start();
                }
            }
        }).start();
    }

    private void stopAlarm() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    private void goToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
