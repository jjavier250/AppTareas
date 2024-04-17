package com.example.apptareas.activities.adapter

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Vibrator
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.apptareas.NotificationReceiver
import com.example.apptareas.NotificationReceiver.Companion.NOTIFICATION_ID
import com.example.apptareas.R

import com.example.apptareas.data.providers.Task
import com.example.apptareas.data.providers.TaskDAO
import java.util.Calendar


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
    lateinit var switch1:Switch





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_modificar)

        var vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        var noti:Boolean=false

        taskDAO = TaskDAO(this)

        txtmodificatarea = findViewById(R.id.txtmodificatarea)
        btngrabatarea = findViewById(R.id.btngrabatarea)
        txtdescripcion = findViewById(R.id.txtdescripcion)
        txtdiasemana = findViewById(R.id.txtdiasemana)
        btncancelar = findViewById(R.id.btncancelar)
        spinermod = findViewById(R.id.spinermod)
        switch1=findViewById(R.id.switch1) // notificación

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
        // añadimos marcado a la lista de los dias de la semana del que tenemos grabado en BD
        spinermod.setSelection(diasSemana.indexOf(tarea.diaSemana))
        switch1.isChecked=tarea.noti




       switch1.setOnClickListener(){

            if (switch1.isChecked) {
                noti=true
                vibrator.vibrate(500) // vibracion
            }
            else
            {
                noti=false
            }
        }


        btngrabatarea.setOnClickListener() {


            // funciona pero me actualiza a false el check var modiftarea: Task = Task (id_tarea,txtmodificatarea.text.toString(),false)
            // val taskDAO = TaskDAO(this)
            //taskDAO.update(modiftarea)

            // Parte de actualizar

            tarea.tarea = txtmodificatarea.text.toString()
            tarea.descripcion = txtdescripcion.text.toString()
            tarea.diaSemana = txtdiasemana.text.toString()
            tarea.noti=noti

            taskDAO.update(tarea)

            if(noti==true){
                // lanzo notificacion
                val permissionState =
                    ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                // If the permission is not granted, request it.
                // If the permission is not granted, request it.
                if (permissionState == PackageManager.PERMISSION_DENIED) {
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                        1
                    )
                } else {

                        val textoIngresado = txtmodificatarea.text.toString()

                        notificacion(textoIngresado.toString())
                        Toast.makeText(this, "notificación: $textoIngresado ", Toast.LENGTH_SHORT).show()
                        //llamar a la funcion notificacion y pasar parametro de el texto

                }
            }

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

            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Opcional: Si no hay nada seleccionado
                txtdiasemana.setText(tarea.diaSemana)
            }
        })

    }

    private fun notificacion(texto:String) {

        // Setear la alarma para el próximo lunes a las 13:05 AM
        /*val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY)
        calendar.set(Calendar.HOUR_OF_DAY,10)
        calendar.set(Calendar.MINUTE,1)
        calendar.set(Calendar.SECOND, 0)
        Log.i("NOTIFICACION", "PASA POR AQUI")

        // Si hoy es lunes y ya pasó las 13:50 AM, programar la notificación para el próximo lunes
        if (Calendar.getInstance().after(calendar)) {
            calendar.add(Calendar.DATE, 7)
        }

        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
        */

        createChannel(tarea.id)


        val intent = Intent(applicationContext, NotificationReceiver()::class.java)  // el NOTIFICATION_ID es unico puedo usar el de BD
        val pendingIntent = PendingIntent.getBroadcast(
            applicationContext,
            tarea.id,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, Calendar.getInstance().timeInMillis , pendingIntent)
    }



    private fun createChannel(identificador:Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "identificador$identificador",
                "MySuperChannel",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "SUSCRIBETE"
            }

            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            notificationManager.createNotificationChannel(channel)
        }
    }


    }


