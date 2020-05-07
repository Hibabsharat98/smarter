package com.example.smarter.forDrivemode

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.widget.Toast
import com.example.smarter.R
import com.example.smarter.drivemode
import com.google.android.gms.location.ActivityRecognitionClient

class BackgroundDetectedActivitiesService: Service() {

    private lateinit var mIntentService: Intent
    private lateinit var mPendingIntent: PendingIntent
    private lateinit var mActivityRecognitionClient: ActivityRecognitionClient

 //   internal var mBinder: IBinder = LocalBinder()

   // inner class LocalBinder : Binder() {
 //       val serverInstance: BackgroundDetectedActivitiesService
 //           get() = this@BackgroundDetectedActivitiesService
  //  }

    override fun onBind(intent: Intent?): IBinder? {
        return null //mBinder
    }

    override fun onCreate() {

        val notificationId = 98765
        val notificationIntent = Intent(this, drivemode::class.java)
        val pendingIntent = PendingIntent.getActivity(applicationContext, 0, notificationIntent, 0)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannelId = "com.example.activityrecognition"
            val notificationChannelName = "DriveMode"
            val notificationChannel = NotificationChannel(notificationChannelId, notificationChannelName, NotificationManager.IMPORTANCE_DEFAULT)
            notificationChannel.lightColor = Color.BLUE
            notificationChannel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(notificationChannel)
            val notification = Notification.Builder(applicationContext, notificationChannelId).setContentTitle("DriveMode").setContentText("DriveMode start").setSmallIcon(R.drawable.logo11).setContentIntent(pendingIntent).build()
            try {
                startForeground(notificationId, notification)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else {
            val notification = Notification.Builder(applicationContext).setContentTitle("DriveMode").setContentText("DriveMode start collect.").setSmallIcon(
                R.mipmap.ic_launcher).setContentIntent(pendingIntent).build()
            try {
                startForeground(notificationId, notification)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }


        super.onCreate()

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        var thread=Thread{
        while (true){
            mActivityRecognitionClient = ActivityRecognitionClient(this)
            mIntentService = Intent(this,DetectedActivitiesIntentService::class.java)
            mPendingIntent = PendingIntent.getService(this,1,mIntentService,PendingIntent.FLAG_UPDATE_CURRENT)
            requestActivityUpdatesButtonHandler()
            Thread.sleep(5000)

        }}
        thread.start()

       // return Service.START_STICKY
       return super.onStartCommand(intent, flags, startId)
    }


    private fun requestActivityUpdatesButtonHandler() {
        val task = mActivityRecognitionClient?.requestActivityUpdates(
            drivemode.DETECTION_INTERVAL_IN_MILLISECONDS,
            mPendingIntent)


    }


    companion object {
        private val TAG = BackgroundDetectedActivitiesService::class.java?.getSimpleName()
    }


}