package com.example.listviewsample


import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import com.example.listviewsample.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val stations = resources.getStringArray(R.array.stations)
        val listView = findViewById<ListView>(R.id.stationListView)
        val adapter = ArrayAdapter(this, R.layout.list_item, stations)
        listView.adapter = adapter

        listView.setOnItemClickListener { parent, view, position, id ->
            val text = (view as TextView).text
            Toast.makeText(this, text, Toast.LENGTH_LONG).show()
        }
    }
}
