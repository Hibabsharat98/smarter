package com.example.smarter

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class setting_message : AppCompatActivity() {
    private lateinit var sharedpref: Sharedpref
    override fun onCreate(savedInstanceState: Bundle?) {
        sharedpref = Sharedpref(this)
        if (sharedpref.loadNightmodeState() == true) {
            setTheme(R.style.darkTheme)

        }
        else
            setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting_message)
    }
    override fun onBackPressed() {
        finish()
        startActivity(Intent(this,setting::class.java))
    }
}
