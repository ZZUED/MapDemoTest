package com.example.fei.edmapdemotest;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.util.Log;

import java.net.URISyntaxException;
import java.util.ArrayList;

/**
 * Created by FEI on 2018/4/14.
 * 导航工具类
 */

public class Navigate {

    //--------------以下都是高德坐标系的坐标-------------------//
    private static final double LATITUDE_A = 34.8171930000;  //起点纬度
    private static final double LONGTITUDE_A = 113.5372840000;  //起点经度

    private static final double LATITUDE_B = 34.8171930000;  //终点纬度
    private static final double LONGTITUDE_B = 113.5372840000;  //终点经度



    //----------------以下都是百度坐标系的坐标------------------//
    private static final double LATITUDE_QIDIAN = 34.8234278343;  //起点纬度
    private static final double LONGTITUDE_QIDIAN = 113.5437323900;  //起点经度

    private static final double LATITUDE_ZHONGDIAN = 34.8234278343;  //终点纬度
    private static final double LONGTITUDE_ZHONGDIAN = 113.5437323900;  //终点经度

    public static void startNavigate(){
        setMapAppListAndListener();

    }

    /**
     * 启动地图APP选择对话框
     * @param context 上下文
     */
    private static void startDialog(Context context){
        new AlertDialog.Builder(context)
                .setTitle("请选择要使用的APP")
                .show();
    }

    private static CharSequence[] setMapAppListAndListener(){
        //TODO 设置已安装地图软件列表和监听器
        return new CharSequence[2];
    }

    /**
     * 我的位置BY高德
     */
    void setUpGaodeAppByMine(Context context){
        try {
            Intent intent = Intent.getIntent("androidamap://route?sourceApplication=softname&sname=我的位置&dlat="+LATITUDE_B+"&dlon="+LONGTITUDE_B+"&dname="+"钟楼广场"+"&dev=0&m=0&t=1");
            context.startActivity(intent);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    /**
     * 我的位置到终点通过百度地图
     */
    void setUpBaiduAPPByMine(Context context){
        try {
            Intent intent = Intent.getIntent("intent://map/direction?origin=我的位置&destination=钟楼广场&mode=driving&src=yourCompanyName|yourAppName#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end");
            context.startActivity(intent);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
