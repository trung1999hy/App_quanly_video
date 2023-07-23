package com.example.valut.ui.repository

import android.app.Application

import androidx.lifecycle.LiveData
import com.example.valut.ui.local.DatabaseApp
import com.example.valut.ui.local.dao.AppMediaVaultDao
import com.example.valut.ui.model.MediaVault

class AppRepository(application: Application) {
    private val mediaVaultDao: AppMediaVaultDao
    private val database: DatabaseApp
   init {
       database = DatabaseApp.getInstance(application.applicationContext)
       mediaVaultDao = database.appMediaVaultDao()!!
   }
    fun getDataType(type: Int): LiveData<List<MediaVault>> {
        return mediaVaultDao.getDataType(type)
    }

    val dataAllMedia: LiveData<List<MediaVault>>
        get() = mediaVaultDao.getAllMedia()

    fun addItemMediaVault(mediaVault: MediaVault?) = mediaVaultDao.addItem(mediaVault)


    fun removeListMediaVault(mediaVaults: List<MediaVault?>?) =  mediaVaultDao.deleteList(mediaVaults)

    fun removeMediaVault(mediaVaults: MediaVault?) =   mediaVaultDao.delete(mediaVaults)

    fun updateItemMediaVault(mediaVault: MediaVault?) = mediaVaultDao.updateItem(mediaVault)




}