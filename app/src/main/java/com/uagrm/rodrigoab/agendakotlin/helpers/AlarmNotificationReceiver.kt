package com.uagrm.rodrigoab.agendakotlin.helpers

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v4.app.NotificationCompat
import com.uagrm.rodrigoab.agendakotlin.R
import com.uagrm.rodrigoab.agendakotlin.activities.Formulario

class AlarmNotificationReceiver() : BroadcastReceiver() {

    @RequiresApi(Build.VERSION_CODES.O)
    @Override
    override fun onReceive(context: Context, intent: Intent) {

        var formularioIntent = Intent(context, Formulario::class.java)
        formularioIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        formularioIntent.putExtra("id", intent.extras["id"].toString().toInt())
        formularioIntent.putExtra("color", intent.extras["color"].toString())
        formularioIntent.putExtra("nombre", intent.extras["nombre"].toString())
        formularioIntent.putExtra("lugar", intent.extras["lugar"].toString())
        formularioIntent.putExtra("inicio", intent.extras["inicio"].toString())
        formularioIntent.putExtra("fin", intent.extras["fin"].toString())
        formularioIntent.putExtra("alarma", intent.extras["alarma"].toString())
        formularioIntent.putExtra("descripcion", intent.extras["descripcion"].toString())

        val stackBuilder = TaskStackBuilder.create(context)
        stackBuilder.addNextIntentWithParentStack(formularioIntent)


        var formularioPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)

        // -----------------------------------------------------------------------------------------




        var builder1: NotificationCompat.Builder=NotificationCompat.Builder(context)

        builder1.setAutoCancel(true)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.icono_logo)
                .setColorized(true)
                .setContentTitle(intent.extras["nombre"].toString())
                .setContentText(intent.extras["lugar"].toString())
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(Notification.DEFAULT_ALL)
                .setContentIntent(formularioPendingIntent)
                .setContentInfo(intent.extras["descripcion"].toString())

        var id:Int=intent.extras["id"].toString().toInt()
        var notificationManager: NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(id,builder1.build())

    }
}