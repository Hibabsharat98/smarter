package com.example.smarter

import android.annotation.SuppressLint
import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.ProgressBar
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smarter.forTimeline.TimelineAdapter
import com.example.smarter.forTimeline.TimelineData
import kotlinx.android.synthetic.main.activity_timeline.*
import org.json.JSONObject
import java.net.URL


class timeline : AppCompatActivity() {
    var dataList = ArrayList<HashMap<String, String>>()
    private  var lineList = ArrayList<TimelineData>()
    private lateinit var sharedpref: Sharedpref
    var userid= 0
    @SuppressLint("WrongConstant")
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
        setContentView(R.layout.activity_timeline)
        fetchJsonData().execute()

        //initDB
//        val  TL_RV = findViewById<RecyclerView>(R.id.TL_RV)
//        TL_RV.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        ceratetimeline.setOnClickListener {

            startActivity(Intent(this, timeline_cerate::class.java))
            finish()
        }
        //loadTimeline()
        //swaptodelete
        //loadTimeline()


    }
    inner class fetchJsonData() : AsyncTask<String, Void, String>() {
        override fun onPreExecute() {
            super.onPreExecute()
            findViewById<ProgressBar>(R.id.loader).visibility = View.VISIBLE
        }

        override fun doInBackground(vararg params: String?): String? {
            return URL("https://smarter.arabyhosting.com/api/timeline/login.php?userID=$userid").readText(
                Charsets.UTF_8)
        }
        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)

            findViewById<ProgressBar>(R.id.loader).visibility = View.GONE

            val jsonObj = JSONObject(result)
            val mafeedata = jsonObj.getBoolean("isempty")
            if(!mafeedata) {
                val usersArr = jsonObj.getJSONArray("timeline")
                for (i in 0 until usersArr.length()) {
                    val singleUser = usersArr.getJSONObject(i)

                    val map = HashMap<String, String>()
                    map["date"] = singleUser.getString("date")
                    map["time"] = singleUser.getString("time")
                    map["emoji"] = singleUser.getString("emoji")
                    map["text"] = singleUser.getString("text")
                    dataList.add(map)
                }
            }
                else{

                }

            findViewById<ListView>(R.id.listView).adapter = TimelineAdapter(this@timeline, dataList)
        }
    }

//    private fun loadTimeline() {
//
//        lineList = ArrayList()
//       // val results: DBResult where DBResult is TimelineList
//        //TL_RV.adapter =  TimelineAdapter(results)
//
//        //TL_RV.adapter =  TimelineAdapter(lineList)
//        val swipe = ItemTouchHelper(swipeToDelete())
//       // swipe.attachToRecyclerView(TL_RV)
//    }

    private fun swipeToDelete(): ItemTouchHelper.Callback {
        //Swipe recycler view items on RIGHT
        return object : ItemTouchHelper.SimpleCallback(
            0, ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position:Int = viewHolder.adapterPosition
                //delete position
               // loadTimeline()
            }
        }

    }

    override fun onBackPressed() {
        startActivity(Intent(this,mainscrren::class.java))
        finish()
    }

}
