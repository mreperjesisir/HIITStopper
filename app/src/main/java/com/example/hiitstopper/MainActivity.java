package com.example.hiitstopper;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

import me.tankery.lib.circularseekbar.CircularSeekBar;

// COMPLETED 1: Implement a rest time timer
// COMPLETED 2: Modify timer to add sets
// COMPLETED 3: Replace vertical seekbars with circular seekbars
// COMPLETED 4: Add Labels and TextViews to circular seekbars
// COMPLETED 5: Added correction values to the timers for accuracy
// COMPLETED 6: Updated font and style
// COMPLETED 7: To remove code repetition, refactor setCountDownTime()
//  so it returns a formatted String instead of setting text directly
// TODO 8: Move methods setting the timer in a separate utility class
// TODO 9: Add indication of progress on Timer view for user to know how many sets are left

public class MainActivity extends AppCompatActivity {

    private int mNumberOfSets;

    private CircularSeekBar mSetsSeekBar;
    private CircularSeekBar mRestSeekBar;
    private MaterialButton mStartTimerButton;
    private MaterialButton mPauseTimerButton;
    private MaterialButton mCancelTimerButton;
    private NumberPicker mSetPicker;
    private TextView mTimerText;
    private TextView mLabelExercise;
    private TextView mLabelRest;
    private TextView mLabelSets;
    private TextView mExerciseTime;
    private TextView mRestTime;

    private CountDownTimer mActiveTimer;
    private long mMillisUntilExerciseFinished;
    private long mMillisUntilRestFinished;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSetsSeekBar = (CircularSeekBar) findViewById(R.id.seekbar_set);
        mRestSeekBar = (CircularSeekBar) findViewById(R.id.seekbar_rest);
        mStartTimerButton = (MaterialButton) findViewById(R.id.button_start);
        mPauseTimerButton = (MaterialButton) findViewById(R.id.button_pause);
        mCancelTimerButton = (MaterialButton) findViewById(R.id.button_cancel);
        mSetPicker = (NumberPicker) findViewById(R.id.set_picker);
        mTimerText = (TextView) findViewById(R.id.tv_timer);
        mLabelExercise = (TextView) findViewById(R.id.label_exercise);
        mLabelRest = (TextView) findViewById(R.id.label_rest);
        mLabelSets = (TextView) findViewById(R.id.label_sets);
        mExerciseTime = (TextView) findViewById(R.id.tv_exercise_time);
        mRestTime = (TextView) findViewById(R.id.tv_rest_time);

        mSetsSeekBar.setMax(180);
        mSetsSeekBar.setProgress(60);
        mExerciseTime.setText("1:00\u00A0");
        mRestSeekBar.setMax(90);
        mRestSeekBar.setProgress(30);
        mRestTime.setText("0:30\u00A0");

        mSetsSeekBar.setOnSeekBarChangeListener(new CircularSeekBar.OnCircularSeekBarChangeListener() {
            @Override
            public void onProgressChanged(CircularSeekBar circularSeekBar, float v, boolean b) {

                Float f = v * 1000;
                //add long values of float v
                mExerciseTime.setText(getFormattedCountDownTime(f.longValue()));
            }

            @Override
            public void onStopTrackingTouch(CircularSeekBar circularSeekBar) {

            }

            @Override
            public void onStartTrackingTouch(CircularSeekBar circularSeekBar) {

            }
        });

        mRestSeekBar.setOnSeekBarChangeListener(new CircularSeekBar.OnCircularSeekBarChangeListener() {
            @Override
            public void onProgressChanged(CircularSeekBar circularSeekBar, float v, boolean b) {
                Float f = v * 1000;
                //add long values of float v
                mRestTime.setText(getFormattedCountDownTime(f.longValue()));

            }

            @Override
            public void onStopTrackingTouch(CircularSeekBar circularSeekBar) {

            }

            @Override
            public void onStartTrackingTouch(CircularSeekBar circularSeekBar) {

            }
        });


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
                startExerciseTimer((int)mSetsSeekBar.getProgress()*1000);
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

