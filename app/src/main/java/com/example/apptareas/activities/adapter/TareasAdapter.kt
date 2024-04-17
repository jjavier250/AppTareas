package com.example.apptareas.activities.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.apptareas.R
import com.example.apptareas.data.providers.Task

//  *********************************************** ADAPTER  ********************************************************
class TareasAdapter(private var dataSet: List<Task> =listOf(), val onClickListener:(Int)->Unit,
                    private val onclickcheck:(position:Int)->Unit,private val onclickpapelera:(position:Int)->Unit) :
    RecyclerView.Adapter<TareasAdapter.MiViewHolder>() {

    class MiViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txttarea: TextView
        val checkhecho: CheckBox
        val imagenpapelera:ImageButton

        //val txtidtarea : TextView


        init {
            // Define click listener for the ViewHolder's View
            txttarea = view.findViewById(R.id.txttarea)  // hace referencia el textview que esta en item_tareas
            checkhecho = view.findViewById(R.id.checkhecho)
            imagenpapelera=view.findViewById(R.id.imagenpapelera)
            //txtidtarea=view.findViewById(R.id.txtidtarea)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): MiViewHolder {

        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_tarea, viewGroup, false) // item_tarea hace referencia al xml de item_tarea.xml

        return MiViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: MiViewHolder, position: Int) {

        viewHolder.itemView.setOnClickListener{onClickListener(position)} // capturamos el click del control
        viewHolder.imagenpapelera.setOnClickListener{onclickpapelera(position)} // capturamos el click del papelera
        viewHolder.checkhecho.setOnClickListener{onclickcheck(position)}  // capturamos click de check

        val tarea:Task = dataSet[position]

       // val drawable = viewHolder.textView.context.getDrawable(image)
        // a mi variable viewHolder le asigno al xml de la imagen y del text los valores

        viewHolder.txttarea.text =  tarea.tarea
        //viewHolder.imageView.setImageDrawable(drawable)
        viewHolder.checkhecho.isChecked = tarea.hecho


        if (dataSet[position].noti==true){
           //viewHolder.itemView.setBackgroundColor(Color.CYAN)
            viewHolder.itemView.setBackgroundColor(Color.argb(35, 0, 255, 255))
        }

    }


    override fun getItemCount() = dataSet.size

    fun updateData(list:List<Task>){
        dataSet=list
        notifyDataSetChanged()
    }

}


