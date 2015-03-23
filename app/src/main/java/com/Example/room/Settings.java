package com.Example.room;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

/**
 * Created by knowroaming on 15-03-17.
 */
public class Settings extends Activity{

    public boolean shouldRecord;

    private RadioGroup radioOption;
    private RadioButton radioOptionButton;

    private NumberPicker minutePicker = null;
    private NumberPicker secondPicker = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

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
        boolean shouldRecord = ((MyApplication) this.getApplication()).getShouldRecord();
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
        int minuteValue = ((MyApplication) this.getApplication()).getMinuteTime()/60000;
        minutePicker.setValue(minuteValue);
        int secondValue = ((MyApplication) this.getApplication()).getSecondTime()/1000;
        secondPicker.setValue(secondValue);
    }

    public void easyModeClicked(View view) {
        Toast.makeText(getApplicationContext(), "So,... I see you chose the easy eh!",
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressed(){
        // Save the settings options set by user
        int selectedId = radioOption.getCheckedRadioButtonId();
        radioOptionButton = (RadioButton)findViewById(selectedId);
        if (radioOptionButton.getId() == R.id.radioRecord){
            ((MyApplication) this.getApplication()).setShouldRecord(true);
        } else {
            ((MyApplication) this.getApplication()).setShouldRecord(false);
        }

//        int delayTime = secondPicker.getValue()*1000 + minutePicker.getValue()*60000;
        int minuteTime = minutePicker.getValue() * 60000;
        int secondTime = secondPicker.getValue() * 1000;
        ((MyApplication) this.getApplication()).setMinuteTime(minuteTime);
        ((MyApplication) this.getApplication()).setSecondTime(secondTime);

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
