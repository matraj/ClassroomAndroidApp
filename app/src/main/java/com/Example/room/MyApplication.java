package com.Example.room;

import android.app.Application;

import java.util.HashMap;

/**
 * Created by knowroaming on 15-03-17.
 */
public class MyApplication extends Application {

    private boolean shouldRecord = true;
    public boolean getShouldRecord() {
        return shouldRecord;
    }
    public void setShouldRecord(boolean shouldRecord) {
        this.shouldRecord = shouldRecord;
    }

    private int minuteTime = 0;
    public int getMinuteTime() {
        return minuteTime;
    }
    public void setMinuteTime(int minuteTime) {
        this.minuteTime = minuteTime;
    }

    private int secondTime = 5000;
    public int getSecondTime() {
        return secondTime;
    }
    public void setSecondTime(int secondTime) {
        this.secondTime = secondTime;
    }

    private HashMap<String, Integer> crutchWordCount = new HashMap<String, Integer>();
    public HashMap getCrutchWordCount() { return  crutchWordCount; }
    public void setCrutchWordCount(HashMap<String, Integer> crutchWordCount) { this.crutchWordCount = crutchWordCount; }
}
