// app/src/main/java/com.example.mycontentproviderapp/MyDatabaseHelper.kt
package com.example.mycontentproviderapp

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.mycontentproviderapp.WordContract.WordEntry

class MyDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "words.db"
        private const val DATABASE_VERSION = 1

        // Запит на створення таблиці
        // --- ВИПРАВЛЕНО: Змінено 'const val' на 'val' ---
        private val SQL_CREATE_ENTRIES = """
            CREATE TABLE ${WordEntry.TABLE_NAME} (
                ${WordEntry._ID} INTEGER PRIMARY KEY AUTOINCREMENT,
                ${WordEntry.COLUMN_WORD} TEXT NOT NULL UNIQUE);
        """.trimIndent()
        // -----------------------------------------------------
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS ${WordEntry.TABLE_NAME}")
        onCreate(db)
    }

    fun clearTable() {
        val db = writableDatabase
        db.execSQL("DELETE FROM ${WordEntry.TABLE_NAME}")
        db.close()
    }
}