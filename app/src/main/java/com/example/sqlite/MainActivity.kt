package com.example.sqlite

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    private lateinit var name : EditText
    private lateinit var course : EditText
    private lateinit var addbtn : Button
    private lateinit var tv : TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
      val  dbHelper = DatabaseHelper(this)
tv = findViewById(R.id.tvstudent)

        // Observe LiveData
        dbHelper.getStudents().observe(this) { students ->

            // This code runs whenever the student list changes

            var result = ""

            for (student in students) {

                result += "${student.id} - ${student.name} - ${student.course}\n"
            }

            tv.text = result
        }

        name = findViewById(R.id.etname)
        course  = findViewById(R.id.etcourse)
        addbtn = findViewById(R.id.addbtn)


        addbtn.setOnClickListener {
            val crs = course.text.toString()
            val nme = name.text.toString()
            dbHelper.addStudent(nme,crs)
            Toast.makeText(this, "Added Successfully", Toast.LENGTH_SHORT).show()
            name.text.clear()
            course.text.clear()
        }

    }
}