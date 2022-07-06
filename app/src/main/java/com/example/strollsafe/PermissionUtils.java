package com.example.strollsafe;

import android.content.Context;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;

public class PermissionUtils {

    /**
     * Checks the permission to see if it is granted or denied
     * */
    public static boolean hasPermissions(Context context, String... PERMISSIONS) {
        boolean isGranted = true;
        if (context != null && PERMISSIONS != null) {
            for (String permission : PERMISSIONS) {
                if (ActivityCompat.checkSelfPermission(context, permission) !=
                        PackageManager.PERMISSION_GRANTED) {
                    isGranted = false;
                }
            }
        }
        return isGranted;
    }



}
