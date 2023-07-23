package com.example.valut.ui.ultis

import android.database.Cursor
import android.os.Build

object CursorUtils {
    fun getString(c: Cursor, columnName: String): String? {
        return getString(c, getColumnIndexOrThrow(c, columnName))
    }

    fun getString(c: Cursor, columnIndex: Int): String? {
        return if (c.isNull(columnIndex)) {
            null
        } else c.getString(columnIndex)
    }

    fun getLong(c: Cursor, columnName: String): Long {
        return getLong(c, getColumnIndexOrThrow(c, columnName))
    }

    fun getLong(c: Cursor, columnIndex: Int): Long {
        return if (c.isNull(columnIndex)) {
            0L
        } else c.getLong(columnIndex)
    }

    fun getInt(c: Cursor, columnName: String): Int {
        return getInt(c, getColumnIndexOrThrow(c, columnName))
    }

    fun getInt(c: Cursor, columnIndex: Int): Int {
        return if (c.isNull(columnIndex)) {
            0
        } else c.getInt(columnIndex)
    }

    fun getFloat(c: Cursor, columnName: String): Float {
        return getFloat(c, getColumnIndexOrThrow(c, columnName))
    }

    fun getFloat(c: Cursor, columnIndex: Int): Float {
        return if (c.isNull(columnIndex)) {
            0f
        } else c.getFloat(columnIndex)
    }

    fun getDouble(c: Cursor, columnName: String): Double {
        return getDouble(c, getColumnIndexOrThrow(c, columnName))
    }

    fun getDouble(c: Cursor, columnIndex: Int): Double {
        return if (c.isNull(columnIndex)) {
            0.0
        } else c.getDouble(columnIndex)
    }

    fun getShort(c: Cursor, columnName: String): Short {
        return getShort(c, getColumnIndexOrThrow(c, columnName))
    }

    fun getShort(c: Cursor, columnIndex: Int): Short {
        return if (c.isNull(columnIndex)) {
            0
        } else c.getShort(columnIndex)
    }

    //##################
    fun getColumnIndex(c: Cursor, columnName: String): Int {
        var index = c.getColumnIndex(columnName)
        if (index >= 0) {
            return index
        }
        index = c.getColumnIndex("`$columnName`")
        return if (index >= 0) {
            index
        } else findColumnIndexBySuffix(c, columnName)
    }

    fun getColumnIndexOrThrow(c: Cursor, columnName: String): Int {
        val index = getColumnIndex(c, columnName)
        return if (index >= 0) {
            index
        } else -1
    }

    private fun findColumnIndexBySuffix(cursor: Cursor, name: String): Int {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N_MR1) {
            // we need this workaround only on APIs < 26. So just return not found on newer APIs
            return -1
        }
        if (name.length == 0) {
            return -1
        }
        val columnNames = cursor.columnNames
        return findColumnIndexBySuffix(columnNames, name)
    }

    fun findColumnIndexBySuffix(columnNames: Array<String>, name: String): Int {
        val dotSuffix = ".$name"
        val backtickSuffix = ".$name`"
        for (index in columnNames.indices) {
            val columnName = columnNames[index]
            // do not check if column name is not long enough. 1 char for table name, 1 char for '.'
            if (columnName.length >= name.length + 2) {
                if (columnName.endsWith(dotSuffix)) {
                    return index
                } else if (columnName[0] == '`'
                    && columnName.endsWith(backtickSuffix)
                ) {
                    return index
                }
            }
        }
        return -1
    }

    //##################################
    fun getCount(cursor: Cursor?): Int {
        if (null != cursor) {
            try {
                return cursor.count
            } catch (e: Throwable) {
                //
            }
        }
        return 0
    }

    fun getPosition(cursor: Cursor?): Int {
        if (null != cursor) {
            try {
                return cursor.position
            } catch (e: Throwable) {
                //
            }
        }
        return -1
    }

    fun moveToFirst(cursor: Cursor?): Boolean {
        if (null != cursor) {
            try {
                return cursor.moveToFirst()
            } catch (e: Throwable) {
                //
            }
        }
        return false
    }

    fun moveToNext(cursor: Cursor?): Boolean {
        if (null != cursor) {
            try {
                return cursor.moveToNext()
            } catch (e: Throwable) {
                //
            }
        }
        return false
    }
}