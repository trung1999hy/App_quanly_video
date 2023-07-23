package com.example.valut.ui

import android.app.Application
import android.database.Cursor
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.valut.ui.model.MediaVault
import com.example.valut.ui.repository.AppRepository
import com.example.valut.ui.ultis.Constant
import com.example.valut.ui.ultis.CursorUtils
import com.example.valut.ui.ultis.FileUtils
import com.example.valut.ui.ultis.TimeUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.util.Collections


class MediaVaultViewModel(application: Application) : AndroidViewModel(application) {
    private val appRepository: AppRepository = AppRepository(application)
    private val _listLiveDataSoundDevice = MutableLiveData<ArrayList<MediaVault>>()
    var listLiveDataSoundDevice: LiveData<ArrayList<MediaVault>> = _listLiveDataSoundDevice
    private val _files = MutableLiveData<ArrayList<MediaClassify>>()
    var files: LiveData<ArrayList<MediaClassify>> = _files


    fun getAllMedia(type: Int): LiveData<List<MediaVault>> {
        return if (type == TypeMedia.ALL) appRepository.dataAllMedia else appRepository.getDataType(
            type
        )
    }

    fun getData() {
        viewModelScope.launch(Dispatchers.IO) {
            val mItems = ArrayList<MediaVault>()
            var cursor: Cursor? = null
            try {
                val projection = arrayOf(
                    MediaStore.Audio.Media._ID,
                    MediaStore.Audio.Media.DATA,
                    MediaStore.Audio.Media.DISPLAY_NAME,
                    MediaStore.Audio.Media.DURATION,
                    MediaStore.Audio.Media.SIZE
                )
                val selection: String
                val selectionsArgs: Array<String>? = null
                selection = MediaStore.Audio.Media.DATA + " != 0"
                val sortOrder = MediaStore.Audio.Media.DATE_ADDED + " DESC"
                //
                val application = getApplication<Application>()
                cursor = application.contentResolver.query(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                    projection, selection, selectionsArgs, sortOrder
                )
                var move = CursorUtils.moveToFirst(cursor)
                while (move) {
                    try {
                        val id = CursorUtils.getLong(cursor!!, MediaStore.Audio.Media._ID)
                        val path = CursorUtils.getString(cursor, MediaStore.Audio.Media.DATA)
                        if (null != path && path.length > 0) {
                            val file = File(path)
                            if (file.exists()) {
                                var duration =
                                    CursorUtils.getInt(cursor, MediaStore.Audio.Media.DURATION)
                                val size = Math.max(
                                    CursorUtils.getLong(
                                        cursor, MediaStore.Audio.Media.SIZE
                                    ), file.length()
                                )
                                val single =
                                    CursorUtils.getString(cursor, MediaStore.Audio.Media.ARTIST)
                                //update duration and file size: in some cases it's incorrect
                                if (duration <= 0) {
                                    duration = TimeUtils.getVideoOrAudioFileDuration(path)
                                }
                                //add to list
                                mItems.add(
                                    MediaVault(
                                        CursorUtils.getString(
                                            cursor,
                                            MediaStore.Audio.Media.DISPLAY_NAME
                                        ),
                                        single,
                                        path,
                                        null,
                                        size,
                                        0, 0,
                                        duration.toLong(), TypeMedia.SOUND_TYPE
                                    )
                                )
                                System.err.println("nam: $mItems")
                            }
                        }
                    } catch (e: Throwable) {
                        Log.d("ERr", e.message.toString())
                    }
                    move = CursorUtils.moveToNext(cursor)
                }
                _listLiveDataSoundDevice.postValue(mItems)
                cursor!!.close()
            } catch (e: Throwable) {
                Log.d("ERr", e.message.toString())
            }
        }
    }


    fun changFile(path: String?, single: String?, type: Int) {
        viewModelScope.launch(Dispatchers.IO) { }
        val file = File(path)
        var duration: Long = 0
        if (file.exists()) {
            val fileName = file.name
            val size = file.length()
            val lastModified = file.lastModified()
            System.err.println("nam: lastModified$lastModified")
            if (type == TypeMedia.VIDEO_TYPE || type == TypeMedia.SOUND_TYPE) duration =
                TimeUtils.getVideoOrAudioFileDuration(path).toLong()
            val newPath = path?.let { FileUtils.changPathToVault(type, it) }
            val mediaVault = MediaVault(
                FileUtils.getFileNameWithoutExtension(fileName),
                single,
                path,
                newPath,
                size,
                lastModified,
                0,
                duration,
                type
            )
            appRepository.addItemMediaVault(mediaVault)
        }
    }


