package com.yishenghuo.schedule.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.yishenghuo.schedule.R;

public class WelcomeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);
        SharedPreferences isFirst = getSharedPreferences("isFirstFlag", MODE_PRIVATE);
        boolean isFirstFlag = isFirst.getBoolean("isFirstFlag", true);
        SharedPreferences.Editor editor = isFirst.edit();
        //判断是否第一次启动程序
        if (isFirstFlag) {
            Log.d("测试", "第一次运行");
            editor.putBoolean("isFirstFlag",false);
            editor.commit();
            Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            Log.d("测试", "不是第一次运行");
            Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }


    }
}
