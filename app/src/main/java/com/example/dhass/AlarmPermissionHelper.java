package com.example.dhass;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

public class AlarmPermissionHelper {
    private static final int ALARM_PERMISSION_REQUEST_CODE = 1;

    public static boolean isAlarmPermissionGranted(Activity act) {
        Activity activity = act;
        if (activity != null) {
            int permissionStatus = ContextCompat.checkSelfPermission(activity, Manifest.permission.SCHEDULE_EXACT_ALARM);
            return permissionStatus == PackageManager.PERMISSION_GRANTED;
        }
        return false;
    }

    public static void requestAlarmPermission(Activity act) {
        Activity activity = act;
        if (activity != null) {
            ActivityCompat.requestPermissions(
                    activity,
                    new String[]{Manifest.permission.SCHEDULE_EXACT_ALARM},
                    ALARM_PERMISSION_REQUEST_CODE
            );
        }
    }

    public static void onRequestPermissionsResult(Fragment fragment, int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == ALARM_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission has been granted.
                // You can proceed with setting alarms or other actions that require this permission.
            } else {
                // Permission request was denied.
                // You can handle this case, for example, by showing a message to the user.
            }
        }
    }
}

