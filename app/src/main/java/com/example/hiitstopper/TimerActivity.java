package com.example.hiitstopper;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

import me.tankery.lib.circularseekbar.CircularSeekBar;

public class TimerActivity extends AppCompatActivity {

    //TODO:

    // These are intent extras
    private int mNumberOfSets;
    private int mRestTimeInMs;
    private int mExerciseTimeInMs;

    private CircularSeekBar mCountdownTimer;
    private MaterialButton mResumeTimerButton;
    private MaterialButton mPauseTimerButton;
    private MaterialButton mCancelTimerButton;
    private TextView mTimerText;
    private CountDownTimer mActiveTimer;

    private long mMillisUntilExerciseFinished;
    private long mMillisUntilRestFinished;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        mPauseTimerButton = (MaterialButton) findViewById(R.id.button_pause);
        mResumeTimerButton = (MaterialButton) findViewById(R.id.button_resume);
        mCancelTimerButton = (MaterialButton) findViewById(R.id.button_cancel);
        mTimerText = (TextView) findViewById(R.id.tv_timer);
        mCountdownTimer = (CircularSeekBar) findViewById(R.id.seekbar_countdown);
        mExerciseTimeInMs = getIntent().getIntExtra(MainActivity.EXERCISE_IN_MS, 0);
        mRestTimeInMs = getIntent().getIntExtra(MainActivity.REST_IN_MS, 0);
        mNumberOfSets = getIntent().getIntExtra(MainActivity.NUMBER_OF_SETS,0);

        mCancelTimerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mPauseTimerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActiveTimer.cancel();
                mCancelTimerButton.setVisibility(View.VISIBLE);
                mResumeTimerButton.setVisibility(View.VISIBLE);
                mPauseTimerButton.setVisibility(View.INVISIBLE);
            }
        });

        mResumeTimerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCancelTimerButton.setVisibility(View.INVISIBLE);
                mResumeTimerButton.setVisibility(View.INVISIBLE);
                mPauseTimerButton.setVisibility(View.VISIBLE);

                mActiveTimer = new CountDownTimer(mMillisUntilExerciseFinished, 100) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        long roundedTime = Math.round((float) millisUntilFinished / 1000);
                        mMillisUntilExerciseFinished = roundedTime * 1000;
                        mCountdownTimer.setProgress(roundedTime);
                        mTimerText.setText(MainActivity.getFormattedCountDownTime(mMillisUntilExerciseFinished));
                    }

                    @Override
                    public void onFinish() {
                        mTimerText.setText(MainActivity.getFormattedCountDownTime(0));
                        try {
                            wait(499);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
            }
        });

        startExerciseTimer(mExerciseTimeInMs);
    }

    private void startExerciseTimer(int exerciseTimeInMilliseconds){

        mCountdownTimer.setMax((float) exerciseTimeInMilliseconds / 1000);
        int timeCorrectionMilliseconds = 499;
        exerciseTimeInMilliseconds = timeCorrectionMilliseconds + exerciseTimeInMilliseconds;

        mTimerText.setText(MainActivity.getFormattedCountDownTime(exerciseTimeInMilliseconds));
        mActiveTimer = new CountDownTimer(exerciseTimeInMilliseconds, 100) {
            @Override
            public void onTick(long millisUntilFinished) {

                //TODO (optional): check if 0 is 1.5 seconds long. If it is, finish 499s earlier

                long roundedTime = Math.round((float) millisUntilFinished / 1000);
                mMillisUntilExerciseFinished = roundedTime * 1000;
                mCountdownTimer.setProgress(roundedTime);
                mTimerText.setText(MainActivity.getFormattedCountDownTime(mMillisUntilExerciseFinished));
            }

            @Override
            public void onFinish() {
                mTimerText.setText(MainActivity.getFormattedCountDownTime(0));
                mNumberOfSets --;
                try {
                    wait(499);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (mNumberOfSets>0){
                    startRestTimer(mRestTimeInMs);
                } else {
                    finish();
                }
            }
        }.start();
    }

    //TODO 2: Cancel and Pause buttons work yet when rest timer is active.

    private void startRestTimer(int restTimeInMs){
        mActiveTimer.cancel();
        mCountdownTimer.setMax((float) restTimeInMs / 1000);
        int timeCorrectionMilliseconds = 499;
        restTimeInMs = timeCorrectionMilliseconds + restTimeInMs;

        mTimerText.setText(MainActivity.getFormattedCountDownTime(restTimeInMs));
        mActiveTimer = new CountDownTimer(restTimeInMs, 100) {
            @Override
            public void onTick(long millisUntilFinished) {
                long roundedTime = Math.round((float) millisUntilFinished / 1000);
                mMillisUntilRestFinished = roundedTime * 1000;
                mCountdownTimer.setProgress(roundedTime);
                mTimerText.setText(MainActivity.getFormattedCountDownTime(mMillisUntilRestFinished));
            }
            @Override
            public void onFinish() {
                mTimerText.setText(MainActivity.getFormattedCountDownTime(0));
                try {
                    wait(499);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                startExerciseTimer(mExerciseTimeInMs);
            }
        }.start();
    }
}