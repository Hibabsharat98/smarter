package com.example.smarter

import android.app.Activity
import android.app.ProgressDialog
import android.content.BroadcastReceiver
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.location.Location
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.smarter.forDrivemode.BackgroundDetectedActivitiesService
import com.example.smarter.forNote.DateTime
import com.example.smarter.forNote.NoteData
import com.example.smarter.work.DriveNotefi
import com.example.smarter.work.NotifyWork
import com.google.android.gms.location.LocationServices
import com.example.smarter.forMap.LocationMonitoringService
import com.example.smarter.work.MapNotefi
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.activity_note_cerate.*
import java.io.ByteArrayOutputStream
import java.util.HashMap
import java.util.concurrent.TimeUnit
import kotlin.properties.Delegates


class note_cerate : AppCompatActivity() {

    companion object {
        val BROADCAST_DETECT_LOCATION = "MAP_intent"
        val BROADCAST_DETECTed_LOCATION = "MAP_intent2"
        internal  lateinit var broadcastReceiverN: BroadcastReceiver
    }

    lateinit var location :Location
    var lat :Double=0.0
    var long:Double=0.0
    lateinit var location2 : Location
    private lateinit var sharedpref: Sharedpref
    private lateinit var editText3: EditText
    private lateinit var create: Button
    private var imageUri: Uri? = null
    private val cameraRequestCode = 124
    private val imageCaptureCode = 123
    private val imagePickCode = 1000
    private val imageRequestCode =1001
    var userid= 0
    var image: ImageView? = null
    var choose: ImageButton? = null
    var upload: Button? = null
    var PICK_IMAGE_REQUEST = 111
    var URL = "https://smarter.arabyhosting.com/api/note/create.php"
    var bitmap: Bitmap? = null
    var progressDialog: ProgressDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        sharedpref = Sharedpref(this)
        userid= sharedpref.loaduserid()
        if (sharedpref.loadNightmodeState() == true) {
            setTheme(R.style.darkTheme)

        } else
            setTheme(R.style.AppTheme)
        if (sharedpref.loaduserid() > 0) {
            val userid= sharedpref.loaduserid() }
        lat=intent.getDoubleExtra("lat", 0.0)
        long=intent.getDoubleExtra("long", 0.0)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_cerate)
        image = findViewById<View>(R.id.imageCaptured) as ImageView
        choose = findViewById<View>(R.id.getimage) as ImageButton
        upload = findViewById<View>(R.id.create) as Button

        //init views
        /*  if (intent.hasExtra("lat")) {

              location2!!.latitude = intent.getDoubleExtra("lat", 0.0)
              location2!!.longitude = intent.getDoubleExtra("long", 0.0)
              Toast.makeText(this, "SAVED", Toast.LENGTH_SHORT).show()
          }*/

        //  locationK.latitude!=0.0
        // locationK.longitude!=0.0






        addlocation.setOnClickListener {
            startActivity(Intent(this, MapsActivity::class.java))
        }
        editText3 = findViewById(R.id.editText3)
        create = findViewById(R.id.create)

        create.setOnClickListener { startActivityTransitionService()

            saveData()

        }


        //open gallary
        getimage.setOnClickListener {
            //Check runtime permission
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_DENIED
                ) {
                    //Permission denied
                    val galleryPermission =
                        arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    //to show request permission message:
                    requestPermissions(galleryPermission, imageRequestCode)
                } else {
                    //already granted
                    pickFromGallery()
                }
            } else {
                //system less than marshmallow
                pickFromGallery()
            }
        }
        //open pic image
        getcamra.setOnClickListener {
            //If system os is Marshmallow or above, we need to request runtime permission
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(android.Manifest.permission.CAMERA) ==
                    PackageManager.PERMISSION_DENIED ||
                    checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_DENIED
                ) {
                    //Permission denied
                    val cameraPermission = arrayOf(
                        android.Manifest.permission.CAMERA,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
                    //to show request permission message:
                    requestPermissions(cameraPermission, cameraRequestCode)
                } else//permission already grant
                {
                    openCamera()
                }
            } else//system os is less than Marshmallow
            {
                openCamera()
            }
        }
        cancel.setOnClickListener {
            startActivity(Intent(this, note::class.java))
            finish()
        }


        fun saveArray()
        {

            val sharedPreferences=getSharedPreferences("shardp",Context.MODE_PRIVATE)
            val iditor = sharedPreferences.edit()

        }



        /*  broadcastReceiverN = object : BroadcastReceiver() {
              override fun onReceive(context: Context, intent: Intent) {

                      if (intent.action == note_cerate.BROADCAST_DETECTed_LOCATION) {
                          chekmap =intent.getBooleanExtra("check" , false)
                          location2!!.longitude = intent.getDoubleExtra("long",0.0)
                          location2!!.latitude = intent.getDoubleExtra("lat", 0.0)




                  }
              }
          }*/

        /*   broadcastReceiverN = object : BroadcastReceiver() {
               override fun onReceive(context: Context, intent: Intent) {

                   if (intent.action == note_cerate.BROADCAST_DETECT_LOCATION) {
                       location!!.longitude = intent.getDoubleExtra("myLong",0.0)
                       location!!.latitude = intent.getDoubleExtra("myLat", 0.0)
                       Toast.makeText(this@note_cerate,"Tacke informayion",Toast.LENGTH_LONG).show()
                   }
                   if(location.distanceTo(locatio )<1000)
                   {


                   }
                   else{Toast.makeText(this@note_cerate,"AWAY",Toast.LENGTH_LONG).show()}
               }
           }*/












    }

    private fun pickFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, imagePickCode)
    }

    private fun openCamera() {
        val conValues = ContentValues()
        conValues.put(MediaStore.Images.Media.TITLE, "New Picture")
        conValues.put(MediaStore.Images.Media.DESCRIPTION, "From The Camera")
        imageUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, conValues)
        //camera intent
        val camIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        camIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
        startActivityForResult(camIntent, imageCaptureCode)
    }

    //handle permission result, called when user press allow or deny permission
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        // super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            cameraRequestCode -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //user was grant permission
                    openCamera()
                } else {
                    //user denied permission :
                    Toast.makeText(this, "Camera Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
            imageRequestCode -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //user was grant permission
                    pickFromGallery()
                } else {
                    //user denied permission :
                    Toast.makeText(this, "Gallery Permission denied", Toast.LENGTH_SHORT).show()
                }

            }
        }
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        //called after capture Image
        println("----called after capture Image----")
        if (resultCode == Activity.RESULT_OK && requestCode == imageCaptureCode) {
            println("-rr:$requestCode------icc:-$imageCaptureCode")
            println("----move captured image to imageVie----")
            imageCaptured.setImageURI(imageUri)
            println("----Imageurl:$imageUri----")

            if ( imageUri != null ) {
                val filePath = imageUri
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePath)

                    imageCaptured!!.setImageBitmap(bitmap)
                    bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePath)

                    image!!.setImageBitmap(bitmap)

                } catch (e: Exception) {
//                println("image catch start")

                    e.printStackTrace()
//                println("image catch end")

                }
            }

        }
