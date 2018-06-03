package com.example.haidt.mylivewallpaper;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.felipecsl.gifimageview.library.GifImageView;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {
    GifImageView gifImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gifImageView=findViewById(R.id.main_app_bg);
        InputStream is = null;
        byte[] bytes=null;
        try {
            is = getResources().getAssets().open("app_bg.gif");
            bytes = new byte[is.available()];
            is.read(bytes);
            gifImageView.setBytes(bytes);
            gifImageView.startAnimation();
            gifImageView.setScaleY((float)1.8);
            is.read(bytes);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent homeIntent= new Intent(MainActivity.this,HomeActivity.class);
                startActivity(homeIntent);
                finish();
            }
        },10000);
    }
}
