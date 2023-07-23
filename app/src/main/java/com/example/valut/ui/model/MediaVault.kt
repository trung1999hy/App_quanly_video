package com.example.valut.ui.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.Objects

@Entity(tableName = "media_vault")
class MediaVault : Serializable {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
        private set

    @ColumnInfo(name = "name")
    var name: String? = null

    @ColumnInfo(name = "pathCurrent")
    var pathCurrent: String? = null

    @ColumnInfo(name = "newPath")
    var newPath: String? = null

    @Ignore
    var isCheck = false

    @ColumnInfo(name = "size")
    var size: Long = 0

    @ColumnInfo(name = "Single")
    var single: String? = null

    @ColumnInfo(name = "timeModified")
    var timeModified: Long = 0

    @ColumnInfo(name = "timeOpen")
    var timeOpen: Long = 0

    @ColumnInfo(name = "Duration")
    var duration: Long = 0

    @ColumnInfo(name = "type")
    var type = 0

    constructor() {}
    constructor(pathCurrent: String?, newPath: String?, type: Int) {
        this.pathCurrent = pathCurrent
        this.newPath = newPath
        this.type = type
    }

    constructor(
        name: String?,
        single: String?,
        pathCurrent: String?,
        newPath: String?,
        size: Long,
        timeModified: Long,
        timeOpen: Long,
        duration: Long,
        type: Int
    ) {
        this.name = name
        this.single = single
        this.pathCurrent = pathCurrent
        this.newPath = newPath
        this.size = size
        this.timeModified = timeModified
        this.timeOpen = timeOpen
        this.duration = duration
        this.type = type
    }

    fun setId(id: Int) {
        this.id = id.toLong()
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val fileModel = o as MediaVault
        return id == fileModel.id && pathCurrent == fileModel.pathCurrent
    }

    override fun hashCode(): Int {
        return Objects.hash(id, pathCurrent)
    }

    override fun toString(): String {
        return "MediaVault{" +
                "name='" + name + '\'' +
                ", pathCurrent='" + pathCurrent + '\'' +
                ", newPath='" + newPath + '\'' +
                ", isCheck=" + isCheck +
                ", size=" + size +
                ", timeModified=" + timeModified +
                ", timeOpen=" + timeOpen +
                ", duration=" + duration +
                ", type=" + type +
                '}'
    }
}