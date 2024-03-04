package com.example.apptareas.data.providers

import com.example.apptareas.utils.DatabaseManager

class Task (var id: Int, var tarea: String, var hecho: Boolean) {

    companion object {
        const val TABLE_NAME = "tareas"
        const val COLUMN_NAME_TASK = "tarea"
        const val COLUMN_NAME_DONE = "hecho"


        val COLUMN_NAMES = arrayOf(
            DatabaseManager.COLUMN_NAME_ID,
            COLUMN_NAME_TASK,
            COLUMN_NAME_DONE
        )
    }

    //Para imprimir
    override fun toString(): String {
        return "$id -> Task: $tarea - $hecho"
    }
}