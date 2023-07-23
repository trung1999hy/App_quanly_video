package com.example.valut.ui.ultis

import android.Manifest

object Constant {

    const val INDEX_0 = 0
    const val INDEX_1 = 1

    const val INDEX_9 = 9
    const val REQUEST_CODE_PERMISSION = 2000
    const val TYPE = "type"

    const val KEY_PKG = "KEY_PKG"
    const val KEY_PATH = "KEY_PATH"
    const val DEVICE = "DEVICE"
    const val KEY_BACK_UP = "KEY_BACK_UP"

    const val DATE_FORMAT = "yyyyMMdd_HHmmss"

    @JvmField
    val PERMISSIONS_EXTERNAL_STORAGE = arrayOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )
    const val KEY_MEDIA_VAULT = "KEY_MEDIA_VAULT"
    const val KEY_5_COIN = "KEY_COIN_5" // 5 coin
    const val KEY_15_COIN = "key_in_app_15" // 100 coin

    const val KEY_30_COIN = "key_in_app_30_coin" // 150 coin

    const val KEY_100_COIN = "key_in_app_100_coin" // 300 coin

    const val KEY_200_COIN = "key_in_app_200_coin" // 500 coin

    const val KEY_300_COIN = "key_in_app_300_coin" // 700 coin

    const val KEY_500_COIN = "key_in_app_500_coin" // 999 coin


}