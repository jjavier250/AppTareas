package com.example.apptareas.activities.adapter

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import com.example.apptareas.R

import com.example.apptareas.data.providers.Task
import com.example.apptareas.data.providers.TaskDAO


class MainActivityModificar : AppCompatActivity() {

    lateinit var txtmodificatarea:EditText
    lateinit var btngrabatarea:AppCompatButton
    lateinit var txtdescripcion:EditText
    lateinit var taskDAO: TaskDAO
    lateinit var tarea: Task
    lateinit var txtdiasemana:EditText
    lateinit var btncancelar:AppCompatButton
    lateinit var spinermod:Spinner
    lateinit var  selectedDay:String





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_modificar)

        taskDAO = TaskDAO(this)

        txtmodificatarea = findViewById(R.id.txtmodificatarea)
        btngrabatarea = findViewById(R.id.btngrabatarea)
        txtdescripcion = findViewById(R.id.txtdescripcion)
        txtdiasemana = findViewById(R.id.txtdiasemana)
        btncancelar = findViewById(R.id.btncancelar)
        spinermod = findViewById(R.id.spinermod)

        // llenar el spinner
        val diasSemana = arrayOf(
            "Ninguno",
            "Lunes",
            "Martes",
            "Miércoles",
            "Jueves",
            "Viernes",
            "Sábado",
            "Domingo"
        )
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, diasSemana)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        // Asigna el adaptador al Spinner
        spinermod.adapter = adapter

        // fin llenar spinner

        //buscamos el id en BD nos devuelve de tipo tarea
        var id_tarea: Int = intent.getIntExtra("ID_TABLA", -1)
        tarea = taskDAO.find(id_tarea)!!

        //pintamos lo que nos ha devuelto en las cajas
        txtmodificatarea.setText(tarea.tarea)
        txtdescripcion.setText(tarea.descripcion)
        txtdiasemana.setText(tarea.diaSemana)


        btngrabatarea.setOnClickListener() {


            // funciona pero me actualiza a false el check var modiftarea: Task = Task (id_tarea,txtmodificatarea.text.toString(),false)
            // val taskDAO = TaskDAO(this)
            //taskDAO.update(modiftarea)

            // Parte de actualizar

            tarea.tarea = txtmodificatarea.text.toString()
            tarea.descripcion = txtdescripcion.text.toString()
            tarea.diaSemana = txtdiasemana.text.toString()

            taskDAO.update(tarea)

            // fin parte de actualizar
            finish()

        }

        btncancelar.setOnClickListener() {
            finish()
        }


        spinermod.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                // Maneja la selección del usuario aquí
                selectedDay = parent.getItemAtPosition(position).toString()
                txtdiasemana.setText(selectedDay.toString())

                if (selectedDay != "Ninguno") {
                    txtdiasemana.setText(selectedDay.toString())
                } else {
                    txtdiasemana.setText(tarea.diaSemana)

                }


            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Opcional: Si no hay nada seleccionado
                txtdiasemana.setText(tarea.diaSemana)
            }
        })

    }


    }


