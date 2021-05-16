package com.example.sodiumdiettrack

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import kotlinx.android.synthetic.main.activity_blood_pressure.*
import java.sql.Date
import java.sql.Timestamp
import java.time.*

class BloodPressure : AppCompatActivity() {

    lateinit var sysText: EditText
    lateinit var diaText: EditText
    lateinit var button: Button
    lateinit var listView: ListView
    var list: ArrayList<String> = ArrayList()
    lateinit var arrayAdapter: ArrayAdapter<String>
    val currentTimestamp = Timestamp(System.currentTimeMillis())
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_blood_pressure)

        listView = findViewById(R.id.last5readings)
        sysText = findViewById(R.id.systolicinput)
        diaText = findViewById(R.id.diastolicinput)
        button = findViewById(R.id.btnBPCalc)
        arrayAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, list)

        btnBPCalc.setOnClickListener {
            list.add(sysText.text.toString() + "/" + diaText.text.toString() + " " + currentTimestamp)
            sysText.setText("")
            diaText.setText("")
            arrayAdapter.notifyDataSetChanged()
            listView.adapter = arrayAdapter
            if (list.size > 5) {
                list.removeAt(0)
            }
        }
    }
}