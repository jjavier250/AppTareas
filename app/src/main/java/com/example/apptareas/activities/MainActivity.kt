package com.example.apptareas.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.apptareas.R
import com.example.apptareas.activities.adapter.MainActivityModificar
import com.example.apptareas.activities.adapter.TareasAdapter
import com.example.apptareas.data.providers.Task
import com.example.apptareas.data.providers.TaskDAO
import org.w3c.dom.Text


class MainActivity : AppCompatActivity() {


    lateinit var recyclerView: RecyclerView
    lateinit var adapter:TareasAdapter
    lateinit var listaTareas: MutableList<Task>
    lateinit var txtnueva:TextView
    lateinit var btnnueva:ImageButton
    lateinit var btnborratodo:ImageButton
    lateinit var btnborrarealizado:ImageButton
    //lateinit var imagenpapelera:ImageButton


    val taskDAO = TaskDAO(this)



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


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
}