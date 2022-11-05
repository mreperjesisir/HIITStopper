package com.example.hiitstopper;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.switchmaterial.SwitchMaterial;

import me.tankery.lib.circularseekbar.CircularSeekBar;

public class MainActivity extends AppCompatActivity{

    //TODO 1: add a style, theme, and background picture
    //TODO 2: Add a Bluetooth connection feature for heartrate monitors
    //TODO 3: Wire up the Heartrate Textview


    private static final int ENABLE_BLUETOOTH_REQUEST_CODE = 1;

    public static final String NUMBER_OF_SETS = "sets";
    public static final String EXERCISE_IN_MS = "exercise";
    public static final String REST_IN_MS = "rest";

    private BluetoothManager mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
    private BluetoothAdapter mBluetoothAdapter = mBluetoothManager.getAdapter();


    private CircularSeekBar mExerciseTimeSeekBar;
    private CircularSeekBar mRestSeekBar;
    private MaterialButton mStartTimerButton;
    private NumberPicker mSetPicker;
    private SwitchMaterial mSwitch;
    private TextView mLabelExercise;
    private TextView mLabelRest;
    private TextView mLabelSets;
    private TextView mExerciseTextView;
    private TextView mRestTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Sets up the views

        mExerciseTimeSeekBar = findViewById(R.id.seekbar_set);
        mRestSeekBar = findViewById(R.id.seekbar_rest);
        mStartTimerButton = findViewById(R.id.button_start);
        mSetPicker = findViewById(R.id.set_picker);
        mSwitch = findViewById(R.id.switch_bluetooth);
        mLabelExercise = findViewById(R.id.label_exercise);
        mLabelRest = findViewById(R.id.label_rest);
        mLabelSets = findViewById(R.id.label_sets);
        mExerciseTextView = findViewById(R.id.tv_exercise_time);
        mRestTime = findViewById(R.id.tv_rest_time);

        // Sets up Seekbars and Pickers

        mExerciseTimeSeekBar.setMax(180);
        mExerciseTimeSeekBar.setProgress(60);
        mExerciseTextView.setText("1:00\u00A0");
        mRestSeekBar.setMax(90);
        mRestSeekBar.setProgress(30);
        mRestTime.setText("0:30\u00A0");
        mSetPicker.setDisplayedValues(getResources().getStringArray(R.array.sets));
        mSetPicker.setMinValue(1);
        mSetPicker.setMaxValue(getResources().getStringArray(R.array.sets).length);
        mSetPicker.setWrapSelectorWheel(false);
        mSetPicker.setValue(3);

        // Adds click and change listeners

        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    //TODO: check if Bluetooth is enabled

                }
            }
        });

        mExerciseTimeSeekBar.setOnSeekBarChangeListener(new CircularSeekBar.OnCircularSeekBarChangeListener() {
            @Override
            public void onProgressChanged(CircularSeekBar circularSeekBar, float v, boolean b) {

                Float f = v * 1000;
                //add long values of float v
                mExerciseTextView.setText(TimerUtilities.getFormattedTime(f.longValue()));
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
                mRestTime.setText(TimerUtilities.getFormattedTime(f.longValue()));

            }

            @Override
            public void onStopTrackingTouch(CircularSeekBar circularSeekBar) {

            }

            @Override
            public void onStartTrackingTouch(CircularSeekBar circularSeekBar) {

            }
        });

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
    }

    @Override
    protected void onResume() {
        super.onResume();
        //TODO: Check bluetooth connection here
    }
}