    fun getListFolder(type: Int, list: MutableList<MediaVault>): ArrayList<MediaClassify> {
        val mediaClassifies = ArrayList<MediaClassify>()
        val keyPaths = ArrayList<String>()
        val removeMediaVault = ArrayList<MediaVault>()
        if (!list.isEmpty()) {
            for (vault in list) {
                if (vault.newPath != null) {
                    if (File(vault.newPath).exists()) {
                        val file = File(vault.newPath).parentFile
                        val key = file.path
                        if (!keyPaths.contains(key)) {
                            keyPaths.add(key)
                            mediaClassifies.add(
                                MediaClassify(
                                    key,
                                    file.name,
                                    Constant.INDEX_1,
                                    0,
                                    vault.newPath
                                )
                            )
                        } else {
                            val idx = keyPaths.indexOf(key)
                            if (idx > -1) {
                                val item = mediaClassifies[idx]
                                item.number = item.number + 1
                            }
                        }
                    } else {
                        removeMediaVault.add(vault)
                    }
                } else removeMediaVault.add(vault)
            }
            list.removeAll(removeMediaVault)
            viewModelScope.launch(Dispatchers.IO) {
                appRepository.removeListMediaVault(removeMediaVault)
            }
            if (list.size > 0) {
                val mediaClassify = MediaClassify(
                    FileUtils.createFolderMediaVault(type),
                    "Tất cả",
                    list.size,
                    0,
                    list[0].newPath
                )
                mediaClassifies.add(Constant.INDEX_0, mediaClassify)
            }
        }
        return mediaClassifies
    }

    fun filterDataByPath(path: String, mediaVaults: List<MediaVault>): List<MediaVault> {
        val mediaVaultList: MutableList<MediaVault> = ArrayList()
        for (mediaVault in mediaVaults) {
            if (path == File(mediaVault.newPath).parent) {
                mediaVaultList.add(mediaVault)
            }
        }
        return mediaVaultList
    }

    fun sortByName(mediaVaults: List<MediaVault>): List<MediaVault> {
        Collections.sort(mediaVaults) { o1, o2 -> o2.name?.let { o1.name?.compareTo(it) ?: 0 }!! }
        return mediaVaults
    }

    fun sortByTime(mediaVaults: List<MediaVault>): List<MediaVault> {
        Collections.sort(mediaVaults) { o1, o2 ->
            java.lang.Long.compare(
                o1.timeModified,
                o2.timeModified
            )
        }
        return mediaVaults
    }

    fun removeListMediaVault(mediaVaults: ArrayList<MediaVault>) {
        viewModelScope.launch(Dispatchers.IO) { }
        for (mediaVault in mediaVaults) {
            val file = File(mediaVault.newPath)
            if (file.exists()) {
                if (file.delete()) appRepository.removeMediaVault(mediaVault)
            }
        }
    }


    fun removeMediaVault(mediaVault: MediaVault) {
        viewModelScope.launch(Dispatchers.IO) {
            val file = File(mediaVault.newPath)
            if (file.exists()) {
                if (file.delete()) appRepository.removeMediaVault(mediaVault)
            }
        }

    }

    fun restoreListVault(mediaVaults: ArrayList<MediaVault>) {
        viewModelScope.launch(Dispatchers.IO) {
            for (mediaVault in mediaVaults) {
                if (FileUtils.restoreFile(
                        mediaVault.pathCurrent,
                        mediaVault.newPath
                    )
                )
                    appRepository.removeMediaVault(mediaVault)

            }
        }
    }


    fun checkTimeOpenFile(mediaVaults: List<MediaVault>): ArrayList<MediaVault> {
        val mediaVaultArrayList = ArrayList<MediaVault>()
        for (mediaVault in mediaVaults) {
            if (mediaVault.timeOpen > 0) {
                mediaVaultArrayList.add(mediaVault)
            }
        }
        mediaVaultArrayList.sortWith(Comparator { o1, o2 ->
            java.lang.Long.compare(
                o2.timeOpen,
                o1.timeOpen
            )
        })
        return mediaVaultArrayList
    }

    fun updateItemMediaVault(mediaVault: MediaVault?) {
        viewModelScope.launch(Dispatchers.IO) { appRepository.updateItemMediaVault(mediaVault) }
    }
}




