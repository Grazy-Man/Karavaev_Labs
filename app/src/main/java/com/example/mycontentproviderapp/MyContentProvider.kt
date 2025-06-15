// app/src/main/java/com.example.mycontentproviderapp/MyContentProvider.kt
package com.example.mycontentproviderapp

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.net.Uri
import android.util.Log
import com.example.mycontentproviderapp.WordContract.WordEntry
import java.lang.IllegalArgumentException

class MyContentProvider : ContentProvider() {

    private lateinit var dbHelper: MyDatabaseHelper

    // Коди для UriMatcher
    companion object {
        const val WORDS = 100 // Код для URI, який вказує на всю таблицю слів
        const val WORD_ID = 101 // Код для URI, який вказує на одне слово за ID

        private val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
            // Додаємо URI для всієї таблиці слів
            addURI(WordContract.CONTENT_AUTHORITY, WordContract.PATH_WORDS, WORDS)
            // Додаємо URI для одного слова (за ID)
            addURI(WordContract.CONTENT_AUTHORITY, WordContract.PATH_WORDS + "/#", WORD_ID)
        }
    }

    override fun onCreate(): Boolean {
        // Ініціалізуємо допоміжний клас для бази даних
        dbHelper = MyDatabaseHelper(context!!)
        Log.d("MyContentProvider", "Content Provider initialized.")
        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        val database: SQLiteDatabase = dbHelper.readableDatabase
        val cursor: Cursor?
        val match = sUriMatcher.match(uri)

        when (match) {
            WORDS -> {
                // Запит до всієї таблиці слів
                cursor = database.query(
                    WordEntry.TABLE_NAME,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    sortOrder
                )
            }
            WORD_ID -> {
                // Запит до одного слова за ID
                val id = uri.lastPathSegment // Отримуємо ID з URI
                val selectionWithId = "${WordEntry._ID}=?"
                val selectionArgsWithId = arrayOf(id!!)

                cursor = database.query(
                    WordEntry.TABLE_NAME,
                    projection,
                    selectionWithId,
                    selectionArgsWithId,
                    null,
                    null,
                    sortOrder
                )
            }
            else -> throw IllegalArgumentException("Cannot query unknown URI $uri")
        }

        // Повідомляємо Cursor, що йому потрібно спостерігати за змінами в даних URI
        cursor?.setNotificationUri(context?.contentResolver, uri)
        Log.d("MyContentProvider", "Query successful for URI: $uri")
        return cursor
    }

    override fun getType(uri: Uri): String? {
        val match = sUriMatcher.match(uri)
        return when (match) {
            WORDS -> WordEntry.CONTENT_LIST_TYPE
            WORD_ID -> WordEntry.CONTENT_ITEM_TYPE
            else -> null //throw IllegalArgumentException("Unknown URI $uri with match $match")
        }
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val database: SQLiteDatabase = dbHelper.writableDatabase
        val match = sUriMatcher.match(uri)
        val newUri: Uri?

        when (match) {
            WORDS -> {
                val id = database.insert(WordEntry.TABLE_NAME, null, values)
                if (id == -1L) {
                    Log.e("MyContentProvider", "Failed to insert row for $uri")
                    return null
                }
                newUri = WordEntry.CONTENT_URI.buildUpon().appendPath(id.toString()).build()
            }
            else -> throw IllegalArgumentException("Insertion is not supported for $uri")
        }

        // Повідомляємо спостерігачів, що дані змінилися
        context?.contentResolver?.notifyChange(uri, null)
        Log.d("MyContentProvider", "Insert successful for URI: $newUri")
        return newUri
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        val database: SQLiteDatabase = dbHelper.writableDatabase
        val rowsDeleted: Int
        val match = sUriMatcher.match(uri)

        when (match) {
            WORDS -> {
                // Видаляємо всі відповідні рядки
                rowsDeleted = database.delete(WordEntry.TABLE_NAME, selection, selectionArgs)
            }
            WORD_ID -> {
                // Видаляємо один рядок за ID
                val id = uri.lastPathSegment
                val selectionWithId = "${WordEntry._ID}=?"
                val selectionArgsWithId = arrayOf(id!!)
                rowsDeleted = database.delete(WordEntry.TABLE_NAME, selectionWithId, selectionArgsWithId)
            }
            else -> throw IllegalArgumentException("Deletion is not supported for $uri")
        }

        if (rowsDeleted != 0) {
            // Повідомляємо спостерігачів, що дані змінилися
            context?.contentResolver?.notifyChange(uri, null)
            Log.d("MyContentProvider", "Delete successful. Rows deleted: $rowsDeleted for URI: $uri")
        } else {
            Log.w("MyContentProvider", "No rows deleted for URI: $uri")
        }
        return rowsDeleted
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        val database: SQLiteDatabase = dbHelper.writableDatabase
        val rowsAffected: Int
        val match = sUriMatcher.match(uri)

        when (match) {
            WORDS -> {
                rowsAffected = database.update(WordEntry.TABLE_NAME, values, selection, selectionArgs)
            }
            WORD_ID -> {
                val id = uri.lastPathSegment
                val selectionWithId = "${WordEntry._ID}=?"
                val selectionArgsWithId = arrayOf(id!!)
                rowsAffected = database.update(WordEntry.TABLE_NAME, values, selectionWithId, selectionArgsWithId)
            }
            else -> throw IllegalArgumentException("Update is not supported for $uri")
        }

        if (rowsAffected != 0) {
            // Повідомляємо спостерігачів, що дані змінилися
            context?.contentResolver?.notifyChange(uri, null)
            Log.d("MyContentProvider", "Update successful. Rows affected: $rowsAffected for URI: $uri")
        } else {
            Log.w("MyContentProvider", "No rows updated for URI: $uri")
        }
        return rowsAffected
    }
}