package com.Example.room;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Mat on 3/28/2015.
 */
public class ReportActivity extends Activity {

    public static final String MyPREFS = "MyPref";
    public static final String myWords = "Words";

    SharedPreferences sharedPreferences;

    private HashMap<String, Integer> crutchWordCount = new HashMap<String, Integer>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //Remove notification bar
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //set content view AFTER ABOVE sequence (to avoid crash)
        this.setContentView(R.layout.activity_report);
        sharedPreferences = getSharedPreferences(MyPREFS, Context.MODE_PRIVATE);
        setWordList();
    }

    public void segue(View view) {
        Intent intent = new Intent(ReportActivity.this, Settings.class);
        startActivity(intent);
    }

    public void setWordList() {
        crutchWordCount = loadMap();
        Toast.makeText(getApplicationContext(), "YOUR CRUTCH WORD COUNT: " + crutchWordCount,
                Toast.LENGTH_LONG).show();

        String buildStr = null;
//        // Copy keySet into ArrayList.
        Object[] a = crutchWordCount.entrySet().toArray();
        Arrays.sort(a, new Comparator() {
            public int compare(Object o1, Object o2) {
                return ((HashMap.Entry<String, Integer>) o2).getValue().compareTo(
                        ((HashMap.Entry<String, Integer>) o1).getValue());
            }
        });
        for (Object e : a) {
            buildStr += ((HashMap.Entry<String, Integer>) e).getKey() + "\t\t\t\t\t\t"
                    + ((HashMap.Entry<String, Integer>) e).getValue() + "\n";
        }

        buildStr = buildStr.substring(4);
        ((TextView) findViewById(R.id.crutchWordView)).setText(buildStr);
    }

    private HashMap<String,Integer> loadMap(){
        HashMap<String,Integer> outputMap = new HashMap<String,Integer>();
        try{
            if (sharedPreferences != null){
                String jsonString = sharedPreferences.getString(myWords, (new JSONObject()).toString());
                JSONObject jsonObject = new JSONObject(jsonString);
                Iterator<String> keysItr = jsonObject.keys();
                while(keysItr.hasNext()) {
                    String key = keysItr.next();
                    Integer value = (Integer) jsonObject.get(key);
                    outputMap.put(key, value);
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return outputMap;
    }
}
//
//class EntryComparator implements Comparator<Map.Entry<String, Integer>> {
//
//    public int compare(Map.Entry<String, Integer> arg0, Map.Entry<String, Integer> arg1) {
//        // Compare the values.
//        return arg0.getValue().compareTo(arg1.getValue());
//    }
//}
