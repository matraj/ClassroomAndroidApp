package com.Example.room;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Window;
import android.widget.TabHost;

/**
 * Created by Mat on 3/28/2015.
 */
public class TabbarActivity extends TabActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //Remove notification bar
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //set content view AFTER ABOVE sequence (to avoid crash)
        this.setContentView(R.layout.activity_tabbar);

        Resources ressources = getResources();
        TabHost tabHost = getTabHost();

        // Record tab
        Intent intentAndroid = new Intent().setClass(this, RecordActivity.class);
        TabHost.TabSpec tabSpecRecord = tabHost
                .newTabSpec("Record")
                .setIndicator("", ressources.getDrawable(R.drawable.icon_record_config))
                .setContent(intentAndroid);

        // Start tab
        Intent intentApple = new Intent().setClass(this, MainActivity.class);
        TabHost.TabSpec tabSpecStart = tabHost
                .newTabSpec("Start")
                .setIndicator("", ressources.getDrawable(R.drawable.icon_start_config))
                .setContent(intentApple);

        // Report tab
        Intent intentWindows = new Intent().setClass(this, ReportActivity.class);
        TabHost.TabSpec tabSpecReport = tabHost
                .newTabSpec("Report")
                .setIndicator("", ressources.getDrawable(R.drawable.icon_report_config))
                .setContent(intentWindows);

        // add all tabs
        tabHost.addTab(tabSpecRecord);
        tabHost.addTab(tabSpecStart);
        tabHost.addTab(tabSpecReport);

        //set Windows tab as default (zero based)
        tabHost.setCurrentTab(1);
    }
}
