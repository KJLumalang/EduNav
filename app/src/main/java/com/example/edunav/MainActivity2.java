package com.example.edunav;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;

public class MainActivity2 extends AppCompatActivity {

    ImageButton myImageButton;
    ImageButton myImageButton2;

    Button Button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide();//hide action bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.activity_main2);

        //school info
        myImageButton = (ImageButton)findViewById(R.id.imageButton2);
        myImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent school_info = new Intent(MainActivity2.this, school_info.class);
                startActivity(school_info);
            }
            });

        //about us
        myImageButton2 = (ImageButton)findViewById(R.id.imageButton);
        myImageButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent about_us = new Intent(MainActivity2.this, about_us.class);
                startActivity(about_us);
            }
        });

        //maps
        Button = (Button)findViewById(R.id.button);
        Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent maps = new Intent(MainActivity2.this, MapsActivity.class);
                startActivity(maps);
            }
        });




    }
}