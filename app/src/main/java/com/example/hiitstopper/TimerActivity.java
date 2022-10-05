package com.example.hiitstopper;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

import org.w3c.dom.Text;

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
    private TextView mSetsLeft;
    private TextView mHeartRate;
    private boolean mIsItRest;

    private long mTimeRemaining;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        mPauseTimerButton = findViewById(R.id.button_pause);
        mResumeTimerButton = findViewById(R.id.button_resume);
        mCancelTimerButton = findViewById(R.id.button_cancel);
        mTimerText = findViewById(R.id.tv_timer);
        mCountdownTimer = findViewById(R.id.seekbar_countdown);
        mSetsLeft = findViewById(R.id.tv_sets_left);
        mHeartRate = findViewById(R.id.tv_heart_rate);

        mCountdownTimer.setClickable(false);
        mExerciseTimeInMs = getIntent().getIntExtra(MainActivity.EXERCISE_IN_MS, 0);
        mRestTimeInMs = getIntent().getIntExtra(MainActivity.REST_IN_MS, 0);
        mNumberOfSets = getIntent().getIntExtra(MainActivity.NUMBER_OF_SETS,0);


        mCancelTimerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIsItRest = false;
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

                mActiveTimer = new CountDownTimer(mTimeRemaining, 100) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        long roundedTime = Math.round((float) millisUntilFinished / 1000);
                        mTimeRemaining = roundedTime * 1000;
                        mCountdownTimer.setProgress(millisUntilFinished);
                        mTimerText.setText(TimerUtilities.getFormattedTime(mTimeRemaining));
                    }

                    @Override
                    public void onFinish() {
                        mTimerText.setText(TimerUtilities.getFormattedTime(0));
                        try {
                            wait(499);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if (mIsItRest){
                            startExerciseTimer(mExerciseTimeInMs);
                        } else if (mNumberOfSets>0){
                            mNumberOfSets --;
                            startRestTimer(mRestTimeInMs);
                            } else {
                            finish();
                        }
                    }
                }.start();
            }
        });
        startExerciseTimer(mExerciseTimeInMs);
    }

    private void startExerciseTimer(int exerciseTimeInMilliseconds){
        mIsItRest = false;
        setRemainingSetsTv();
        int timeCorrectionMilliseconds = 499;
        exerciseTimeInMilliseconds = timeCorrectionMilliseconds + exerciseTimeInMilliseconds;
        mCountdownTimer.setMax((float) exerciseTimeInMilliseconds);

        mTimerText.setTextColor(getResources().getColor(R.color.green));
        mTimerText.setText(TimerUtilities.getFormattedTime(exerciseTimeInMilliseconds));
        mActiveTimer = new CountDownTimer(exerciseTimeInMilliseconds, 100) {
            @Override
            public void onTick(long millisUntilFinished) {
                Log.i("TimerActivity", "The time show is: " +
                        mTimerText.getText().toString() +
                        " and the time remaining is " + millisUntilFinished);

                long roundedTime = Math.round((float) millisUntilFinished / 1000);
                mTimeRemaining = roundedTime * 1000;
                mCountdownTimer.setProgress(millisUntilFinished);
                mTimerText.setText(TimerUtilities.getFormattedTime(mTimeRemaining));

            }

            @Override
            public void onFinish() {
                mTimerText.setText(TimerUtilities.getFormattedTime(0));
                mNumberOfSets --;
                mCountdownTimer.setProgress(0);
                try {
                    wait(100);
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

    private void startRestTimer(int restTimeInMs){
        mIsItRest = true;
        mActiveTimer.cancel();

        int timeCorrectionMilliseconds = 499;
        restTimeInMs = timeCorrectionMilliseconds + restTimeInMs;
        mCountdownTimer.setMax((float) restTimeInMs);

        mTimerText.setTextColor(getResources().getColor(R.color.yellow));
        mTimerText.setText(TimerUtilities.getFormattedTime(restTimeInMs));
        mActiveTimer = new CountDownTimer(restTimeInMs, 100) {
            @Override
            public void onTick(long millisUntilFinished) {
                long roundedTime = Math.round((float) millisUntilFinished / 1000);
                mTimeRemaining = roundedTime * 1000;
                mCountdownTimer.setProgress(millisUntilFinished);
                mTimerText.setText(TimerUtilities.getFormattedTime(mTimeRemaining));
            }
            @Override
            public void onFinish() {
                mTimerText.setText(TimerUtilities.getFormattedTime(0));
                mCountdownTimer.setProgress(0);
                try {
                    wait(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                startExerciseTimer(mExerciseTimeInMs);
            }
        }.start();
    }

    private void setRemainingSetsTv(){
        int remainingSets = mNumberOfSets-1;
        mSetsLeft.setText("Sets Left: " + remainingSets + "\u00A0");
    }
}