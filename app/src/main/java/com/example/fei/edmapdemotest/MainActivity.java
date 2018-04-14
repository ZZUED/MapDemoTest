package com.example.fei.edmapdemotest;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.io.File;

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
                new AlertDialog.Builder(MainActivity.this)
                        .setItems(new CharSequence[]{"百度地图", "高德地图"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case 0:
                                //todo baidu
                                break;
                            case 1:
                                //todo gaode
                                break;
                        }
                    }
                })
                        .setTitle("选择要使用的软件")
                        .show();
            }
        });
    }

    /**
     * 判断是否安装目标应用
     *
     * @param packageName 目标应用安装后的包名
     * @return 是否已安装目标应用
     */
    private boolean isAppInstalled(String packageName) {
        return new File(getFilesDir().getPath() + packageName).exists();
    }

}
