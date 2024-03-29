package com.example.inventorymangement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.multidex.MultiDex;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MultiDex.install(this);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
            }
        },7000);
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }
}