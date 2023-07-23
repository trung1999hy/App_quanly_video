package com.example.valut

import android.app.Application
import com.example.valut.ui.local.Preferences

class MainApp : Application() {
    var preference: Preferences? = null
    override fun onCreate() {
        super.onCreate()
        instance = this
        preference = Preferences.getInstance(this)
        if (preference?.firstInstall == false) {
            preference?.firstInstall = true
            preference?.setValueCoin(10)
        }

    }

    companion object {
        private var instance: MainApp? = null

        @JvmStatic
        fun newInstance(): MainApp? {
            if (instance == null) {
                instance = MainApp()
            }
            return instance
        }
    }
}