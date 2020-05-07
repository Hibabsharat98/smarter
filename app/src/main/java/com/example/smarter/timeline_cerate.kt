package com.example.smarter

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.example.smarter.forTimeline.TimelineData
import com.example.smarter.work.NotifyWork.Companion.NOTIFICATION_ID
import com.example.smarter.work.NotifyWork.Companion.NOTIFICATION_WORK
import com.google.android.material.snackbar.Snackbar.make
import kotlinx.android.synthetic.main.activity_timeline_cerate.*
import java.lang.Exception
/*import java.nio.charset.CodingErrorAction.REPLACE*/
import androidx.work.ExistingWorkPolicy.REPLACE
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.smarter.work.NotifyWork
import java.text.SimpleDateFormat
import java.util.*
import com.google.android.material.snackbar.Snackbar.LENGTH_LONG
import org.json.JSONObject
import java.lang.System.currentTimeMillis
import java.util.Locale.getDefault
import java.util.concurrent.TimeUnit.MILLISECONDS

class
timeline_cerate : AppCompatActivity() {


    var datee: String = "" //Date d/mon/y
    var timee: String = ""//time HH:MM
    var userid= 0
    val url = "https://smarter.arabyhosting.com/api/timeline/createtimeline.php"
    private lateinit var sharedpref: Sharedpref
    override fun onCreate(savedInstanceState: Bundle?) {

        sharedpref = Sharedpref(this)
        userid= sharedpref.loaduserid()
        if (sharedpref.loadNightmodeState() == true) {
            setTheme(R.style.darkTheme)

        } else
            setTheme(R.style.AppTheme)
        if (sharedpref.loaduserid() > 0) {
            val userid= sharedpref.loaduserid()      }


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timeline_cerate)


        userInterface()

        cancelTC.setOnClickListener {
            startActivity(Intent(this, timeline::class.java))
            finish()}


    }

    private fun userInterface() {

        val eventText: EditText = findViewById(R.id.editText) // get text
        var eventemoji = ""//get emoji

       //EMOJI EVENT START
        //Using UNI-CODE

        val emoji = arrayOf(
            "❔ ", "\uD83C\uDF82 ", "\uD83D\uDCD6", "🛠️", "\uD83C\uDF84 ", "\uD83E\uDDA0", "\uD83D\uDC89", "\uD83D\uDC8A",
            "\uD83C\uDF82", "\uD83D\uDC8D", "\uD83C\uDF93", "⚽", "\uD83C\uDFC0", "\uD83C\uDFB9 ", "\uD83C\uDFD6️", "\uD83C\uDFA1",
            "\uD83D\uDD4B", "✏️", "\uD83D\uDCDA", "\uD83D\uDED2", "\uD83D\uDC89", "\uD83D\uDD2C", "⚖️", "\uD83D\uDCBF",
            "\uD83D\uDCBB", "☢️", "\uD83C\uDFE7", "\uD83C\uDCCF", "\uD83C\uDF89")

        val arrayAdapterx = ArrayAdapter(this, android.R.layout.simple_spinner_item, emoji)
        spinner_event.adapter = arrayAdapterx
        spinner_event.onItemSelectedListener = object :

            AdapterView.OnItemSelectedListener
        {
            override fun onNothingSelected(parent: AdapterView<*>?)
            {
                eventemoji = "❔"
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            )
            {
                eventemoji = spinner_event.selectedItem.toString()
            }
           //EMOJI EVENT END
        }




        createTC.setOnClickListener{
            var customCalendar = Calendar.getInstance()
            customCalendar.set(date_p.year, date_p.month, date_p.dayOfMonth, time_p.hour, time_p.minute, 0)

            val customTime = customCalendar.timeInMillis
//            val customTime2 = customCalendar.time.
var date2=date_p.dayOfMonth.toString() +'-'+ (date_p.month+1).toString()+'-'+ date_p.year.toString()+" "+ time_p.hour.toString()+":"+ time_p.minute.toString()
            datee =date2.toString()
            val currentTime = currentTimeMillis()
          timee = currentTime.toString()
            if (customTime > currentTime) {
                val data = Data.Builder().putInt(NOTIFICATION_ID, 0).build()
                val delay = customTime - currentTime
                scheduleNotification(delay, data)

                val titleNotificationSchedule =  getString(R.string.notification_schedule_title)
                val patternNotificationSchedule = getString(R.string.notification_schedule_pattern)
               make(
                   coordinator_l,
                   titleNotificationSchedule + SimpleDateFormat(
                       patternNotificationSchedule, getDefault()
                   ).format(customCalendar.time).toString(),
                   LENGTH_LONG
               ).show()
            } else {
                val errorNotificationSchedule ="error notification"
                make(coordinator_l, errorNotificationSchedule, LENGTH_LONG).show()
            }
              //call DB class
                val data = TimelineData()
                data.txt = eventText.text.toString()
                data.date = datee
                data.emoji = eventemoji
                data.time = timee
            val jsonobj = JSONObject()
            jsonobj.put("date",datee)
            jsonobj.put("time",timee )
            jsonobj.put("text",editText.text )
            jsonobj.put("emoji",eventemoji)
            jsonobj.put("userID",userid )

            //print("the json is :  $jsonobj")
            println("--------------------------------------------")
            println(jsonobj.toString())
            println("--------------------------------------------")
            val que = Volley.newRequestQueue(this@timeline_cerate)
            val req = JsonObjectRequest(
                Request.Method.POST, url, jsonobj,
                Response.Listener {
                    Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, timeline::class.java))
                    finish()

                }, Response.ErrorListener {
                    Toast.makeText(this, "Failure", Toast.LENGTH_SHORT).show()


                })
            que.add(req)

                //saving to data to DB
                //DB
                Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()


        }

    }

    private fun scheduleNotification(delay: Long, data: Data) {
        val notificationWork = OneTimeWorkRequest.Builder(NotifyWork::class.java)
            .setInitialDelay(delay, MILLISECONDS).setInputData(data).build()

         val instanceWorkManager = WorkManager.getInstance(this)
         instanceWorkManager.beginUniqueWork(NOTIFICATION_WORK, REPLACE, notificationWork).enqueue()
    }

}