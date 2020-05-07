package com.example.smarter

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer

class MainActivity : AppCompatActivity() {
    private lateinit var sharedpref: Sharedpref
    override fun onCreate(savedInstanceState: Bundle?) {
        sharedpref = Sharedpref(this)
        if (sharedpref.loadNightmodeState() == true) {
            setTheme(R.style.darkTheme)

        }
        else
            setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val time=object : CountDownTimer(2000, 1000) {
            override fun onFinish() {
                val x= Intent(this@MainActivity,login::class.java)
                startActivity(x)
                finish()
            }
            override fun onTick(P0: Long) {}
        }
        time.start()
    }
}
