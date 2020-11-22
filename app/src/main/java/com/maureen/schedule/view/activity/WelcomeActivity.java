package com.maureen.schedule.view.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.maureen.schedule.R;

public class WelcomeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        SharedPreferences isFirst = getSharedPreferences("isFirstFlag", MODE_PRIVATE);
        boolean isFirstFlag = isFirst.getBoolean("isFirstFlag", true);
        SharedPreferences.Editor editor = isFirst.edit();
        //判断是否第一次启动程序
        if (isFirstFlag) {
            editor.putBoolean("isFirstFlag", false);
            editor.apply();
        }
        Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
