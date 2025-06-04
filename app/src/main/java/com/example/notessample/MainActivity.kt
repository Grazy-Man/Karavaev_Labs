package com.example.notessample

import android.app.AlertDialog
import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var dbAdapter: NotesDbAdapter
    private lateinit var listView: ListView
    private lateinit var adapter: ArrayAdapter<String>
    private var notes = mutableListOf<String>()
    private var ids = mutableListOf<Long>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)

        dbAdapter = NotesDbAdapter(this)
        dbAdapter.open()

        val editText = findViewById<EditText>(R.id.edit_text)
        val saveButton = findViewById<Button>(R.id.save_button)
        listView = findViewById(R.id.myListView)

        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, notes)
        listView.adapter = adapter
        registerForContextMenu(listView)

        loadNotes()

        saveButton.setOnClickListener {
            val text = editText.text.toString()
            if (text.isNotBlank()) {
                dbAdapter.insertNote(text)
                editText.text.clear()
                loadNotes()
            }
        }
    }

    private fun loadNotes() {
        notes.clear()
        ids.clear()
        val cursor = dbAdapter.getAllNotes()
        while (cursor.moveToNext()) {
            ids.add(cursor.getLong(0))
            notes.add(cursor.getString(1))
        }
        adapter.notifyDataSetChanged()
    }

    override fun onCreateContextMenu(menu: ContextMenu, v: View, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menu.setHeaderTitle("Опції")
        menu.add(0, v.id, 0, "Видалити")
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val info = item.menuInfo as AdapterView.AdapterContextMenuInfo
        if (item.title == "Видалити") {
            val id = ids[info.position]
            dbAdapter.deleteNote(id)
            loadNotes()
            return true
        }
        return super.onContextItemSelected(item)
    }

    override fun onDestroy() {
        dbAdapter.close()
        super.onDestroy()
    }
}