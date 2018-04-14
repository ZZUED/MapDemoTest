package com.example.fei.edmapdemotest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //启动定位
        findViewById(R.id.Main_location).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            startActivity(new Intent(MainActivity.this, LocationActivity.class));
            }
        });


        //启动导航
        findViewById(R.id.Main_navigate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigate.startNavigate(MainActivity.this);
            }
        });
    }


}
