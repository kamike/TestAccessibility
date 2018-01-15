package com.example.wangtao.testaccessibility;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;

import com.blankj.utilcode.util.ScreenUtils;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        com.blankj.utilcode.util.Utils.init(getApplication());
        ScreenUtils.setFullScreen(this);
        setContentView(R.layout.activity_main);
        LogUtils.i("MainActivity=====================");

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getX();

        LogUtils.i("===========" + x + "," + y);
        return super.onTouchEvent(event);
    }
}
