package com.example.sodiumdiettrack.utilities

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class SqLiteHelper(val context: Context) : SQLiteOpenHelper(
    context,
    DATABASE_NAME,
    null,
    DATABASE_VERSION
) {
    companion object{
        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "SDT"
        private val TABLE_STATS = "stats"
        private val KEY_ID = "id"
        private val KEY_DATE = "date"
        private val KEY_CURRENT = "current"
        private val KEY_TOTAL_INTAKE = "totalintake"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_STATS_TABLE = ("CREATE TABLE " + TABLE_STATS + "(" +
                KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_DATE +
                " TEXT UNIQUE," + KEY_CURRENT + " INT," + KEY_TOTAL_INTAKE + " INT" + ")")
        db?.execSQL(CREATE_STATS_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS" + TABLE_STATS)
        onCreate(db)
    }

    fun addAll(date: String, current: Int, totalintake: Int): Long {
        if (checkExistence(date) == 0) {
            val values = ContentValues()
            values.put(KEY_DATE, date)
            values.put(KEY_CURRENT, current)
            values.put(KEY_TOTAL_INTAKE, totalintake)
            val db = this.writableDatabase
            val response = db.insert(TABLE_STATS, null, values)
            db.close()
            return response
        }
        return -1
    }

    fun getCurrent(date: String): Int {
        val selectQuery = "SELECT $KEY_CURRENT FROM $TABLE_STATS WHERE $KEY_DATE = ?"
        val db = this.readableDatabase
        db.rawQuery(selectQuery, arrayOf(date)).use {
            if (it.moveToFirst()) {
                return it.getInt(it.getColumnIndex(KEY_CURRENT))
            }
        }
        return 0
    }

    fun addCurrent(date: String, selectedOption: Int): Int {
        val current = getCurrent(date)
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_CURRENT, current + selectedOption)
        val response = db.update(TABLE_STATS, contentValues, "$KEY_DATE = ?", arrayOf(date))
        db.close()
        return response
    }

    private fun checkExistence(date: String): Int {
        val selectQuery = "SELECT $KEY_CURRENT FROM $TABLE_STATS WHERE $KEY_DATE = ?"
        val db = this.readableDatabase
        db.rawQuery(selectQuery, arrayOf(date)).use {
            if (it.moveToFirst()) {
                return it.count
            }
        }
        return 0
    }

    fun updateTotalIntake(date:String, totalintake: Int): Int {
        val current = getCurrent(date)
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_TOTAL_INTAKE, totalintake)
        val response = db.update(TABLE_STATS, contentValues, "$KEY_DATE = ?", arrayOf(date))
        db.close()
        return response
    }
}