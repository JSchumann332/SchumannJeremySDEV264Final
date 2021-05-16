package com.example.sodiumdiettrack.utilities

import java.text.DateFormat
import java.text.DateFormat.SHORT
import java.text.DateFormat.getDateInstance
import java.text.SimpleDateFormat
import java.util.*

class AppCalcs {
    companion object{
        fun getDate(): String? {
            val c = Calendar.getInstance().time
            val df = getDateInstance(SHORT)
            return df.format(c)
        }

        val USERS_SHARED_PREF = "user_pref"
        val PRIVATE_MODE = 0
        val TOTAL_INTAKE = "totalintake"
    }
}