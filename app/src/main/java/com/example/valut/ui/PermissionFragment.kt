package com.example.valut.ui

import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.valut.R

abstract class PermissionFragment() : Fragment() {

    override fun onResume() {
        super.onResume()
    }

    fun checkPermissions(permissions: Array<String>): Boolean {
        return try {
            for (permission in permissions) {
                if (
                    ContextCompat.checkSelfPermission(
                        requireActivity(),
                        permission
                    )
                    != PackageManager.PERMISSION_GRANTED
                ) {
                    return false
                }
            }
            true
        } catch (throwable: Throwable) {
            true
        }
    }

    fun requestPermission(permission: Array<String>, code: Int) {
        if (checkShouldShowRequestPermissionRationale(permission)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(permission, code)
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(permission, code)
            }
        }
    }

    private fun checkShouldShowRequestPermissionRationale(permissions: Array<String>): Boolean {
        return try {
            for (permission in permissions) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (shouldShowRequestPermissionRationale(permission!!)) {
                        return true
                    }
                }
            }
            false
        } catch (e: Throwable) {
            false
        }
    }

    fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        code: Int,
        title: Int,
        content: Int,
        callBackPermission: CallBackPermission
    ) {
        if (requestCode == code) {
            if (checkPermissions(permissions)) {
                callBackPermission.onAllPermissionAllow()
            } else if (!checkShouldShowRequestPermissionRationale(permissions)) {
                AlertDialog.Builder(requireActivity())
                    .setTitle(title)
                    .setMessage(content)
                    .setPositiveButton(
                        R.string.title_setting,
                        object : DialogInterface.OnClickListener {
                            override fun onClick(dialog: DialogInterface, which: Int) {
                                callBackPermission.onPermissionNotAllow()
                            }
                        }).setNegativeButton(R.string.title_cancel, null)
                    .show()
            }
        }
    }

    fun checkPermissionFileAll(callBackPermission: CallBackPermission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (Environment.isExternalStorageManager()) {
                callBackPermission.onAllPermissionAllow()
            } else {
                AlertDialog.Builder(requireActivity())
                    .setTitle(R.string.title_permission_manage_all_files)
                    .setMessage(R.string.title_requets_permiss_manage_all_files)
                    .setPositiveButton(R.string.title_ok, object : DialogInterface.OnClickListener {
                        override fun onClick(dialog: DialogInterface, which: Int) {
                            val intent =
                                Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                            val uri = Uri.fromParts("package", activity!!.packageName, null)
                            intent.setData(uri)
                            startActivity(intent)
                        }
                    })
                    .setNegativeButton(R.string.title_cancel, null)
                    .show()
            }
        } else {
            callBackPermission.onPermissionNotAllow()
        }
    }
}