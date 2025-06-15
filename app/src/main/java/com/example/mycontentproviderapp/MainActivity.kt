// app/src/main/java/com.example.mycontentproviderapp/MainActivity.kt
package com.example.mycontentproviderapp

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.mycontentproviderapp.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import android.provider.BaseColumns // Якщо ви використовуєте _ID без префікса, краще використовувати WordContract.WordEntry._ID
import com.example.mycontentproviderapp.WordContract.WordEntry

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var dbHelper: MyDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = MyDatabaseHelper(this)

        lifecycleScope.launch(Dispatchers.IO) {
            dbHelper.clearTable()
            Log.d("MainActivity", "Database cleared for testing.")

            // Викликаємо suspend функції
            insertWord("sunrise")
            insertWord("freedom")
            insertWord("create")
            insertWord("babes")
            insertWord("create")

            queryAllWords()
            queryWordById(1)

            updateWord("Date", "Durian")

            queryAllWords()

            deleteWord("Banana")

            queryAllWords()

            withContext(Dispatchers.Main) {
                binding.statusTextView.text = "Операції з Content Provider завершено. Перевірте Logcat."
            }
        }
    }

    // --- ДОДАНО suspend ---
    private suspend fun insertWord(word: String) {
        val values = ContentValues().apply {
            put(WordContract.WordEntry.COLUMN_WORD, word)
        }
        val uri = contentResolver.insert(WordContract.WordEntry.CONTENT_URI, values)
        Log.d("MainActivity", "Inserted word '$word', URI: $uri")
    }

    // --- ДОДАНО suspend ---
    private suspend fun queryAllWords() {
        val cursor = contentResolver.query(
            WordContract.WordEntry.CONTENT_URI,
            arrayOf(WordContract.WordEntry._ID, WordContract.WordEntry.COLUMN_WORD),
            null,
            null,
            null
        )

        withContext(Dispatchers.Main) { // Цей withContext вимагає, щоб queryAllWords була suspend
            if (cursor != null && cursor.moveToFirst()) {
                val idColumnIndex = cursor.getColumnIndex(WordContract.WordEntry._ID)
                val wordColumnIndex = cursor.getColumnIndex(WordContract.WordEntry.COLUMN_WORD)
                val stringBuilder = StringBuilder("Current Words:\n")
                do {
                    val id = cursor.getLong(idColumnIndex)
                    val word = cursor.getString(wordColumnIndex)
                    stringBuilder.append("ID: $id, Word: $word\n")
                    Log.d("MainActivity", "Query Result - ID: $id, Word: $word")
                } while (cursor.moveToNext())
                binding.resultTextView.text = stringBuilder.toString()
            } else {
                Log.d("MainActivity", "No words found.")
                binding.resultTextView.text = "Слів не знайдено."
            }
            cursor?.close()
        }
    }

    // --- ДОДАНО suspend ---
    private suspend fun queryWordById(id: Long) {
        val uri = WordContract.WordEntry.CONTENT_URI.buildUpon().appendPath(id.toString()).build()
        val cursor = contentResolver.query(
            uri,
            arrayOf(WordContract.WordEntry._ID, WordContract.WordEntry.COLUMN_WORD),
            null,
            null,
            null
        )
        withContext(Dispatchers.Main) { // Цей withContext вимагає, щоб queryWordById була suspend
            if (cursor != null && cursor.moveToFirst()) {
                val idColumnIndex = cursor.getColumnIndex(WordContract.WordEntry._ID)
                val wordColumnIndex = cursor.getColumnIndex(WordContract.WordEntry.COLUMN_WORD)
                val foundId = cursor.getLong(idColumnIndex)
                val foundWord = cursor.getString(wordColumnIndex)
                Log.d("MainActivity", "Query By ID ($id) Result - ID: $foundId, Word: $foundWord")
            } else {
                Log.d("MainActivity", "Word with ID $id not found.")
            }
            cursor?.close()
        }
    }

    // --- ДОДАНО suspend ---
    private suspend fun updateWord(oldWord: String, newWord: String) {
        val values = ContentValues().apply {
            put(WordContract.WordEntry.COLUMN_WORD, newWord)
        }
        val selection = "${WordContract.WordEntry.COLUMN_WORD}=?"
        val selectionArgs = arrayOf(oldWord)

        val rowsUpdated = contentResolver.update(
            WordContract.WordEntry.CONTENT_URI,
            values,
            selection,
            selectionArgs
        )
        Log.d("MainActivity", "Updated $rowsUpdated rows from '$oldWord' to '$newWord'")
    }

    // --- ДОДАНО suspend ---
    private suspend fun deleteWord(word: String) {
        val selection = "${WordContract.WordEntry.COLUMN_WORD}=?"
        val selectionArgs = arrayOf(word)

        val rowsDeleted = contentResolver.delete(
            WordContract.WordEntry.CONTENT_URI,
            selection,
            selectionArgs
        )
        Log.d("MainActivity", "Deleted $rowsDeleted rows with word '$word'")
    }
}