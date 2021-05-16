package com.example.sodiumdiettrack

import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.util.TypedValue
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.example.sodiumdiettrack.utilities.AppCalcs
import com.example.sodiumdiettrack.utilities.SqLiteHelper
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_settings.*

class MainActivity : AppCompatActivity() {

    private var totalIntake: Int = 2400
    private var dailyCurrent: Int = 0
    private lateinit var dateCurrent: String
    private lateinit var sharedPref: SharedPreferences
    private lateinit var SqLiteHelper: SqLiteHelper
    lateinit var foodText: EditText
    lateinit var sodiumText: EditText
    lateinit var button: Button
    lateinit var listView: ListView
    var list: ArrayList<String> = ArrayList()
    lateinit var arrayAdapter: ArrayAdapter<String>
    var foodSum: ArrayList<Int> = ArrayList()
    
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listView = findViewById(R.id.DailyList)
        foodText = findViewById(R.id.userFoodItem)
        sodiumText = findViewById(R.id.userSodiumItem)
        button = findViewById(R.id.AddFoodButton)
        arrayAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, list)

        sharedPref = getSharedPreferences(AppCalcs.USERS_SHARED_PREF, AppCalcs.PRIVATE_MODE)
        SqLiteHelper = SqLiteHelper(this)
        totalIntake = sharedPref.getInt(AppCalcs.TOTAL_INTAKE, 2400)

        dateCurrent = AppCalcs.getDate()!!

        updateDaily()
        setLineChartData()
        SqLiteHelper.updateTotalIntake(AppCalcs.getDate()!!, foodSum.sum())
    }

    fun setLineChartData() {

        val monTotal = foodSum.sum().toFloat()
        val tueTotal = foodSum.sum().toFloat()
        val wedTotal = foodSum.sum().toFloat()
        val thuTotal = foodSum.sum().toFloat()
        val friTotal = foodSum.sum().toFloat()
        val satTotal = foodSum.sum().toFloat()
        val sunTotal = foodSum.sum().toFloat()

        val entries = ArrayList<String>()
        entries.add("Mon")
        entries.add("Tue")
        entries.add("Wed")
        entries.add("Thu")
        entries.add("Fri")
        entries.add("Sat")
        entries.add("Sun")

        val lineEntry = ArrayList<Entry>()
        lineEntry.add(Entry(monTotal, 0))
        lineEntry.add(Entry(tueTotal, 1))
        lineEntry.add(Entry(wedTotal, 2))
        lineEntry.add(Entry(thuTotal, 3))
        lineEntry.add(Entry(friTotal, 4))
        lineEntry.add(Entry(satTotal, 5))
        lineEntry.add(Entry(sunTotal, 6))

        val leftAxis = WeeklyChart.axisLeft
        leftAxis.setAxisMinValue(0f)
        val rightAxis = WeeklyChart.axisRight
        rightAxis.setDrawGridLines(false)
        rightAxis.setDrawZeroLine(false)
        rightAxis.setDrawAxisLine(false)
        rightAxis.setDrawLabels(false)

        val linedataset = LineDataSet(lineEntry, "Weekly Sodium")
        linedataset.color = ContextCompat.getColor(this, R.color.med_blue)
        linedataset.lineWidth = 4f

        linedataset.circleRadius = 0f
        linedataset.setDrawFilled(true)
        linedataset.fillColor = ContextCompat.getColor(this, R.color.design_default_color_primary)
        linedataset.fillAlpha = 30

        val data = LineData(entries, linedataset)
        WeeklyChart.data = data
        WeeklyChart.animateXY(3000, 3000)
    }

    fun updateDaily() {
        totalIntake = sharedPref.getInt(AppCalcs.TOTAL_INTAKE, 2400)
        dailyCurrent = SqLiteHelper.getCurrent(dateCurrent)
        setSodium(foodSum.sum(), totalIntake)
    }

    override fun onStart() {
        super.onStart()
        val outValue = TypedValue()
        applicationContext.theme.resolveAttribute(
            android.R.attr.selectableItemBackground, outValue, true
        )

        SqLiteHelper.addAll(dateCurrent, 0, totalIntake)

        AddFoodButton.setOnClickListener {
            foodSum.add(sodiumText.text.toString().toInt())
            list.add(foodText.text.toString() + ", " + sodiumText.text.toString() + "mg")
            foodText.setText("")
            sodiumText.setText("")
            arrayAdapter.notifyDataSetChanged()
            listView.adapter = arrayAdapter
        }

        notesbutton.setOnClickListener {
            startActivity(Intent(this, NotesPage::class.java))
        }

        bpbutton.setOnClickListener {
            startActivity(Intent(this, BloodPressure::class.java))
        }

        settingsbutton.setOnClickListener {
            startActivity(Intent(this, Settings::class.java))
        }
    }

    private fun setSodium(dailyCurrent: Int, totalIntake: Int) {
        YoYo.with(Techniques.SlideInDown)
            .duration(500)
            .playOn(txProgress)
        txProgress.text = foodSum.sum().toString()
        val progress = ((foodSum.sum() / totalIntake.toFloat()) * 100).toInt()
        YoYo.with(Techniques.Pulse)
            .duration(500)
            .playOn(dailyProgress)
        dailyProgress.currentProgress = progress
        if ((foodSum.sum() * 100 / totalIntake) > 100) {
            Snackbar.make(main_activity_parent, "You have reached your daily limit", Snackbar.LENGTH_SHORT).show()
        }
    }
}

