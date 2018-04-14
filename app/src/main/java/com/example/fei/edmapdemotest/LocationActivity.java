package com.example.fei.edmapdemotest;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MyLocationStyle;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class LocationActivity extends AppCompatActivity implements AMapLocationListener, LocationSource{
    /**
     * mFirstLocate用来指示是否为第一次加载程序
     * true:则申请并且获得定位权限后执行{@link #initLoc()}方法
     * false:则申请并且获得定位权限后执行{@link #locateToMyPos()}方法
     */
    private boolean mFirstLocate;

    /**
     * 高德地图接口类
     */
    private MapView mMapView;
    private AMap aMap;
    private AMapLocation myAMapLocation = null;
    private AMapLocationClient mLocationClient = null;//定位发起端
    private LocationSource.OnLocationChangedListener mListener = null;//定位监听器

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        mMapView = findViewById(R.id.Location_map);
        mMapView.onCreate(savedInstanceState);

        aMap = mMapView.getMap();
        mFirstLocate = true;
        initLoc();

        //如果已经拥有权限，则直接定位
        if (requestLocationPermission()){
            initLoc();
        }

        initLocIcon();      //定义蓝色按钮

        findViewById(R.id.Location_locate_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (requestLocationPermission()){
                    locateToMyPos();
                }
            }
        });
    }

    /**
     * 初始化定位服务
     */
    private void initLoc() {
        mLocationClient = new AMapLocationClient(getApplicationContext());               //初始化定位
        mLocationClient.setLocationListener(this);                                      //设置定位回调监听
        AMapLocationClientOption mLocationOption = new AMapLocationClientOption();
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy); //设置高精度模式
        mLocationOption.setNeedAddress(true);                                           //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setOnceLocation(true);                                          //设置是否只定位一次,默认为false;
        mLocationOption.setMockEnable(false);                                           //设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.setInterval(2000);                                              //设置定位间隔,单位毫秒,默认为2000ms
        mLocationClient.setLocationOption(mLocationOption);                             //给定位客户端对象设置定位参数
        mLocationClient.startLocation();                                                //启动定位
    }

    /**
     * 初始化定位蓝点的样式以及定位类型
     */
    private void initLocIcon() {
        aMap.moveCamera(CameraUpdateFactory.zoomTo(19));  //设置缩放级别
        MyLocationStyle myLocationStyle;
        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER);//连续定位、蓝点不会移动到地图中心点，定位点依照设备方向旋转，并且蓝点会跟随设备移动。
        myLocationStyle.strokeColor(Color.argb(80, 0, 0, 205));//设置定位蓝点精度圆圈的边框颜色的方法。
        myLocationStyle.radiusFillColor(Color.argb(50, 0, 191, 255));//设置定位蓝点精度圆圈的填充颜色的方法。
        //myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.drawable.location64));//设置定位蓝点的icon图标方法，需要用到BitmapDescriptor类对象作为参数。
        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
        aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
    }

    /**
     * 定位到我的位置
     * 若我的位置获取失败，则定位到经纬度：(39.9032676346, 116.3977673938)
     */
    private void locateToMyPos(){
        aMap.moveCamera(CameraUpdateFactory.zoomTo(15)); //设置缩放级别
        aMap.moveCamera(CameraUpdateFactory.changeBearing(0));
        if (myAMapLocation != null && myAMapLocation.getErrorCode() == 0) {
            aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(myAMapLocation.getLatitude(), myAMapLocation.getLongitude())));
        } else {
            aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(39.9032676346, 116.3977673938)));
            if (myAMapLocation != null)
                Toast.makeText(LocationActivity.this, "" + myAMapLocation.getErrorInfo().split("信息:")[1], Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(LocationActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 申请定位权限
     * 若应用尚未获取定位权限则申请定位权限
     *
     * @return true:已经拥有获取位置的权限
     *          false:未拥有获取位置的权限
     */
    private boolean requestLocationPermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            return false;
        }
        return true;
    }

    /**
     * 申请权限后回调
     *
     * @param requestCode 请求码
     *                    值为 1: 表示申请定位权限，获得权限后调用定位方法 {@link #locateToMyPos()}
     * @param permissions 申请的权限列表
     * @param grantResults 申请权限的结果
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    if (mFirstLocate){
                        initLoc();
                    }
                    else{
                        locateToMyPos();
                    }
                }
                else {
                    Toast.makeText(this, "请主人给我权限", Toast.LENGTH_SHORT).show();
                }
                mFirstLocate = false;
                break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        myAMapLocation = aMapLocation;
        Toast.makeText(LocationActivity.this, "hhh我的经纬度是: " + aMapLocation.getLongitude() + " " +
                aMapLocation.getLatitude(), Toast.LENGTH_SHORT).show();
        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                //定位成功回调信息，设置相关消息
                aMapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见官方定位类型表
                aMapLocation.getLatitude();//获取纬度
                aMapLocation.getLongitude();//获取经度
                aMapLocation.getAccuracy();//获取精度信息
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
                Date date = new Date(aMapLocation.getTime());
                df.format(date);//定位时间
                aMapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
                aMapLocation.getCountry();//国家信息
                aMapLocation.getProvince();//省信息
                aMapLocation.getDistrict();//城区信息
                aMapLocation.getStreet();//街道信息
                aMapLocation.getStreetNum();//街道门牌号信息
                aMapLocation.getCityCode();//城市编码
                aMapLocation.getAdCode();//地区编码

                // 如果不设置标志位，此时再拖动地图时，它会不断将地图移动到当前的位置
                aMap.moveCamera(CameraUpdateFactory.zoomTo(17)); //设置缩放级别
                aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude())));//将地图移动到定位点
                mListener.onLocationChanged(aMapLocation);//点击定位按钮 能够将地图的中心移动到定位点
                //aMap.addMarker(getMarkerOptions(aMapLocation));//添加图钉
                String buffer = aMapLocation.getCountry() + "" + aMapLocation.getProvince() + "" + aMapLocation.getCity() + "" + aMapLocation.getProvince()
                        + "" + aMapLocation.getDistrict() + "" + aMapLocation.getStreet() + "" + aMapLocation.getStreetNum();//获取定位信息
                Toast.makeText(getApplicationContext(), buffer, Toast.LENGTH_LONG).show();
            } else {
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                Log.e("AmapError", "location Error, ErrCode:"
                        + aMapLocation.getErrorCode() + ", errInfo:"
                        + aMapLocation.getErrorInfo());

                Toast.makeText(getApplicationContext(), "定位失败", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
        if (mLocationClient == null) {
            Toast.makeText(getApplicationContext(), "mLocationClient == null", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void deactivate() {
        mListener = null;
        if (mLocationClient != null) {
            mLocationClient.stopLocation();
            mLocationClient.onDestroy();
        }
        mLocationClient = null;
    }

}
