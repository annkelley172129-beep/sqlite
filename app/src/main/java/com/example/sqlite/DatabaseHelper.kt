package com.example.sqlite

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData


data class Student(
    val id: Int,
    val name: String,
    val course: String
)
class DatabaseHelper (context: Context) :
    SQLiteOpenHelper (context, "StudentDB", null, 1){
    private val studentsLiveData = MutableLiveData<List<Student>>()
    override fun onCreate(db: SQLiteDatabase) {

        val query = """
            CREATE TABLE students (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT,
               course TEXT
            )
        """.trimIndent()

        db.execSQL(query)
    }
        override fun onUpgrade(
            db: SQLiteDatabase,
            oldVersion: Int,
            newVersion: Int
        ) {

            db.execSQL("DROP TABLE IF EXISTS students")
            onCreate(db)
        }
    fun addStudent(name: String, course: String) {

        val db = writableDatabase

        val values = ContentValues()

        values.put("name", name)
        values.put("course", course)

        db.insert("students", null, values)

        db.close()  // Update LiveData after inserting
        loadStudents()

    }
    fun loadStudents() {

        val students = mutableListOf<Student>()

        val db = readableDatabase

        val cursor = db.rawQuery(
            "SELECT * FROM students",
            null
        )

        while (cursor.moveToNext()) {

            val id = cursor.getInt(
                cursor.getColumnIndexOrThrow("id")
            )

            val name = cursor.getString(
                cursor.getColumnIndexOrThrow("name")
            )


            val course = cursor.getString(
                cursor.getColumnIndexOrThrow("course")
            )
            students.add(
                Student(
                    id,
                    name,
                    course
                )
            )
        }

        cursor.close()
        db.close()

        studentsLiveData.postValue(students)
    }

    fun getStudents(): LiveData<List<Student>> {
        loadStudents()
        return studentsLiveData
    }
        }

