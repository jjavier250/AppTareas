package com.example.apptareas

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.apptareas.R
import com.example.apptareas.activities.MainActivity

var globalVariable: String = ""
class NotificationReceiver() : BroadcastReceiver() {

    companion object{
        const val NOTIFICATION_ID = 1

    }

     var textonoti:String="Tienes alguna tarea pendiente"
    override fun onReceive(context: Context, p1: Intent?) {
        val message=p1?.getStringExtra(globalVariable)
        createSimpleNotification(context,globalVariable)
    }

    private fun createSimpleNotification(context: Context,textobignoti:String) {
        Log.i("NOTI", "Se ha recibido una notificación")
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val flag = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0
        val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, intent, flag)

        val notification = NotificationCompat.Builder(context, "prueba")
            .setSmallIcon(R.drawable.portapapeles)
            .setContentTitle("LA APP DE JAVI TIENE UNA NOTI")
            .setContentText(textonoti)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(textobignoti)
            )
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(NOTIFICATION_ID, notification)
    }
}