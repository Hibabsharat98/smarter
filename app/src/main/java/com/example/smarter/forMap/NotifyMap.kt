package com.example.smarter.forMap

import android.app.IntentService
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.location.Location
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.smarter.*
import com.example.smarter.notify.extension.vectorToBitmap
import com.example.smarter.work.MapNotefi

class NotifyMap: IntentService("NotifyMap") {
    lateinit var myloc:Location
    override fun onHandleIntent(intent: Intent?) {

        myloc.longitude=intent!!.getDoubleExtra("mylong",0.0)
        myloc.latitude=intent!!.getDoubleExtra("mylat",0.0)

       // if (myloc.distanceTo(locationK)<5)
        //{sendNotification(10)}



    }

    private fun sendNotification(id: Int) {
        val intent = Intent(applicationContext, timeline_cerate::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        intent.putExtra(MapNotefi.NOTIFICATION_ID, id)

        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val bitmap = applicationContext.vectorToBitmap(R.drawable.logo11)
        val titleNotification = "Smarter Event"
        val subtitleNotification = "Note GPS "
        val pendingIntent = PendingIntent.getActivity(applicationContext, 0, intent, 0)
        val notification = NotificationCompat.Builder(applicationContext,
            MapNotefi.NOTIFICATION_CHANNEL
        )
            .setLargeIcon(bitmap).setSmallIcon(R.drawable.logo11)
            .setContentTitle(titleNotification).setContentText(subtitleNotification)
            .setDefaults(NotificationCompat.DEFAULT_ALL).setContentIntent(pendingIntent).setAutoCancel(true)

        notification.priority = NotificationCompat.PRIORITY_MAX

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notification.setChannelId(MapNotefi.NOTIFICATION_CHANNEL)

            val ringtoneManager = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val audioAttributes = AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION).build()

            val channel =
                NotificationChannel(MapNotefi.NOTIFICATION_CHANNEL, MapNotefi.NOTIFICATION_NAME, NotificationManager.IMPORTANCE_HIGH)

            channel.enableLights(true)
            channel.lightColor = Color.RED
            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
            channel.setSound(ringtoneManager, audioAttributes)
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(id, notification.build())
    }


}