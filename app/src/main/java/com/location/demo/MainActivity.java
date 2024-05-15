package com.location.demo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.TextView;

import com.location.demo.permission.Permission;
import com.location.demo.permission.PermissionDialog;

public class MainActivity extends AppCompatActivity implements LocationController.MyNmeaMessage.OnMyNmeaMessageListener {
    private static final String TAG = "Location_TAG";
    private static final int REQUEST_CODE = 5;
    private LocationController.MyNmeaMessage myNmeaMessage;
    private static String locationType;
    private TextView lngView, latView, typeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        //监听NmeaMessage回调
        myNmeaMessage = new LocationController.MyNmeaMessage();
        myNmeaMessage.setOnMyNmeaMessageListener(this);

        //申请APP正常运行必须的权限
        if (!Permission.isPermissionGranted(this)) {
            Permission.checkPermission(this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            for (int grantResult : grantResults) {
                if (grantResult != PackageManager.PERMISSION_GRANTED) {
                    //showPermissionDenyDialog();
                }
            }
        }
    }

    private void showPermissionDenyDialog() {
        PermissionDialog dialog = new PermissionDialog();
        dialog.show(getSupportFragmentManager(), "PermissionDeny");
    }

    private void initView() {
        lngView = findViewById(R.id.lng);
        latView = findViewById(R.id.lat);
        typeView = findViewById(R.id.type);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (myNmeaMessage != null) {
            myNmeaMessage.setOnMyNmeaMessageListener(null);
        }
    }

    private String GetnSolutionState(int nType) {
        String strSolutionState = "";
        switch (nType) {
            case 0:
                strSolutionState = "无效解";
                break;
            case 1:
                strSolutionState = "单点解";
                break;
            case 2:
                strSolutionState = "差分解";
                break;
            case 4:
                strSolutionState = "固定解";
                break;
            case 5:
                strSolutionState = "浮点解";
                break;
            default:
                strSolutionState = "" + nType;
                break;
        }
        return strSolutionState;
    }

    @Override
    public void onMyNmeaMessage(String message, long timestamp) {
        LogUtil.d(TAG, " OnNmeaMessageListener message：" + message + ", timestamp: " + timestamp);
        if (message.contains("GPGGA") || message.contains("GNGGA")) {
            LogUtil.d(TAG, " OnNmeaMessageListener GPGGA：" + message);
            String[] result = message.split(",");
            if (result.length >= 11) {
                LogUtil.d(TAG, "locationType：" + locationType);
                LogUtil.d(TAG, "lng-result[4]：" + result[4]);
                LogUtil.d(TAG, "lat-result[2]：" + result[2]);
                locationType = GetnSolutionState(Integer.parseInt(result[6]));
                runOnUiThread(() -> {
                    if (lngView != null) {
                        lngView.setText(String.format(getString(R.string.location_lng), result[4]));
                    }
                    if (latView != null) {
                        latView.setText(String.format(getString(R.string.location_lng), result[2]));
                    }
                    if (typeView != null) {
                        typeView.setText(String.format(getString(R.string.location_type), locationType));
                    }
                });
            }
        }
    }
}