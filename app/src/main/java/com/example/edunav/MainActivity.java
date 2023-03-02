package com.example.edunav;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //SPLASH CODES
        Intent splash = new Intent(MainActivity.this, MainActivity2.class);

        new Handler().postDelayed(() -> startActivity(splash), 4000);





    }
}