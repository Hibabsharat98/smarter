package com.example.smarter

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Switch

class setting_mode : AppCompatActivity() {
    public var eyp: Switch? = null
    internal lateinit var sharedpref: Sharedpref
    override fun onCreate(savedInstanceState: Bundle?) {

        sharedpref = Sharedpref(this)
        if (sharedpref.loadNightmodeState() == true) {
            setTheme(R.style.darkTheme)

        }
        else
            setTheme(R.style.AppTheme)


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting_mode)

        eyp = findViewById<View>(R.id.enabledark) as Switch?
        if (sharedpref.loadNightmodeState() == true) {
            eyp!!.isChecked = true

        }
        eyp!!.setOnCheckedChangeListener() { buttonview, isChecked ->
            if (isChecked) {

                sharedpref.setNightmodeState(true)
                finish()
               // restartApp()

            } else {
                sharedpref.setNightmodeState(false)
                finish()
               // restartApp()
            }
        }
        val x= findViewById<Switch>(R.id.enabledark)
        x.setOnClickListener {
            finish()
            val intent=Intent(this@setting_mode,MainActivity::class.java)
            startActivity(intent)
        }
    }

    fun restartApp() {
        val i = Intent(getApplicationContext(), setting_mode::class.java)
        finish()
        startActivity(i)
    }
    override fun onBackPressed() {
        finish()
        startActivity(Intent(this,mainscrren::class.java))
    }
}
