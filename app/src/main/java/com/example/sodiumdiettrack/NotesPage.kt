package com.example.sodiumdiettrack

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_notes_page.*
import kotlinx.android.synthetic.main.activity_settings.*

class NotesPage : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notes_page)

        btnLoad.setOnClickListener {
            loadData()
        }

        btnSave.setOnClickListener {
            saveData()
        }
    }

    private fun saveData() {
        val userNotes = usernotes.text.toString()
        val sharedPref = getSharedPreferences("notessharedPrefs", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.apply {
            putString("NOTES_KEY", userNotes)
        }.apply()
        Toast.makeText(this, "Data saved", Toast.LENGTH_SHORT).show()
    }

    private fun loadData() {
        val sharedPreferences = getSharedPreferences("notessharedPrefs", Context.MODE_PRIVATE)
        val savedNotes = sharedPreferences.getString("NOTES_KEY", null)
        usernotes.setText(savedNotes)
    }
}