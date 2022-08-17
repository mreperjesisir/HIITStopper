package com.example.hiitstopper;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private SeekBar mSetTime;
    private SeekBar mSetRest;
    private Button mStartTimerButton;
    private NumberPicker mSetPicker;
    private TextView mTimerText;

    private boolean mIsPaused;
    private boolean mIsTimerRunning;
    private CountDownTimer mActiveTimer;
    private long mMillisUntilFinished;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSetTime = (SeekBar) findViewById(R.id.seekbar_set);
        mSetRest = (SeekBar) findViewById(R.id.seekbar_rest);
        mStartTimerButton = (Button) findViewById(R.id.button_start);
        mSetPicker = (NumberPicker) findViewById(R.id.set_picker);
        mTimerText = (TextView) findViewById(R.id.tv_timer);

        mSetTime.setMax(180);
        mSetRest.setMax(90);

        String[] numbers = new String[] {"12", "11", "10", "9","8", "7", "6", "5", "4", "3", "2", "1"};
        mSetPicker.setDisplayedValues(numbers);
        mSetPicker.setMinValue(1);
        mSetPicker.setMaxValue(numbers.length);
        mSetPicker.setWrapSelectorWheel(false);
        mSetPicker.setValue(10);

        mStartTimerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("MainActivity", "The Start button was pressed");
                startTimer();
            }
        });
    }

    private void startTimer(){

        if (mIsTimerRunning){
            return;
        }
        mIsPaused = false;
        mIsTimerRunning = true;
        mSetRest.setVisibility(View.INVISIBLE);
        mSetTime.setVisibility(View.INVISIBLE);
        mSetPicker.setVisibility(View.INVISIBLE);
        mTimerText.setVisibility(View.VISIBLE);
        mStartTimerButton.setText("Pause");
        mStartTimerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pauseTimer();
            }
        });

        mActiveTimer = new CountDownTimer(mSetTime.getProgress() * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mMillisUntilFinished = millisUntilFinished;
                setCurrentTime(mMillisUntilFinished);
            }
            @Override
            public void onFinish() {
                mSetRest.setVisibility(View.VISIBLE);
                mSetTime.setVisibility(View.VISIBLE);
                mSetPicker.setVisibility(View.VISIBLE);
                mTimerText.setVisibility(View.INVISIBLE);
                mStartTimerButton.setText("Start");
                mIsTimerRunning = false;
            }
        }.start();
    }

    //TODO: Rewrite startTimer() and pauseTimer() so that the user can pause multiple times

    private void pauseTimer(){
        mIsPaused = true;
        mActiveTimer.cancel();
        mStartTimerButton.setText("Resume");
        mStartTimerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mStartTimerButton.setText("Pause");
                mActiveTimer = new CountDownTimer(mMillisUntilFinished, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        mMillisUntilFinished = millisUntilFinished;
                        setCurrentTime(mMillisUntilFinished);
                    }

                    @Override
                    public void onFinish() {
                        mSetRest.setVisibility(View.VISIBLE);
                        mSetTime.setVisibility(View.VISIBLE);
                        mSetPicker.setVisibility(View.VISIBLE);
                        mTimerText.setVisibility(View.INVISIBLE);
                        mStartTimerButton.setText("Start");
                        mIsTimerRunning = false;
                    }
                }.start();
            }
        });

    }

    private void setCurrentTime(long millisUntilFinished) {
        int minutes = (int) millisUntilFinished / 1000 / 60;
        int seconds = (int) millisUntilFinished /1000 % 60;
        mTimerText.setText(minutes + ":" + seconds);
    }

}