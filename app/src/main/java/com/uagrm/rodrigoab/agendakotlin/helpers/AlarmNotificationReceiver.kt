package com.uagrm.rodrigoab.agendakotlin.helpers

import android.app.Notification
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v4.app.NotificationCompat
import com.uagrm.rodrigoab.agendakotlin.R

class AlarmNotificationReceiver() : BroadcastReceiver() {

    @RequiresApi(Build.VERSION_CODES.O)
    @Override
    override fun onReceive(context: Context, intent: Intent) {
        var builder1: NotificationCompat.Builder=NotificationCompat.Builder(context)

        builder1.setAutoCancel(true)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_check)
                .setColorized(true)
                .setContentTitle(intent.extras["titulo"].toString())
                .setContentText(intent.extras["lugar"].toString())
                .setDefaults(Notification.DEFAULT_ALL)
                //.setContentInfo("INFOO") es algo chiquitito q va debajo de la hora

        var id:Int=intent.extras["id"].toString().toInt()
        var notificationManager: NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(id,builder1.build())

    }
}