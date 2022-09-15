package com.example.hiitstopper;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

import me.tankery.lib.circularseekbar.CircularSeekBar;

public class MainActivity extends AppCompatActivity {

    public static final String NUMBER_OF_SETS = "sets";
    public static final String EXERCISE_IN_MS = "exercise";
    public static final String REST_IN_MS = "rest";

    private CircularSeekBar mExerciseTimeSeekBar;
    private CircularSeekBar mRestSeekBar;
    private MaterialButton mStartTimerButton;
    private NumberPicker mSetPicker;
    private TextView mLabelExercise;
    private TextView mLabelRest;
    private TextView mLabelSets;
    private TextView mExerciseTextView;
    private TextView mRestTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mExerciseTimeSeekBar = (CircularSeekBar) findViewById(R.id.seekbar_set);
        mRestSeekBar = (CircularSeekBar) findViewById(R.id.seekbar_rest);
        mStartTimerButton = (MaterialButton) findViewById(R.id.button_start);
        mSetPicker = (NumberPicker) findViewById(R.id.set_picker);
        mLabelExercise = (TextView) findViewById(R.id.label_exercise);
        mLabelRest = (TextView) findViewById(R.id.label_rest);
        mLabelSets = (TextView) findViewById(R.id.label_sets);
        mExerciseTextView = (TextView) findViewById(R.id.tv_exercise_time);
        mRestTime = (TextView) findViewById(R.id.tv_rest_time);

        mExerciseTimeSeekBar.setMax(180);
        mExerciseTimeSeekBar.setProgress(60);
        mExerciseTextView.setText("1:00\u00A0");
        mRestSeekBar.setMax(90);
        mRestSeekBar.setProgress(30);
        mRestTime.setText("0:30\u00A0");
        mExerciseTimeSeekBar.setOnSeekBarChangeListener(new CircularSeekBar.OnCircularSeekBarChangeListener() {
            @Override
            public void onProgressChanged(CircularSeekBar circularSeekBar, float v, boolean b) {

                Float f = v * 1000;
                //add long values of float v
                mExerciseTextView.setText(getFormattedCountDownTime(f.longValue()));
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

                Intent i = new Intent(MainActivity.this, TimerActivity.class);
                i.putExtra(NUMBER_OF_SETS, mSetPicker.getValue());
                i.putExtra(EXERCISE_IN_MS, (int)mExerciseTimeSeekBar.getProgress()*1000);
                i.putExtra(REST_IN_MS, (int)mRestSeekBar.getProgress()*1000);
                startActivity(i);
            }
        });

        // Bug fixed: pressing the cancel button while paused then pressing start
        // won't cause two timers to run

    }

    public static String getFormattedCountDownTime(long millisUntilFinished) {
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