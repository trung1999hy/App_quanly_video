package com.example.valut.ui.ultis

import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.os.Environment
import android.util.Base64
import android.util.Log
import com.example.valut.ui.TypeMedia
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.nio.file.Files
import java.nio.file.StandardCopyOption
import java.text.SimpleDateFormat
import java.util.Arrays
import java.util.Date
import java.util.Locale

object FileUtils {
    const val nameDB = "apps_locker.db"
    private val dCIMDirectory: String
        private get() {
            val dcimDirectory =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
            return dcimDirectory.absolutePath
        }

    fun createFolder(): String {
        System.err.println("nam: " + dCIMDirectory)
        val folderName = "Vault" // Tên thư mục mới
        val folder = File(dCIMDirectory, folderName)
        if (!folder.exists()) {
            val created = folder.mkdir()
            if (created) {
                Log.d("", "Thư mục mới đã được tạo thành công")
            } else {
                Log.e("", "Không thể tạo thư mục mới")
            }
        } else {
            Log.d("", "Thư mục đã tồn tại")
        }
        return folder.absolutePath
    }


    fun checkDb(): Boolean {
        val filePath = createFolder() + "/" + nameDB
        val file = File(filePath)
        return file.exists()
    }

    fun createFolderMediaVault(type: Int): String {
        val path: String
        val pathFolder = createFolder()
        if (type != TypeMedia.ALL) {
            val newFolder: String
            newFolder = if (type == TypeMedia.SOUND_TYPE) {
                "sound"
            } else if (type == TypeMedia.VIDEO_TYPE) {
                "video"
            } else if (type == TypeMedia.IMAGE_TYPE) {
                "image"
            } else {
                ""
            }
            val folder = File(pathFolder, newFolder)
            if (!folder.exists()) {
                val created = folder.mkdir()
                if (created) {
                    Log.d("", "Thư mục mới đã được tạo thành công")
                } else {
                    Log.e("", "Không thể tạo thư mục mới")
                }
            } else {
                Log.d("", "Thư mục đã tồn tại")
            }
            path = folder.absolutePath
        } else path = createFolder()
        return path
    }

    fun changPathToVault(type: Int, currentPath: String): String? {
        var newFilePath: String? = null
        val currentFile = File(currentPath)
        val list = Arrays.asList(*currentPath.split("/".toRegex()).dropLastWhile { it.isEmpty() }
            .toTypedArray())
        if (currentFile.exists() && list.size > 2) {
            val folderPath = createFolderMediaVault(type) + "/" + list[list.size - 2]
            val folder = File(folderPath)
            if (!folder.exists() && !folder.mkdir()) {
                return null
            }
            newFilePath = getOutPathEncodeBase64(
                folder.absolutePath,
                encodeBase64(currentFile.name).trim { it <= ' ' })
            val newFile = File(newFilePath)
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    Files.move(
                        currentFile.toPath(),
                        newFile.toPath(),
                        StandardCopyOption.REPLACE_EXISTING
                    )
                    return newFilePath
                } else {
                    if (currentFile.renameTo(newFile)) {
                        println("Moved")
                        return newFilePath
                    } else {
                        println("Failed")
                    }
                }
            } catch (e: Throwable) {
                System.err.println("Error: " + e.message)
            }
        }
        return null
    }

    private fun getFileExtension(fileName: String): String {
        val dotIndex = fileName.lastIndexOf(".")
        return if (dotIndex != -1 && dotIndex < fileName.length - 1) {
            fileName.substring(dotIndex + 1)
        } else ""
    }

    fun restoreFile(currentPath: String?, newPath: String?): Boolean {
        val currentFile = File(currentPath)
        val newFile = File(newPath)
        if (newFile.exists()) {
            val file = currentFile.parentFile
            if (!file.exists()) {
                file.mkdirs()
            }
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    Files.move(
                        newFile.toPath(),
                        currentFile.toPath(),
                        StandardCopyOption.REPLACE_EXISTING
                    )
                    return true
                } else {
                    if (newFile.renameTo(currentFile)) {
                        println("Moved")
                        return true
                    } else {
                        println("Failed")
                    }
                }
            } catch (e: Throwable) {
                System.err.println("Error: " + e.message)
            }
        }
        return false
    }

    //
    fun encodeBase64(data: String): String {
        val bytes = data.toByteArray()
        return Base64.encodeToString(bytes, Base64.DEFAULT)
    }

    fun decodeBase64(encodedData: String?): String {
        val bytes =
            Base64.decode(encodedData, Base64.DEFAULT)
        return String(bytes)
    }

    private fun getOutPathEncodeBase64(directoryPath: String, fileName: String): String? {
        return try {
            var newFileName = fileName
            var counter = 1
            val directory = File(directoryPath)
            val existingFiles = directory.listFiles()
            if (null != existingFiles && existingFiles.size > 0) {
                for (file in existingFiles) {
                    val existingFileName = encodeBase64(file.name)
                    if (existingFileName == existingFileName) {
                        val name = decodeBase64(fileName)
                        newFileName =
                            getFileNameWithoutExtension(name) + "_" + counter + getFileExtension(
                                name
                            )
                        counter++
                    }
                }
            }
            directoryPath + "/" + encodeBase64(newFileName).trim { it <= ' ' }
        } catch (throwable: Throwable) {
            null
        }
    }

    fun getFileNameWithoutExtension(fileName: String): String {
        val dotIndex = fileName.lastIndexOf(".")
        return if (dotIndex > 0 && dotIndex < fileName.length - 1) {
            fileName.substring(0, dotIndex)
        } else fileName
    }

    fun createTempImageFileReport(context: Context): File {
        val folder = context.cacheDir
        val file = File(folder, "image")
        if (!file.exists()) {
            file.mkdirs()
        }
        return File(file.absolutePath, defaultName + ".jpg")
    }

    private val defaultName: String
        private get() = "Valut" + SimpleDateFormat(
            Constant.DATE_FORMAT,
            Locale.getDefault()
        ).format(
            Date()
        )

    fun saveToInternalStorage(context: Context, bitmapImage: Bitmap): String {
        val folder = context.cacheDir
        val file = File(folder, "theme")
        if (!file.exists()) {
            file.mkdirs()
        }
        val mypath = File(file.absolutePath, defaultName + ".jpg")
        var outputStream: OutputStream? = null
        try {
            outputStream = FileOutputStream(mypath)
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 0, outputStream)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                outputStream!!.close()
            } catch (e: Throwable) {
                e.printStackTrace()
            }
        }
        return mypath.absolutePath
    }
}