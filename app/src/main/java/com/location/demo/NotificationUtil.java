package com.location.demo;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

public class NotificationUtil {
    public static NotificationManager notificationManager;
    public NotificationCompat.Builder builder;
    public static NotificationUtil instance;

    public static NotificationUtil getInstance() {
        if (instance == null) {
            instance = new NotificationUtil();
        }

        return instance;
    }

    public NotificationUtil() {
        notificationManager = (NotificationManager) LocationApp.getInstance().getSystemService(Context.NOTIFICATION_SERVICE);
    }

    private String getContentText() {

        return "";
    }

    public void updateNotificationInfo() {
        if (notificationManager != null) {
            notificationManager.notify(Constant.NOTIFICATION.ID, createNotification(getContentText()));
        }
    }

    public Notification createNotification(String contentText) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(Constant.NOTIFICATION.CHANNEL_ID, Constant.NOTIFICATION.CHANNEL_NAME, NotificationManager.IMPORTANCE_LOW);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
            builder = new NotificationCompat.Builder(LocationApp.getInstance(), Constant.NOTIFICATION.CHANNEL_ID);
            builder.setSmallIcon(R.drawable.ic_launcher_background)  // the status icon
                    .setWhen(System.currentTimeMillis())  // the time stamp
                    .setContentText(contentText)  // the contents of the entry
                    .setAutoCancel(true);

            Intent mIntent = new Intent(LocationApp.getInstance(), MainActivity.class);
            mIntent.setAction(Intent.ACTION_MAIN);
            mIntent.addCategory(Intent.CATEGORY_LAUNCHER);
            PendingIntent pendingIntent;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                pendingIntent = PendingIntent.getActivity(LocationApp.getInstance(), 0, mIntent, PendingIntent.FLAG_MUTABLE);
            } else {
                pendingIntent = PendingIntent.getActivity(LocationApp.getInstance(), 0, mIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            }
            builder.setContentIntent(pendingIntent);

        } else {
            builder = new NotificationCompat.Builder(LocationApp.getInstance(), Constant.NOTIFICATION.CHANNEL_ID);
            builder.setSmallIcon(R.drawable.ic_launcher_background)  // the status icon
                    .setWhen(System.currentTimeMillis())  // the time stamp
                    .setContentText(contentText)  // the contents of the entry
                    .setAutoCancel(true);

            Intent mIntent = new Intent(LocationApp.getInstance(), MainActivity.class);
            mIntent.setAction(Intent.ACTION_MAIN);
            mIntent.addCategory(Intent.CATEGORY_LAUNCHER);
            PendingIntent pendingIntent = PendingIntent.getActivity(LocationApp.getInstance(), 0, mIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(pendingIntent);
        }

        return builder.build();
    }

    public void clearBuilder() {
        if (builder != null) {
            builder.clearActions();
            builder = null;
        }
    }
}