    private void startExerciseTimer(int exerciseTimeInMilliseconds){

        int timeCorrectionMilliseconds = 499;
        exerciseTimeInMilliseconds = timeCorrectionMilliseconds + exerciseTimeInMilliseconds;

        setTimerView();
        mTimerText.setText(getFormattedCountDownTime(exerciseTimeInMilliseconds));
            mActiveTimer = new CountDownTimer(exerciseTimeInMilliseconds, 100) {
                @Override
                public void onTick(long millisUntilFinished) {

                    //TODO (optional): check if 0 is 1.5 seconds long. If it is, finish 499s earlier

                    long roundedTime = Math.round((float) millisUntilFinished / 1000);
                    mMillisUntilExerciseFinished = roundedTime * 1000;
                    mTimerText.setText(getFormattedCountDownTime(mMillisUntilExerciseFinished));
                }

                @Override
                public void onFinish() {
                    mTimerText.setText(getFormattedCountDownTime(0));
                    mNumberOfSets --;
                    try {
                        wait(499);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (mNumberOfSets>0){
                        startRestTimer((int)mRestSeekBar.getProgress()*1000);
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
                        long roundedTime = Math.round((float) millisUntilFinished / 1000);
                        mMillisUntilRestFinished = roundedTime * 1000;
                        mTimerText.setText(getFormattedCountDownTime(mMillisUntilRestFinished));
                    }

                    @Override
                    public void onFinish() {
                        mTimerText.setText(getFormattedCountDownTime(0));
                        try {
                            wait(499);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        setSetupView();
                    }
                }.start();
            }
        });
    }

    private void startRestTimer(int restTimeInMilliseconds){

        int timeCorrectionMilliseconds = 499;
        restTimeInMilliseconds = timeCorrectionMilliseconds + restTimeInMilliseconds;

        setTimerView();
        mTimerText.setText(getFormattedCountDownTime(restTimeInMilliseconds));

        new CountDownTimer(restTimeInMilliseconds, 100) {
            @Override
            public void onTick(long millisUntilFinished) {
                long roundedTime = Math.round((float) millisUntilFinished / 1000);
                mMillisUntilRestFinished = roundedTime * 1000;
                mTimerText.setText(getFormattedCountDownTime(mMillisUntilRestFinished));
            }

            @Override
            public void onFinish() {
                try {
                    wait(499);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                startExerciseTimer((int)mSetsSeekBar.getProgress()*1000);
            }
        }.start();
    }

    private void setTimerView() {
        mExerciseTime.setVisibility(View.INVISIBLE);
        mRestTime.setVisibility(View.INVISIBLE);
        mLabelRest.setVisibility(View.INVISIBLE);
        mLabelExercise.setVisibility(View.INVISIBLE);
        mLabelSets.setVisibility(View.INVISIBLE);
        mStartTimerButton.setVisibility(View.INVISIBLE);
        mRestSeekBar.setVisibility(View.INVISIBLE);
        mSetsSeekBar.setVisibility(View.INVISIBLE);
        mSetPicker.setVisibility(View.INVISIBLE);
        mPauseTimerButton.setVisibility(View.VISIBLE);
        mCancelTimerButton.setVisibility(View.VISIBLE);
        mTimerText.setVisibility(View.VISIBLE);
    }

    private void setSetupView() {
        mExerciseTime.setVisibility(View.VISIBLE);
        mRestTime.setVisibility(View.VISIBLE);
        mLabelRest.setVisibility(View.VISIBLE);
        mLabelExercise.setVisibility(View.VISIBLE);
        mLabelSets.setVisibility(View.VISIBLE);
        mRestSeekBar.setVisibility(View.VISIBLE);
        mSetsSeekBar.setVisibility(View.VISIBLE);
        mSetPicker.setVisibility(View.VISIBLE);
        mStartTimerButton.setVisibility(View.VISIBLE);
        mTimerText.setVisibility(View.INVISIBLE);
        mCancelTimerButton.setVisibility(View.INVISIBLE);
        mPauseTimerButton.setVisibility(View.INVISIBLE);
    }

    private String getFormattedCountDownTime(long millisUntilFinished) {
        int minutes = (int) millisUntilFinished / 1000 / 60;
        int seconds = (int) millisUntilFinished / 1000 % 60;

        String secondsWithZero = String.valueOf(seconds);
        if (seconds<10){
            secondsWithZero = "0" + seconds;
        }

        String formattedTime = minutes + ":" + secondsWithZero + "\u00A0";
        return formattedTime;
    }
}