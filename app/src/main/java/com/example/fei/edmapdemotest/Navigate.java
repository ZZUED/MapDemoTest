package com.example.fei.edmapdemotest;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

import java.util.ArrayList;

/**
 * Created by FEI on 2018/4/14.
 * 导航工具类
 */

public class Navigate {

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
}
