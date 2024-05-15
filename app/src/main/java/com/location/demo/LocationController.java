package com.location.demo;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.GnssStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.OnNmeaMessageListener;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

/**
 * Created by liangcw on 2021/3/25 - 9:05
 */
public class LocationController {
    private final static String TAG = "Location_TAG";
    private final Context mContext;
    private final static int PERIOD_DISTANCE = 0;//0米
    private final static long PERIOD_TIME_TWO_SECONDS = 2 * 1000L;//2秒钟
    private final static long PERIOD_TIME_FIVE_MINUTES = 300 * 1000L;//5分钟
    private final LocationManager mLocationManager;
    private final MyGnssStatusCallback myGnssStatusCallback;
    private static final float mSnr = 0;

    public LocationController(Context context) {
        mContext = context;
        myGnssStatusCallback = new MyGnssStatusCallback();
        mLocationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    }

    @SuppressLint("MissingPermission")
    private void getLastKnownLocation() {
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);//高精度
        criteria.setAltitudeRequired(false);//无海拔要求
        // criteria.setBearingRequired(false);//无方位要求
        criteria.setCostAllowed(false);//允许产生资费
        // criteria.setPowerRequirement(Criteria.POWER_LOW);//低功耗
        // 获取最佳服务对象
        String provider = mLocationManager.getBestProvider(criteria, true);
        if (provider != null) {
            mLocationManager.getLastKnownLocation(provider);
        }
    }

    public synchronized void startLocation() {
        Log.d(TAG, "---startLocation()---");
        if (mLocationManager != null) {
            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }

            mLocationManager.registerGnssStatusCallback(myGnssStatusCallback);
            getLastKnownLocation();
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, PERIOD_TIME_TWO_SECONDS, PERIOD_DISTANCE, gpsLocationListener);
            mLocationManager.addNmeaListener(nmeaMessageListener);
        }
    }

    public synchronized void stopLocation() {
        Log.d(TAG, "---stopLocation()---");
        if (mLocationManager != null) {
            if (myGnssStatusCallback != null) {
                mLocationManager.unregisterGnssStatusCallback(myGnssStatusCallback);
            }
            mLocationManager.removeUpdates(gpsLocationListener);
        }
    }

    private static class MyGnssStatusCallback extends GnssStatus.Callback {
        @Override
        public void onStarted() {
            super.onStarted();
        }

        @Override
        public void onStopped() {
            super.onStopped();
        }

        @Override
        public void onFirstFix(int ttffMillis) {
            super.onFirstFix(ttffMillis);
        }

        /**
         *                 int type = status.getConstellationType(i);// 卫星类型
         *                 switch (type) {
         *                     case GnssStatus.CONSTELLATION_BEIDOU:
         *                         Log.d(TAG, "BEIDOU");
         *                         break;
         *                     case GnssStatus.CONSTELLATION_GPS:
         *                         Log.d(TAG, "GPS");
         *                         break;
         *                     case GnssStatus.CONSTELLATION_GLONASS:
         *                         Log.d(TAG, "GLONASS");
         *                         break;
         *                     case GnssStatus.CONSTELLATION_GALILEO:
         *                         Log.d(TAG, "GALILEO");
         *                         break;
         *                     case GnssStatus.CONSTELLATION_IRNSS:
         *                         Log.d(TAG, "IRNSS");
         *                         break;
         *                     case GnssStatus.CONSTELLATION_QZSS:
         *                         Log.d(TAG, "QZSS");
         *                         break;
         *                     case GnssStatus.CONSTELLATION_SBAS:
         *                         Log.d(TAG, "SBAS");
         *                         break;
         *                     case GnssStatus.CONSTELLATION_UNKNOWN:
         *                         Log.d(TAG, "UNKNOWN");
         *                         break;
         *                 }
         * @param status the current status of all satellites.
         */
        @Override
        public void onSatelliteStatusChanged(@NonNull GnssStatus status) {
            super.onSatelliteStatusChanged(status);
            int count = status.getSatelliteCount(); // 卫星数量
            //Log.d(TAG, "count: " + count);
        }
    }

    /**
     *
     *             //根据坐标获取地理位置
     *             Geocoder geocoder = new Geocoder(mContext);
     *             List<Address> list = null;
     *             try {
     *                 list = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
     *             } catch (IOException e) {
     *                 e.printStackTrace();
     *             }
     *             if (null != list) {
     *                 for (Address address : list) {
     *                     Log.d(TAG, "address: " + address.toString());
     *                 }
     *             }
     *
     */
    private final LocationListener gpsLocationListener = new LocationListener() {

        //当坐标改变时触发此函数，如果Provider传进相同的坐标，它就不会被触发
        @Override
        public void onLocationChanged(Location location) {
            LogUtil.d(TAG, "gps-lat: " + location.getLatitude() + "   lng: " + location.getLongitude());
        }

        // Provider被enable时触发此函数，比如GPS被打开
        @Override
        public void onProviderDisabled(@NonNull String provider) {
        }

        // Provider被disable时触发此函数，比如GPS被关闭
        @Override
        public void onProviderEnabled(@NonNull String provider) {
        }

        // Provider的状态在可用、暂时不可用和无服务三个状态直接切换时触发此函数
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    };

    public static class MyNmeaMessage {
        public static OnMyNmeaMessageListener onMyNmeaMessageListener;

        public interface OnMyNmeaMessageListener {
            void onMyNmeaMessage(String message, long timestamp);
        }

        public void setOnMyNmeaMessageListener(OnMyNmeaMessageListener onMyNmeaMessageListener) {
            MyNmeaMessage.onMyNmeaMessageListener = onMyNmeaMessageListener;
        }
    }

    OnNmeaMessageListener nmeaMessageListener = (s, l) -> {
        if (MyNmeaMessage.onMyNmeaMessageListener != null) {
            MyNmeaMessage.onMyNmeaMessageListener.onMyNmeaMessage(s, l);
        }
    };
}
