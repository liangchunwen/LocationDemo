package com.location.demo;

import android.util.Log;

public class LogUtil {
    private static final String TAG = "LocationApp:";
    private static boolean debug = true;

    public static void setDebug(boolean debug) {
        LogUtil.debug = debug;
    }

    public static boolean isDebug() {
        return debug;
    }

    public static void d(String tag, String log) {
        if (isDebug()) Log.d(TAG + tag, log);
    }
    public static void e(String tag, String log) {
        if (isDebug()) Log.d(TAG + tag, log);
    }
}
