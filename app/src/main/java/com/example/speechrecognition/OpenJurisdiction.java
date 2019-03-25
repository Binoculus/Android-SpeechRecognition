package com.example.speechrecognition;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;

public class OpenJurisdiction {
    //要申请的权限
    //读写权限
    private static String[] PERMISSIONS_STORAGE = {Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CAMERA};

    public void addPER(String string,Activity activity){
        String [] ret = new String[PERMISSIONS_STORAGE.length+1];
        System.arraycopy(PERMISSIONS_STORAGE, 0, ret, 0, PERMISSIONS_STORAGE.length);
        ret [PERMISSIONS_STORAGE.length] = string;
        PERMISSIONS_STORAGE = ret;
        open(activity);
    }
    void open(Activity obj){
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            for (String aPERMISSIONS_STORAGE : PERMISSIONS_STORAGE) {
                if (ActivityCompat.checkSelfPermission(obj,aPERMISSIONS_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    int REQUEST_PERMISSION_CODE = 2;
                    ActivityCompat.requestPermissions(obj, PERMISSIONS_STORAGE, REQUEST_PERMISSION_CODE);
                }
            }
        }
    }
}
