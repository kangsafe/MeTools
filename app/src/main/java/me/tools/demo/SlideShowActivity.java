package me.tools.demo;

import android.app.Activity;
import android.os.Bundle;

import me.tools.banner.SlideShowView;

public class SlideShowActivity extends Activity {

    private SlideShowView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slideshow);
        view = (SlideShowView) findViewById(R.id.slideshowView);
    }
}