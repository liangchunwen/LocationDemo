package com.location.demo;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class LocationService extends Service {
    private static final String TAG = "BDMService";
    private LocationController locationController;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtil.d(TAG, "-onCreate()-");
        //启动前台服务
        startForeground(Constant.NOTIFICATION.ID, NotificationUtil.getInstance().createNotification(getString(R.string.app_name)));
        locationController = new LocationController(this);
        locationController.startLocation();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtil.d(TAG, "-onStartCommand()-");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtil.d(TAG, "-onDestroy()-");
        locationController.stopLocation();
        NotificationUtil.getInstance().clearBuilder();
    }
}
