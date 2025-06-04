package com.example.notessample

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class NotesDbAdapter(context: Context) {
    private val dbHelper = DBHelper(context)
    private lateinit var db: SQLiteDatabase

    fun open() {
        db = dbHelper.writableDatabase
    }

    fun close() {
        dbHelper.close()
    }

    fun insertNote(text: String): Long {
        val values = ContentValues()
        values.put("note", text)
        return db.insert("notes", null, values)
    }

    fun getAllNotes(): Cursor {
        return db.query("notes", arrayOf("_id", "note"), null, null, null, null, null)
    }

    fun deleteNote(id: Long): Boolean {
        return db.delete("notes", "_id = ?", arrayOf(id.toString())) > 0
    }

    private class DBHelper(context: Context) : SQLiteOpenHelper(context, "notes.db", null, 1) {
        override fun onCreate(db: SQLiteDatabase) {
            db.execSQL("CREATE TABLE notes (_id INTEGER PRIMARY KEY AUTOINCREMENT, note TEXT);")
        }

        override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
            db.execSQL("DROP TABLE IF EXISTS notes")
            onCreate(db)
        }
    }
}
