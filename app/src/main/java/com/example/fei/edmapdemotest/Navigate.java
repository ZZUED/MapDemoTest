package com.example.fei.edmapdemotest;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.net.URISyntaxException;

/**
 * Created by FEI on 2018/4/14.
 * 导航工具类
 */

class Navigate {
    /**
     * cntOfInstalledMapApp: 若未安装百度地图和高德地图，则为 0
     *                      若安装其中之一，则为 1
     *                      都安装为 2
     */
    private static int cntOfInstalledMapApp;
    /**
     * 对话框的选项列表
     */
    private static CharSequence[] items;

    //--------------以下都是高德坐标系的坐标-------------------//
    private static final double LATITUDE_A = 34.8171930000;  //起点纬度
    private static final double LONGITUDE_A = 113.5372840000;  //起点经度

    private static final double LATITUDE_B = 34.8171930000;  //终点纬度
    private static final double LONGITUDE_B = 113.5372840000;  //终点经度



    //----------------以下都是百度坐标系的坐标------------------//
    private static final double LATITUDE_QIDIAN = 34.8234278343;  //起点纬度
    private static final double LONGITUDE_QIDIAN = 113.5437323900;  //起点经度

    private static final double LATITUDE_ZHONGDIAN = 34.8234278343;  //终点纬度
    private static final double LONGITUDE_ZHONGDIAN = 113.5437323900;  //终点经度

    /**
     * 启动导航功能
     *
     *
     * @param context 用于启动新活动的上下文
     */
    static void startNavigate(Context context){
        setMapAppList();
        if (cntOfInstalledMapApp > 0){
            startDialog(context);
        }
        else {
            Toast.makeText(context, "您尚未安装百度地图或高德地图", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 启动地图APP选择对话框
     * @param context 上下文
     */
    private static void startDialog(final Context context){
        new AlertDialog.Builder(context)
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                if (cntOfInstalledMapApp > 1) {
                                    setUpBaiduAPPByMine(context);
                                }
                                else{
                                    setUpGaodeAppByMine(context);
                                }
                                break;
                            case 1:
                                setUpGaodeAppByMine(context);
                                break;
                        }
                    }
                })
                .setTitle("请选择要使用的APP")
                .show();
    }

    /**
     * 判断是否已经安装百度地图和高德地图
     * 若安装，则按照先后顺序放入数组{@link #items}中并返回
     * 默认百度地图在前
     */
    private static void setMapAppList(){
        boolean gaodeInstalled, baiduInstalled;
        baiduInstalled = gaodeInstalled = false;
        cntOfInstalledMapApp = 0;

        if (isAppInstalled("com.baidu.BaiduMap")){
            baiduInstalled = true;
            cntOfInstalledMapApp++;
        }
        if (isAppInstalled("com.autonavi.minimap")){
            gaodeInstalled = true;
            cntOfInstalledMapApp++;
        }
        items = new String[cntOfInstalledMapApp];
        if (baiduInstalled) {
            items[0] = "百度地图";
        }
        if (gaodeInstalled) {
            items[cntOfInstalledMapApp - 1] = "高德地图";
        }
    }

    /**
     * 我的位置BY高德
     * @param context 用于启动另一个活动的上下文
     */
    private static void setUpGaodeAppByMine(Context context){
        try {
            Intent intent = Intent.parseUri("androidamap://route?sourceApplication=" +
                    "softname&sname=我的位置&dlat="+LATITUDE_B+"&dlon="+ LONGITUDE_B +
                    "&dname="+"钟楼广场"+"&dev=0&m=0&t=1", 0);
            context.startActivity(intent);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    /**
     * 我的位置到终点通过百度地图
     *
     * @param context 用于启动另一个活动的上下文
     */
    private static void setUpBaiduAPPByMine(Context context){
        try {
            Intent intent = Intent.parseUri("intent://map/direction?origin=我的位置&destination=" +
                    "钟楼广场&mode=driving&src=yourCompanyName|yourAppName#Intent;" +
                    "scheme=bdapp;package=com.baidu.BaiduMap;end", 0);
            context.startActivity(intent);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    /**
     * 判断是否安装目标应用
     * @param packageName 目标应用安装后的包名
     * @return 是否已安装目标应用
     */
    @SuppressLint("SdCardPath")
    private static boolean isAppInstalled(String packageName) {
        return new File("/data/data/" + packageName).exists();
    }
}
