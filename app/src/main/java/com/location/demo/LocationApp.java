package com.location.demo;

import android.app.Application;
import android.content.Intent;
import android.os.Build;

public class LocationApp extends Application {
    private static Application instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        startLocationService();
    }

    private void startLocationService() {
        Intent sevInt = new Intent(this, LocationService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(sevInt);
        } else {
            startService(sevInt);
        }
    }
    public static Application getInstance() {
        return instance;
    }
}
