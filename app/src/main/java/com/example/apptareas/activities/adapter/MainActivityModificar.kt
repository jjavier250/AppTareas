package com.example.apptareas.activities.adapter

import android.Manifest
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.media.AudioManager
import android.media.ToneGenerator
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Vibrator
import android.text.format.DateUtils.isToday
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.CalendarView
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.apptareas.NotificationReceiver
import com.example.apptareas.R

import com.example.apptareas.data.providers.Task
import com.example.apptareas.data.providers.TaskDAO
import com.example.apptareas.globalVariable
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


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
    lateinit var imagenlapiz:ImageView
    lateinit var calendarView:CalendarView
    lateinit var spinertiempo:Spinner
    lateinit var textaviso:TextView


    var fechaAlarma:Long=-1
    var fechaAlarmaP:Long=-1
    var formatofecha:String=""
    var tiemposeleccionado:String=""




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
        imagenlapiz = findViewById(R.id.imagenlapiz)
        switch1=findViewById(R.id.switch1) // notificación
        calendarView=findViewById(R.id.calendarView)
        spinertiempo=findViewById(R.id.spinertiempo)
        textaviso=findViewById(R.id.textaviso)


        spinertiempo.visibility=View.GONE
        textaviso.visibility=View.GONE

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

        val tiempo = arrayOf(
            "15 minutos",
            "30 minutos",
            "1 hora",
            "2 horas",
            "3 horas",
            "4 horas",
            "5 horas"
        )
        val adaptertiempo = ArrayAdapter(this, android.R.layout.simple_spinner_item, tiempo)
        adaptertiempo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        // Asigna el adaptador al Spinner
        spinertiempo.adapter = adaptertiempo


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
        // para mostrar o no elcalendario si viene con el valor true o false de la base de datos
        if (tarea.noti){
            calendarView.visibility=View.VISIBLE
        }
        else{
            calendarView.visibility=View.GONE
        }


        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val calendar = Calendar.getInstance()
            calendar.set(year, month, dayOfMonth)
            val dateInMillis = calendar.timeInMillis
            val formattedDate = convertirFecha(dateInMillis)
            formatofecha=formattedDate


            if (isToday(dateInMillis)) {
               // Toast.makeText(this, "Has seleccionado el día de hoy.", Toast.LENGTH_SHORT).show()
                spinertiempo.visibility=View.VISIBLE
                textaviso.visibility=View.VISIBLE
                fechaAlarma=dateInMillis
                fechaAlarmaP=fechaAlarma
            }
            else{
                fechaAlarma=dateInMillis
                fechaAlarmaP=fechaAlarma
            }


            calendarView.visibility=View.GONE
        }



       switch1.setOnClickListener(){

            if (switch1.isChecked) {
                noti=true
                vibrator.vibrate(500) // vibracion
                imagenlapiz.setColorFilter(Color.BLUE)
                calendarView.visibility=View.VISIBLE
            }
            else
            {
                noti=false
                imagenlapiz.setColorFilter(Color.BLACK)
                calendarView.visibility=View.GONE
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

                        sonido()
                        notificacion(textoIngresado.toString())
                        Toast.makeText(this, "notificación: $textoIngresado", Toast.LENGTH_SHORT).show()
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


        spinertiempo.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                var horas:Int=0
                // Maneja la selección del usuario aquí
                tiemposeleccionado = parent.getItemAtPosition(position).toString()

                fechaAlarmaP=-1

                if (tiemposeleccionado=="15 minutos"){
                    fechaAlarmaP=fechaAlarma + 900000

                }
                if (tiemposeleccionado=="30 minutos") {
                    fechaAlarmaP=fechaAlarma + 1800000

                }
                if (tiemposeleccionado=="1 hora") {
                    horas=1
                    fechaAlarmaP=fechaAlarma + horas * 3600 * 1000

                }
                if (tiemposeleccionado=="2 horas") {
                    horas=2
                    fechaAlarmaP=fechaAlarma + horas * 3600 * 1000


                }
                if (tiemposeleccionado=="3 horas") {
                    horas=3
                    fechaAlarmaP=fechaAlarma + horas * 3600 * 1000


                }
                if (tiemposeleccionado=="4 horas") {
                    horas=4
                    fechaAlarmaP=fechaAlarma + horas * 3600 * 1000


                }
                if (tiemposeleccionado=="5 horas") {
                    horas=5
                    fechaAlarmaP=fechaAlarma + horas * 3600 * 1000


                }

            }


            override fun onNothingSelected(parent: AdapterView<*>) {
                // Opcional: Si no hay nada seleccionado

            }
        })

    }

    private fun notificacion(texto:String) {


        createChannel(tarea.id)


        val intent = Intent(applicationContext, NotificationReceiver()::class.java)  // el NOTIFICATION_ID es unico puedo usar el de BD
        intent.putExtra("texto", tarea.tarea)
        globalVariable = tarea.tarea
        val pendingIntent = PendingIntent.getBroadcast(
            applicationContext,
            tarea.id,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
       // alarmManager.setExact(AlarmManager.RTC_WAKEUP, Calendar.getInstance().timeInMillis , pendingIntent)
        alarmManager.setExact(AlarmManager.RTC_WAKEUP,fechaAlarmaP, pendingIntent)
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
    fun sonido(){
        var toneGenerator =
            ToneGenerator(AudioManager.STREAM_ALARM, 100) // El segundo parámetro es el volumen

        // Lanzar un pitido
        toneGenerator.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 800)
    }

    fun convertirFecha(millis: Long): String {
        val formatter = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = millis
        return formatter.format(calendar.time)
    }

    }


