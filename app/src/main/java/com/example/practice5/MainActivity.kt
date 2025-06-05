package com.example.practice5

import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // EditText + Enter key listener
        val userName = findViewById<EditText>(R.id.user_name)
        userName.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                Toast.makeText(applicationContext, userName.text, Toast.LENGTH_SHORT).show()
                true
            } else {
                false
            }
        }

        // Clear button listener
        val clearButton = findViewById<Button>(R.id.clear_button)
        clearButton.setOnClickListener {
            userName.setText("")
        }
    }

    fun onToggleClicked(view: View) {
        val toggleButton = view as ToggleButton
        if (toggleButton.isChecked) {
            Toast.makeText(this, "Ввімкнено", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Вимкнено", Toast.LENGTH_SHORT).show()
        }
    }

    fun onRadioButtonClicked(view: View) {
        val radioButton = view as RadioButton
        Toast.makeText(this, "Вибраний звір: ${radioButton.text}", Toast.LENGTH_SHORT).show()
    }
}