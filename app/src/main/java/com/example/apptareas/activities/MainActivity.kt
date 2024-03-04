package com.example.apptareas.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.apptareas.R
import com.example.apptareas.activities.adapter.TareasAdapter
import com.example.apptareas.data.providers.Task
import com.example.apptareas.data.providers.TaskDAO
import org.w3c.dom.Text


class MainActivity : AppCompatActivity() {


    lateinit var recyclerView: RecyclerView
    lateinit var adapter:TareasAdapter
    lateinit var listaTareas: List<Task>
    lateinit var txtnueva:TextView
    lateinit var imagenbotonadd: AppCompatButton

    val taskDAO = TaskDAO(this)



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)




        //var tarea: Task = Task(1, "Comprar el pan", false)
        //var tarea: Task = Task(1, "Comprar el periódico", true) // para tener algún dato al inicio

        listaTareas = taskDAO.findAll()

        inicializar()
    }


    fun inicializar(){
        recyclerView = findViewById(R.id.recyclerView)

        adapter = TareasAdapter(listaTareas, { llamarPantallaClick(it) })

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

       //nueva tarea
        txtnueva=findViewById(R.id.txtnueva)
        imagenbotonadd=findViewById(R.id.txtnueva)

        imagenbotonadd.setOnClickListener(){
           // LLamar a la otra pantalla
           val intent = Intent(this, MainActivityinsertar::class.java)
            startActivity(intent)
        }


    }

    private fun llamarPantallaClick(position: Int) {
        var task: Task = listaTareas[position]

        task.hecho = !task.hecho

        taskDAO.update(task)

        adapter.notifyItemChanged(position)
    }
}