package com.example.smarter.forMap

import android.Manifest
import android.app.*
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.example.smarter.R
import com.example.smarter.work.DriveNotefi
import com.example.smarter.work.NotifyWork
import java.util.concurrent.TimeUnit

class LocationMonitoringService : Service(), GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{
    internal lateinit  var mLocationClient: GoogleApiClient
    internal var mLocationRequest = LocationRequest()
    internal var mLastLocation: Location? = null
    internal lateinit  var mFusedLocationClient: FusedLocationProviderClient



    internal var mLocationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val locationList = locationResult.locations
            if (locationList.size > 0) {
                //The last location in the list is the newest
                val location = locationList[locationList.size - 1]
                Log.i("MapsActivity", "Location: " + location.latitude + " " + location.longitude)
                mLastLocation = location
               // broadcastActivity(1)

                if (mLastLocation != null) {
                    Log.d(TAG, "== location != null")




                    //Send result to activities
                    sendMessageToUI(mLastLocation!!.latitude.toString(), mLastLocation!!.longitude.toString())
                }
                var intent=Intent(this@LocationMonitoringService,NotifyMap::class.java)
                intent.putExtra("mylong", location.longitude)
                intent.putExtra("mylat",location.latitude)
                startService(intent)
            }
        }
    }

    private fun createNotificationChannel(channelId: String, channelName: String): String{
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val description = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, name, importance)
            channel.description = description
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            val notificationManager = getSystemService(NotificationManager::class.java)

            notificationManager!!.createNotificationChannel(channel)
        }
        return channelId
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "ENTRA onCreate")
        val channelId = createNotificationChannel("my_service", "My Background Service")

        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle("Tracking")
            .setContentText("Traking your current position")
            .setSmallIcon(R.drawable.logo11)
            .build()
        //val notificationIntent =  Intent(this, ExampleActivity::class.java)
        //val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0)
        //notification.setLatestEventInfo(this, getText(R.string.notification_title), getText(R.string.notification_message), pendingIntent)
        startForeground(Constants.ONGOING_NOTIFICATION_ID, notification);
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {


        mLocationClient = GoogleApiClient.Builder(this)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(LocationServices.API)
            .build()

        mLocationRequest.setInterval(Constants.LOCATION_INTERVAL)
        mLocationRequest.setFastestInterval(Constants.FASTEST_LOCATION_INTERVAL)


        val priority = LocationRequest.PRIORITY_HIGH_ACCURACY //by default
        //PRIORITY_BALANCED_POWER_ACCURACY, PRIORITY_LOW_POWER, PRIORITY_NO_POWER are the other priority modes


        mLocationRequest.priority = priority
        mLocationClient.connect()

        Log.d(TAG, "mFusedLocationClient")
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)


        //Make it stick to the notification panel so it is less prone to get cancelled by the Operating System.
        return Service.START_STICKY
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    /*
     * LOCATION CALLBACKS
     */
    override fun onConnected(dataBundle: Bundle?) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            Log.d(TAG, "== Error On onConnected() Permission not granted")
            //Permission not granted by user so cancel the further execution.

            return
        }
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper())


        Log.d(TAG, "Connected to Google API")
    }

    /*
     * Called by Location Services if the connection to the
     * location client drops because of an error.
     */
    override fun onConnectionSuspended(i: Int) {
        Log.d(TAG, "Connection suspended")
    }

    private fun sendMessageToUI(lat: String, lng: String) {

        Log.d(TAG, "Sending info...")

        val intent = Intent(ACTION_LOCATION_BROADCAST)
        intent.putExtra(EXTRA_LATITUDE, lat)
        intent.putExtra(EXTRA_LONGITUDE, lng)
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
    }

    override fun onConnectionFailed(connectionResult: ConnectionResult) {
        Log.d(TAG, "Failed to connect to Google API")

    }

    companion object {

        private val TAG = LocationMonitoringService::class.java!!.getSimpleName()

        val ACTION_LOCATION_BROADCAST = LocationMonitoringService::class.java!!.getName() + "LocationBroadcast"
        val EXTRA_LATITUDE = "extra_latitude"
        val EXTRA_LONGITUDE = "extra_longitude"
    }
    private fun scheduleNotification(delay: Long, data: Data) {
        val notificationWork = OneTimeWorkRequest.Builder(DriveNotefi::class.java)
            .setInitialDelay(delay, TimeUnit.MILLISECONDS).setInputData(data).build()

        val instanceWorkManager = WorkManager.getInstance(this)
        instanceWorkManager.beginUniqueWork(
            NotifyWork.NOTIFICATION_WORK,
            ExistingWorkPolicy.REPLACE, notificationWork).enqueue()
    }

    private fun broadcastActivity(A:Int) {

     //   val intent = Intent(note_cerate.BROADCAST_DETECT_LOCATION)
     //   intent.putExtra("myLong" , locationG.longitude )
     //   intent.putExtra("myLat",locationG.latitude)
     //   LocalBroadcastManager.getInstance(this).sendBroadcast(intent)


    }




}
