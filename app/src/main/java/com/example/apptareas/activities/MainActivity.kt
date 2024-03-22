package com.example.apptareas.activities

import android.Manifest
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.apptareas.NotificationReceiver
import com.example.apptareas.R
import com.example.apptareas.activities.adapter.MainActivityModificar
import com.example.apptareas.activities.adapter.TareasAdapter
import com.example.apptareas.data.providers.Task
import com.example.apptareas.data.providers.TaskDAO
import java.util.Calendar


class MainActivity : AppCompatActivity() {

    companion object {
        val NOTIFICATION_PERMISSION_REQUEST_CODE = 1001
    }


    lateinit var recyclerView: RecyclerView
    lateinit var adapter:TareasAdapter
    lateinit var listaTareas: MutableList<Task>
    lateinit var txtnueva:TextView
    lateinit var btnnueva:ImageButton
    lateinit var btnborratodo:ImageButton
    lateinit var btnborrarealizado:ImageButton
    lateinit var prueba:ImageButton
    lateinit var editText:EditText
    //lateinit var imagenpapelera:ImageButton

    val taskDAO = TaskDAO(this)



    //alerta
    private val CHANNEL_ID = "my_channel"
    private val NOTIFICATION_ID = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


            // Programar la notificación


        //var tarea: Task = Task(1, "Comprar el pan", false)
        //var tarea: Task = Task(1, "Comprar el periódico", true) // para tener algún dato al inicio

    }






    override fun onResume() {
        super.onResume()

        listaTareas = taskDAO.findAll().toMutableList()

        inicializar()
    }


    fun inicializar(){



        recyclerView = findViewById(R.id.recyclerView)
        //imagenpapelera=findViewById(R.id.imagenpapelera)

        adapter = TareasAdapter(listaTareas, { llamarPantallaClick(it) }, {llamarCkeckClick(it)}, {llamarPapeleraClick(it)})

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

       //nueva tarea
        btnnueva=findViewById(R.id.btnnueva)
        //borrar todo
        btnborratodo=findViewById(R.id.btnborratodo)
        btnborrarealizado=findViewById(R.id.btnborrarealizado)
        prueba=findViewById(R.id.prueba)

        prueba.setOnClickListener(){
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

                val builder = AlertDialog.Builder(this)

                // Inflar el layout que contiene el EditText
                val inflater = layoutInflater
                val dialogLayout = inflater.inflate(R.layout.dialog_layout, null)
                val editText = dialogLayout.findViewById<EditText>(R.id.editText)

                // Establecer el título y el mensaje del AlertDialog
                builder.setTitle("Activar notificaciones")
                builder.setMessage("Indica el tiempo en horas para la alerta")

                // Establecer el layout personalizado para el AlertDialog
                builder.setView(dialogLayout)

                // Configurar el botón "Aceptar"
                builder.setPositiveButton("Aceptar") { dialog, which ->
                    // Acción a realizar cuando se presiona el botón Aceptar
                    val textoIngresado = editText.text.toString()
                    // Aquí puedes hacer lo que necesites con el texto ingresado
                    // Por ejemplo, convertirlo a un valor numérico y utilizarlo

                    notificacion(textoIngresado.toInt())
                    Toast.makeText(this, "notificacion a las $textoIngresado horas", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()


                }

                    // Configurar el botón "Cancelar"
                builder.setNegativeButton("Cancelar") { dialog, which ->
                    // Acción a realizar cuando se presiona el botón Cancelar
                    // Por ejemplo, puedes realizar alguna acción o cerrar el diálogo
                    dialog.dismiss()
                }

                // Mostrar AlertDialog
                val dialog: AlertDialog = builder.create()
                dialog.show()




            }
        }

        btnnueva.setOnClickListener(){
           // LLamar a la otra pantalla
           val intent = Intent(this, MainActivityinsertar::class.java)
            startActivity(intent)
        }

        btnborratodo.setOnClickListener(){

            // Crear AlertDialog
            val builder = AlertDialog.Builder(this)

            // Establecer el título y el mensaje
            builder.setTitle("Borrar agenda")
            builder.setMessage("¿Esta seguro que desea eliminar la agenda completa?")

            builder.setPositiveButton("Aceptar") { dialog, which ->
                // Acción a realizar cuando se presiona el botón Aceptar

                taskDAO.deleteAll()
                Toast.makeText(this, "Agenda borrada", Toast.LENGTH_SHORT).show()
                onResume()

                dialog.dismiss()
            }

            // Agregar botón negativo
            builder.setNegativeButton("Cancelar") { dialog, which ->
                // Acción a realizar cuando se presiona el botón Cancelar
                // Por ejemplo, puedes realizar alguna acción o cerrar el diálogo
                dialog.dismiss()
            }

            // Mostrar AlertDialog
            val dialog: AlertDialog = builder.create()
            dialog.show()
        }


        btnborrarealizado.setOnClickListener(){
            // Crear AlertDialog
            val builder = AlertDialog.Builder(this)

            // Establecer el título y el mensaje
            builder.setTitle("Borrar tareas realizadas")
            builder.setMessage("¿Desea borrar las tareas realizadas?")

            builder.setPositiveButton("Aceptar") { dialog, which ->
                // Acción a realizar cuando se presiona el botón Aceptar

                taskDAO.deleteRealizados()
                Toast.makeText(this, "Taras realizadas borradas", Toast.LENGTH_SHORT).show()
                onResume()

                dialog.dismiss()
            }

            // Agregar botón negativo
            builder.setNegativeButton("Cancelar") { dialog, which ->
                // Acción a realizar cuando se presiona el botón Cancelar
                // Por ejemplo, puedes realizar alguna acción o cerrar el diálogo
                dialog.dismiss()
            }

            // Mostrar AlertDialog
            val dialog: AlertDialog = builder.create()
            dialog.show()
        }

    }

    private fun llamarPapeleraClick(it: Int) {

        var task: Task = listaTareas[it]
        taskDAO.delete(task)
        listaTareas.removeAt(it)
        adapter.notifyDataSetChanged()
    }

    private fun llamarCkeckClick(it: Int) {
        var task: Task = listaTareas[it]

        task.hecho = !task.hecho

        taskDAO.update(task)

        adapter.notifyItemChanged(it)
    }

    private fun llamarPantallaClick(position: Int) {

        var task: Task = listaTareas[position]

        val intent = Intent(this, MainActivityModificar::class.java)
        intent.putExtra("ID_TABLA",task.id)

        startActivity(intent)

    }
    private fun notificacion(horas:Int) {

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

        createChannel()


        val intent = Intent(applicationContext, NotificationReceiver::class.java)  // el NOTIFICATION_ID es unico puedo usar el de BD
        val pendingIntent = PendingIntent.getBroadcast(
            applicationContext,
            NOTIFICATION_ID,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, Calendar.getInstance().timeInMillis + horas * 3600 * 1000, pendingIntent)
    }



    private fun createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "prueba",
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

