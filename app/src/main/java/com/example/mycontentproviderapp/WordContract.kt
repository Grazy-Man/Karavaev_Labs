// app/src/main/java/com.example.mycontentproviderapp/WordContract.kt
package com.example.mycontentproviderapp

import android.content.ContentResolver
import android.net.Uri
import android.provider.BaseColumns // Важливо, що цей імпорт є

object WordContract {

    // Унікальний ідентифікатор для нашого Content Provider
    // Це має бути УНІКАЛЬНИМ для вашого застосунку в системі Android!
    const val CONTENT_AUTHORITY = "com.example.mycontentproviderapp.provider"

    // Базовий URI для доступу до Content Provider
    val BASE_CONTENT_URI: Uri = Uri.parse("content://$CONTENT_AUTHORITY")

    // Шлях до таблиці слів
    const val PATH_WORDS = "words"

    // Внутрішній клас, який визначає вміст таблиці "words"
    object WordEntry : BaseColumns {
        // --- ДОДАЙТЕ ЦЕЙ РЯДОК, ЩОБ ВИПРАВИТИ ПОМИЛКУ _ID ---
        val _ID: String = BaseColumns._ID // Явно робимо _ID доступним через WordEntry._ID
        // -----------------------------------------------------

        const val TABLE_NAME = "words"
        const val COLUMN_WORD = "word"

        // Повний URI для таблиці слів
        val CONTENT_URI: Uri = BASE_CONTENT_URI.buildUpon().appendPath(PATH_WORDS).build()

        // Типи MIME для списку та одного елемента
        const val CONTENT_LIST_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_WORDS
        const val CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_WORDS
    }
}