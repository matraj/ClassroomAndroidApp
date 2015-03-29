package com.Example.room;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;

/**
 * Created by Mat on 3/28/2015.
 */
public class RecordActivity extends Activity {

    private Button playButton, pauseButton;
    private Boolean isPaused = false;

    private MediaPlayer m;
    private String outputFile = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //Remove notification bar
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //set content view AFTER ABOVE sequence (to avoid crash)
        this.setContentView(R.layout.activity_record);

        playButton = (Button)findViewById(R.id.playButton);
        pauseButton = (Button)findViewById(R.id.pauseButton);

        m = new MediaPlayer();

        // Set up recording
        outputFile = Environment.getExternalStorageDirectory().
                getAbsolutePath() + "/myrecording.3gp";
    }

    public void segue(View view) {
        Intent intent = new Intent(RecordActivity.this, Settings.class);
        startActivity(intent);
    }

    public void play(View view) throws IllegalArgumentException,
            SecurityException, IllegalStateException, IOException {
        pauseButton.setEnabled(true);
        playButton.setEnabled(false);
        if (!isPaused) {
            m.setDataSource(outputFile);
            m.prepare();
            m.start();
            Toast.makeText(getApplicationContext(), "Start playing audio", Toast.LENGTH_LONG).show();
        } else {
            m.start();
            Toast.makeText(getApplicationContext(), "Re playing audio", Toast.LENGTH_LONG).show();
        }
    }

    public void pause(View view) {
        pauseButton.setEnabled(false);
        playButton.setEnabled(true);
        m.pause();
        isPaused = true;
    }
}
