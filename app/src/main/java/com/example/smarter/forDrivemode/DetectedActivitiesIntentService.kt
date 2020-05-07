package com.example.smarter.forDrivemode
import android.app.IntentService
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.smarter.R
import com.example.smarter.drivemode
import com.example.smarter.notify.extension.vectorToBitmap
import com.example.smarter.timeline_cerate
import com.example.smarter.work.DriveNotefi
import com.google.android.gms.location.ActivityRecognitionResult
import com.google.android.gms.location.DetectedActivity
lateinit var type_of_activity :String
class DetectedActivitiesIntentService : IntentService(TAG) {

    override fun onCreate() {
        super.onCreate()
    }

    override fun onHandleIntent(intent: Intent?) {
        val result = ActivityRecognitionResult.extractResult(intent)

        val detectedActivities = result.probableActivities as ArrayList<*>


        for (activity in detectedActivities){
            ConvertTwoKnow(activity as DetectedActivity)
        }
    }

    private fun ConvertTwoKnow(activity: DetectedActivity)
    {
        type_of_activity= handleUserActivity(activity.type,activity.confidence)
        if (activity.confidence>70&& type_of_activity=="WALKING")//"IN_VEHICLE"
        {sendNotification(10)}

    }





    private fun handleUserActivity(type: Int, confidence: Int) :String{


       return when (type) {
            DetectedActivity.IN_VEHICLE -> "IN_VEHICLE"

            DetectedActivity.RUNNING -> "RUNNING"

            DetectedActivity.STILL -> "STILL"

            DetectedActivity.WALKING -> "WALKING"

           else -> "UNKNOWN"
       }


    }







    companion object {

        val TAG = DetectedActivitiesIntentService::class.java.simpleName
    }

    private fun sendNotification(id: Int) {
        val intent = Intent(applicationContext, timeline_cerate::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        intent.putExtra(DriveNotefi.NOTIFICATION_ID, id)

        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val bitmap = applicationContext.vectorToBitmap(R.drawable.logo11)
        val titleNotification = "Smarter Event"
        val subtitleNotification = "you are in drive state"
        val pendingIntent = PendingIntent.getActivity(applicationContext, 0, intent, 0)
        val notification = NotificationCompat.Builder(applicationContext,
            DriveNotefi.NOTIFICATION_CHANNEL
        )
            .setLargeIcon(bitmap).setSmallIcon(R.drawable.logo11)
            .setContentTitle(titleNotification).setContentText(subtitleNotification)
            .setDefaults(NotificationCompat.DEFAULT_ALL).setContentIntent(pendingIntent).setAutoCancel(true)

        notification.priority = NotificationCompat.PRIORITY_MAX

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notification.setChannelId(DriveNotefi.NOTIFICATION_CHANNEL)

            val ringtoneManager = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val audioAttributes = AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION).build()

            val channel =
                NotificationChannel(DriveNotefi.NOTIFICATION_CHANNEL, DriveNotefi.NOTIFICATION_NAME, NotificationManager.IMPORTANCE_HIGH)

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