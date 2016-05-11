package com.youyou.shopping.manager;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.youyou.shopping.utils.MyLogger;
import com.youyou.shopping.utils.UserUtils;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

@EBean(scope = EBean.Scope.Singleton)
public class LocationManager implements AMapLocationListener {

    @RootContext
    Context context;

    @Bean
    UserUtils userUtils;

    private MyLogger log ;

    public interface LocationListener{
        void locationResult(AMapLocation aMapLocation);
    }

    private LocationListener mListener;
    private LocationManagerProxy mLocationManagerProxy;


    @AfterInject
    void afterInject(){
        if(mLocationManagerProxy==null){
            mLocationManagerProxy = LocationManagerProxy.getInstance(context);
        }
        log = MyLogger.getLogger("LocationManager");
    }

    public void setLocationListener(LocationListener mListener){
        this.mListener = mListener;
    }

    //获取当前地址信息
    public void getCurrentLocation(){
        if(mLocationManagerProxy==null){
            mLocationManagerProxy = LocationManagerProxy.getInstance(context);
        }
        mLocationManagerProxy.requestLocationData(LocationProviderProxy.AMapNetwork, 60 * 1000, 15, this);
    }

    public void pauseLocation(){
        if(mLocationManagerProxy!=null){
            mLocationManagerProxy.removeUpdates(this);
        }
    }

    //停止获取当前地址
    public void stopLocation(){
        mListener = null;
        if(mLocationManagerProxy!=null){
            mLocationManagerProxy.removeUpdates(this);
            mLocationManagerProxy.destroy();
        }
    }


    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null&& aMapLocation.getAMapException().getErrorCode() == 0) {
            userUtils.saveLocations(aMapLocation.getCity(), aMapLocation.getLatitude(), aMapLocation.getLongitude());
            mListener.locationResult(aMapLocation);
        }else{
            mListener.locationResult(null);
            try{
                log.e("Location Error :" + aMapLocation.getAMapException().getErrorMessage());
            }catch (NullPointerException ex){
                ex.printStackTrace();
            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }




}
