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

    private int mNumberOfSets;

    private SeekBar mSetsSeekBar;
    private SeekBar mRestSeekBar;
    private Button mStartTimerButton;
    private Button mPauseTimerButton;
    private Button mCancelTimerButton;
    private NumberPicker mSetPicker;
    private TextView mTimerText;

    private CountDownTimer mActiveTimer;
    private long mMillisUntilExerciseFinished;
    private long mMillisUntilRestFinished;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mSetsSeekBar = (SeekBar) findViewById(R.id.seekbar_set);
        mRestSeekBar = (SeekBar) findViewById(R.id.seekbar_rest);
        mStartTimerButton = (Button) findViewById(R.id.button_start);
        mPauseTimerButton = (Button) findViewById(R.id.button_pause);
        mCancelTimerButton = (Button) findViewById(R.id.button_cancel);
        mSetPicker = (NumberPicker) findViewById(R.id.set_picker);
        mTimerText = (TextView) findViewById(R.id.tv_timer);

        mSetsSeekBar.setMax(180);
        mRestSeekBar.setMax(90);

        mSetPicker.setDisplayedValues(getResources().getStringArray(R.array.sets));
        mSetPicker.setMinValue(1);
        mSetPicker.setMaxValue(getResources().getStringArray(R.array.sets).length);
        mSetPicker.setWrapSelectorWheel(false);
        mSetPicker.setValue(3);

        mStartTimerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNumberOfSets = mSetPicker.getValue();
                Log.i("MainActivity", "The setsSeekbar progress value is " + mSetsSeekBar.getProgress());
                startExerciseTimer(mSetsSeekBar.getProgress()*1000);
            }
        });
        mPauseTimerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pauseExerciseTimer();
            }
        });
        mCancelTimerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActiveTimer.cancel();
                setSetupView();
            }
        });
    }


    // TODO: There is a problem here. The timer often skips a second when it starts.
    //  Lower the interval to 100 milliseconds and round it. This is as close you can get
    //  to showing accurate seconds. Check stackoverflow for implementation examples

    private void startExerciseTimer(int exerciseTimeInMilliseconds){

        setTimerView();
        setCurrentTime(exerciseTimeInMilliseconds);
            mActiveTimer = new CountDownTimer(exerciseTimeInMilliseconds, 100) {
                @Override
                public void onTick(long millisUntilFinished) {

                    mMillisUntilExerciseFinished = millisUntilFinished;
                    setCurrentTime(mMillisUntilExerciseFinished);
                }

                @Override
                public void onFinish() {
                    mNumberOfSets --;
                    if (mNumberOfSets>0){
                        startRestTimer(mRestSeekBar.getProgress()*1000);
                    } else {
                        setSetupView();
                    }

                }
            }.start();
    }

    private void pauseExerciseTimer(){
        mActiveTimer.cancel();
        mPauseTimerButton.setText("Resume");
        mPauseTimerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPauseTimerButton.setText("Pause");
                mPauseTimerButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pauseExerciseTimer();
                    }
                });
                mActiveTimer = new CountDownTimer(mMillisUntilExerciseFinished, 100) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        mMillisUntilExerciseFinished = millisUntilFinished;
                        setCurrentTime(mMillisUntilExerciseFinished);
                    }

                    @Override
                    public void onFinish() {
                        setSetupView();
                    }
                }.start();
            }
        });
    }

    private void startRestTimer(int restTimeInMilliseconds){

        setTimerView();
        setCurrentTime(restTimeInMilliseconds);

        new CountDownTimer(restTimeInMilliseconds, 100) {
            @Override
            public void onTick(long millisUntilFinished) {
                mMillisUntilRestFinished = millisUntilFinished;
                setCurrentTime(mMillisUntilRestFinished);
            }

            @Override
            public void onFinish() {
                startExerciseTimer(mSetsSeekBar.getProgress()*1000);
            }
        }.start();
    }

    private void setTimerView() {
        mStartTimerButton.setVisibility(View.INVISIBLE);
        mRestSeekBar.setVisibility(View.INVISIBLE);
        mSetsSeekBar.setVisibility(View.INVISIBLE);
        mSetPicker.setVisibility(View.INVISIBLE);
        mPauseTimerButton.setVisibility(View.VISIBLE);
        mCancelTimerButton.setVisibility(View.VISIBLE);
        mTimerText.setVisibility(View.VISIBLE);
    }

    private void setSetupView() {
        mRestSeekBar.setVisibility(View.VISIBLE);
        mSetsSeekBar.setVisibility(View.VISIBLE);
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