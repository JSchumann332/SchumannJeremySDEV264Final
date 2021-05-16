package com.example.sodiumdiettrack

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import kotlinx.android.synthetic.main.activity_settings.*

class Settings : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        loadData()

        btnSettingSubmit.setOnClickListener {
            saveData()
        }

        feedbackbutton.setOnClickListener {
            startActivity(Intent(this, FeedBack::class.java))
        }

        cleardatabutton.setOnClickListener {
            clearData()
        }
    }
        fun saveData() {
            val userIntake = intakeamount.text.toString()
            intakeText.text = userIntake
            val userData = retentionamount.text.toString()
            retentionText.text = userData

            val sharedPreferences = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.apply {
                putString("INTAKE_KEY", userIntake)
                putString("DATA_KEY", userData)
            }.apply()

            Toast.makeText(this, "Data saved", Toast.LENGTH_SHORT).show()
        }

        private fun loadData() {
            val sharedPreferences = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
            val savedIntake = sharedPreferences.getString("INTAKE_KEY", null)
            val savedRetention = sharedPreferences.getString("DATA_KEY", null)

            intakeText.text = savedIntake
            retentionText.text = savedRetention
        }

        private fun clearData() {
            getSharedPreferences("sharedPrefs", MODE_PRIVATE)
                .edit()
                .clear()
                .apply()
            val intent = Intent(this, Settings::class.java)
            startActivity(intent)
            finishAffinity()
        }
    }