package com.example.apptareas.data.providers

import com.example.apptareas.utils.DatabaseManager

class Task (var id: Int, var tarea: String, var hecho: Boolean,var descripcion:String,var diaSemana:String,var noti:Boolean) {

    companion object {
        const val TABLE_NAME = "tareas"
        const val COLUMN_NAME_TASK = "tarea"
        const val COLUMN_NAME_DONE = "hecho"
        const val COLUMN_NAME_DESCRIPCION="descripcion"
        const val COLUMN_NAME_DIASEMANA="diaSemana"
        const val COLUMN_NAME_NOTI="noti"


        val COLUMN_NAMES = arrayOf(
            DatabaseManager.COLUMN_NAME_ID,
            COLUMN_NAME_TASK,
            COLUMN_NAME_DONE,
            COLUMN_NAME_DESCRIPCION,
            COLUMN_NAME_DIASEMANA,
            COLUMN_NAME_NOTI
        )
    }

    //Para imprimir
    override fun toString(): String {
        return "$id -> Task: $tarea - $hecho"
    }
}