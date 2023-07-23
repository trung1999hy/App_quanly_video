package com.example.valut.ui.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.valut.ui.local.dao.AppMediaVaultDao
import com.example.valut.ui.model.MediaVault
import java.util.concurrent.Executors

@Database(
    entities = [MediaVault::class],
    version = 1
)
abstract class DatabaseApp : RoomDatabase() {
    val databaseWriteExecutor = Executors.newFixedThreadPool(2)
    abstract fun appMediaVaultDao(): AppMediaVaultDao?
    companion object {
        private var instance: DatabaseApp? = null
        fun getInstance(context: Context): DatabaseApp {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context, DatabaseApp::class.java, "database-name"
                ).allowMainThreadQueries().build()
            }
            return instance!!
        }
    }
}