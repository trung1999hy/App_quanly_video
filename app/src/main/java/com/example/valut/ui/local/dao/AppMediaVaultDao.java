package com.example.valut.ui.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.valut.ui.model.MediaVault;

import java.util.List;

@Dao
public interface AppMediaVaultDao {
    @Query("SELECT * FROM media_vault")
    LiveData<List<MediaVault>> getAllMedia();

    @Insert
    void addItem(MediaVault item);

    @Insert
    void addListItem(List<MediaVault> items);

    @Query("SELECT * FROM media_vault where type=:type")
    LiveData<List<MediaVault>> getDataType(int type);

    @Update
    void updateItem(MediaVault item);

    @Delete
    void deleteList(List<MediaVault> items);

    @Delete
    void delete(MediaVault mediaVault);

    @Query("DELETE FROM media_vault")
    void deleteAll();

    @Query("SELECT * FROM media_vault")
    List<MediaVault> getAllMediaBackUp();
}