//        Activity.RESULT_OK
        if (resultCode == Activity.RESULT_OK && requestCode == imagePickCode) {
            //move captured image to imageView
            imageCaptured.setImageURI(data?.data)
            if ( data != null && data.data != null) {
                val filePath = data.data
                try {
//                println("image try start")

                    //getting image from gallery
                    bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePath)

                    //Setting image to ImageView
                    imageCaptured!!.setImageBitmap(bitmap)
                    bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePath)

                    //Setting image to ImageView
                    image!!.setImageBitmap(bitmap)
//                println("image try end")

                } catch (e: Exception) {
//                println("image catch start")

                    e.printStackTrace()
//                println("image catch end")

                }
            }

        }

        if (data != null) {
//            println("----4----")
//
//            println(data.data)
        }
//        Activity.RESULT_OK
//requestCode == imageRequestCode && resultCode == 1001 &&
    }

    private fun saveData() {

        val noteData = NoteData()
        val date = DateTime()
        noteData.noteDate = date.dateTime()
        noteData.noteText = editText3.text.toString()
        //saving to Database
//        create!!.setOnClickListener {
//
//        }
        //            progressDialog = ProgressDialog(this@note_cerate)
//            progressDialog!!.setMessage("Uploading, please wait...")
//            progressDialog!!.show()

        //converting image to base64 string
        var baos = ByteArrayOutputStream()
//            try {
        bitmap!!.compress(Bitmap.CompressFormat.JPEG, 100, baos)
//            }
//            catch (e: Exception){
//println(e)
//
//            }

        val imageBytes = baos.toByteArray()
        val imageString =Base64.encodeToString(imageBytes, Base64.DEFAULT)

        //sending image to server
        val request: StringRequest = object : StringRequest(
            Method.POST,
            URL,
            Response.Listener { s ->
//                    progressDialog!!.dismiss()
                if (s == "true") {
                    Toast.makeText(
                        this@note_cerate,
                        "Uploaded Successful",
                        Toast.LENGTH_LONG
                    ).show()
                    println("Uploaded Successful")
                } else {
                    Toast.makeText(
                        this@note_cerate,
                        "Some error occurred!",
                        Toast.LENGTH_LONG
                    ).show()
                    //  println(s.toString())
                    println("Uploaded error")

                }
            },
            Response.ErrorListener { volleyError ->
                Toast.makeText(this@note_cerate, "Some error occurred -> $volleyError",
                    Toast.LENGTH_LONG).show()
//                    println("--------------------------------------------")
//                    println(volleyError.toString())
//                    println("--------------------------------------------")
            }) {
            //adding parameters to send
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {

                val parameters: MutableMap<String, String> =
                    HashMap()
                parameters["text"] = editText3.text.toString()
                parameters["location"] = lat.toString()+","+long.toString()
                parameters["userID"] = userid.toString()
//                parameters["imgtype"] = "3"
                parameters["image"] = imageString
                return parameters
            }
        }
        val rQueue = Volley.newRequestQueue(this@note_cerate)
        rQueue.add(request)
        startActivity(Intent(this, note::class.java))
        finish()


    }

    override fun onBackPressed() {
        startActivity(Intent(this, note::class.java))
        finish()
    }


    private fun startActivityTransitionService() {

        val intent = Intent(applicationContext, LocationMonitoringService::class.java)
        stopService(intent)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            startForegroundService(intent)
        else
            startService(intent)
    }



    private fun scheduleNotification(delay: Long, data: Data) {
        val notificationWork = OneTimeWorkRequest.Builder(MapNotefi::class.java)
            .setInitialDelay(delay, TimeUnit.MILLISECONDS).setInputData(data).build()

        val instanceWorkManager = WorkManager.getInstance(this)
        instanceWorkManager.beginUniqueWork(
            NotifyWork.NOTIFICATION_WORK,
            ExistingWorkPolicy.REPLACE, notificationWork).enqueue()
    }


}