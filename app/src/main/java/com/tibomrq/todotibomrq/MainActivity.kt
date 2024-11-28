package com.tibomrq.todotibomrq

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.view.ViewStructure
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.tibomrq.todotibomrq.list.Task
import com.tibomrq.todotibomrq.list.TaskListAdapter
import java.util.UUID

class MainActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
/*
        findViewById<FloatingActionButton>(R.id.floatingActionButton).setOnClickListener(){
            findViewById<FloatingActionButton>(R.id.floatingActionButton).setOnClickListener(){
                // Instanciation d'un objet task avec des données préremplies:
                val newTask = Task(id = UUID.randomUUID().toString(), title = "Task ${currentlist.size + 1}")
                currentlist = currentlist + newTask
            }
        } */



    }

}