package com.Example.room;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

/**
 * Created by knowroaming on 15-03-17.
 */
public class Settings extends Activity{

//    public boolean shouldRecord;

    private RadioGroup radioOption;
    private RadioButton radioOptionButton;

    private NumberPicker minutePicker = null;
    private NumberPicker secondPicker = null;

    public static final String MyPREFS = "MyPref";
    public static final String record = "Record";
    public static final String minuteTime = "Minutes";
    public static final String secondTime = "Seconds";

    SharedPreferences sharedPreferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //Remove notification bar
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //set content view AFTER ABOVE sequence (to avoid crash)
        this.setContentView(R.layout.activity_setting);

        sharedPreferences = getSharedPreferences(MyPREFS, Context.MODE_PRIVATE);

        radioOption = (RadioGroup)findViewById(R.id.radioOption);

        minutePicker = (NumberPicker)findViewById(R.id.minutePicker);
        minutePicker.setMaxValue(59);
        minutePicker.setMinValue(0);

        secondPicker = (NumberPicker)findViewById(R.id.secondPicker);
        secondPicker.setMaxValue(59);
        secondPicker.setMinValue(5);
//        setDefaultSettings();
    }

    @Override
    public void onStart() {
        setDefaultSettings();
        super.onStart();
    }

    public void setDefaultSettings() {
        // Set Radio buttons to previous value set by user
        boolean shouldRecord = sharedPreferences.getBoolean(record,true);
        if (shouldRecord) {
            radioOption.check(R.id.radioRecord);
            radioOptionButton = (RadioButton)findViewById(R.id.radioRecord);
            radioOptionButton.setEnabled(true);
        } else {
            radioOption.check(R.id.radioRecognize);
            radioOptionButton = (RadioButton)findViewById(R.id.radioRecognize);
            radioOptionButton.setEnabled(true);
        }

        // Set timer to previous value set by user
        int minuteValue = sharedPreferences.getInt(minuteTime, 0)/60000;
        minutePicker.setValue(minuteValue);

        int secondValue = sharedPreferences.getInt(secondTime, 5000)/1000;
        secondPicker.setValue(secondValue);
    }

    public void easyModeClicked(View view) {
        Toast.makeText(getApplicationContext(), "So,... I see you chose the easy eh!",
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressed(){
        // Save the settings options set by user
        Editor editor = sharedPreferences.edit();

        int selectedId = radioOption.getCheckedRadioButtonId();
        radioOptionButton = (RadioButton)findViewById(selectedId);
        if (radioOptionButton.getId() == R.id.radioRecord){
            editor.putBoolean(record,true);
        } else {
            editor.putBoolean(record,false);
        }

        int minute_Time = minutePicker.getValue() * 60000;
        int second_Time = secondPicker.getValue() * 1000;
        editor.putInt(minuteTime, minute_Time);
        editor.putInt(secondTime, second_Time);

        editor.commit();

        super.onBackPressed();
    }
//    public void shouldRecord(View view) {
//        if (audioToggle.isChecked()){
//            ((MyApplication) this.getApplication()).setShouldRecord(true);
//        } else {
//            ((MyApplication) this.getApplication()).setShouldRecord(false);
//        }
//    }
}
