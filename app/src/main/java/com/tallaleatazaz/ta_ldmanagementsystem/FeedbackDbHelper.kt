package com.tallaleatazaz.ta_ldmanagementsystem

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class FeedbackDbHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }

    fun insertFeedback(taEmail: String, courseName: String, feedbackText: String): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TA_EMAIL, taEmail)
            put(COLUMN_COURSE_NAME, courseName)
            put(COLUMN_FEEDBACK_TEXT, feedbackText)
        }
        return db.insert(TABLE_NAME, null, values)
    }

    fun getUnsentFeedback(): List<Feedback> {
        val db = readableDatabase
        val cursor: Cursor = db.rawQuery("SELECT * FROM $TABLE_NAME", null)
        val unsentFeedback = mutableListOf<Feedback>()

        with(cursor) {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(COLUMN_ID))
                val taEmail = getString(getColumnIndexOrThrow(COLUMN_TA_EMAIL))
                val courseName = getString(getColumnIndexOrThrow(COLUMN_COURSE_NAME))
                val feedbackText = getString(getColumnIndexOrThrow(COLUMN_FEEDBACK_TEXT))
                unsentFeedback.add(Feedback(id, taEmail, courseName, feedbackText))
            }
        }

        return unsentFeedback
    }

    fun deleteFeedback(id: Int): Int {
        val db = writableDatabase
        val selection = "$COLUMN_ID = ?"
        val selectionArgs = arrayOf(id.toString())
        return db.delete(TABLE_NAME, selection, selectionArgs)
    }

    companion object {
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "project.db"
        const val TABLE_NAME = "feedback"
        const val COLUMN_ID = "id"
        const val COLUMN_TA_EMAIL = "ta_email"
        const val COLUMN_COURSE_NAME = "course_name"
        const val COLUMN_FEEDBACK_TEXT = "feedback_text"

        private const val SQL_CREATE_ENTRIES =
            "CREATE TABLE $TABLE_NAME (" +
                    "$COLUMN_ID INTEGER PRIMARY KEY," +
                    "$COLUMN_TA_EMAIL TEXT," +
                    "$COLUMN_COURSE_NAME TEXT," +
                    "$COLUMN_FEEDBACK_TEXT TEXT)"

        private const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS $TABLE_NAME"
    }
}

data class Feedback(val id: Int, val taEmail: String, val courseName: String, val feedbackText: String)
