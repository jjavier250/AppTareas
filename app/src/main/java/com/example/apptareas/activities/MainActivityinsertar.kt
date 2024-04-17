package com.example.apptareas.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import com.example.apptareas.R
import com.example.apptareas.data.providers.Task
import com.example.apptareas.data.providers.TaskDAO


lateinit var txtnuevatarea:EditText
lateinit var txtdescripcion:EditText
lateinit var btngrabatarea: AppCompatButton
lateinit var txtdiasemana:EditText
lateinit var btncancelar:AppCompatButton
lateinit var spinner: Spinner



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
        spinner = findViewById(R.id.spinner)

        // llenar el spinner
        val diasSemana = arrayOf("Ninguno","Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, diasSemana)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        // Asigna el adaptador al Spinner
        spinner.adapter = adapter
        spinner.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                // Maneja la selección del usuario aquí
                val selectedDay = parent.getItemAtPosition(position).toString()

                if(selectedDay!="Ninguno"){
                    txtdiasemana.setText(selectedDay.toString())
                }
                else{
                    //txtdiasemana.text.clear()
                    txtdiasemana.setText("Ninguno")
                }


            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Opcional: Si no hay nada seleccionado
            }
        })
        // fin llenar spinner


        btngrabatarea.setOnClickListener(){
            // Insertar
            if (!txtnuevatarea.text.toString().isEmpty()) {
                var tarea: Task = Task(-1, txtnuevatarea.text.toString(), false, txtdescripcion.text.toString(), txtdiasemana.text.toString(),false)
                taskDAO.insert(tarea)

                finish()
            }
            else
            {
                Toast.makeText(this, "La tarea no puede estar vacía", Toast.LENGTH_SHORT).show()
            }

            }

        btncancelar.setOnClickListener(){
            finish()
        }

        }



}