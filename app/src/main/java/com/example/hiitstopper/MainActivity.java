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

// TODO 1: Implement a rest time timer
// TODO 2: Modify timer to add sets
// TODO 3: Replace vertical seekbars with circular seekbars
// TODO 4: Add TextViews to circular seekbars
// TODO 5: Add indication of progress on Timer view for user to know how many sets are left

public class MainActivity extends AppCompatActivity {

    private SeekBar mSetTime;
    private SeekBar mSetRest;
    private Button mStartTimerButton;
    private Button mPauseTimerButton;
    private Button mCancelTimerButton;
    private NumberPicker mSetPicker;
    private TextView mTimerText;

    private CountDownTimer mActiveTimer;
    private long mMillisUntilFinished;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mSetTime = (SeekBar) findViewById(R.id.seekbar_set);
        mSetRest = (SeekBar) findViewById(R.id.seekbar_rest);
        mStartTimerButton = (Button) findViewById(R.id.button_start);
        mPauseTimerButton = (Button) findViewById(R.id.button_pause);
        mCancelTimerButton = (Button) findViewById(R.id.button_cancel);
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
                startTimer();
            }
        });
        mPauseTimerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pauseTimer();
            }
        });
        mCancelTimerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActiveTimer.cancel();
                setStartingView();
            }
        });
    }

    private void startTimer(){

        setTimerView();

        mActiveTimer = new CountDownTimer(mSetTime.getProgress() * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mMillisUntilFinished = millisUntilFinished;
                setCurrentTime(mMillisUntilFinished);
            }
            @Override
            public void onFinish() {
                setStartingView();
            }
        }.start();
    }

    private void pauseTimer(){
        mActiveTimer.cancel();
        mPauseTimerButton.setText("Resume");
        mPauseTimerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPauseTimerButton.setText("Pause");
                mPauseTimerButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pauseTimer();
                    }
                });
                mActiveTimer = new CountDownTimer(mMillisUntilFinished, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        mMillisUntilFinished = millisUntilFinished;
                        setCurrentTime(mMillisUntilFinished);
                    }

                    @Override
                    public void onFinish() {
                        setStartingView();
                    }
                }.start();
            }
        });

    }

    private void setTimerView() {
        mStartTimerButton.setVisibility(View.INVISIBLE);
        mSetRest.setVisibility(View.INVISIBLE);
        mSetTime.setVisibility(View.INVISIBLE);
        mSetPicker.setVisibility(View.INVISIBLE);
        mPauseTimerButton.setVisibility(View.VISIBLE);
        mCancelTimerButton.setVisibility(View.VISIBLE);
        mTimerText.setVisibility(View.VISIBLE);
    }

    private void setStartingView() {
        mSetRest.setVisibility(View.VISIBLE);
        mSetTime.setVisibility(View.VISIBLE);
        mSetPicker.setVisibility(View.VISIBLE);
        mStartTimerButton.setVisibility(View.VISIBLE);
        mTimerText.setVisibility(View.INVISIBLE);
        mCancelTimerButton.setVisibility(View.INVISIBLE);
        mPauseTimerButton.setVisibility(View.INVISIBLE);
    }

    private void setCurrentTime(long millisUntilFinished) {
        int minutes = (int) millisUntilFinished / 1000 / 60;
        int seconds = (int) millisUntilFinished /1000 % 60;

        String secondsWithZero = String.valueOf(seconds);
        if (seconds<10){
            secondsWithZero = "0" + seconds;
        }

        String formattedTime = minutes + ":" + secondsWithZero;
        mTimerText.setText(formattedTime);
    }





}