package com.example.a97557.movie;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by 97557 on 2017/5/16.
 */

public class LogoActivity extends Activity {

    Timer timer = new Timer();
    TimerTask task = new TimerTask() {
        @Override
        public void run() {
            Intent in = new Intent(LogoActivity.this, MainActivity.class);
            startActivity(in);
            finish();
        }
    };
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logo);
        timer.schedule(task,2000);
    }
}
