package com.example.apptareas.activities.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.apptareas.R
import com.example.apptareas.data.providers.Task

//  *********************************************** ADAPTER  ********************************************************
class TareasAdapter(private var dataSet: List<Task> =listOf(), val onClickListener:(Int)->Unit) :
    RecyclerView.Adapter<TareasAdapter.MiViewHolder>() {

    class MiViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView
        val checkhecho: CheckBox


        init {
            // Define click listener for the ViewHolder's View
            textView = view.findViewById(R.id.txttarea)  // hace referencia el textview que esta en item_tareas
            checkhecho = view.findViewById(R.id.checkhecho)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): MiViewHolder {

        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_tarea, viewGroup, false) // item_tarea hace referencia al xml de item_tarea.xml

        return MiViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: MiViewHolder, position: Int) {

        viewHolder.itemView.setOnClickListener{onClickListener(position)} // capturamos el click del control

        val tarea:Task = dataSet[position]

       // val drawable = viewHolder.textView.context.getDrawable(horoscopo.image)

        // a mi variable viewHolder le asigno al xml de la imagen y del text los valores
        viewHolder.textView.text = tarea.tarea
        //viewHolder.imageView.setImageDrawable(drawable)

        viewHolder.checkhecho.isChecked = tarea.hecho

    }


    override fun getItemCount() = dataSet.size

    fun updateData(list:List<Task>){
        dataSet=list
        notifyDataSetChanged()
    }

}


