package com.example.apptareas.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.widget.AppCompatButton
import com.example.apptareas.R
import com.example.apptareas.data.providers.Task
import com.example.apptareas.data.providers.TaskDAO


lateinit var txtnuevatarea:EditText
lateinit var txtdescripcion:EditText
lateinit var btngrabatarea: AppCompatButton
lateinit var txtdiasemana:EditText
lateinit var btncancelar:AppCompatButton



class MainActivityinsertar : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_activityinsertar)


        iniciar()
    }

    private fun iniciar() {

        val taskDAO = TaskDAO(this)


        txtnuevatarea=findViewById(R.id.txtnuevatarea)
        txtdescripcion=findViewById(R.id.txtdescripcion)
        btngrabatarea=findViewById(R.id.btngrabatarea)
        txtdiasemana=findViewById(R.id.txtdiasemana)
        btncancelar=findViewById(R.id.btncancelar)

        btngrabatarea.setOnClickListener(){
            // Insertar
            var tarea: Task = Task(-1, txtnuevatarea.text.toString(), false,txtdescripcion.text.toString(),txtdiasemana.text.toString())
            taskDAO.insert(tarea)

                finish()

        }

        btncancelar.setOnClickListener(){
            finish()
        }

    }
}