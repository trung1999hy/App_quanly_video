package com.example.valut.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import com.example.valut.R;

public abstract class PermissionActivity extends AppCompatActivity {

    public boolean checkPermissions(String[] permissions) {
        try {

            for (String permission : permissions) {
                if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }

            }
            return true;
        } catch (Throwable throwable) {
            return true;
        }

    }

    public void requestPermission(String[] permission, int code) {
        if (checkShouldShowRequestPermissionRationale(permission)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(permission, code);
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(permission, code);
            }
        }
    }


    private boolean checkShouldShowRequestPermissionRationale(String[] permissions) {
        try {
            for (String permission : permissions) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (shouldShowRequestPermissionRationale(permission)) {
                        return true;
                    }
                }
            }
            return false;
        } catch (Throwable e) {
            return false;
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           int code, int title, int content,
                                           CallBackPermission callBackPermission) {
        if (requestCode == code) {
            if (checkPermissions(permissions)) {
                callBackPermission.onAllPermissionAllow();
            } else if (!checkShouldShowRequestPermissionRationale(permissions)) {
                new AlertDialog.Builder(this)
                        .setTitle(title)
                        .setMessage(content)
                        .setPositiveButton(R.string.title_setting, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                callBackPermission.onPermissionNotAllow();
                            }
                        })
                        .show();

            }
        }

    }

    public void checkPermissionFileAll(CallBackPermission callBackPermission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (Environment.isExternalStorageManager()) {
                callBackPermission.onAllPermissionAllow();
            } else {
                new AlertDialog.Builder(this)
                        .setTitle(R.string.title_permission_manage_all_files)
                        .setMessage(R.string.title_requets_permiss_manage_all_files)
                        .setPositiveButton(R.string.title_ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Yêu cầu quyền
                                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                                Uri uri = Uri.fromParts("package", getPackageName(), null);
                                intent.setData(uri);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton(R.string.title_cancel, null)
                        .show();
            }
        } else {
            callBackPermission.onPermissionNotAllow();
        }
    }
}

