package com.location.demo;

public class Constant {
    private static float lng = 0;
    private static float lat = 0;

    public static class NOTIFICATION {
        public static final int ID = 666;
        public static final String CHANNEL_ID = "com.bd.message";
        public static final String CHANNEL_NAME = "BDMService";
    }

    public static void setLat(float lat) {
        Constant.lat = lat;
    }

    public static float getLat() {
        return lat;
    }

    public static void setLng(float lng) {
        Constant.lng = lng;
    }

    public static float getLng() {
        return lng;
    }
}
