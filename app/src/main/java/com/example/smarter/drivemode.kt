package com.example.smarter

import android.app.IntentService
import android.content.*
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.example.smarter.forDrivemode.BackgroundDetectedActivitiesService
import com.example.smarter.work.DriveNotefi
import com.example.smarter.work.NotifyWork

import kotlinx.android.synthetic.main.activity_drivemode.*
import java.util.concurrent.TimeUnit
import kotlin.properties.Delegates


class drivemode : AppCompatActivity() {
    private val TAG = drivemode::class.java.simpleName
    internal lateinit var broadcastReceiver: BroadcastReceiver
    var userid= 0

    lateinit var type :String
    var confidence by Delegates.notNull<Int>()

    companion object {

        val BROADCAST_DETECTED_ACTIVITY = "activity_intent"

        internal val DETECTION_INTERVAL_IN_MILLISECONDS: Long = 5000

        val CONFIDENCE = 70
    }
    private lateinit var sharedpref: Sharedpref
    override fun onCreate(savedInstanceState: Bundle?) {

        sharedpref = Sharedpref(this)
        userid= sharedpref.loaduserid()

        if (sharedpref.loadNightmodeState() == true) {
            setTheme(R.style.darkTheme)

        }
        else
            setTheme(R.style.AppTheme)
        if (sharedpref.loaduserid() > 0) {
            val userid= sharedpref.loaduserid()      }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drivemode)
        loadData()

        disabledrivemode.setOnClickListener {
            startActivity(Intent(this, deivemode_disable::class.java)) }


        switchdrivemode .setOnCheckedChangeListener { btn, isChecked ->

            if (isChecked==true){startActivityTransitionService()
                seveData() }
            else{StopTracking()
                seveData()
            }

        }

      

    }

    private fun loadData() {
        val sharedPreferences=getSharedPreferences("shardp",Context.MODE_PRIVATE)
        val saveBoolean =sharedPreferences.getBoolean("BOOLEAN_KEY",false)
        switchdrivemode.isChecked=saveBoolean
    }

    private fun seveData() {
        val sharedPreferences=getSharedPreferences("shardp",Context.MODE_PRIVATE)
        val iditor = sharedPreferences.edit()
        iditor.apply{putBoolean("BOOLEAN_KEY",switchdrivemode.isChecked)}.apply()

    }

    private fun StopTracking(){
        val intent = Intent(this, BackgroundDetectedActivitiesService::class.java)
        stopService(intent)
    }
    override fun onPause() {
        super.onPause()
        // LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver)
//        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver,
  //          IntentFilter(BROADCAST_DETECTED_ACTIVITY)

    }
    override fun onResume() {
        super.onResume()

//        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver,
  //          IntentFilter(BROADCAST_DETECTED_ACTIVITY))

    }
    private fun startActivityTransitionService() {
        val intent = Intent(applicationContext, BackgroundDetectedActivitiesService::class.java)
        stopService(intent)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            startForegroundService(intent)
        else
            startService(intent)
    }


    private fun scheduleNotification(delay: Long, data: Data) {
        val notificationWork = OneTimeWorkRequest.Builder(DriveNotefi::class.java)
            .setInitialDelay(delay, TimeUnit.MILLISECONDS).setInputData(data).build()

        val instanceWorkManager = WorkManager.getInstance(this)
        instanceWorkManager.beginUniqueWork(
            NotifyWork.NOTIFICATION_WORK,
            ExistingWorkPolicy.REPLACE, notificationWork).enqueue()
    }

    override fun onBackPressed() {
        startActivity(Intent(this, mainscrren::class.java))
        finish()
    }
}
