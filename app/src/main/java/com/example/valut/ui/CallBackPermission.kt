package com.example.valut.ui

interface CallBackPermission {
    fun onAllPermissionAllow() {}
    fun onPermissionNotAllow() {}
    fun onOpenSetting() {}
}