package com.example.smarter

import android.annotation.SuppressLint
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ListView
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.smarter.forNote.NoteAdapter
import com.example.smarter.forNote.NoteData
import kotlinx.android.synthetic.main.note_list.*
import org.json.JSONObject
import java.net.URL


class note : AppCompatActivity() {

    private lateinit var sharedpref: Sharedpref
    private var notes = ArrayList<NoteData>()
    private lateinit var ceratenote: Button
    var dataList = ArrayList<HashMap<String, String>>()
    var userid= 0
    @SuppressLint("WrongConstant")
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
        setContentView(R.layout.activity_note)
        fetchJsonData().execute()
        //init views
       // val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        ceratenote = findViewById(R.id.ceratenote)
        //init DB

        // layout manager
//        recyclerView.layoutManager = LinearLayoutManager(
//            this,
//            LinearLayout.VERTICAL, false
//        )

        ceratenote.setOnClickListener {
            startActivity(Intent(this, note_cerate::class.java))
            finish()
        }
        //loadNotes()

        //delete{
        //loadNotes()}
    }
//
//    private fun loadNotes() {
//        // notes = ArrayList()
//        //generate db list
//        recyclerview.adapter = NoteAdapter(this, notes)
//        val swipe = ItemTouchHelper(swipeToDelete())
//        swipe.attachToRecyclerView(recyclerview)
//    }
    inner class fetchJsonData() : AsyncTask<String, Void, String>() {
        override fun onPreExecute() {
            super.onPreExecute()
            findViewById<ProgressBar>(R.id.loader).visibility = View.VISIBLE
        }

        override fun doInBackground(vararg params: String?): String? {
            return URL("https://smarter.arabyhosting.com/api/note/login.php?userID=$userid").readText(
                Charsets.UTF_8)
        }
        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            findViewById<ProgressBar>(R.id.loader).visibility = View.GONE
            val jsonObj = JSONObject(result)
            val mafeedata = jsonObj.getBoolean("isempty")
            if(!mafeedata){
            val usersArr = jsonObj.getJSONArray("note")
            for (i in 0 until usersArr.length()) {
                val singleUser = usersArr.getJSONObject(i)

                val map = HashMap<String, String>()

                map["text"] = singleUser.getString("text")
                map["Images"] = singleUser.getString("Images")
                map["lat"]=singleUser.getString("lat")
                map["long"]=singleUser.getString("long")
                dataList.add(map)
                saveloc.setOnClickListener( map["lat"],map["long"]) {
println("hello location")
                }

            }
        }
            else{

            }

            findViewById<ListView>(R.id.listView).adapter = NoteAdapter(this@note, dataList)

        }
    }

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
                val position: Int = viewHolder.adapterPosition
                //call db to delete note at specific position
                //loadNotes()
            }
        }
}

    override fun onBackPressed() {
        startActivity(Intent(this,mainscrren::class.java))
        finish()
    }
    private fun Button.setOnClickListener(lat: String?, long: String?, function: () -> Unit) {
        println("LAT is $lat")
        println("long is $long")


    }
}



