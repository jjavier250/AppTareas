package com.example.apptareas.utils

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.apptareas.data.providers.Task

class DatabaseManager (context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "tareas.db"
        const val DATABASE_VERSION = 5
        const val COLUMN_NAME_ID = "id"

        private const val SQL_CREATE_TABLE =
            "CREATE TABLE ${Task.TABLE_NAME} (" +
                    "$COLUMN_NAME_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "${Task.COLUMN_NAME_TASK} TEXT," +
                    "${Task.COLUMN_NAME_DONE} BOOLEAN," +
                    "${Task.COLUMN_NAME_DESCRIPCION} TEXT," +
                    "${Task.COLUMN_NAME_DIASEMANA} TEXT," +
                    "${Task.COLUMN_NAME_NOTI} BOOLEAN)"

        private const val SQL_DELETE_TABLE = "DROP TABLE IF EXISTS ${Task.TABLE_NAME}"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (oldVersion == 1) {
            db.execSQL("DROP TABLE IF EXISTS Task")
        }
        destroyDatabase(db)
        onCreate(db)
    }

    private fun destroyDatabase (db: SQLiteDatabase) {
        db.execSQL(SQL_DELETE_TABLE)
    }
}