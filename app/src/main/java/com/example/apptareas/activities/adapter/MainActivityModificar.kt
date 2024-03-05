package com.example.apptareas.activities.adapter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import com.example.apptareas.R
import com.example.apptareas.data.providers.TaskDAO

class MainActivityModificar : AppCompatActivity() {

    lateinit var txtmodificatarea:EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_modificar)

        txtmodificatarea=findViewById(R.id.txtmodificatarea)

        var resultado:Int? =intent.getIntExtra("ID_TABLA",0)

        //buscamos en base de datos el id
        val taskDAO = TaskDAO(this)
        //taskDAO.find(resultado)


        txtmodificatarea.setText(resultado.toString())

    }
}