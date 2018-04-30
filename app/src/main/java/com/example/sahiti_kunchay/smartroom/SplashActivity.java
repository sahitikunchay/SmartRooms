package com.example.sahiti_kunchay.smartroom;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ImageView im5 = (ImageView) findViewById(R.id.imageView5);

        im5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mn = new Intent(SplashActivity.this, MeetingActivity.class);
                startActivity(mn);
            }
        });

        Button b5 = (Button) findViewById(R.id.button);
        b5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent k = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(k);
            }
        });
    }
}
